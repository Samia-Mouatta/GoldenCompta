package com.example.aurore.goldencompta;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;


public class DateAdapter extends ArrayAdapter<String> {
    private Context context;
    private List<String> dateList;

    public DateAdapter(Context context, int textViewResourceId,
                       List<String> values) {
        super(context, textViewResourceId, values);
        this.context = context;
        this.dateList = values;
    }

    public int getCount(){
        return dateList.size();
    }

    public String getItem(int position){
        return dateList.get(position);
    }

    public long getItemId(int position){
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView view = new TextView(context);
        view.setTextColor(Color.BLACK);
        view.setGravity(Gravity.CENTER);
        view.setText(dateList.get(position));

        return view;
    }

    //View of Spinner on dropdown Popping

    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        TextView view = new TextView(context);
        view.setTextColor(Color.BLACK);
        view.setText(dateList.get(position));
        view.setHeight(60);

        return view;
    }

}
