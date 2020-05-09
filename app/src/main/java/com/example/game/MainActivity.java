package com.example.game;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPreferences=getSharedPreferences("MySharedPref",MODE_PRIVATE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(sharedPreferences.contains("playerId"))
                {
                    Intent HomeIntent=new Intent(MainActivity.this,Home.class);
                    startActivity(HomeIntent);
                    finish();
                }
                else {
                    Intent userNameIntent = new Intent(MainActivity.this, UserNameActivity.class);
                    startActivity(userNameIntent);
                    finish();
                }
            }
        },1500);
    }

}
