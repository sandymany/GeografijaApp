package com.leticija.treeapp;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentManager;
import com.leticija.treeapp.net.Requester;
import com.leticija.treeapp.tree.Tree;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Base64;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Trees {

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static JSONArray getAllTrees () {

        JSONArray treesArray = null;

        String response = Requester.request("/api/get.php",new HashMap<String, String>(),null);
        try {
            JSONObject responseJSON = new JSONObject(response);
            JSONArray featuresArray = responseJSON.getJSONArray("features");
            treesArray = featuresArray;

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return treesArray;

    }

    @TargetApi(Build.VERSION_CODES.O)
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static void sendNewTreeToServer () {

        Map<String,String> headersToSend = new HashMap<>();
        //headersToSend.put("img",Tree.encodedImage);

        String bodyToSend = "passcode=1234&feature=";
        bodyToSend+=Tree.features;

        Requester.request("/api/add.php",headersToSend,bodyToSend);

    }

    public static boolean checkAllFields (FragmentManager fragmentManager,Context context) throws JSONException {

        boolean showDialog = false;

        JSONObject featuresObject = new JSONObject(Tree.featuresUnencoded);
        JSONObject propertiesObject = (JSONObject) featuresObject.get("properties");
        JSONObject geometryObject = (JSONObject) featuresObject.get("geometry");
        JSONArray coordinatesArray = (JSONArray) geometryObject.get("coordinates");

        Iterator<String> keys = propertiesObject.keys();
        while(keys.hasNext()) {
            String key = keys.next();
            if (propertiesObject.get(key).equals("") || propertiesObject.get(key)==null) {
                showDialog = true;
            }
        }

        if (coordinatesArray.length() == 0) {
            showDialog = true;
        }

        if (showDialog == true) {
            Effects.showEmptyFieldsDialog(context,fragmentManager);
            return false;
        }
        return true;
    }


}
// HEADER: parametri: features, passcode
// features:{"type":"Feature","properties":{"vrsta":x,"datum":x,"posadio":x,"image_url":""},"geometry":{"type":"Point","coordinates":[x,y]}}
//image url: https://cdn.shopify.com/s/files/1/0014/4038/3023/files/japense-maple-specialist_2048x.jpg?v=1544794246

/* parsanje podatki iz gettanog JSON-a
                try {
                    JSONObject responseJSON = new JSONObject(response);
                    JSONArray featuresArray = responseJSON.getJSONArray("features");

                    for (int i = 0; i < featuresArray.length(); i++) {
                        System.out.println(featuresArray.get(i));
                        JSONObject object = (JSONObject) featuresArray.get(i);
                        JSONObject geometry = (JSONObject) object.get("geometry");
                        JSONArray coordinatesArray = geometry.getJSONArray("coordinates");
                        System.out.println("coordinates: "+coordinatesArray+"FIRST COORDINATE: "+coordinatesArray.get(0)+" SECOND COORDINATE: "+coordinatesArray.get(1));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                */