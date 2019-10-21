package com.leticija.treeapp.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentManager;
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
import com.leticija.treeapp.Effects;
import com.leticija.treeapp.R;
import com.leticija.treeapp.Trees;
import com.leticija.treeapp.net.TaskQueue;
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
    Button rotateButton;
    Bitmap rotatedBitmap;
    ImageView loading;
    FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_tree_activity);

        context = getApplicationContext();
        fragmentManager = getSupportFragmentManager();

        //FIND WHAT YOU NEED
        loading = findViewById(R.id.imageView_loading);
        imageView = findViewById(R.id.image_add_tree);
        umetniSlikuButton = findViewById(R.id.umetniSliku_button);
        gotovoButton = findViewById(R.id.gotovo_button);
        placePickerButton = findViewById(R.id.bt_picker);
        coordinatesTextView = findViewById(R.id.text_view);
        rotateButton = findViewById(R.id.rotate_button);

        Tree.imageBitmap = ((BitmapDrawable)imageView.getDrawable()).getBitmap();
        koordinate = new JSONArray();

        umetniSlikuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddTreeActivity.this,ImageHandlerActivity.class);
                startActivity(intent);
                TaskQueue.prepare().backgroundTask(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }).guiTask(new Runnable() {
                    @Override
                    public void run() {
                        umetniSlikuButton.setText("PROMIJENI SLIKU");
                    }
                }).subscribeMe();
            }
        });

        rotateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loading.setVisibility(View.VISIBLE);
                Effects.setRotateAnimation(loading);

                TaskQueue.prepare().backgroundTask(new Runnable() {
                    @Override
                    public void run() {
                        Bitmap bitmap = ((BitmapDrawable)imageView.getDrawable()).getBitmap();
                        Matrix matrix = new Matrix();
                        matrix.postRotate(90);
                        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight(), true);
                        rotatedBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
                        Tree.imageBitmap = rotatedBitmap;
                    }
                }).guiTask(new Runnable() {
                    @Override
                    public void run() {
                        imageView.setImageBitmap(rotatedBitmap);
                        loading.clearAnimation();
                        loading.setVisibility(View.INVISIBLE);

                    }
                }).subscribeMe();
            }
        });

        gotovoButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {

                loading.setVisibility(View.VISIBLE);
                Effects.setRotateAnimation(loading);

                TaskQueue.prepare().backgroundTask(new Runnable() {
                    @Override
                    public void run() {
                        EditText posadioEdit = findViewById(R.id.posadio_editText);
                        posadio = posadioEdit.getText().toString();
                        EditText datumEdit = findViewById(R.id.datum_editText);
                        datum = datumEdit.getText().toString();
                        EditText vrstaEdit = findViewById(R.id.vrstaStabla_editText);
                        vrsta = vrstaEdit.getText().toString();

                        try {
                            //SETTANJE FEATURA I ENCODANE SLIKE
                            Tree.setFeatures(vrsta,datum,posadio,koordinate);
                            Tree.setEncodedImage(Tree.imageBitmap);

                            Trees.checkAllFields(fragmentManager,context);

                        } catch (JSONException e) {
                            System.out.println("EXCEPTION IN SETTING FEATURES !");
                            e.printStackTrace();
                        }

                        //dodati ako su polja prazna da nejde na slanje!

                        try {
                            Trees.sendNewTreeToServer(context,fragmentManager);
                        } catch (Exception e) {
                            Effects.showServerErrorDialog(context,fragmentManager);
                            e.printStackTrace();
                        }
                        System.out.println("SENDING DATA TO SERVER !!!");
                    }
                }).guiTask(new Runnable() {
                    @Override
                    public void run() {
                        loading.clearAnimation();
                        loading.setVisibility(View.INVISIBLE);
                        Effects.succesfullySentDialog(context,fragmentManager);
                    }
                }).subscribeMe();
            }
        });

        //ZANIMLJIVO, ISTRAŽIVALA SI
        placePickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // zanimljiva linija............................................................... gle do kud ide............
                WifiManager wifiManager = (WifiManager)context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                //wtf, ovo nemre bit dobro... zakaj gasiš pa pališ wifi???
                // to ti troši bezveze bateriju jer moraš ponovno discoverat network a to traje bezveze...
                // možda moreš samo zakomentirat to
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
                // potraži kak se složi da ti zumira na trenutnu mapu gdi si ti jer nema smisla
                // da mu prikažeš aziju i australiju pa da zumira korisnik pol sata do svoje lokacije...
                Place place = PlacePicker.getPlace(data,this);
                StringBuilder stringBuilder = new StringBuilder();
                String latitude = String.valueOf(place.getLatLng().latitude);
                String longitude = String.valueOf(place.getLatLng().longitude);
                stringBuilder.append("G.Š: ");
                stringBuilder.append(latitude);
                stringBuilder.append("\nG.D: ");
                stringBuilder.append(longitude);

                koordinate = new JSONArray();
                try {
                    koordinate.put(place.getLatLng().longitude);
                    koordinate.put(place.getLatLng().latitude);
                } catch (JSONException e) {
                    System.out.println("SOME EXCEPTION IN COORDINATE JSONARRAY SETTING !");
                    e.printStackTrace();
                }
                coordinatesTextView.setText(stringBuilder.toString());

            }
        }
    }
}