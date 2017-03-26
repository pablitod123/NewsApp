package com.example.android.newsapp;

import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v4.content.Loader;
import android.support.v4.widget.ListViewAutoScrollHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static android.R.attr.data;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Story>> {

    private static final String GUARDIAN_REQUEST_URL = "https://content.guardianapis.com/search?q=Uber%20OR%20Lyft&show-tags=contributor&api-key=186ab672-db37-4c0c-877f-fdcda86ea665";

    private static final int STORY_LOADER_ID = 1;

    private StoryAdapter mStoryAdapter;

    private TextView mEmptyStateTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView listView = (ListView) findViewById(R.id.list_view);

        mEmptyStateTextView = (TextView) findViewById(R.id.empty);

        mStoryAdapter = new StoryAdapter(this, new ArrayList<Story>());

        listView.setAdapter(mStoryAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Story currentStory = mStoryAdapter.getItem(position);

                Uri storyUri = Uri.parse(currentStory.getUrl());

                Intent webIntent = new Intent(Intent.ACTION_VIEW, storyUri);

                startActivity(webIntent);
            }
        });

        // Check network connectivity
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        // If internet is connected, then begin to load stories
        if (networkInfo != null && networkInfo.isConnected()) {
            // Get a reference to the LoaderManager, in order to interact with loaders.
            LoaderManager loaderManager = getLoaderManager();

            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            loaderManager.initLoader(STORY_LOADER_ID, null, this);


        } else {
            // Otherwise, display error
            // First, hide loading indicator so error message will be visible
            View loadingIndicator = findViewById(R.id.progress_indicator);
            loadingIndicator.setVisibility(View.GONE);

            mEmptyStateTextView.setText(R.string.network_error);
        }
    }

    @Override
    public android.content.Loader<List<Story>> onCreateLoader(int id, Bundle args) {
        return new StoryLoader(this, GUARDIAN_REQUEST_URL);
    }

    @Override
    public void onLoadFinished(android.content.Loader<List<Story>> loader, List<Story> stories) {
        View loadingIndicator = findViewById(R.id.progress_indicator);
        loadingIndicator.setVisibility(View.GONE);

        mStoryAdapter.clear();

        if (stories != null && !stories.isEmpty()) {
            mStoryAdapter.addAll(stories);
        }
    }

    @Override
    public void onLoaderReset(android.content.Loader<List<Story>> loader) {
        mStoryAdapter.clear();
    }

}

