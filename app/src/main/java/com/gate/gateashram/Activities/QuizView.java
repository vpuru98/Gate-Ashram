package com.gate.gateashram.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.graphics.pdf.PdfDocument;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.gate.gateashram.Adapters.QuizItemAdapter;
import com.gate.gateashram.Adapters.QuizSpinnerAdapter;
import com.gate.gateashram.Helpers.Helper;
import com.gate.gateashram.Interface.OnQuizStatusChange;
import com.gate.gateashram.Models.ListModelClass;
import com.gate.gateashram.Models.QuestionModel;
import com.gate.gateashram.Models.QuizItem;
import com.gate.gateashram.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class QuizView extends AppCompatActivity implements OnQuizStatusChange {

    private static String LOG_TAG = "QuizView";
    private static int STORAGE_REQUEST_CODE = 1;
    private static int SECONDS_PER_QUESTION = 120;

    RelativeLayout mTestToolbar;
    RelativeLayout mEvaluationToolbar;
    RelativeLayout mErrorMessage;
    Button mStartTestButton;
    ProgressBar mProgressBar;
    RelativeLayout mTestInterface;
    LinearLayout mPdfView;
    TextView mSubmit;
    TextView mTimer;
    TextView mScore;
    TextView mExport;
    Spinner mQuizSpinner;
    ListView mList;
    ArrayList<Integer> mQuizInfo;
    QuizItemAdapter mQuizItemAdapter;
    QuizSpinnerAdapter mQuizSpinnerAdapter;
    int mNumQuizItems;
    int mTotalTime;

    Handler mTimerHandler;
    Runnable mTimerRunnable;

    // Activity state variables
    String mTestType;
    String mUrl;
    ArrayList<QuizItem> mQuizItems;
    boolean mLoadTest;
    boolean mStartTest;
    boolean mEvaluateTest;
    int mTimeRemaining;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_view);

        mTestToolbar = findViewById(R.id.test_toolbar);
        mEvaluationToolbar = findViewById(R.id.evaluation_toolbar);
        mErrorMessage = findViewById(R.id.error_message);
        mStartTestButton = findViewById(R.id.start_test_button);
        mProgressBar = findViewById(R.id.load_quiz_progress_bar);
        mTestInterface = findViewById(R.id.test_interface);
        mPdfView = findViewById(R.id.pdf_view);
        mSubmit = findViewById(R.id.submit_test);
        mTimer = findViewById(R.id.time_remaining);
        mScore = findViewById(R.id.score);
        mExport = findViewById(R.id.export_button);
        mQuizSpinner = findViewById(R.id.question_spinner);
        mList = findViewById(R.id.questions_list);

        mTestInterface.setVisibility(View.GONE);
        mStartTestButton.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.GONE);
        mErrorMessage.setVisibility(View.GONE);

        if(savedInstanceState == null){
            mTestType = getIntent().getStringExtra("Type");
            mUrl = getIntent().getStringExtra("Url");
            mQuizItems = new ArrayList<>();
            mLoadTest = true;
            mStartTest = false;
            mEvaluateTest = false;
            mTimeRemaining = -1;
        }

    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("mTestType", mTestType);
        outState.putString("mUrl", mUrl);
        outState.putParcelableArrayList("mQuizItems", mQuizItems);
        outState.putBoolean("mLoadTest", mLoadTest);
        outState.putBoolean("mStartTest", mStartTest);
        outState.putBoolean("mEvaluteTest", mEvaluateTest);
        outState.putInt("mTimeRemaining", mTimeRemaining);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mTestType = savedInstanceState.getString("mTestType");
        mUrl = savedInstanceState.getString("mUrl");
        mQuizItems = savedInstanceState.getParcelableArrayList("mQuizItems");
        mLoadTest = savedInstanceState.getBoolean("mLoadTest");
        mStartTest = savedInstanceState.getBoolean("mStartTest");
        mEvaluateTest = savedInstanceState.getBoolean("mEvaluateTest");
        mTimeRemaining = savedInstanceState.getInt("mTimeRemaining");
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mLoadTest){
            getQuizItems();
        }
        if(mStartTest){
            prepareTest();
        }
        if(mEvaluateTest){
            evauateTest();
        }
    }

    @Override
    protected void onPause() {
        mTimerHandler.removeCallbacksAndMessages(null);
        super.onPause();
    }

    void getQuizItems(){
        mProgressBar.setVisibility(View.VISIBLE);
        mTestInterface.setVisibility(View.GONE);
        mStartTestButton.setVisibility(View.GONE);
        mErrorMessage.setVisibility(View.GONE);

        mLoadTest = true;
        mStartTest = false;
        mEvaluateTest = false;

        String url = mUrl;
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d(LOG_TAG, response.toString());
                parseResponse(response);
                mProgressBar.setVisibility(View.GONE);
                mStartTestButton.setVisibility(View.VISIBLE);
                mStartTestButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        prepareTest();
                    }
                });
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
        mTestInterface.setVisibility(View.VISIBLE);
        mStartTestButton.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.GONE);
        mErrorMessage.setVisibility(View.GONE);

        mLoadTest = false;
        mStartTest = true;
        mEvaluateTest = false;

        mNumQuizItems = mQuizItems.size();
        mTotalTime = mNumQuizItems * SECONDS_PER_QUESTION;

        mTestToolbar.setVisibility(View.VISIBLE);
        mEvaluationToolbar.setVisibility(View.GONE);
        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(QuizView.this);
                builder.setMessage("Are you sure you want to submit?")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            mQuizSpinnerAdapter = null;
                            evauateTest();
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    })
                    .show();
            }
        });

        mQuizInfo = new ArrayList<>();
        for (int i = 0; i < mNumQuizItems; i++) {
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

        mQuizItemAdapter = new QuizItemAdapter(this, 0, mQuizItems, "Test",
                mTestType, this);
        mList.setAdapter(mQuizItemAdapter);

        if(mTimeRemaining == -1){
            if(mTestType.equals("Paper"))
                mTimeRemaining = 180 * 60;
            else
                mTimeRemaining = mNumQuizItems * SECONDS_PER_QUESTION;
        }
        mTimerHandler = new Handler();
        mTimerRunnable = new Runnable() {
            @Override
            public void run() {
                if(mTimeRemaining == 0){
                    AlertDialog.Builder builder = new AlertDialog.Builder(QuizView.this);
                    builder.setMessage("The test has ended")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                evauateTest();
                            }
                        })
                        .show();
                }
                else{
                    mTimeRemaining -= 1;
                    mTimer.setText(getTime(mTimeRemaining));
                    mTimerHandler.postDelayed(this, 1000);
                }
            }
        };

        mTimerHandler.postDelayed(mTimerRunnable, 0);
    }

    void evauateTest(){
        mTestInterface.setVisibility(View.VISIBLE);
        mStartTestButton.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.GONE);
        mErrorMessage.setVisibility(View.GONE);

        mLoadTest = false;
        mStartTest = false;
        mEvaluateTest = true;

        mTestToolbar.setVisibility(View.GONE);
        mEvaluationToolbar.setVisibility(View.VISIBLE);

        mQuizItemAdapter = new QuizItemAdapter(this, 0, mQuizItems, "Evaluate",
                mTestType, null);
        mList.setAdapter(mQuizItemAdapter);

        int marks = 0, total_marks = 0;
        for(int i = 0;i < mQuizItems.size();i ++){
            if(mQuizItems.get(i).getmAnswer().equals(mQuizItems.get(i).getmResponse()))
                marks += Integer.parseInt(mQuizItems.get(i).getmMarks());
            total_marks += Integer.parseInt(mQuizItems.get(i).getmMarks());
        }
        mScore.setText(marks + " / " + total_marks);

        mExport.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                if(ContextCompat.checkSelfPermission(QuizView.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                        PackageManager.PERMISSION_GRANTED){
                    requestStoragePermissions(QuizView.this);
                }
                else{
                    writeAnswerKeyToFile();
                }
            }
        });
    }

    private static String getTime(int remSeconds){
        String hours = remSeconds / 3600 + "";
        if(hours.length() == 1){
            hours = "0" + hours;
        }
        String minutes = (remSeconds % 3600) / 60 + "";
        if(minutes.length() == 1){
            minutes = "0" + minutes;
        }
        String seconds = remSeconds % 60 + "";
        if(seconds.length() == 1){
            seconds = "0" + seconds;
        }
        return hours + ":" + minutes + ":" + seconds;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == STORAGE_REQUEST_CODE) {
            if (grantResults.length == 2 && grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                writeAnswerKeyToFile();
            }
            else{
                Toast.makeText(this, "Export Failed. Permission Denied..", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void writeAnswerKeyToFile() {
        TextView answerContainer = mPdfView.findViewById(R.id.answers_container);
        String answers = "";
        for(int i = 0;i < mQuizItems.size();i ++){
            answers += (i + 1) + ". (" + mQuizItems.get(i).getmAnswer().trim() + ")    ";
        }
        answerContainer.setText(answers);

        PdfDocument document = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(800, 1100,
                1).create();
        PdfDocument.Page page = document.startPage(pageInfo);
        int measureWidth = View.MeasureSpec.makeMeasureSpec(page.getCanvas().getWidth(), View.MeasureSpec.EXACTLY);
        int measureHeight = View.MeasureSpec.makeMeasureSpec(page.getCanvas().getHeight(), View.MeasureSpec.EXACTLY);
        Log.d(LOG_TAG, "" + measureWidth + ", " + measureHeight);
        mPdfView.measure(measureWidth, measureHeight);
        mPdfView.layout(0, 0, page.getCanvas().getWidth(), page.getCanvas().getHeight());
        mPdfView.draw(page.getCanvas());
        document.finishPage(page);

        File file1;
        String directory = Environment.getExternalStorageDirectory().getPath() + "/GateAshram/";
        file1 = new File(directory);
        file1.mkdirs();
        String file_path = directory + "answer_key.pdf";
        File file2 = new File(file_path);
        if(!file2.exists()){
            try {
                file2.createNewFile();
            }
            catch (IOException e){
                e.printStackTrace();
            }
        }
        try{
            document.writeTo(new FileOutputStream(file2));
            Toast.makeText(this, "Answer Key exported to PDF at " + file_path, Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }

        document.close();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public static void requestStoragePermissions(Activity activity){
        String[] permissions = {
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };
        ActivityCompat.requestPermissions(
                activity,
                permissions,
                STORAGE_REQUEST_CODE
        );
    }

    @Override
    public void updateQuizStatus(int position) {
        mQuizInfo.set(position, -mQuizInfo.get(position));
        mQuizSpinnerAdapter.notifyDataSetChanged();
    }
}
