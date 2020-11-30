package com.pixelworks.roam;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Debug;
import android.os.Parcelable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.ApolloClient;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<User> users;

    private ListView listView;
    private Button btnPost, btnUserPost, btnConnect;
    private Toolbar toolbar;
    private ApolloClient apolloClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Initialize our helpers.
        new SharedPreferencesHelper(this);

        //Initialize our client
        apolloClient = ApolloClient.builder()
                .serverUrl(getString(R.string.api_location))
                .build();

        //Initialize our controls
        users = new ArrayList<User>();
        listView = findViewById(R.id.lst_users);
        btnPost = findViewById(R.id.btnPost);
        btnUserPost = findViewById(R.id.btnUserPosts);
        btnConnect = findViewById(R.id.btnConnect);

        //Set event listeners
        listView.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                User selectedUser = users.get(position);


                Log.d("TEST", "I'm hitting the event listener for the list view.");

                //Start our view user activity
                Intent intent = new Intent(view.getContext(), ViewFriendActivity.class);
                intent.putExtra("user", selectedUser);
                startActivity(intent);
            }
        });

        listView.setOnItemLongClickListener(new ListView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                //Get user we want to unfriend
                User user = users.get(position);

                unFriendUser(user.getId(), position);

                return true;
            }
        });

        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), CreatePostActivity.class);
                startActivity(intent);
            }
        });

        btnUserPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ViewPostsActivity.class);
                startActivity(intent);
            }
        });

        btnConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), BluetoothConnectActivity.class);
                startActivity(intent);
            }
        });
    }
    //Toolbar logic
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
    //Toolbar logic
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        //Empty our arraylist
        this.users.clear();
        String uuid = SharedPreferencesHelper.getStringValue("uuid");
        if(uuid.equals("NOVALUE")) {
            //Redirect user to sign in page
            Intent intent = new Intent(this, LogInActivity.class);
            startActivity(intent);
        }
        else {
            getFriendData();
        }
    }

    //Handles logic for populating friend listview
    private void getFriendData() {
        //Create our query
        final GetUsersFriendsByUUIDQuery getFriends = GetUsersFriendsByUUIDQuery.builder()
                .uuid(SharedPreferencesHelper.getStringValue("uuid"))
                .build();

        //Make the call.
        apolloClient
                .query(getFriends)
                .enqueue(
                        new ApolloCall.Callback<GetUsersFriendsByUUIDQuery.Data>() {
                            MainActivity mainActivity = MainActivity.this;

                            @Override
                            public void onResponse(@NotNull Response<GetUsersFriendsByUUIDQuery.Data> response) {
                                Log.d("TEST", "Got a friend uuid response from the API.");
                                //Get the received data into our models
                                //Get all of our data
                                Object[] data = response.getData().GetUserFriendsByUUID().toArray();

                                //Send this back to the main thread.
                                mainActivity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        mainActivity.receiveData(data);
                                    }
                                });
                            }

                            @Override
                            public void onFailure(@NotNull ApolloException e) {
                                mainActivity.runOnUiThread(new Runnable() {
                                    public void run() {
                                        Toast.makeText(mainActivity, "An error has occurred!", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                );
    }

    //Handles logic for unfriending a user
    private void unFriendUser(int userId, int location) {
        //Define our mutation
        final RemoveFriendByIdMutation unFriend = RemoveFriendByIdMutation.builder()
                .uuid(SharedPreferencesHelper.getStringValue("uuid"))
                .id(userId)
                .build();

        //Execute mutation
        apolloClient
                .mutate(unFriend)
                .enqueue(
                        new ApolloCall.Callback<RemoveFriendByIdMutation.Data>() {
                            MainActivity main = MainActivity.this;
                            @Override
                            public void onResponse(@NotNull Response<RemoveFriendByIdMutation.Data> response) {
                                main.runOnUiThread(new Runnable() {
                                    public void run() {
                                        //Remove user from the array
                                        main.removeUser(location);
                                    }
                                });
                            }

                            @Override
                            public void onFailure(@NotNull ApolloException e) {
                                main.runOnUiThread(new Runnable() {
                                    public void run() {
                                        Toast.makeText(main, "An error has occured!", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                );
    }
    //Callback for populating friends listview
    public void receiveData(Object[] incomingData) {
        //For each bit of incoming data, parse into locally defined objects.
        for(Object object : incomingData) {
            GetUsersFriendsByUUIDQuery.GetUserFriendsByUUID user = (GetUsersFriendsByUUIDQuery.GetUserFriendsByUUID)object;

            Post[] posts = new Post[user.posts.size()];

            //Parse posts
            for(int i = 0; i < user.posts().size(); i++) {
                posts[i] = new Post(user.posts().get(i).title(), user.posts().get(i).content());
            }
            //Create new user
            users.add(new User(user.userName(), user.firstName(), user.lastName(), user.description(), user.id(), posts));
        }

        //Apply the data to the list view
        ArrayAdapter<User> arrayAdapter = new ArrayAdapter<User>(
                this,
                android.R.layout.simple_list_item_1,
                users);
        //Update the ListView
        listView.setAdapter(arrayAdapter);
    }

    //Callback for removing friends
    public void removeUser(int index) {
        //Remove user from our list
        users.remove(index);

        //Update the listview
        ArrayAdapter<User> arrayAdapter = new ArrayAdapter<User>(
                this,
                android.R.layout.simple_list_item_1,
                users
        );

        listView.setAdapter(arrayAdapter);
    }
}