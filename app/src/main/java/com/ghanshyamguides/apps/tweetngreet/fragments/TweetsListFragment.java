package com.ghanshyamguides.apps.tweetngreet.fragments;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;
import android.support.annotation.Nullable;

import com.ghanshyamguides.apps.tweetngreet.R;
import com.ghanshyamguides.apps.tweetngreet.adapters.TweetsArrayAdapter;
import com.ghanshyamguides.apps.tweetngreet.helpers.EndlessScrollListener;
import com.ghanshyamguides.apps.tweetngreet.helpers.TwitterClient;
import com.ghanshyamguides.apps.tweetngreet.models.Tweet;

import java.util.ArrayList;

public abstract class TweetsListFragment extends Fragment {

    TweetsArrayAdapter tweetAdapter;
    SwipeRefreshLayout swipeContainer;
    ListView lvTimeline;

    public TweetsListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(isNetworkAvailable()) {
            populateTimeline(null);
        } else {
            Toast.makeText(getActivity().getApplicationContext(), "No internet connection", Toast.LENGTH_SHORT).show();
        }
    }

    public abstract void populateTimeline(String maxId);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_tweets_list, container, false);

        swipeContainer = (SwipeRefreshLayout) v.findViewById(R.id.swipeContainer);

        lvTimeline = (ListView) v.findViewById(R.id.lvTimeline);
        tweetAdapter = new TweetsArrayAdapter(getActivity(), new ArrayList<Tweet>());

        lvTimeline.setAdapter(tweetAdapter);

        lvTimeline.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                System.out.println("tweet adapter: " + tweetAdapter.getCount());
                if(tweetAdapter.getCount() == 0) {
                    populateTimeline(null);
                } else if (tweetAdapter.getCount() >= TwitterClient.TWEETS_PER_PAGE) {
                    Tweet oldest = tweetAdapter.getItem(tweetAdapter.getCount()-1);
                    populateTimeline(oldest.getId());
                }
            }
        });

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                tweetAdapter.clear();
                tweetAdapter.notifyDataSetChanged();
                populateTimeline(null);
            }
        });

        return v;
    }

    protected Boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
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

}
