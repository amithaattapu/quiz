package com.example.game;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class previousScores extends AppCompatActivity {
ListView scoreList;
ImageButton back;
String getHistoryUrl="https://6bo7itbvzd.execute-api.ap-south-1.amazonaws.com/prod?name=getHistory&userId=";
JSONObject scoresJson;
String scoresResponse;
int[] scores,clientTypes;
int n;
String[] dates;
SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_previous_scores);
        scoreList= (ListView)findViewById(R.id.scoreList);
        back=(ImageButton) findViewById(R.id.back);
        sharedPreferences=getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
        String playerId=sharedPreferences.getString("playerId","");
        getHistoryUrl+=playerId;
        try
        {
            run();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        while(scoresJson==null);
        parse();
        Log.e("here","here");
        Log.e(dates[0],dates[0]);
        CustomAdapter customAdapter = new CustomAdapter(getApplicationContext(),dates,scores,clientTypes);
        scoreList.setAdapter(customAdapter);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
    void run() throws IOException
    {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(getHistoryUrl)
                .build();

        Log.e("inside on r","Inside on r");
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                call.cancel();
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {

                scoresResponse = response.body().string();
                scoresResponse=scoresResponse.replace( "\\","");
                scoresResponse=scoresResponse.substring(1,scoresResponse.length());

                try{
                    //question.setText(myResponse);
                    scoresJson= new JSONObject(scoresResponse);
                    Log.e(scoresJson.toString(),scoresJson.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    void parse()
    {try {
          n = scoresJson.getJSONArray("scores").length();
Log.e("size"+n,"size"+n);
       scores=new int[n];
       dates=new String[n];
       clientTypes=new int[n];
       JSONObject temp;
       for(int i=0;i<n;i++)
       {
           scores[i]=scoresJson.getJSONArray("scores").getJSONObject(i).getInt("score");
           dates[i]=scoresJson.getJSONArray("scores").getJSONObject(i).getString("datetimeString");
           //dates[i]=dates[i].substring(0,8);
           clientTypes[i]=scoresJson.getJSONArray("scores").getJSONObject(i).getInt("clientId");
       }
    }
    catch(Exception e)
    {
        e.printStackTrace();
    }
    }
}
