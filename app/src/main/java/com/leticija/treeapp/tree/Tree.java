package com.leticija.treeapp.tree;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Tree {

    String features = "";

    public void setFeatures (String type, String vrsta, String datum, String posadio, JSONArray coordinates) throws JSONException {

        //features:{"type":"Feature","properties":{"vrsta":x,"datum":x,"posadio":x,"image_url":""},"geometry":{"type":"Point","coordinates":[x,y]}}

        JSONObject treeObject = new JSONObject();

        treeObject.put("type","Feature");
        JSONObject propertiesObject = new JSONObject();
        propertiesObject.put("vrsta",vrsta);
        propertiesObject.put("datum",datum);
        propertiesObject.put("posadio",posadio);
        propertiesObject.put("image_url","");

        treeObject.put("properties",propertiesObject);

        JSONObject geometryObject = new JSONObject();
        geometryObject.put("type","Point");
        geometryObject.put("coordinates",coordinates);

        treeObject.put("geometry",geometryObject);

        String objectString = treeObject.toString();
        features = objectString;
        }

    public String getFeatures() {
        return features;
    }
}
