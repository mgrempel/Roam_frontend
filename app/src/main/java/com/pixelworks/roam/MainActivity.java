package com.pixelworks.roam;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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

    public void receiveData(Object[] incomingData) {
        Log.d("TEST", incomingData[0].toString());

        for(Object object : incomingData) {
            GetUsersFriendsByUUIDQuery.GetUserFriendsByUUID user = (GetUsersFriendsByUUIDQuery.GetUserFriendsByUUID)object;
            users.add(new User(user.userName(), user.firstName(), user.lastName(), user.description()));
        }

        //Apply the data to the list view
        ArrayAdapter<User> arrayAdapter = new ArrayAdapter<User>(
                this,
                android.R.layout.simple_list_item_1,
                users);
        //Update the ListView
        listView.setAdapter(arrayAdapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initialize our helpers.
        new SharedPreferencesHelper(this);

        //Initialize our controls
        users = new ArrayList<User>();
        listView = findViewById(R.id.lst_users);

        //Set event listeners
        listView.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                User selectedUser = users.get(position);

                //Start our view user activity
                Intent intent = new Intent(view.getContext(), ViewFriendActivity.class);
                intent.putExtra("user", selectedUser);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

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

    private void getFriendData() {
        //Create our client
        ApolloClient apolloClient = ApolloClient.builder()
                .serverUrl(getString(R.string.api_location))
                .build();

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
}