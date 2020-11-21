package com.pixelworks.roam;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class ViewFriendActivity extends AppCompatActivity {

    private User user;
    private TextView txtUserName, txtDescription;
    private ListView lstPosts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_friend);

        Log.d("TEST", "Starting the view friend activity");

        //Get intent data
        user = (User)getIntent().getSerializableExtra("user");

        //initialize controls
        txtUserName = findViewById(R.id.txtUserName);
        txtDescription = findViewById(R.id.txtDescription);
        lstPosts = findViewById(R.id.lstPosts);

        //Populate user information
        txtUserName.setText(user.toString());
        txtDescription.setText(user.getDescription());

        //Populate posts
        ArrayAdapter<Post> arrayAdapter = new ArrayAdapter<Post>(
                this,
                android.R.layout.simple_list_item_1,
                user.getPosts()
        );

        lstPosts.setAdapter(arrayAdapter);
    }
}