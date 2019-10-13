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

                /*
                headers = new HashMap<>();
                String features = "{\"type\":\"Feature\",\"properties\":{\"vrsta\":\"breza\",\"datum\":\"9.10.2019.\",\"posadio\":\"4.d\",\"image_url\":\"https://cdn.shopify.com/s/files/1/0014/4038/3023/files/japense-maple-specialist_2048x.jpg?v=1544794246\"},\"geometry\":{\"type\":\"Point\",\"coordinates\":[20,30]}}";

                String stringForHeaders = "passcode=1234&feature=";

                byte[] bytesEncoded = Base64.getEncoder().encode(features.getBytes());
                String encodedString = new String(bytesEncoded);

                stringForHeaders = stringForHeaders+encodedString;

                //headers.put("img",imgbase64encoded);

                String secondResponse = Requester.request("/api/add.php",headers,stringForHeaders);
                System.out.println("secondResponse: "+secondResponse);

*/
                //headers = new HashMap<>();
                //String secondResponse = Requester.request("/api/delete.php",headers,"passcode=1234&id=4");

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