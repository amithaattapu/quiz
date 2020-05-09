package com.example.game;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CorrectAnswerAdapter extends BaseAdapter {
    String[] ans;
    Context context;
    LayoutInflater inflater;
    CorrectAnswerAdapter(Context applicationContext,String[] ans)
    {
        this.context=context;
        this.ans=ans;
        inflater=LayoutInflater.from(applicationContext);
    }
    @Override
    public int getCount() {
        return ans.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view=inflater.inflate(R.layout.activity_answer_view,null);
        TextView no=(TextView)view.findViewById(R.id.no);
        TextView answer=(TextView) view.findViewById(R.id.answer);
        answer.setText(ans[i]);
        no.setText((i+1)+"");
        return view;
    }
}
