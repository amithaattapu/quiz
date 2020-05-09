package com.example.game;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class score extends AppCompatActivity {
TextView sc,ca,qa;
Button viewAnswers,newGame,previousScores,home;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_score);
        final Bundle bundle = getIntent().getExtras();
        int quizScore = bundle.getInt("score");
        int noOfCorrectAnswers=bundle.getInt("noOfCorrectAnswers");
        int attempted=bundle.getInt("attempted");
        final String[] correctAnswers=bundle.getStringArray("correctAnswers");
        sc = (TextView) findViewById(R.id.score);
        ca= (TextView) findViewById(R.id.CAnswers);
        qa= (TextView) findViewById(R.id.Qattempted);
        viewAnswers=(Button) findViewById(R.id.viewAnswers);
        newGame=(Button) findViewById(R.id.newGame);
        previousScores=(Button) findViewById(R.id.previousScores);
        home=(Button)findViewById(R.id.home);

            sc.setText(String.valueOf(quizScore));
            qa.setText(attempted+"/5");
            ca.setText(noOfCorrectAnswers+"/5");
         viewAnswers.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 Intent viewAns=new Intent(score.this,viewAnswers.class);
                 viewAns.putExtra("correctAnswers",correctAnswers);
                 startActivity(viewAns);
             }
         });
        newGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newG=new Intent(score.this,apiProgressActivity.class);
                startActivity(newG);
            }
        });
        previousScores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent previousS=new Intent(score.this,previousScores.class);
                startActivity(previousS);
            }
        });
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent home=new Intent(score.this,Home.class);
                startActivity(home);
            }
        });
    }
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK)
        {
            Intent intent=new Intent(score.this,Home.class);
            startActivity(intent);
        }
        return true;
    }
}
