package com.gate.gateashram.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.gate.gateashram.R;

import org.json.JSONArray;
import org.json.JSONException;

public class MainActivity extends AppCompatActivity {

    public static final String mUrl = "http://192.168.43.102:3000";
    private final String LOG_TAG = "Main";
    private CardView mSubjectWise;
    private CardView mYearWise;
    private CardView mCustom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent i = new Intent(this, QuizView.class);
        i.putExtra("Value", "https://gateashram.herokuapp.com/test/CSE/2015");
        startActivity(i);

        mSubjectWise = findViewById(R.id.subject_card);
        mYearWise = findViewById(R.id.year_card);
        mCustom = findViewById(R.id.custom_card);

        final Intent intent = new Intent(MainActivity.this, GenericActivity.class);

        mSubjectWise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent.putExtra("Code", 1);
                startActivity(intent);
            }
        });

        mYearWise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent.putExtra("Code", 2);
                startActivity(intent);
            }
        });

    }
}
