package com.example.android.newsapp;

import java.util.Date;

/**
 * Created by paulstyslinger on 3/26/17.
 */

public class Story {

    private static final Date NO_DATE_PROVIDED = null;

    private static final String NO_AUTHOR_PROVIDED = "";

    //Title of the article
    private String mTitle;

    //Section to which the article belongs
    private String mSection;

    //Url of the article
    private String mUrl;

    //Date the article was posted (if available)
    private Date mDatePosted;

    //Author of the article (if available)
    private String mAuthor;


    public Story(String title, String section, String url, Date datePosted, String author) {
        mTitle = title;
        mSection = section;
        mUrl = url;
        mDatePosted = datePosted;
        mAuthor = author;
    }

    public Story(String title, String section, String url, Date datePosted) {
        mTitle = title;
        mSection = section;
        mUrl = url;
        mDatePosted = datePosted;
    }

    public Story(String title, String section, String url, String author) {
        mTitle = title;
        mSection = section;
        mUrl = url;
        mAuthor = author;
    }


    public Story(String title, String section, String url) {
        mTitle = title;
        mSection = section;
        mUrl = url;
    }


    public String getTitle() {
        return mTitle;
    }

    public String getSection() {
        return mSection;
    }

    public String getUrl() {
        return mUrl;
    }

    public Date getDatePosted() {
        return mDatePosted;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public boolean hasDatePosted() {
        return mDatePosted != NO_DATE_PROVIDED;
    }

    public boolean hasAuthor() {
        return mAuthor != NO_AUTHOR_PROVIDED;
    }



}
