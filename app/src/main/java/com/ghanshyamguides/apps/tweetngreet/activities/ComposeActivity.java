package com.ghanshyamguides.apps.tweetngreet.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.ghanshyamguides.apps.tweetngreet.R;
import com.ghanshyamguides.apps.tweetngreet.TwitterApplication;
import com.ghanshyamguides.apps.tweetngreet.helpers.TwitterClient;
import com.ghanshyamguides.apps.tweetngreet.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONObject;


public class ComposeActivity extends ActionBarActivity {

    public static int REQUEST_CODE = 200;

    EditText tweetText;
    Button submit;

    TwitterClient client = TwitterApplication.getRestClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_tweetngreet);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setTitle(" TweetNGreet");


        tweetText = (EditText) findViewById(R.id.etNewTweetText);
        submit = (Button) findViewById(R.id.btnSubmitNewTweet);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                client.postUpdate(tweetText.getText().toString(), new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        // Go back to the timeline
                        Tweet tweet = Tweet.fromJson(response);
                        Intent i = new Intent();
                        i.putExtra("tweet", tweet);
                        setResult(REQUEST_CODE, i);
                        finish();
                    }

                    // TODO handle error.

                });

            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_compose, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_profile) {
            Intent i = new Intent(this, ProfileActivity.class);
            startActivityForResult(i, ComposeActivity.REQUEST_CODE);
        }

        if(id == R.id.action_timeline) {
            Intent i = new Intent(this, TimelineActivity.class);
            startActivity(i);
        }

        if(id == R.id.action_settings) {
            TwitterApplication.getRestClient().clearAccessToken();
            Intent i = new Intent(ComposeActivity.this, LoginActivity.class);
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }
}
