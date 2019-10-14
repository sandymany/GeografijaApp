package com.leticija.treeapp.activities;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import com.leticija.treeapp.R;
import com.leticija.treeapp.net.Requester;
import com.leticija.treeapp.net.TaskQueue;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class HomeActivity extends AppCompatActivity {

    Map<String,String> headers;
    String response;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);

        //FIND WHAT YOU NEED
        Button addTreeButton = findViewById(R.id.button_newTree);
        Button viewCollectionButton = findViewById(R.id.button_viewCollection);

        TaskQueue.prepare().backgroundTask(new Runnable() {
            @TargetApi(Build.VERSION_CODES.O)
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void run() {

                response = Requester.request("/api/get.php",new HashMap<String, String>(),null);
                System.out.println(response);

            }

        }).subscribeMe();

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