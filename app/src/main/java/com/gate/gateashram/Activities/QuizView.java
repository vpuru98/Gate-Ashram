package com.gate.gateashram.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.gate.gateashram.Adapters.QuizItemAdapter;
import com.gate.gateashram.Adapters.QuizSpinnerAdapter;
import com.gate.gateashram.Interface.OnQuizStatusChange;
import com.gate.gateashram.Models.ListModelClass;
import com.gate.gateashram.Models.QuestionModel;
import com.gate.gateashram.Models.QuizItem;
import com.gate.gateashram.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class QuizView extends AppCompatActivity implements OnQuizStatusChange {
    
    private static String LOG_TAG = "QuizView";
    private static int SECONDS_PER_QUESTION = 120;

    String mUrl;

    RelativeLayout mTestToolbar;
    RelativeLayout mEvaluationToolbar;
    RelativeLayout mErrorMessage;
    Button mStartTestButton;
    ProgressBar mProgressBar;
    RelativeLayout mTestInterface;
    TextView mSubmit;
    TextView mTimer;
    TextView mScore;
    Spinner mQuizSpinner;
    ListView mList;
    ArrayList<QuizItem> mQuizItems;
    ArrayList<Integer> mQuizInfo;
    QuizItemAdapter mQuizItemAdapter;
    QuizSpinnerAdapter mQuizSpinnerAdapter;
    int mNumQuizItems;
    int mTimeRemaining;
    int mTotalTime;

    Handler mTimerHandler;
    Runnable mTimerRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_view);
        
        mUrl = getIntent().getStringExtra("Value");

        mTestToolbar = findViewById(R.id.test_toolbar);
        mEvaluationToolbar = findViewById(R.id.evaluation_toolbar);
        mErrorMessage = findViewById(R.id.error_message);
        mStartTestButton = findViewById(R.id.start_test_button);
        mProgressBar = findViewById(R.id.load_quiz_progress_bar);
        mTestInterface = findViewById(R.id.test_interface);
        mSubmit = findViewById(R.id.submit_test);
        mTimer = findViewById(R.id.time_remaining);
        mScore = findViewById(R.id.score);
        mQuizSpinner = findViewById(R.id.question_spinner);
        mList = findViewById(R.id.questions_list);
        mQuizItems = new ArrayList<>();
        
        mStartTestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prepareTest();
            }
        });
        mTestInterface.setVisibility(View.GONE);
        mTestToolbar.setVisibility(View.GONE);
        mEvaluationToolbar.setVisibility(View.GONE);

        getQuizItems();

    }

    void getQuizItems(){
        String url = mUrl;
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d(LOG_TAG, response.toString());
                parseResponse(response);
                mProgressBar.setVisibility(View.GONE);
                mStartTestButton.setVisibility(View.VISIBLE);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(LOG_TAG, error.getLocalizedMessage());
                mProgressBar.setVisibility(View.GONE);
                mErrorMessage.setVisibility(View.VISIBLE);
            }
        });
        queue.add(jsonArrayRequest);
    }
    
    void parseResponse(JSONArray response){
        for(int i = 0;i < response.length();i ++){
            try {
                QuizItem item = new QuizItem(
                        null,
                        null,
                        null,
                        null, 
                        null,
                        null,
                        null,
                        null,
                        null
                );

                JSONObject jsonObject = response.getJSONObject(i);
                ArrayList<String> options = new ArrayList<>();
                try {
                    if(!jsonObject.getString("Option_A").equals(""))
                        options.add(jsonObject.getString("Option_A").trim());
                } catch (Exception e) {
                }
                try {
                    if(!jsonObject.getString("Option_B").equals(""))
                        options.add(jsonObject.getString("Option_B").trim());
                } catch (Exception e) {
                }
                try {
                    if(!jsonObject.getString("Option_C").equals(""))
                        options.add(jsonObject.getString("Option_C").trim());
                } catch (Exception e) {
                }
                try {
                    if(!jsonObject.getString("Option_D").equals(""))
                       options.add(jsonObject.getString("Option_D").trim());
                } catch (Exception e) {
                }
                item.setmOptions(options);
                
                try {
                    item.setmQuestion(jsonObject.getString("Question").trim());
                } catch (Exception e) {
                    Log.e(LOG_TAG, e.getLocalizedMessage());
                }
                try {
                    item.setmYear(jsonObject.getString("Gate_Year").trim());
                } catch (Exception e) {
                    Log.e(LOG_TAG, e.getLocalizedMessage());
                }

                try {
                    item.setmAnswer(jsonObject.getString("Answer").trim());
                } catch (Exception e) {
                    Log.e(LOG_TAG, e.getLocalizedMessage());
                }

                try {
                    item.setmTopic(jsonObject.getString("Topic").trim());
                } catch (Exception e) {
                    Log.e(LOG_TAG, e.getLocalizedMessage());
                }

                try {
                    item.setmMarks(jsonObject.getString("Marks").trim());
                } catch (Exception e) {
                    Log.e(LOG_TAG, e.getLocalizedMessage());
                }

                try {
                    item.setmExplanation(jsonObject.getString("Explanation").trim());
                } catch (Exception e) {
                    Log.e(LOG_TAG, e.getLocalizedMessage());
                }

                try {
                    item.setmImageUrl(jsonObject.getString("Img").trim());
                } catch (Exception e) {
                    Log.e(LOG_TAG, e.getLocalizedMessage());
                }

                if(options.size() == 4 && (item.getmAnswer().equals("A") || item.getmAnswer().equals("B") ||
                        item.getmAnswer().equals("C") || item.getmAnswer().equals("D")))
                    mQuizItems.add(item);
            }
            catch (Exception e){
                Log.e(LOG_TAG, e.getLocalizedMessage());
            }
        }
    }

    void prepareTest(){
        mNumQuizItems = mQuizItems.size();
        mTotalTime = mNumQuizItems * SECONDS_PER_QUESTION;

        mStartTestButton.setVisibility(View.GONE);
        mTestInterface.setVisibility(View.VISIBLE);
        mTestToolbar.setVisibility(View.VISIBLE);
        mEvaluationToolbar.setVisibility(View.GONE);
        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                evauateTest();
            }
        });

        mQuizInfo = new ArrayList<>();
        for(int i = 0;i < mNumQuizItems;i ++){
            mQuizInfo.add(((mQuizItems.get(i).getmResponse() == null) ? -(i + 1) : (i + 1)));
        }
        mQuizSpinnerAdapter = new QuizSpinnerAdapter(this, R.layout.spinner_item, R.id.ques_number, mQuizInfo);
        mQuizSpinner.setAdapter(mQuizSpinnerAdapter);
        mQuizSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> mQuizItemAdapterView, View view, int i, long l) {
                mList.setSelection(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> mQuizItemAdapterView) {

            }
        });

        mQuizItemAdapter = new QuizItemAdapter(this, 0, mQuizItems, "Test", this);
        mList.setAdapter(mQuizItemAdapter);

        mTimeRemaining = mTotalTime;
        mTimerHandler = new Handler();
        mTimerRunnable = new Runnable() {
            @Override
            public void run() {
                if(mTimeRemaining == 0){
                    evauateTest();
                }
                else{
                    mTimeRemaining -= 1;
                    mTimer.setText(getTime(mTimeRemaining));
                    mTimerHandler.postDelayed(this, 1000);
                }
            }
        };

        mTimerRunnable.run();
    }

    private static String getTime(int remSeconds){
        String hours = remSeconds / 3600 + "";
        if(hours.length() == 1){
            hours = "0" + hours;
        }
        String minutes = (remSeconds % 60) / 60 + "";
        if(minutes.length() == 1){
            minutes = "0" + minutes;
        }
        String seconds = remSeconds % 60 + "";
        if(seconds.length() == 1){
            seconds = "0" + seconds;
        }
        return hours + ":" + minutes + ":" + seconds;
    }

    void evauateTest(){
        mTestToolbar.setVisibility(View.GONE);
        mEvaluationToolbar.setVisibility(View.VISIBLE);

        mNumQuizItems = mQuizItems.size();
        mQuizItemAdapter = new QuizItemAdapter(this, 0, mQuizItems, "Evaluate", null);
        mList.setAdapter(mQuizItemAdapter);

        int marks = 0, total_marks = 0;
        for(int i = 0;i < mNumQuizItems;i ++){
            if(mQuizItems.get(i).getmAnswer().equals(mQuizItems.get(i).getmResponse()))
                marks += Integer.parseInt(mQuizItems.get(i).getmMarks());
            total_marks += Integer.parseInt(mQuizItems.get(i).getmMarks());
        }
        mScore.setText(marks + " / " + total_marks);
    }

    @Override
    public void updateQuizStatus(int position) {
        mQuizInfo.set(position, -mQuizInfo.get(position));
        mQuizSpinnerAdapter.notifyDataSetChanged();
    }
}
