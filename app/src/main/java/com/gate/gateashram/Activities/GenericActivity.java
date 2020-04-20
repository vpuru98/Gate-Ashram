package com.gate.gateashram.Activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.gate.gateashram.Adapters.GenericAdapter;
import com.gate.gateashram.Models.ListModelClass;
import com.gate.gateashram.R;

import java.util.ArrayList;
import java.util.List;

public class GenericActivity extends AppCompatActivity {

    private List<ListModelClass> mListItems;
    private GenericAdapter mAdapter;
    private ListView mListView;
    private TextView mToolbarText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generic);

        mListItems = new ArrayList<>();
        mListView = findViewById(R.id.list);
        mAdapter = new GenericAdapter(this, mListItems);
        mToolbarText = findViewById(R.id.toolbar_text);

        int code = getIntent().getIntExtra("Code", 1);
        if (code == 1) {
            mListItems.add(new ListModelClass("Computer Science and Engineering"));
            mToolbarText.setText("Choose a Branch");
        } else {
            mToolbarText.setText("Choose a Year");
            for (int i = 2018; i > 1990; i--)
                mListItems.add(new ListModelClass(Integer.toString(i)));
        }

        mListView.setAdapter(mAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (mListItems.size() == 1) {
                    Intent intent = new Intent(GenericActivity.this, AnotherGenericActivity.class);
                    intent.putExtra("Value", "CSE");
                    startActivity(intent);
                } else {
                    final int t = i;
                    AlertDialog.Builder builder = new AlertDialog.Builder(GenericActivity.this);
                    builder.setMessage("Do  you want to take a test or practice?");
                    builder.setCancelable(true);
                    builder.setPositiveButton("Take a Test", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent = new Intent(GenericActivity.this, QuizView.class);
                            intent.putExtra("Url", MainActivity.mUrl + "/test/CSE/" + mListItems.get(t).getmText());
                            intent.putExtra("Type", "Paper");
                            startActivity(intent);
                        }
                    }).setNegativeButton("Practice", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Toast.makeText(GenericActivity.this, "Year-Wise Practice not yet available", Toast.LENGTH_SHORT).show();
//                            Intent intent = new Intent(GenericActivity.this, QuestionView.class);
//                            intent.putExtra("Code", 1);
//                            intent.putExtra("Value", MainActivity.mUrl + "/practice/CSE/" + mListItems.get(t).getmText());
//                            startActivity(intent);
                        }
                    }).show();
                }
            }
        });

    }
}
