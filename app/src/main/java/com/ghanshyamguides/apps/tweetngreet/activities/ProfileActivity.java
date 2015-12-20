package com.ghanshyamguides.apps.tweetngreet.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.ghanshyamguides.apps.tweetngreet.R;
import com.ghanshyamguides.apps.tweetngreet.TwitterApplication;
import com.ghanshyamguides.apps.tweetngreet.fragments.UserTimelineFragment;
import com.ghanshyamguides.apps.tweetngreet.helpers.TwitterClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

public class ProfileActivity extends ActionBarActivity {

    TwitterClient client = TwitterApplication.getRestClient();
    ImageView banner;
    ImageView avatar;
    TextView name;
    TextView userName;
    TextView tweetCount;
    TextView followerCount;
    TextView followingCount;
    TextView tagline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        String username = getIntent().getStringExtra("username");

        if (savedInstanceState == null) {
            UserTimelineFragment userTimeline = UserTimelineFragment.newInstance(username);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.profileFragment, userTimeline);
            transaction.commit();
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_tweetngreet);
        getSupportActionBar().setDisplayUseLogoEnabled(true);


        banner = (ImageView) findViewById(R.id.ivBackground);
        avatar = (ImageView) findViewById(R.id.ivProfileAvatar);
        name = (TextView) findViewById(R.id.tvProfileName);
        userName = (TextView) findViewById(R.id.tvProfileUsername);
        tweetCount = (TextView) findViewById(R.id.tvProfileTweetCount);
        followerCount = (TextView) findViewById(R.id.tvProfileFollowerCount);
        followingCount = (TextView) findViewById(R.id.tvProfileFollowingCount);
        tagline = (TextView) findViewById(R.id.tvTagline);

        populateCredentials(username);

    }

    private void populateCredentials(String username) {
        if(username != null) {
            client.getUser(username, handler);
        } else {
            client.getCredentials(handler);
        }
    }

    private JsonHttpResponseHandler handler = new JsonHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            try {

                int followers = response.getInt("followers_count");
                int friends = response.getInt("friends_count");
                int statuses = response.getInt("statuses_count");

                String fullname = response.getString("name");
                String screenName = response.getString("screen_name");
                String desc = response.getString("description");

                String profileImage = response.getString("profile_image_url");
                String bgImage = response.optString("profile_background_image_url");

                followerCount.setText(String.valueOf(followers) + "\nFOLLOWERS");
                followingCount.setText(String.valueOf(friends) + "\nFOLLOWING");
                tweetCount.setText(String.valueOf(statuses) + "\nTWEETS");

                name.setText(fullname);
                userName.setText("@" + screenName);
                tagline.setText(desc);

                Picasso.with(getApplicationContext()).load(Uri.parse(profileImage)).into(avatar);
                Picasso.with(getApplicationContext()).load(Uri.parse(bgImage)).into(banner);

                getSupportActionBar().setTitle("@" + screenName);


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_user, menu);
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

        if(id == R.id.action_timeline) {
            Intent i = new Intent(this, TimelineActivity.class);
            startActivity(i);
        }

        if(id == R.id.action_settings) {
            TwitterApplication.getRestClient().clearAccessToken();
            Intent i = new Intent(ProfileActivity.this, LoginActivity.class);
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }
}
