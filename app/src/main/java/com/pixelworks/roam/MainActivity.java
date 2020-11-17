package com.pixelworks.roam;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Lets check and see if the user has a token.
        SharedPreferences sharedPreferences = getSharedPreferences("roam", Context.MODE_PRIVATE);
        String uuid = sharedPreferences.getString("sessionKey", "NOKEY");

        if(uuid.equals("NOKEY")) {
            //Redirect user to sign in page

        }
        else {
            //Let's hit the database and grab the users information.
        }
    }
}