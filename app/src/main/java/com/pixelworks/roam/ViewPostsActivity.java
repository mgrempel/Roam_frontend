package com.pixelworks.roam;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.ApolloClient;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;

import org.jetbrains.annotations.NotNull;

public class ViewPostsActivity extends AppCompatActivity {

    TextView txtUserName, txtDescription;
    ListView lstPosts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_posts);

        //Initialize controls
        txtUserName = findViewById(R.id.txtUserName);
        txtDescription = findViewById(R.id.txtDescription);
        lstPosts = findViewById(R.id.lstPosts);

        //Get our posts
        ApolloClient apolloClient = ApolloClient.builder()
                .serverUrl(getString(R.string.api_location))
                .build();

        final GetUserTreeByUUIDQuery getSelf = GetUserTreeByUUIDQuery.builder()
                .uuid(SharedPreferencesHelper.getStringValue("uuid"))
                .build();

        apolloClient
                .query(getSelf)
                .enqueue(
                        new ApolloCall.Callback<GetUserTreeByUUIDQuery.Data>() {
                            ViewPostsActivity viewPostsActivity = ViewPostsActivity.this;

                            @Override
                            public void onResponse(@NotNull Response<GetUserTreeByUUIDQuery.Data> response) {

                            }

                            @Override
                            public void onFailure(@NotNull ApolloException e) {
                                viewPostsActivity.runOnUiThread(new Runnable() {
                                    public void run() {
                                        Toast.makeText(viewPostsActivity, "An error has occurred!", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                );
    }
}