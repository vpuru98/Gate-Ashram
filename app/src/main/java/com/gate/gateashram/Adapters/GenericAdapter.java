package com.gate.gateashram.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.gate.gateashram.Helpers.Helper;
import com.gate.gateashram.Models.ListModelClass;
import com.gate.gateashram.R;

import java.util.List;

public class GenericAdapter extends ArrayAdapter<ListModelClass> {

    private Context mContext;
    private List<ListModelClass> mArrayList;

    public GenericAdapter(Context context, List objects) {
        super(context, 0, objects);
        mContext = context;
        mArrayList = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.generic_list_item, parent, false);

        ListModelClass listModelClass = mArrayList.get(position);

        TextView textView = (TextView) listItem.findViewById(R.id.text_view);
        textView.setText(Helper.reformatString(mArrayList.get(position).getmText()));

        return listItem;
    }
}
