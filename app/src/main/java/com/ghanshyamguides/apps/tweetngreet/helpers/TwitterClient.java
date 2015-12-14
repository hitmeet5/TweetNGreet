package com.ghanshyamguides.apps.tweetngreet.helpers;

import android.content.Context;
import android.text.format.DateUtils;

import com.codepath.oauth.OAuthBaseClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.scribe.builder.api.Api;
import org.scribe.builder.api.TwitterApi;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

/*
 * 
 * This is the object responsible for communicating with a REST API. 
 * Specify the constants below to change the API being communicated with.
 * See a full list of supported API classes: 
 *   https://github.com/fernandezpablo85/scribe-java/tree/master/src/main/java/org/scribe/builder/api
 * Key and Secret are provided by the developer site for the given API i.e dev.twitter.com
 * Add methods for each relevant endpoint in the API.
 * 
 * NOTE: You may want to rename this object based on the service i.e TwitterClient or FlickrClient
 * 
 */
public class TwitterClient extends OAuthBaseClient {
	public static final Class<? extends Api> REST_API_CLASS = TwitterApi.class; // Change this
	public static final String REST_URL = "https://api.twitter.com/1.1/"; // Change this, base API URL
	public static final String REST_CONSUMER_KEY = "fy8zHSuyiN1JtaJv9bBNsQkAK";
	public static final String REST_CONSUMER_SECRET = "eKrf4qdpLMfweKPToi0JU4psoBTuQ1dh1JEvYnFQQPt0Q98DQb";
	public static final String REST_CALLBACK_URL = "oauth://codepathtweets";
    public static final int TWEETS_PER_PAGE = 25;

	public TwitterClient(Context context) {
		super(context, REST_API_CLASS, REST_URL, REST_CONSUMER_KEY, REST_CONSUMER_SECRET, REST_CALLBACK_URL);
	}

    public void getHomeTimeline(String maxId, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("statuses/home_timeline.json");
        RequestParams params = new RequestParams();
        params.put("count", TWEETS_PER_PAGE);
        params.put("max_id", maxId);
        System.out.println(apiUrl.toString() + " " + params.toString());
        client.get(apiUrl, params, handler);
    }

    public void postUpdate(String tweetBody, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("statuses/update.json");
        RequestParams params = new RequestParams();
        params.put("status", tweetBody);
        client.post(apiUrl, params, handler);
    }

    public void getMentions(String maxId, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("statuses/mentions_timeline.json");
        RequestParams params = new RequestParams();
        params.put("count", TWEETS_PER_PAGE);
        params.put("max_id", maxId);
        params.put("include_rts", 1);
        System.out.println(apiUrl.toString() + " " + params.toString());
        client.get(apiUrl, params, handler);
    }

    public void getUser(String username, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("users/show.json");
        RequestParams params = new RequestParams();
        params.put("include_entities", "false");
        params.put("screen_name", username);
        client.get(apiUrl, params, handler);
    }

    public void getCredentials(AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("account/verify_credentials.json");
        RequestParams params = new RequestParams();
        params.put("skip_status", 1);
        params.put("include_entities", "false");
        client.get(apiUrl, params, handler);
    }


    public void getUserTimeline(String username, String maxId, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("statuses/user_timeline.json");
        RequestParams params = new RequestParams();
        params.put("max_id", maxId);
        params.put("screen_name", username);
        params.put("count", TWEETS_PER_PAGE);
        client.get(apiUrl, params, handler);
    }

    // Got this from https://gist.github.com/nesquena/f786232f5ef72f6e10a7
    public static String getRelativeTimeAgo(String rawJsonDate) {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);
        String relativeDate = "";

        try {
            long dateMillis = sf.parse(rawJsonDate).getTime();
            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS, DateUtils.FORMAT_ABBREV_RELATIVE).toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return relativeDate;
    }

}