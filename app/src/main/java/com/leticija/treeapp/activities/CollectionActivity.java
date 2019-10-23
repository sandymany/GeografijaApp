package com.leticija.treeapp.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.leticija.treeapp.Effects;
import com.leticija.treeapp.R;
import com.leticija.treeapp.net.Requester;
import com.leticija.treeapp.net.TaskQueue;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class CollectionActivity extends AppCompatActivity {

    String response;
    Map<String,String> headerToSend;
    Context context;
    FragmentManager fragmentManager;
    JSONObject responseObject;
    LinearLayout scrollLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.collection_activity);

        fragmentManager = getSupportFragmentManager();
        context = getApplicationContext();

        //FIND WHAT YOU NEED
        Button refreshButton = findViewById(R.id.refreshButton_collection);
        final ImageView loading = findViewById(R.id.loading_collection);
        final TextView ukupnoStabala = findViewById(R.id.ukupnoStabala_textView);
        scrollLayout = findViewById(R.id.scroll_layout_collection);

        headerToSend = new HashMap<>();

        loading.setVisibility(View.VISIBLE);
        Effects.setRotateAnimation(loading);

        TaskQueue.prepare().backgroundTask(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void run() {
                response = Effects.cleanHiddenCharacters(Requester.request("/api/get.php",headerToSend,null,context,fragmentManager));
                try {
                    responseObject = new JSONObject(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).guiTask(new Runnable() {
            @Override
            public void run() {
                try {
                    loading.clearAnimation();
                    loading.setVisibility(View.INVISIBLE);
                    Effects.loadAllTreesToScrollview(responseObject,scrollLayout,context,fragmentManager,ukupnoStabala);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).subscribeMe();

        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                finish();
                startActivity(intent);
            }
        });

    }
}
