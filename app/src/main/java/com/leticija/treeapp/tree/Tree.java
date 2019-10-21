package com.leticija.treeapp.tree;

import android.graphics.Bitmap;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Base64;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;

// VERY GOOD, DATA TRANSFER OBJECT KOJI SE MORE SERIALIZIRAT!
public class Tree {

    public static Bitmap imageBitmap;
    public static String features = ""; //encoded string with features
    public static String encodedImage = ""; //encoded bitmap
    public static String featuresUnencoded = "";

    //OVO JE LOŠE
    //NAPRAVI GETTERE I SETTERE ZA SVAKO POLJE, A NE SVE ODJEDNOM!
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void setFeatures (String vrsta, String datum, String posadio, JSONArray coordinates) throws JSONException {

        //{"type":"Feature","properties":{"vrsta":x,"datum":x,"posadio":x,"image_url":""},"geometry":{"type":"Point","coordinates":[x,y]}}

        JSONObject treeObject = new JSONObject();

        treeObject.put("type","Feature");
        JSONObject propertiesObject = new JSONObject();
        propertiesObject.put("vrsta",vrsta);
        propertiesObject.put("datum",datum);
        propertiesObject.put("posadio",posadio);
        propertiesObject.put("image_url","none");

        treeObject.put("properties",propertiesObject);

        JSONObject geometryObject = new JSONObject();
        geometryObject.put("type","Point");
        geometryObject.put("coordinates",coordinates);

        treeObject.put("geometry",geometryObject);

        String featuresString = treeObject.toString();

        featuresUnencoded = featuresString;
        System.out.println(featuresUnencoded);

        byte[] bytesEncoded = java.util.Base64.getEncoder().encode(featuresString.getBytes());
        String encodedString = new String(bytesEncoded);

        features = encodedString;
    }

    public static void setEncodedImage (Bitmap bitmap) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);
        byte[] b = baos.toByteArray();

        encodedImage = Base64.encodeToString(b,Base64.NO_WRAP);

    }

    public String getFeatures() {
        return features;
    }

    public String getEncodedImage (){
        return encodedImage;
    }

}
