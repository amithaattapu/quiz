package com.example.game;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class UserNameActivity extends AppCompatActivity {
TextView playerName;
String getUserUrl="https://6bo7itbvzd.execute-api.ap-south-1.amazonaws.com/prod?name=getUser&userName=";
SharedPreferences sharedPreferences;
    SharedPreferences.Editor myEdit;
String getUserResponse;
String tempPlayerId;
ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.player_name);
        Button start=findViewById(R.id.start);
        playerName=(TextView)findViewById(R.id.username) ;
        progressBar=(ProgressBar)findViewById(R.id.progressBar);
        sharedPreferences=getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
        myEdit= sharedPreferences.edit();
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(playerName.getText()))
                {playerName.setError( "player name is required*" );}
                else
                {getUserUrl+=playerName.getText().toString().trim();
                    try {
                        run();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }
        });
    }

    void run() throws IOException {
        progressBar.setVisibility(View.VISIBLE);
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(getUserUrl)
                .build();


        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                call.cancel();
                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(UserNameActivity.this, "Unable to connect to the Internet", Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
               String res=response.body().string();
               if(res!=null) {
                   getUserResponse =res;
                   getUserResponse = getUserResponse.replace("\\", "");
                   getUserResponse = getUserResponse.substring(1, getUserResponse.length());


                   try {
                       //question.setText(myResponse);
                       tempPlayerId = new JSONObject(getUserResponse).getString("userId");

                       Log.e("player id", tempPlayerId);

                       //Toast.makeText(UserNameActivity.this, tempPlayerId.toString(), Toast.LENGTH_SHORT).show();
                   } catch (JSONException e) {
                       e.printStackTrace();
                   }
                   myEdit.putString("playerId",tempPlayerId);
                   myEdit.apply();
                   Intent home = new Intent(UserNameActivity.this, Home.class);
                   startActivity(home);
                   finish();

               }

            }
        });

    }
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK)
        {
            moveTaskToBack(true);
        }
        return true;
    }

}
