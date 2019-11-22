package com.gate.gateashram.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
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
        if (code == 1)
            mListItems.add(new ListModelClass("Computer Science and Engineering"));
        else {
            for (int i = 2018; i > 1990; i--)
                mListItems.add(new ListModelClass(Integer.toString(i)));
        }
        mListView.setAdapter(mAdapter);
    }
}
