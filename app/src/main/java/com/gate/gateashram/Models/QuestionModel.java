package com.gate.gateashram.Models;

import java.util.ArrayList;

public class QuestionModel {
    private String mQuestion;
    private String mYear;
    private String mAnswer;
    private String mTopic;
    private String mMarks;
    private String mExplanation;
    private String mImageUrl;
    private ArrayList<ListModelClass> mOptions;
    private String mId;

    public void setmQuestion(String mQuestion) {
        this.mQuestion = mQuestion;
    }

    public void setmYear(String mYear) {
        this.mYear = mYear;
    }

    public void setmAnswer(String mAnswer) {
        this.mAnswer = mAnswer;
    }

    public void setmTopic(String mTopic) {
        this.mTopic = mTopic;
    }

    public void setmMarks(String mMarks) {
        this.mMarks = mMarks;
    }

    public void setmExplanation(String mExplanation) {
        this.mExplanation = mExplanation;
    }

    public void setmImageUrl(String mImageUrl) {
        this.mImageUrl = mImageUrl;
    }

    public void setmOptions(ArrayList<ListModelClass> mOptions) {
        this.mOptions = mOptions;
    }

    public void setmId(String mId) {
        this.mId = mId;
    }

    public QuestionModel(String mQuestion, String mYear, String mAnswer, String mTopic, String mMarks, String mExplanation, String mImageUrl, ArrayList<ListModelClass> mOptions, String mId) {
        this.mQuestion = mQuestion;
        this.mYear = mYear;
        this.mAnswer = mAnswer;
        this.mTopic = mTopic;
        this.mMarks = mMarks;
        this.mExplanation = mExplanation;
        this.mImageUrl = mImageUrl;
        this.mOptions = mOptions;
        this.mId = mId;
    }

    public String getmQuestion() {
        return mQuestion;
    }

    public String getmYear() {
        return mYear;
    }

    public String getmAnswer() {
        return mAnswer;
    }

    public String getmTopic() {
        return mTopic;
    }

    public String getmMarks() {
        return mMarks;
    }

    public String getmExplanation() {
        return mExplanation;
    }

    public String getmImageUrl() {
        return mImageUrl;
    }

    public ArrayList<ListModelClass> getmOptions() {
        return mOptions;
    }

    public String getmId() {
        return mId;
    }
}
