package com.example.newsaggregator;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class DrawerItemAdapter extends ArrayAdapter<DrawerItem>
{
    private Context context;
    private int layoutResourceId;
    private DrawerItem data[] = null;

    public DrawerItemAdapter(Context context, int layoutResourceId, DrawerItem [] data)
    {
        super(context, layoutResourceId, data);
        this.context = context;
        this.layoutResourceId = layoutResourceId;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();

        View v = inflater.inflate(layoutResourceId, parent, false);

        TextView textView = (TextView) v.findViewById(R.id.article_author);

        DrawerItem choice = data[position];

        textView.setTextColor(Color.WHITE);

        return v;
    }
}
