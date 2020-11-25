package com.pixelworks.roam;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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

                    //Shoot query up to the API
                }
            }
        });
    }
}