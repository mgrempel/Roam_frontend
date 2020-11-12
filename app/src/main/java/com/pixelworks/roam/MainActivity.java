package com.pixelworks.roam;

import androidx.appcompat.app.AppCompatActivity;

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

public class MainActivity extends AppCompatActivity {

    private EditText editTextUserName, editTextPassword, editTextEmail, editTextFirstName, editTextLastName, editTextDescription;
    private Button btnCreateAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
                                        int result = response.getData().createUser().id();
                                        Log.d("TEST", String.valueOf(result));
                                    }

                                    @Override
                                    public void onFailure(@NotNull ApolloException e) {
                                        //Toast.makeText(MainActivity.this, "It didn't work!", Toast.LENGTH_LONG).show();
                                        Log.d("TEST", e.getCause().toString());
                                    }
                                }
                        );

                //Handle request to determine validity of response.
            }
        });
    }
}