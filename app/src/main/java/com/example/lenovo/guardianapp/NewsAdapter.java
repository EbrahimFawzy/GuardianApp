package com.example.lenovo.guardianapp;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class NewsAdapter extends ArrayAdapter<NewsClass> {
    public NewsAdapter(Activity context, ArrayList<NewsClass> quakes) {
        super(context, 0, quakes);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.items_list, parent, false);
        }
        NewsClass currentNew = getItem(position);
        TextView titleText = (TextView) listItemView.findViewById(R.id.titleView);
        titleText.setText(currentNew.getTitle());
        TextView nameText = (TextView) listItemView.findViewById(R.id.nameView);
        nameText.setText(currentNew.getSectionName());
        TextView authorText = (TextView) listItemView.findViewById(R.id.autherName);
        if (currentNew.getAuthor() == " ") {
            authorText.setText("");
        } else {
            authorText.setText(currentNew.getAuthor());
        }
        TextView datePublished = (TextView) listItemView.findViewById(R.id.dateView);
        if (currentNew.getDate() == null) {
            datePublished.setText("");
        } else {
            datePublished.setText(currentNew.getDate());
        }
        return listItemView;
    }
}
