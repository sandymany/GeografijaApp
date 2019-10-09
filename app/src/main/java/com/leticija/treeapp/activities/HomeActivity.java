package com.leticija.treeapp.activities;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.leticija.treeapp.R;
import com.leticija.treeapp.net.Requester;
import com.leticija.treeapp.net.TaskQueue;

import java.util.HashMap;

public class HomeActivity extends AppCompatActivity {

    String response;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);

        final TextView textView = findViewById(R.id.text_home);

        TaskQueue.prepare().backgroundTask(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void run() {

                response = Requester.request("/api/get.php",new HashMap<String, String>(),null);
            }

        }).guiTask(new Runnable() {
            @Override
            public void run() {
                 textView.setText("RESPONSE JSON:\n"+response);
            }
        }).subscribeMe();

    }
}
