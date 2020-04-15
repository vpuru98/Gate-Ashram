package com.gate.gateashram.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.gate.gateashram.R;

import java.util.List;

public class QuizSpinnerAdapter extends ArrayAdapter<Integer> {

    private Context mContext;

    public QuizSpinnerAdapter(@NonNull Context context, int resource, int textViewResourceId, @NonNull List<Integer> objects) {
        super(context, resource, textViewResourceId, objects);
        mContext = context;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.spinner_item, parent, false);
        }

        ImageView indicator = (ImageView) convertView.findViewById(R.id.ques_attempted);
        TextView number = (TextView) convertView.findViewById(R.id.ques_number);
        indicator.setVisibility(View.GONE);
        number.setVisibility(View.INVISIBLE);
        return convertView;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.spinner_item, parent, false);
        }

        ImageView indicator = (ImageView) convertView.findViewById(R.id.ques_attempted);
        TextView number = (TextView) convertView.findViewById(R.id.ques_number);
        if(getItem(position) < 0){
            indicator.setVisibility(View.GONE);
            number.setText("" + (-getItem(position)));
        }
        else{
            indicator.setVisibility(View.VISIBLE);
            number.setText(("" + getItem(position)));
        }
        return convertView;
    }
}
