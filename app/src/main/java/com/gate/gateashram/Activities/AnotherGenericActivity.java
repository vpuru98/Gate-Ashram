package com.gate.gateashram.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.gate.gateashram.Adapters.ExpandableListAdapter;
import com.gate.gateashram.Models.ExpandableListModel;
import com.gate.gateashram.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AnotherGenericActivity extends AppCompatActivity {
    private List<ExpandableListModel> mListItems;
    private ExpandableListAdapter mAdapter;
    private ExpandableListView mListView;
    private TextView mToolbarText;
    private final String LOG_TAG = "AnotherGenericActivity";
    private ProgressBar mProgressBar;
    private RelativeLayout mErrorMessage;

    private HashMap<String, List<String>> mHashmap;
    private ArrayList<String> mTitles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_another_generic);

        mListItems = new ArrayList<>();
        mListView = findViewById(R.id.list);
        mHashmap = new HashMap<>();
        mTitles = new ArrayList<>();
        mToolbarText = findViewById(R.id.toolbar_text);
        mProgressBar = findViewById(R.id.progress_circular);
        mErrorMessage = findViewById(R.id.error_message);

        String url = "";
        mToolbarText.setText("Choose a Subject");
        url = MainActivity.mUrl + "/" + getIntent().getStringExtra("Value") + "/subjects";

        RequestQueue queue = Volley.newRequestQueue(this);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int j = 0; j < response.length(); j++) {
                    try {
                        mProgressBar.setVisibility(View.GONE);
                        mListView.setVisibility(View.VISIBLE);
                        mListItems.add(new ExpandableListModel(response.getJSONObject(j).getString("subject"), MainActivity.mUrl + "/" + response.getJSONObject(j).getString("subject") + "/topics"));
                        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                        final String key = response.getJSONObject(j).getString("subject");
                        mTitles.add(key);
                        String url = MainActivity.mUrl + "/" + response.getJSONObject(j).getString("subject") + "/topics";
                        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
                                ArrayList<String> topics = new ArrayList<>();
                                topics.add("All");
                                for (int j = 0; j < response.length(); j++) {
                                    try {
                                        JSONObject jsonObject = response.getJSONObject(j);
                                        topics.add(jsonObject.getString("topic"));
                                    } catch (JSONException e) {
                                        Log.e(LOG_TAG, "" + e);
                                    }
                                }

                                mHashmap.put(key, topics);

                                if (mHashmap.size() == mListItems.size()) {
                                    Log.e(LOG_TAG, mHashmap.size() + "" + mTitles.size());
                                    mAdapter = new ExpandableListAdapter(getApplicationContext(), mTitles, mHashmap);
                                    mListView.setAdapter(mAdapter);
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e(LOG_TAG, "" + error.getMessage());
                            }
                        });

                        queue.add(jsonArrayRequest);
                    } catch (JSONException e) {
                        Log.e(LOG_TAG, "" + e);
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mErrorMessage.setVisibility(View.VISIBLE);
                Log.e(LOG_TAG, "" + error.getMessage());
            }
        });

        queue.add(jsonArrayRequest);

        mListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
            }
        });

        mListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {

            }
        });

        mListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                String url = "";
                if (mHashmap.get(mTitles.get(groupPosition)).get(childPosition).equalsIgnoreCase("all")) {
                    url = MainActivity.mUrl + "/" + mTitles.get(groupPosition) + "/practice";
                } else
                    url = MainActivity.mUrl + "/CSE/subjects/" + mTitles.get(groupPosition) + "/" + mHashmap.get(mTitles.get(groupPosition)).get(childPosition) + "/practice";

                Intent intent = new Intent(AnotherGenericActivity.this, QuestionView.class);
                intent.putExtra("Value", url);
                intent.putExtra("Code", 2);
                startActivity(intent);
                return false;
            }
        });
    }
}

