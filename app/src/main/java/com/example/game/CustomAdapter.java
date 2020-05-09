package com.example.game;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomAdapter extends BaseAdapter {
    Context context;
    int[] scores,clientTypes;
    String[] dates;
    LayoutInflater inflater;
    CustomAdapter(Context applicationContext, String[] dates, int[] scores,int[] clientTypes)
    {
        this.context=context;
        this.dates=dates;
        this.scores=scores;
        this.clientTypes=clientTypes;
        inflater=LayoutInflater.from(applicationContext);

    }
    @Override
    public int getCount() {
        return scores.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.activity_listview,null);
        TextView score = (TextView) view.findViewById(R.id.score);
        TextView date = (TextView) view.findViewById(R.id.date);
        score.setText(Integer.toString(scores[i]));
        date.setText(dates[i]);
        //type.setImageResource("a"+clientTypes[i]+".png");
        return view;
    }
}
