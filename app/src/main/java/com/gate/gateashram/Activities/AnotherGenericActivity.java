package com.gate.gateashram.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.gate.gateashram.Adapters.GenericAdapter;
import com.gate.gateashram.Models.ListModelClass;
import com.gate.gateashram.R;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class AnotherGenericActivity extends AppCompatActivity {
    private List<ListModelClass> mListItems;
    private GenericAdapter mAdapter;
    private ListView mListView;
    private TextView mToolbarText;
    private final String LOG_TAG = "AnotherGenericActivity";
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_another_generic);

        mListItems = new ArrayList<>();
        mListView = findViewById(R.id.list);
        mAdapter = new GenericAdapter(this, mListItems);
        mToolbarText = findViewById(R.id.toolbar_text);
        mProgressBar = findViewById(R.id.progress_circular);

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
                        mListItems.add(new ListModelClass(response.getJSONObject(j).getString("subject")));
                        mAdapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        Log.e(LOG_TAG, "" + e);
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(LOG_TAG, "" + error.getMessage());
            }
        });

        queue.add(jsonArrayRequest);
        mListView.setAdapter(mAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(AnotherGenericActivity.this, QuestionView.class);
                intent.putExtra("Code", 2);
                intent.putExtra("Value", mListItems.get(i).getmText());
                startActivity(intent);
            }
        });
    }
}
