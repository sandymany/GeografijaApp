package com.leticija.treeapp;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentManager;
import android.util.Log;

import com.leticija.treeapp.net.Requester;
import com.leticija.treeapp.tree.Tree;

import org.apache.http.FormattedHeader;
import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.FormBodyPart;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Trees {

    public static String passcode = null;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static JSONArray getAllTrees (Context context,FragmentManager fragmentManager) {

        JSONArray treesArray = null;

        String response = Requester.request("/api/get.php",new HashMap<String, String>(),null,context,fragmentManager);
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
    public static void sendNewTreeToServer (Context context, FragmentManager fragmentManager) throws IOException {

        HttpClient httpClient = new DefaultHttpClient();
        HttpPost postRequest = new HttpPost("https://posadistablo.000webhostapp.com/api/add.php");
        MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
        reqEntity.addPart("passcode",new StringBody("1234"));
        reqEntity.addPart("feature",new StringBody(Tree.features));
        try{

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            (Tree.imageBitmap).compress(Bitmap.CompressFormat.JPEG, 50, baos);
            byte[] data = baos.toByteArray();
            ByteArrayBody biteArrayBody = new ByteArrayBody(data, "tree.jpg");
            reqEntity.addPart("img", biteArrayBody);

        }
        catch(Exception e) {
            Log.v("Exception in Image", ""+e);
            reqEntity.addPart("img", new StringBody(""));
        }
        try {
            postRequest.setEntity(reqEntity);
            HttpResponse response = httpClient.execute(postRequest);
            BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
            String sResponse;
            StringBuilder s = new StringBuilder();
            while ((sResponse = reader.readLine()) != null) {
                s = s.append(sResponse);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Effects.showServerErrorDialog(context,fragmentManager);
        }

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