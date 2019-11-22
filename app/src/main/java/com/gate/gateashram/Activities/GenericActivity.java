package com.gate.gateashram.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

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
                    Intent intent = new Intent(GenericActivity.this, QuestionView.class);
                    intent.putExtra("Code", 1);
                    intent.putExtra("Value", "CSE/" + mListItems.get(i).getmText());
                    startActivity(intent);
                }
            }
        });

    }
}
