package com.pixelworks.roam;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
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

public class CreateAccountActivity extends AppCompatActivity {

    //Controls
    private EditText editTextUserName, editTextPassword, editTextEmail, editTextFirstName, editTextLastName, editTextDescription;
    private Button btnCreateAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        //Let's find our controls
        editTextUserName = findViewById(R.id.editTextUserName);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextFirstName = findViewById(R.id.editTextFirstName);
        editTextLastName = findViewById(R.id.editTextLastName);
        editTextDescription = findViewById(R.id.editTextDescription);

        btnCreateAccount = findViewById(R.id.btnCreateAccount);

        ApolloClient apolloClient = ApolloClient.builder()
                .serverUrl(getString(R.string.api_location))
                .build();

        //Let's handle our click event
        btnCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editTextUserName.getText().toString().equals("") || editTextPassword.getText().toString().equals("")) {
                    Toast.makeText(v.getContext(), "Please enter a username and password", Toast.LENGTH_SHORT).show();
                }
                else {
                    //Create our GraphQL request
                    CreateUserMutation createUserMutation = CreateUserMutation.builder()
                            .userName(editTextUserName.getText().toString())
                            .password(editTextPassword.getText().toString())
                            .email(editTextEmail.getText().toString())
                            .firstName(editTextFirstName.getText().toString())
                            .lastName(editTextLastName.getText().toString())
                            .description(editTextDescription.getText().toString())
                            .build();

                    //Let's ring up the GQL server
                    apolloClient
                            .mutate(createUserMutation)
                            .enqueue(
                                    new ApolloCall.Callback<CreateUserMutation.Data>() {
                                        @Override
                                        public void onResponse(@NotNull Response<CreateUserMutation.Data> response) {
                                            if(response.getErrors() != null) {
                                                Log.d("TEST", "API success, invalid fields.");
                                            }
                                            else {
                                                int id = response.getData().createUser().id();
                                                String uuid = response.getData().createUser().uuid();
                                                Log.d("TEST", String.valueOf(id));

                                                //Persist our users data, end the activity.
                                                SharedPreferencesHelper.setIntValue("id", id);
                                                SharedPreferencesHelper.setStringValue("uuid", uuid);

                                                finish();
                                            }
                                        }

                                        @Override
                                        public void onFailure(@NotNull ApolloException e) {
                                            Log.d("TEST", e.getCause().toString());
                                            CreateAccountActivity createAccountActivity = CreateAccountActivity.this;
                                            createAccountActivity.runOnUiThread(new Runnable() {
                                                public void run() {
                                                    Toast.makeText(createAccountActivity, "An error has occurred!", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        }
                                    }
                            );
                }
            }
        });
    }
}