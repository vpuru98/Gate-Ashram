package com.gate.gateashram.Models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class QuizItem implements Parcelable {
    private String mQuestion;
    private String mYear;
    private String mAnswer;
    private String mResponse;
    private String mTopic;
    private String mMarks;
    private String mExplanation;
    private String mImageUrl;
    private ArrayList<String> mOptions;


    public QuizItem(String question, String year, String answer, String response, String topic, String marks,
                    String explanation, String imageurl, ArrayList<String> options){
        mQuestion = question;
        mYear = year;
        mAnswer = answer;
        mResponse = response;
        mTopic = topic;
        mMarks = marks;
        mExplanation = explanation;
        mImageUrl =imageurl;
        mOptions = options;
    }


    protected QuizItem(Parcel in) {
        mQuestion = in.readString();
        mYear = in.readString();
        mAnswer = in.readString();
        mResponse = in.readString();
        mTopic = in.readString();
        mMarks = in.readString();
        mExplanation = in.readString();
        mImageUrl = in.readString();
        mOptions = in.createStringArrayList();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mQuestion);
        dest.writeString(mYear);
        dest.writeString(mAnswer);
        dest.writeString(mResponse);
        dest.writeString(mTopic);
        dest.writeString(mMarks);
        dest.writeString(mExplanation);
        dest.writeString(mImageUrl);
        dest.writeStringList(mOptions);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<QuizItem> CREATOR = new Creator<QuizItem>() {
        @Override
        public QuizItem createFromParcel(Parcel in) {
            return new QuizItem(in);
        }

        @Override
        public QuizItem[] newArray(int size) {
            return new QuizItem[size];
        }
    };

    public String getmQuestion() {
        return mQuestion;
    }

    public void setmQuestion(String mQuestion) {
        this.mQuestion = mQuestion;
    }

    public String getmYear() {
        return mYear;
    }

    public void setmYear(String mYear) {
        this.mYear = mYear;
    }

    public String getmAnswer() {
        return mAnswer;
    }

    public void setmAnswer(String mAnswer) {
        this.mAnswer = mAnswer;
    }

    public String getmResponse() {
        return mResponse;
    }

    public void setmResponse(String mResponse) {
        this.mResponse = mResponse;
    }

    public String getmTopic() {
        return mTopic;
    }

    public void setmTopic(String mTopic) {
        this.mTopic = mTopic;
    }

    public String getmMarks() {
        return mMarks;
    }

    public void setmMarks(String mMarks) {
        this.mMarks = mMarks;
    }

    public String getmExplanation() {
        return mExplanation;
    }

    public void setmExplanation(String mExplanation) {
        this.mExplanation = mExplanation;
    }

    public String getmImageUrl() {
        return mImageUrl;
    }

    public void setmImageUrl(String mImageUrl) {
        this.mImageUrl = mImageUrl;
    }

    public ArrayList<String> getmOptions() {
        return mOptions;
    }

    public void setmOptions(ArrayList<String> mOptions) {
        this.mOptions = mOptions;
    }
}
