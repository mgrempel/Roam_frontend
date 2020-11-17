package com.pixelworks.roam;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
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

public class CreateAccountActivity extends AppCompatActivity {

    private EditText editTextUserName, editTextPassword, editTextEmail, editTextFirstName, editTextLastName, editTextDescription;
    private Button btnCreateAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        //Let's find our controls
        editTextUserName = findViewById(R.id.editTextDescription);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextFirstName = findViewById(R.id.editTextFirstName);
        editTextLastName = findViewById(R.id.editTextLastName);
        editTextDescription = findViewById(R.id.editTextDescription);

        btnCreateAccount = findViewById(R.id.btnCreateAccount);

        ApolloClient apolloClient = ApolloClient.builder()
                .serverUrl("http://10.0.2.2:8080/query")
                .build();

        //Let's handle our click event
        btnCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Ensure all necessary fields are populated

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
                                        int id = response.getData().createUser().id();
                                        String uuid = response.getData().createUser().uuid();
                                        Log.d("TEST", String.valueOf(id));
                                        SharedPreferences sharedPreferences = getSharedPreferences("roam", Context.MODE_PRIVATE);
                                        SharedPreferences.Editor editor = sharedPreferences.edit();

                                        editor.putInt("id", id);
                                        editor.putString("uuid", uuid);
                                        editor.apply();
                                        finish();
                                    }

                                    @Override
                                    public void onFailure(@NotNull ApolloException e) {
                                        //Toast.makeText(CreateAccountActivity.this, "It didn't work!", Toast.LENGTH_LONG).show();
                                        Log.d("TEST", e.getCause().toString());
                                    }
                                }
                        );

                //Handle request to determine validity of response.
            }
        });
    }
}