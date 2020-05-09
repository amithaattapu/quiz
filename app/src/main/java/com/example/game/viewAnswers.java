package com.example.game;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class viewAnswers extends AppCompatActivity {
ListView listview;
String cAnswer[]=new String[5];
ImageButton back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.answers);
        listview=(ListView)findViewById(R.id.answersList);
        back=(ImageButton)findViewById(R.id.back);
        Bundle bundle=getIntent().getExtras();
        cAnswer=bundle.getStringArray("correctAnswers");
        CorrectAnswerAdapter correctAnswerAdapter=new CorrectAnswerAdapter(getApplicationContext(),cAnswer);
        listview.setAdapter(correctAnswerAdapter);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }
}
