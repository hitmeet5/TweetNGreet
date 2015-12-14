package com.ghanshyamguides.apps.tweetngreet.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ghanshyamguides.apps.tweetngreet.R;
import com.ghanshyamguides.apps.tweetngreet.activities.ProfileActivity;
import com.ghanshyamguides.apps.tweetngreet.models.Tweet;
import com.ghanshyamguides.apps.tweetngreet.models.User;
import com.squareup.picasso.Picasso;

import java.util.List;

public class TweetsArrayAdapter extends ArrayAdapter<Tweet> {

    public TweetsArrayAdapter(Context context, List<Tweet> tweets) {
        super(context, 0, tweets);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_tweet, parent, false);
            viewHolder.ivAvatar = (ImageView) convertView.findViewById(R.id.ivAvatar);
            viewHolder.tvTweetFullName = (TextView) convertView.findViewById(R.id.tvTweetFullName);
            viewHolder.tvTweetUsername = (TextView) convertView.findViewById(R.id.tvTweetUsername);
            viewHolder.tvTweetAge = (TextView) convertView.findViewById(R.id.tvTweetAge);
            viewHolder.tvTweetBody = (TextView) convertView.findViewById(R.id.tvTweetBody);
            viewHolder.tvRetweets = (TextView) convertView.findViewById(R.id.tvRetweetCount);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Tweet tweet = getItem(position);
        viewHolder.tvTweetAge.setText(tweet.getCreatedAt());
        viewHolder.tvTweetBody.setText(tweet.getBody());
        viewHolder.tvRetweets.setText(String.valueOf(tweet.getRetweetCount()));

        User user = tweet.getUser();
        Picasso.with(getContext()).load(Uri.parse(user.getUserProfileImage())).into(viewHolder.ivAvatar);
        viewHolder.tvTweetFullName.setText(user.getUserName().toString());
        viewHolder.tvTweetUsername.setText("@" + user.getUserScreenName().toString());

        viewHolder.ivAvatar.setTag(user.getUserScreenName().toString());
        viewHolder.ivAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), ProfileActivity.class);
                i.putExtra("username", v.getTag().toString());
                getContext().startActivity(i);
            }
        });

        return convertView;

    }

    private static class ViewHolder {
        ImageView ivAvatar;
        TextView tvTweetFullName;
        TextView tvTweetUsername;
        TextView tvTweetAge;
        TextView tvTweetBody;
        TextView tvRetweets;
    }
}
