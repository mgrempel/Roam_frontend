package com.pixelworks.roam;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.ApolloClient;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;

import org.jetbrains.annotations.NotNull;

public class LogInActivity extends AppCompatActivity {

    //Controls
    Button btnLogIn, btnCreateAccount;
    EditText editTextUserName, editTextPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        //find our controls
        btnLogIn = findViewById(R.id.btnLogIn);
        btnCreateAccount = findViewById(R.id.btnCreateAccount);
        editTextUserName = findViewById(R.id.editTextLoginUser);
        editTextPassword = findViewById(R.id.editTextLoginPassword);

        //Register event handlers
        btnLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                authenticateUser();
            }
        });

        btnCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Start our create account activity
                Intent intent = new Intent(v.getContext(), CreateAccountActivity.class);
                startActivity(intent);

                //We don't need a login if id and uuid exist, finish the activity.
                int key = SharedPreferencesHelper.getIntValue("id");
                String uuid = SharedPreferencesHelper.getStringValue("uuid");

                if(key != 0 && !uuid.equals("")) {
                    finish();
                }
            }
        });
    }

    //Handles user sign in logic
    private void authenticateUser() {
        //Open connection with the API
        ApolloClient apolloClient = ApolloClient.builder()
                .serverUrl(getString(R.string.api_location))
                .build();

        //Let's structure our query
        final LogInQuery logIn = LogInQuery.builder()
                .username(editTextUserName.getText().toString())
                .password(editTextPassword.getText().toString())
                .build();

        //Let's pass our query off to the api.
        apolloClient
                .query(logIn)
                .enqueue(
                        new ApolloCall.Callback<LogInQuery.Data>() {

                            LogInActivity logInActivity = LogInActivity.this;
                            @Override
                            public void onResponse(@NotNull Response<LogInQuery.Data> response) {
                                Log.d("TEST", "Successfully received log in data from the api.");

                                String uuid = response.getData().LogIn().uuid();
                                int id = response.getData().LogIn().id();
                                //Check to see if any information was returned
                                if(id == 0 || uuid.equals("")) {
                                    logInActivity.runOnUiThread(new Runnable() {
                                        public void run() {
                                            Toast.makeText(logInActivity, "Could not find account!", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                                else {
                                    //User was logged in, persist values.
                                    SharedPreferencesHelper.setIntValue("id", id);
                                    SharedPreferencesHelper.setStringValue("uuid", uuid);
                                    finish();
                                }
                            }

                            @Override
                            public void onFailure(@NotNull ApolloException e) {
                                Log.d("TEST", "Could not reach the api");
                                logInActivity.runOnUiThread(new Runnable() {
                                    public void run() {
                                        Toast.makeText(logInActivity, "An error has occurred!", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                );
    }
}