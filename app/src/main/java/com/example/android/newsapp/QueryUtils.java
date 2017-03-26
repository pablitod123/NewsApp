package com.example.android.newsapp;

import android.text.TextUtils;
import android.text.format.DateFormat;
import android.text.format.DateUtils;
import android.util.Log;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.text.SimpleDateFormat;
import java.util.Locale;

import static android.R.attr.format;

/**
 * Created by paulstyslinger on 3/26/17.
 */

public final class QueryUtils {
    private static final String LOG_TAG = QueryUtils.class.getSimpleName();

    private QueryUtils() {

    }

    public static List<Story> extractStories(String reqeuestUrl) {
        URL url = createURl(reqeuestUrl);

        String jsonResponse = null;

        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        List<Story> stories = extractStoriesFromJson(jsonResponse);

        return stories;
    }

    private static URL createURl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException exception) {
            Log.e(LOG_TAG, "Error with creating url", exception);
            return null;
        }
        return url;
    }


    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200) {
                Log.e(LOG_TAG, "reads as 200");
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the Guardian JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    private static List<Story> extractStoriesFromJson(String storyJSON) {
        if (TextUtils.isEmpty(storyJSON)){
            return null;
        }

        //Create an empty ArrayList of Stories
        List<Story> stories = new ArrayList<>();

        try {
            JSONObject baseJsonResponse = new JSONObject(storyJSON);

            JSONObject responseObject = baseJsonResponse.getJSONObject("response");

            JSONArray resultsArray = responseObject.getJSONArray("results");

            for (int i = 0; i < resultsArray.length(); i++) {
                JSONObject currentStory = resultsArray.getJSONObject(i);

                String title = "(No title)";
                if(currentStory.has("webTitle")) {
                    title = currentStory.getString("webTitle");
                }
                String section = "(No section found)";
                if(currentStory.has("sectionName")) {
                    section = currentStory.getString("sectionName");
                }

                String url = "";
                if(currentStory.has("webUrl")) {
                    url = currentStory.getString("webUrl");
                }

                String dateString = "";
                Date date = null;
                if(currentStory.has("webPublicationDate")) {
                    dateString = currentStory.getString("webPublicationDate");
                    String formattedDate = formatDateString(dateString);
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    try {
                        date = dateFormat.parse(formattedDate);
                    } catch (java.text.ParseException e){
                        Log.e(LOG_TAG, "Problem parsing date", e);
                    }

                }

                //check if the story has a "tags" field, which will contain a contributor
                String contributor = "";
                if(currentStory.has("tags")) {
                    JSONArray tagsArray = currentStory.getJSONArray("tags");
                    JSONObject firstAuthorObject = tagsArray.getJSONObject(0);
                    contributor = firstAuthorObject.getString("webTitle");
                }

                stories.add(new Story(title, section, url, date, contributor));

            }

        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem parsing the JSON results", e);
        }
        return stories;
    }

    static String formatDateString (String longDate) {
        String[] dividedDate = longDate.split("T");
        return dividedDate[0];
    }
}
