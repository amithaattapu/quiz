package com.example.game;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.Arrays;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class quiz extends AppCompatActivity {
TextView question,qno;
Button op1,op2,op3,op4,prev,next,home;
ImageButton pause,play;
int cur=0,ans[],crctAns[]=new int[5],score=0,noOfCorrectAnswers=0,notAttempted=0,audioDuration=0;
String[] correctAnswers=new String[5];
JSONObject quizJson;
MediaPlayer mediaPlayer = new MediaPlayer();
String setScoreUrl="https://6bo7itbvzd.execute-api.ap-south-1.amazonaws.com/prod?name=setScore&userId=";
JSONArray answers;
SeekBar playerSeekBar;
SharedPreferences sharedPreferences;
String quizString;
String temp;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_quiz);
        op1=(Button)findViewById(R.id.op1);
        op2=(Button)findViewById(R.id.op2);
        op3=(Button)findViewById(R.id.op3);
        op4=(Button)findViewById(R.id.op4);
        prev=(Button)findViewById(R.id.prev);
        next=(Button) findViewById(R.id.next);
        home=(Button)findViewById(R.id.home);
        play=(ImageButton) findViewById(R.id.play);
        pause=(ImageButton) findViewById(R.id.pause);
        question=(TextView)findViewById(R.id.question);
        qno=(TextView)findViewById(R.id.qno);
        ans=new int[5];
        playerSeekBar=(SeekBar)findViewById(R.id.playerSeekBar);
        sharedPreferences=getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
        Bundle bundle=getIntent().getExtras();
        quizString=bundle.getString("quizString");
        quizString=quizString.replace("\\","");
        try{
            quizJson= new JSONObject(quizString);
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        updateQuestion(cur);

        final Handler mHandler = new Handler();

        quiz.this.runOnUiThread(new Runnable() {

            @Override
            public void run() {

                if(mediaPlayer != null && play.getVisibility()==View.INVISIBLE){
                    if(!mediaPlayer.isPlaying())
                    {pause.setVisibility(View.INVISIBLE);
                        play.setVisibility(View.VISIBLE);
                        playerSeekBar.setProgress(0);
                    }
                    else {
                        int mCurrentPosition = mediaPlayer.getCurrentPosition() / 1000;
                        playerSeekBar.setProgress(mCurrentPosition);
                    }
                }
                mHandler.postDelayed(this, 100);
            }
        });
        playerSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(mediaPlayer != null && fromUser){
                    mediaPlayer.seekTo(progress * 1000);
                   // Toast.makeText(quiz.this, progress, Toast.LENGTH_LONG).show();
                }
            }
        });
        op1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(cur==5)
               {ans[cur-1]=1;
                   decideToShowAlertOrNot(1);
               }
               else {
                   ans[cur-1]=1;
                   updateQuestion(cur);
                }
            }
        });
        op2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(cur==5)
                {ans[cur-1]=2;
                    decideToShowAlertOrNot(1);
                }
                else {
                    ans[cur-1]=2;
                    updateQuestion(cur);
                }
            }
        });
        op3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(cur==5)
                {ans[cur-1]=3;
                    decideToShowAlertOrNot(1);
                }
                else {
                    ans[cur-1]=3;
                    updateQuestion(cur);
                }
            }
        });
        op4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(cur==5)
                {ans[cur-1]=4;
                 decideToShowAlertOrNot(1);
                }
                else {
                    ans[cur-1]=4;
                    updateQuestion(cur);
                }
            }
        });
        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(cur-2>=0)
                {
                    cur-=2;
                    updateQuestion(cur);
                }
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(cur!=5)
                {
                    updateQuestion(cur);
                }
                else
                {
                    decideToShowAlertOrNot(0);
                }
            }
        });
        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.pause();
                pause.setVisibility(View.INVISIBLE);
                play.setVisibility(View.VISIBLE);
            }
        });
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.start();
                play.setVisibility(View.INVISIBLE);
                pause.setVisibility(View.VISIBLE);
            }
        });
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               prompt();
            }
        });
    }

     void updateQuestion(int i)
    {
        try {
            mediaPlayer.reset();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setDataSource(quizJson.getJSONObject("quiz").getJSONArray("questions").getJSONObject(i).getString("voiceclipurl"));
            mediaPlayer.prepare(); // might take long! (for buffering, etc)
            mediaPlayer.start();
            audioDuration=mediaPlayer.getDuration();
            playerSeekBar.setMax(audioDuration/1000);
            play.setVisibility(View.INVISIBLE);
            pause.setVisibility(View.VISIBLE);
            if(i==0)
                prev.setEnabled(false);
            else
                prev.setEnabled(true);
            if(ans[i]!=0)
                next.setEnabled(true);
            else
                next.setEnabled(false);
            if(i==4)//last question
                next.setEnabled(true);
            question.setText(quizJson.getJSONObject("quiz").getJSONArray("questions").getJSONObject(i).getString("qtext"));
            answers = quizJson.getJSONObject("quiz").getJSONArray("questions").getJSONObject(i).getJSONArray("options");
            op1.setText(answers.get(0).toString());
            op2.setText(answers.get(1).toString());
            op3.setText(answers.get(2).toString());
            op4.setText(answers.get(3).toString());

            if(crctAns[i]==0) {
                crctAns[i] = Integer.parseInt(quizJson.getJSONObject("quiz").getJSONArray("questions").getJSONObject(i).getString("correctanswer"));
                correctAnswers[i]=answers.get(crctAns[i]-1).toString();
            }
        }
        catch(Exception e)
        {
            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
        }
        qno.setText((cur+1)+"/5");
        cur++;
        if(cur==5)
            next.setText("SUBMIT");
        else
            next.setText("NEXT");
        updatecolor(cur-1);

    }

    void updatecolor(int i)
    {op1.setBackgroundColor(getResources().getColor(R.color.blue));
     op2.setBackgroundColor(getResources().getColor(R.color.blue));
     op3.setBackgroundColor(getResources().getColor(R.color.blue));
     op4.setBackgroundColor(getResources().getColor(R.color.blue));

        if(i!=5 && ans[i]!=0)
        {
            switch (ans[i])
            {
                case 1:op1.setBackgroundColor(getResources().getColor(R.color.white));
                    break;
                case 2:op2.setBackgroundColor(getResources().getColor(R.color.white));
                    break;
                case 3:op3.setBackgroundColor(getResources().getColor(R.color.white));
                    break;
                case 4:op4.setBackgroundColor(getResources().getColor(R.color.white));
                    break;
            }
        }
    }
    void decideToShowAlertOrNot(int k)
    {if(k==0)
    {
        submitQuiz();
    }
    else
    {updatecolor(4);
        AlertDialog.Builder alertDialogBuilder=new AlertDialog.Builder(quiz.this);
        alertDialogBuilder.setMessage("Do you want to submit the quiz?");
        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int j) {
                submitQuiz();
            }
        })
        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int j) {
                           //for last quetion , so 5-1=4
                    }
        });
        AlertDialog alertDialog=alertDialogBuilder.create();
        alertDialog.show();
    }

    }
    void submitQuiz()
    {
        Intent scoreIntent=new Intent(quiz.this,score.class);
        for(int i=0;i<5;i++)
        {
            if(crctAns[i]==ans[i])
            {
                score++;
                noOfCorrectAnswers++;
            }
            if(ans[i]==0)
                notAttempted++;
        }
        scoreIntent.putExtra("score",score*10);
        scoreIntent.putExtra("noOfCorrectAnswers",noOfCorrectAnswers);
        scoreIntent.putExtra("attempted",5-notAttempted);
        scoreIntent.putExtra("correctAnswers",correctAnswers);
        setScoreUrl+=sharedPreferences.getString("playerId","playerId")+"&score="+(score*10)+"&clientId=2";
        try {
            runSetScore();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(mediaPlayer.isPlaying())
        mediaPlayer.pause();
        mediaPlayer.stop();
        mediaPlayer.release();
        mediaPlayer=null;
        startActivity(scoreIntent);
    }

    void runSetScore() throws IOException {
        Log.e("inside on run","Inside on run");
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(setScoreUrl)
                .build();

        Log.e("inside on r","Inside on r");
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                call.cancel();
                Log.e("fail","fail");
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {

                 temp=response.body().string();


            }
        });

    }
    void prompt()
    {
        AlertDialog.Builder alertDialogBuilder=new AlertDialog.Builder(quiz.this);
        alertDialogBuilder.setMessage("Do you want to quit the quiz? (progress will be lost)");
        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(mediaPlayer.isPlaying())
                mediaPlayer.pause();
                mediaPlayer.stop();
                mediaPlayer.release();
                mediaPlayer=null;
                Intent home=new Intent(quiz.this,Home.class);
                startActivity(home);
            }
        })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
        AlertDialog alertDialog=alertDialogBuilder.create();
        alertDialog.show();
    }
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK)
        {
             prompt();
        }
        return true;
    }
}
