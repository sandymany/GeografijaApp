package com.leticija.treeapp.activities;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import com.leticija.treeapp.R;
import com.leticija.treeapp.net.Requester;
import com.leticija.treeapp.net.TaskQueue;

import java.sql.Connection;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class HomeActivity extends AppCompatActivity {

    Map<String,String> headers;
    String response;
    Context context;
    FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);

        context = getApplicationContext();
        fragmentManager = getSupportFragmentManager();

        //FIND WHAT YOU NEED
        Button addTreeButton = findViewById(R.id.button_newTree);
        Button viewCollectionButton = findViewById(R.id.button_viewCollection);

        addTreeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(HomeActivity.this,AddTreeActivity.class);
                startActivity(intent);

            }
        });

        viewCollectionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this,CollectionActivity.class);
                startActivity(intent);
            }
        });

    }
    @Override
    public void onBackPressed() {
        return;
    }
}