package com.example.android.newsapp;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static android.R.attr.resource;
import static com.example.android.newsapp.R.id.contributorView;
import static com.example.android.newsapp.R.id.dateView;
import static com.example.android.newsapp.R.string.posted_on;

/**
 * Created by paulstyslinger on 3/26/17.
 */

public class StoryAdapter extends ArrayAdapter<Story> {

    public StoryAdapter(Context context, List<Story> stories) {
        super(context, 0, stories);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }

        Story currentStory = getItem(position);

        TextView titleView = (TextView) listItemView.findViewById(R.id.titleView);
        titleView.setText(currentStory.getTitle());

        TextView sectionView = (TextView) listItemView.findViewById(R.id.sectionView);
        sectionView.setText(currentStory.getSection());

        TextView contributorView = (TextView) listItemView.findViewById(R.id.contributorView);

        if (currentStory.hasAuthor()) {
            contributorView.setText(currentStory.getAuthor());
        } else {
            contributorView.setVisibility(View.GONE);
        }

        TextView dateView = (TextView) listItemView.findViewById(R.id.dateView);

        if (currentStory.hasAuthor()) {
            String formattedDate = formatDate(currentStory.getDatePosted());
            dateView.setText(getContext().getResources().getString(R.string.posted_on, formattedDate));
        } else {
            dateView.setVisibility(View.GONE);
        }

        return listItemView;

    }

    private String formatDate(Date dateObject) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("LLL dd, yyyy");
        return dateFormat.format(dateObject);
    }
}
