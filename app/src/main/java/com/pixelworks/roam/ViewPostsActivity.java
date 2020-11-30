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

import java.util.ArrayList;

public class ViewPostsActivity extends AppCompatActivity {

    private TextView txtUserName, txtDescription;
    private ListView lstPosts;

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

        //Structure query
        final GetUserTreeByUUIDQuery getSelf = GetUserTreeByUUIDQuery.builder()
                .uuid(SharedPreferencesHelper.getStringValue("uuid"))
                .build();

        //Execute query
        apolloClient
                .query(getSelf)
                .enqueue(
                        new ApolloCall.Callback<GetUserTreeByUUIDQuery.Data>() {
                            ViewPostsActivity viewPostsActivity = ViewPostsActivity.this;

                            @Override
                            public void onResponse(@NotNull Response<GetUserTreeByUUIDQuery.Data> response) {
                                //Call our listener to pass off the details.
                                viewPostsActivity.runOnUiThread(new Runnable() {
                                    public void run() {
                                        viewPostsActivity.response(response.getData().GetUserTreeByUUID().userName(),
                                                                   response.getData().GetUserTreeByUUID().description(),
                                                                   response.getData().GetUserTreeByUUID().posts().toArray());
                                    }
                                });
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

    //Handles the response from the thread contacting the API. contains information about the users username, description, and their posts.
    public void response(String username, String description, Object[] posts) {
        txtUserName.setText(username);
        txtDescription.setText(description);

        //Handle the posts for our listview
        ArrayList<Post> selfPosts = new ArrayList<Post>();
        for(Object object : posts) {
            GetUserTreeByUUIDQuery.Post selfPost = (GetUserTreeByUUIDQuery.Post)object;

            selfPosts.add(new Post(selfPost.title(), selfPost.content()));
        }

        //Update our listview
        ArrayAdapter<Post> arrayAdapter = new ArrayAdapter<Post>(
                this,
                android.R.layout.simple_list_item_1,
                selfPosts
        );
        lstPosts.setAdapter(arrayAdapter);
    }
}