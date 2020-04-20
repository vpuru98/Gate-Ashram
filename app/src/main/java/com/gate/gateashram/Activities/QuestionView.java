package com.gate.gateashram.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.gate.gateashram.Adapters.GenericAdapter;
import com.gate.gateashram.Adapters.OptionsAdapter;
import com.gate.gateashram.Models.ListModelClass;
import com.gate.gateashram.Models.QuestionModel;
import com.gate.gateashram.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class QuestionView extends AppCompatActivity {

    private final String LOG_TAG = "QuestionView";
    private int mInd = 0;
    private TextView mToolbarText;
    private TextView mQuestionView;
    private TextView mTopicTags;
    private TextView mGateYear;
    private ImageView mQuestionImage;
    private ListView mListView;
    private ArrayList<QuestionModel> mQuestions;
    private OptionsAdapter mAdapter;
    private CardView mNext;
    private JSONArray mResponse;
    private HashSet<Integer> mChecked;
    private CardView mPrevious;
    private ProgressBar mProgress;
    private LinearLayout mLinearLayout;
    private RelativeLayout mErrorMessage;
    private String mUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_view);
        mToolbarText = findViewById(R.id.toolbar_text);
        mQuestionView = findViewById(R.id.question_view);
        mTopicTags = findViewById(R.id.topic_tags);
        mGateYear = findViewById(R.id.gate_year);
        mQuestionImage = findViewById(R.id.img);
        mListView = findViewById(R.id.list);
        mNext = findViewById(R.id.next);
        mPrevious = findViewById(R.id.previous);
        mQuestions = new ArrayList<>();
        mResponse = new JSONArray();
        mChecked = new HashSet<>();
        mProgress = findViewById(R.id.progress_circular);
        mLinearLayout = findViewById(R.id.ll);
        mErrorMessage = findViewById(R.id.error_message);
        mUrl = getIntent().getStringExtra("Value");

        if (mInd == 0)
            mPrevious.setVisibility(View.GONE);

        mToolbarText.setText("Question " + (mInd + 1));
        int code = getIntent().getIntExtra("Code", 1);
        if (code == 1) {
            performNetworkRequest();
        } else{
        }

        mNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mNext.setVisibility(View.VISIBLE);
                mPrevious.setVisibility(View.VISIBLE);
                mInd++;
                mToolbarText.setText("Question " + (mInd + 1));
                if (mInd == mResponse.length() - 1)
                    mNext.setVisibility(View.GONE);
                mTopicTags.setText("View Topic Tags");
                mListView.setEnabled(true);
                extractResponse(mResponse);
            }
        });

        mPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mNext.setVisibility(View.VISIBLE);
                mPrevious.setVisibility(View.VISIBLE);
                mInd--;
                mToolbarText.setText("Question " + (mInd + 1));
                if (mInd == 0)
                    mPrevious.setVisibility(View.GONE);
                mTopicTags.setText("View Topic Tags");
                mListView.setEnabled(true);
                extractResponse(mResponse);
            }
        });

    }

    void performNetworkRequest() {
        String url = mUrl;

        RequestQueue queue = Volley.newRequestQueue(this);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                mResponse = response;
                int i = mInd;
                mNext.setVisibility(View.VISIBLE);
                mPrevious.setVisibility(View.VISIBLE);
                extractResponse(mResponse);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mProgress.setVisibility(View.GONE);
                mErrorMessage.setVisibility(View.VISIBLE);
                Log.e(LOG_TAG, "error" + error.getMessage());
            }
        });
        queue.add(jsonArrayRequest);
    }

    void extractResponse(JSONArray response) {
        int i = mInd;
        if (mInd == 0)
            mPrevious.setVisibility(View.GONE);
        if (mInd == mResponse.length() - 1)
            mNext.setVisibility(View.GONE);
        mProgress.setVisibility(View.GONE);
        mLinearLayout.setVisibility(View.VISIBLE);
        try {
            QuestionModel questionModel = new QuestionModel(null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null);
            if (!mChecked.contains(mInd)) {
                JSONObject jsonObject = response.getJSONObject(i);
                ArrayList<ListModelClass> options = new ArrayList<>();
                try {
                    options.add(new ListModelClass("A:\t" + jsonObject.getString("Option_A")));
                } catch (Exception e) {
                    options.add(new ListModelClass(null));
                }
                try {
                    options.add(new ListModelClass("B:\t" + jsonObject.getString("Option_B")));
                } catch (Exception e) {
                    options.add(new ListModelClass(null));
                }
                try {
                    options.add(new ListModelClass("C:\t" + jsonObject.getString("Option_C")));
                } catch (Exception e) {
                    options.add(new ListModelClass(null));
                }
                try {
                    options.add(new ListModelClass("D:\t" + jsonObject.getString("Option_D")));
                } catch (Exception e) {
                    options.add(new ListModelClass(null));
                }
                questionModel.setmOptions(options);
                try {
                    questionModel.setmQuestion(jsonObject.getString("Question"));
                } catch (Exception e) {
                    Log.e(LOG_TAG, e.getLocalizedMessage());
                }
                try {
                    questionModel.setmYear(jsonObject.getString("Gate_Year"));
                } catch (Exception e) {
                    Log.e(LOG_TAG, e.getLocalizedMessage());
                }

                try {
                    questionModel.setmAnswer(jsonObject.getString("Answer"));
                } catch (Exception e) {
                    Log.e(LOG_TAG, e.getLocalizedMessage());
                }

                try {
                    questionModel.setmTopic(jsonObject.getString("Topic"));
                } catch (Exception e) {
                    Log.e(LOG_TAG, e.getLocalizedMessage());
                }

                try {
                    questionModel.setmMarks(jsonObject.getString("Marks"));
                } catch (Exception e) {
                    Log.e(LOG_TAG, e.getLocalizedMessage());
                }

                try {
                    questionModel.setmExplanation(jsonObject.getString("Explanation"));
                } catch (Exception e) {
                    Log.e(LOG_TAG, e.getLocalizedMessage());
                }

                try {
                    questionModel.setmImageUrl(jsonObject.getString("Img"));
                } catch (Exception e) {
                    Log.e(LOG_TAG, e.getLocalizedMessage());
                }

                try {
                    questionModel.setmId(jsonObject.getString("_id"));
                } catch (Exception e) {
                    Log.e(LOG_TAG, e.getLocalizedMessage());
                }
                mQuestions.add(questionModel);
                mChecked.add(mInd);
            }

            if (mQuestions.size() > mInd) {
                mQuestionView.setText(mQuestions.get(mInd).getmQuestion());
                mGateYear.setText(mQuestions.get(mInd).getmYear());
                mTopicTags.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mTopicTags.setClickable(false);
                        mTopicTags.setText(mQuestions.get(mInd).getmTopic());
                    }
                });
                mAdapter = new OptionsAdapter(getApplicationContext(), mQuestions.get(mInd).getmOptions());
                mAdapter.notifyDataSetChanged();
                mListView.setAdapter(mAdapter);

                mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        int correctAnswer = (int) ((mQuestions.get(mInd).getmAnswer().charAt(0))) - 65;
                        Log.e(LOG_TAG, mQuestions.get(mInd).getmAnswer() + " Correct Answer ");
                        mListView.setEnabled(false);
                        if (correctAnswer == i)
                            view.setBackgroundColor(Color.parseColor("#32CD32"));
                        else {
                            view.setBackgroundColor(Color.parseColor("#FF9494"));
                            //findViewById((int)adapterView.getItemIdAtPosition(correctAnswer)).setBackgroundColor(Color.parseColor("#32CD32"));
                        }
                    }
                });
            }
        } catch (Exception e) {
            Log.e(LOG_TAG, "" + e.getLocalizedMessage());
        }
    }
}
