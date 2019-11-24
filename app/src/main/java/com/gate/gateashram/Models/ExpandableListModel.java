package com.gate.gateashram.Models;

public class ExpandableListModel {
    private String mText;

    public ExpandableListModel(String mText, String mTopicsUrl) {
        this.mText = mText;
        this.mTopicsUrl = mTopicsUrl;
    }

    public String getmTopicsUrl() {
        return mTopicsUrl;
    }

    private String mTopicsUrl;

    public String getmText() {
        return mText;
    }
}
