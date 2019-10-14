package com.leticija.treeapp.activities;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;

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
        scrollLayout = findViewById(R.id.scroll_layout_collection);

        headerToSend = new HashMap<>();

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
                    Effects.loadAllTreesToScrollview(responseObject,scrollLayout,context);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).subscribeMe();

    }
}
