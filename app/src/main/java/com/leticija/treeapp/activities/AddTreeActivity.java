package com.leticija.treeapp.activities;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.leticija.treeapp.R;
import com.leticija.treeapp.tree.Tree;
import org.json.JSONArray;
import org.json.JSONException;


public class AddTreeActivity  extends AppCompatActivity {

    Context context;
    Button placePickerButton;
    TextView coordinatesTextView;
    int PLACE_PICKER_REQUEST=1;
    String posadio;
    String datum;
    String vrsta;
    JSONArray koordinate;
    Button gotovoButton;
    Button umetniSlikuButton;
    public static ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_tree_activity);

        context = getApplicationContext();

        //FIND WHAT YOU NEED
        imageView = findViewById(R.id.image_add_tree);
        umetniSlikuButton = findViewById(R.id.umetniSliku_button);
        gotovoButton = findViewById(R.id.gotovo_button);
        placePickerButton = findViewById(R.id.bt_picker);
        coordinatesTextView = findViewById(R.id.text_view);

        umetniSlikuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddTreeActivity.this,ImageHandlerActivity.class);
                startActivity(intent);
            }
        });

        gotovoButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                EditText posadioEdit = findViewById(R.id.posadio_editText);
                posadio = posadioEdit.getText().toString();
                EditText datumEdit = findViewById(R.id.datum_editText);
                datum = datumEdit.getText().toString();
                EditText vrstaEdit = findViewById(R.id.vrstaStabla_editText);
                vrsta = vrstaEdit.getText().toString();

                try {
                    Tree.setFeatures(vrsta,datum,posadio,koordinate);
                    Tree.setEncodedImage(Tree.imageBitmap);
                } catch (JSONException e) {
                    System.out.println("EXCEPTION IN SETTING FEATURES !");
                    e.printStackTrace();
                }

                //slanje na server
                //prije toga provjera jel sve ispunjeno !
                System.out.println("SENDING DATA TO SERVER !!!");
            }
        });

        placePickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WifiManager wifiManager = (WifiManager)context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                wifiManager.setWifiEnabled(false);

                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

                wifiManager.setWifiEnabled(true);
                try {
                    startActivityForResult(builder.build(AddTreeActivity.this),PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode==PLACE_PICKER_REQUEST) {
            if (resultCode==RESULT_OK) {
                Place place = PlacePicker.getPlace(data,this);
                StringBuilder stringBuilder = new StringBuilder();
                String latitude = String.valueOf(place.getLatLng().latitude);
                String longitude = String.valueOf(place.getLatLng().longitude);
                stringBuilder.append("G.Š: ");
                stringBuilder.append(latitude);
                stringBuilder.append(" G.D: ");
                stringBuilder.append(longitude);

                koordinate = new JSONArray();
                koordinate.put(latitude);
                koordinate.put(longitude);

                coordinatesTextView.setText(stringBuilder.toString());

            }
        }
    }
}