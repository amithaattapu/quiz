package com.example.game;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.FileUtils;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class apiProgressActivity extends AppCompatActivity {
String myResponse,fetchQuizUrl="https://6bo7itbvzd.execute-api.ap-south-1.amazonaws.com/prod?name=getQuiz";
ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_api_progress);
        progressBar=(ProgressBar) findViewById(R.id.progressBar);

        try {
            run();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    @Override
    protected void onResume()
    {
        super.onResume();
    }
    void run() throws IOException {
        progressBar.setVisibility(View.VISIBLE);
        Log.e("inside on run","Inside on run");
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(fetchQuizUrl)
                .build();

        Log.e("inside on r","Inside on r");
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                call.cancel();
                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(apiProgressActivity.this, "Unable to connect to the Internet", Toast.LENGTH_LONG).show();
                    }
                });

                Intent intent = new Intent(apiProgressActivity.this, Home.class);
                startActivity(intent);
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
String res=response.body().string();

                if(res!=null) {
                    myResponse = res;
                    myResponse=myResponse.replace( "\\","");
                    myResponse=myResponse.substring(1,myResponse.length());
                    Intent intent = new Intent(apiProgressActivity.this, quiz.class);
                    intent.putExtra("quizString", myResponse);
                    //Toast.makeText(this, "here", Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                }else{
                    Toast.makeText(apiProgressActivity.this, "Unable to get any response", Toast.LENGTH_SHORT).show();
                }
                //  myResponse="{\"quiz\":{\"questions\":[{\"qtext\":\"Who is the author and composer of the below audio?\",\"voiceclipurl\":\"https://quiz-audio-files.s3.amazonaws.com/janaganamana.mp3\",\"correctanswer\":1,\"options\":[\"Rabindranath Tagore\",\"Bankim Chandra Chatterjee\",\"Muhammad Iqbal\",\"Sarojini Naidu\"],\"qid\":1},{\"qtext\":\"Guess the Musical Instrument?\",\"voiceclipurl\":\"https://quiz-audio-files.s3.amazonaws.com/veena.mp3\",\"correctanswer\":3,\"options\":[\"Violen\",\"Guitar\",\"Veena\",\"Piano\"],\"qid\":2},{\"qtext\":\"Which movie has this BGM?\",\"voiceclipurl\":\"https://quiz-audio-files.s3.amazonaws.com/bahubali.mp3\",\"correctanswer\":4,\"options\":[\"Manikarnika\",\"KGF\",\"Padmavat\",\"Bahubali\"],\"qid\":3},{\"qtext\":\"Guess the Animal?\",\"voiceclipurl\":\"https://quiz-audio-files.s3.amazonaws.com/tiger.mp3\",\"correctanswer\":2,\"options\":[\"Horse\",\"Tiger\",\"Dinosaur\",\"Fox\"],\"qid\":4},{\"qtext\":\"Guess the movie of this dialogue?\",\"voiceclipurl\":\"https://quiz-audio-files.s3.amazonaws.com/Sholay.mp3\",\"correctanswer\":1,\"options\":[\"Sholay\",\"Don\",\"Zanjeer\",\"Saat Hindustani\"],\"qid\":5}]},\"quizId\":1}";
               /* quiz.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            question.setText(myResponse);
                            quizJson= new JSONObject(myResponse);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });*/

            }
        });

    }

}
