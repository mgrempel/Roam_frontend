package com.pixelworks.roam;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.ApolloClient;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;

import org.jetbrains.annotations.NotNull;

public class CreatePostActivity extends AppCompatActivity {

    EditText txtTitle, txtContent;
    Button btnPost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);

        //Set up our controls
        txtTitle = findViewById(R.id.txtTitle);
        txtContent = findViewById(R.id.txtContent);
        btnPost = findViewById(R.id.btnPost);

        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Check to ensure our fields aren't empty
                if(txtTitle.getText().toString().equals("") || txtContent.getText().toString().equals("")) {
                    Toast.makeText(v.getContext(), "Please fill out all fields!", Toast.LENGTH_SHORT).show();
                }
                else {
                    //Create our query
                    ApolloClient apolloClient = ApolloClient.builder()
                            .serverUrl(getString(R.string.api_location))
                            .build();

                    //Create our graphql request
                    CreatePostMutation createPostMutation = CreatePostMutation.builder()
                            .title(txtTitle.getText().toString())
                            .content(txtContent.getText().toString())
                            .uuid(SharedPreferencesHelper.getStringValue("uuid"))
                            .build();

                    //Shoot query up to the API
                    apolloClient
                            .mutate(createPostMutation)
                            .enqueue(
                                    new ApolloCall.Callback<CreatePostMutation.Data>() {
                                        @Override
                                        public void onResponse(@NotNull Response<CreatePostMutation.Data> response) {
                                            finish();
                                        }

                                        @Override
                                        public void onFailure(@NotNull ApolloException e) {
                                            CreatePostActivity createPostActivity = CreatePostActivity.this;
                                            createPostActivity.runOnUiThread(new Runnable() {
                                                public void run() {
                                                    Toast.makeText(createPostActivity, "An error has occurred!", Toast.LENGTH_SHORT).show();
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