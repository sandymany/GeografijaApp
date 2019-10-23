package com.leticija.treeapp;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
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
import com.leticija.treeapp.net.Requester;
import com.leticija.treeapp.net.TaskQueue;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

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

    public static void showDeleteTreeDialog (final Context context, final FragmentManager fragmentManager, final String treeID) {

        int color = context.getResources().getColor(R.color.red);

        Runnable  none = new Runnable() {
            @Override
            public void run() {
                return;
            }
        };

        Runnable yesRunnable = new Runnable() {
            @Override
            public void run() {
                final String passcode = Trees.passcode;
                TaskQueue.prepare().backgroundTask(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Requester.request("/api/delete.php", new HashMap<String, String>(), "passcode=" + passcode + "&id=" + treeID, context, fragmentManager);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).subscribeMe();
            }
        };

        DialogCreator dialogCreator = new DialogCreator(color,"!","Jeste li sigurni da želite\nobrisati stablo?","DA","NE",none,yesRunnable);
        dialogCreator.show(fragmentManager,"prazna polja upozorenje");

    }

    public static void loadAllTreesToScrollview(final JSONObject objectFromRequest, LinearLayout scrollLayout, final Context context, final FragmentManager fragmentManager,TextView ukupnoStabala) throws JSONException {

        JSONArray arrayOfTreesData = (JSONArray) objectFromRequest.get("features");

        ukupnoStabala.setText("Ukupno stabala: "+String.valueOf(arrayOfTreesData.length()));

        for (int i = arrayOfTreesData.length()-1; i>-1; i--) {

            System.out.println(String.valueOf(i));

            final JSONObject treeObject = (JSONObject) arrayOfTreesData.get(i);
            String contentTextString="";

            System.out.println(treeObject.toString());
            String treeID = null;

            try {

                if (treeObject.has("properties")) {
                    JSONObject treeProperties = (JSONObject) treeObject.get("properties");
                    String vrsta = (String) treeProperties.get("vrsta");
                    contentTextString = "Vrsta: " + vrsta;
                    String posadio = (String) treeProperties.get("posadio");
                    contentTextString += "\nPosadio: " + posadio;
                    String datum = (String) treeProperties.get("datum");
                    contentTextString += "\nDatum: " + datum;
                    treeID = (String) treeProperties.get("id").toString();

                }
                if (treeObject.has("geometry")) {
                    JSONObject geometryObject = (JSONObject) treeObject.get("geometry");
                    String koordinate = geometryObject.get("coordinates").toString();
                    contentTextString += "\nKoordinate[G.D.,G.Š.]: " + koordinate;
                }

                LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
                final CardView templateLayout = (CardView) inflater.inflate(R.layout.template_tree_collection, null);

                TextView subtitle = templateLayout.findViewById(R.id.collection_subtitle);
                subtitle.setText("STABLO " + String.valueOf(arrayOfTreesData.length()-i) + ".");
                TextView contentText = templateLayout.findViewById(R.id.content_text);
                contentText.setText(contentTextString);

                final CardView cardView = new CardView(context);
                cardView.setRadius(10);
                cardView.setCardBackgroundColor(context.getResources().getColor(R.color.partiallyTransparentBrown));
                cardView.setVisibility(View.INVISIBLE);
                Effects.fadeIn(cardView,200);
                cardView.addView(templateLayout);
                scrollLayout.addView(cardView);

                final String finalTreeID = treeID;
                cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cardView.setCardBackgroundColor(context.getResources().getColor(R.color.success));
                        showDeleteTreeDialog(context,fragmentManager, finalTreeID);
                        TaskQueue.prepare().backgroundTask(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(200);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }).guiTask(new Runnable() {
                            @Override
                            public void run() {
                                cardView.setCardBackgroundColor(context.getResources().getColor(R.color.partiallyTransparentBrown));
                            }
                        }).subscribeMe();
                    }
                });

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
