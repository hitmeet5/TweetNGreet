package com.ghanshyamguides.apps.tweetngreet.activities;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.ghanshyamguides.apps.tweetngreet.R;
import com.ghanshyamguides.apps.tweetngreet.TwitterApplication;
import com.ghanshyamguides.apps.tweetngreet.adapters.TweetsArrayAdapter;
import com.ghanshyamguides.apps.tweetngreet.helpers.TwitterClient;
import com.ghanshyamguides.apps.tweetngreet.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class TimelineActivity extends ActionBarActivity {

   // TweetsPagerAdapter pagerAdapter;
    ViewPager vp;

    TweetsArrayAdapter tweetAdapter;
    SwipeRefreshLayout swipeContainer;
    ListView lvTimeline;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        // Set a ToolBar to replace the ActionBar.
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_tweetngreet);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setTitle(" TweetNGreet");

        ArrayList<Tweet> tweets = new ArrayList();

        tweetAdapter = new com.ghanshyamguides.apps.tweetngreet.adapters.TweetsArrayAdapter(this,tweets);
        lvTimeline = (ListView) findViewById(R.id.lvTimeline);

        // Bind adapter to the listview
        lvTimeline.setAdapter(tweetAdapter);



       // vp = (ViewPager) findViewById(R.id.viewpager);
      //  pagerAdapter = new TweetsPagerAdapter(getSupportFragmentManager());
      //  vp.setAdapter(pagerAdapter);
      //  PagerSlidingTabStrip tabStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);
      //  tabStrip.setViewPager(vp);

        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                populateTimeline(null);
            }
        });

        populateTimeline(null);

    }


    private TwitterClient client = TwitterApplication.getRestClient();

    public void populateTimeline(String maxId) {
        if(isNetworkAvailable()) {
            client.getHomeTimeline(maxId, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                    ArrayList<Tweet> tweets = Tweet.fromJson(response);
                    addAll(tweets);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    System.out.println("Home timeline failed");
                    System.out.println(errorResponse);
                    Toast.makeText(getApplicationContext(),
                            "Couldn't get Tweets :(", Toast.LENGTH_SHORT).show();
                    swipeContainer.setRefreshing(false);
                }
            });
        } else {
            Toast.makeText(getApplicationContext(), "No internet connection", Toast.LENGTH_SHORT).show();
        }

    }

    protected Boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = cm.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

    public void add(Tweet tweet) {
        tweetAdapter.insert(tweet, 0);
        tweetAdapter.notifyDataSetChanged();
    }

    protected void addAll(ArrayList<Tweet> tweets) {
        tweetAdapter.addAll(tweets);
        tweetAdapter.notifyDataSetChanged();
        swipeContainer.setRefreshing(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_timeline, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_compose) {
            Intent i = new Intent(this, ComposeActivity.class);
            startActivityForResult(i, ComposeActivity.REQUEST_CODE);
        }

        if(id == R.id.action_profile) {
            Intent i = new Intent(this, ProfileActivity.class);
            startActivity(i);
        }

        if(id == R.id.action_settings) {
            TwitterApplication.getRestClient().clearAccessToken();
            Intent i = new Intent(TimelineActivity.this, LoginActivity.class);
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == ComposeActivity.REQUEST_CODE && resultCode == ComposeActivity.REQUEST_CODE) {
            // Append this tweet to the top of the feed
            Tweet tweet = (Tweet) data.getParcelableExtra("tweet");
            add(tweet);
        }
    }

}
