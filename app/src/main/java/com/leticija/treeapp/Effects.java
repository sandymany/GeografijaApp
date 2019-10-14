package com.leticija.treeapp;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.leticija.treeapp.DialogCreator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Effects {

    public static void alterTextView (TextView textView, boolean visibility, String text, int color) {
        textView.setText(text);
        if (visibility) {
            textView.setVisibility(View.VISIBLE);
        }
        else {
            textView.setVisibility(View.INVISIBLE);
        }
        textView.setTextColor(ContextCompat.getColor(textView.getContext(),color));
    }

    public static void setRotateAnimation(View view) {
        RotateAnimation rotateAnimation = new RotateAnimation(0, 1440, Animation.RELATIVE_TO_SELF,
                .5f, Animation.RELATIVE_TO_SELF, .5f);
        rotateAnimation.setDuration(5000);
        rotateAnimation.setRepeatCount(Animation.INFINITE);
        view.startAnimation(rotateAnimation);
    }

    public static void fadeIn (final View view,int duration) {

        Animation fadeOut = new AlphaAnimation(0,1);
        fadeOut.setInterpolator(new AccelerateInterpolator());
        fadeOut.setDuration(duration);

        fadeOut.setAnimationListener(new Animation.AnimationListener()
        {
            public void onAnimationEnd(Animation animation)
            {
                view.setVisibility(View.VISIBLE);
            }
            public void onAnimationRepeat(Animation animation) {}
            public void onAnimationStart(Animation animation) {}
        });

        view.startAnimation(fadeOut);
    }

    public static void fadeOut (final View view,int duration) {
        Animation fadeOut = new AlphaAnimation(1, 0);
        fadeOut.setInterpolator(new AccelerateInterpolator());
        fadeOut.setDuration(duration);

        fadeOut.setAnimationListener(new Animation.AnimationListener()
        {
            public void onAnimationEnd(Animation animation)
            {
                view.setVisibility(View.GONE);
            }
            public void onAnimationRepeat(Animation animation) {}
            public void onAnimationStart(Animation animation) {}
        });

        view.startAnimation(fadeOut);
    }

    public static void showKeyboard (View view,boolean bool, Context context) {

        if (bool == false) {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(),0);
        }
    }

    public static void succesfullySentDialog (final Context context, FragmentManager fragmentManager) {

        int color = context.getResources().getColor(R.color.success);

        Runnable  none = new Runnable() {
            @Override
            public void run() {
                return;
            }
        };

        Runnable okRunnable = new Runnable() {
            @Override
            public void run() {
                return;
            }
        };

        DialogCreator dialogCreator = new DialogCreator(color,"Poslano","Podaci uspješno poslani na server !","OK","",none,okRunnable);
        dialogCreator.show(fragmentManager,"successfully sent dialog");

    }

    public static void showEmptyFieldsDialog (final Context context,FragmentManager fragmentManager) {

        int color = context.getResources().getColor(R.color.red);

        Runnable  none = new Runnable() {
            @Override
            public void run() {
                return;
            }
        };

        Runnable okRunnable = new Runnable() {
            @Override
            public void run() {
                return;
            }
        };

        DialogCreator dialogCreator = new DialogCreator(color,"Pozor","Neka polja su prazna.\nMolimo ispunite sve.","OK","",none,okRunnable);
        dialogCreator.show(fragmentManager,"prazna polja upozorenje");

    }

    public static void showServerErrorDialog (final Context context,FragmentManager fragmentManager) {

        int color = context.getResources().getColor(R.color.red);

        Runnable  none = new Runnable() {
            @Override
            public void run() {
                return;
            }
        };

        Runnable okRunnable = new Runnable() {
            @Override
            public void run() {
                return;
            }
        };

        DialogCreator dialogCreator = new DialogCreator(color,"Pozor","Dogodila se greška pri slanju.\nMolimo provjerite konekciju i pokušajte ponovno.","OK","",none,okRunnable);
        dialogCreator.show(fragmentManager,"prazna polja upozorenje");

    }

    public static void loadAllTreesToScrollview(final JSONObject objectFromRequest, LinearLayout scrollLayout,Context context) throws JSONException {

        JSONArray arrayOfTreesData = (JSONArray) objectFromRequest.get("features");

        for (int i = 0; i<arrayOfTreesData.length(); i++) {

            JSONObject treeObject = (JSONObject) arrayOfTreesData.get(i);
            String contentTextString="";

            System.out.println(treeObject.toString());

            try {

                if (treeObject.has("properties")) {
                    JSONObject treeProperties = (JSONObject) treeObject.get("properties");
                    String vrsta = (String) treeProperties.get("vrsta");
                    contentTextString = "Vrsta: " + vrsta;
                    String posadio = (String) treeProperties.get("posadio");
                    contentTextString += "\nPosadio: " + posadio;
                    String datum = (String) treeProperties.get("datum");
                    contentTextString += "\nDatum: " + datum;

                }
                if (treeObject.has("geometry")) {
                    JSONObject geometryObject = (JSONObject) treeObject.get("geometry");
                    String koordinate = geometryObject.get("coordinates").toString();
                    contentTextString += "\nKoordinate[G.D.,G.Š.]: " + koordinate;
                }

                LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
                LinearLayout templateLayout = (LinearLayout) inflater.inflate(R.layout.template_tree_collection, null);


                TextView subtitle = templateLayout.findViewById(R.id.collection_subtitle);
                subtitle.setText("STABLO " + String.valueOf(i + 1) + ".");
                TextView contentText = templateLayout.findViewById(R.id.content_text);
                contentText.setText(contentTextString);

                scrollLayout.addView(templateLayout);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public static String cleanHiddenCharacters(String text)
    {
        // strips off all non-ASCII characters
        text = text.replaceAll("[^\\x00-\\x7F]", "");

        // erases all the ASCII control characters
        text = text.replaceAll("[\\p{Cntrl}&&[^\r\n\t]]", "");

        // removes non-printable characters from Unicode
        text = text.replaceAll("\\p{C}", "");

        return text.trim();
    }
}
