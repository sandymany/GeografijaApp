package com.leticija.treeapp.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.opengl.Visibility;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.leticija.treeapp.Effects;
import com.leticija.treeapp.R;
import com.leticija.treeapp.Trees;
import com.leticija.treeapp.net.Requester;
import com.leticija.treeapp.net.TaskQueue;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    EditText passcodeField;
    TextView message;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = getApplicationContext();

        //SETTANJE URL-a
        Requester.setUrl(context);

        passcodeField = findViewById(R.id.passcode_editText);
        message = findViewById(R.id.passcode_message);

        Button submitButton = findViewById(R.id.button_submit);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                goToNewActivity();
            }
        });

        passcodeField.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    goToNewActivity();
                }
                return false;
            }
        });

    }

    @SuppressLint("ResourceAsColor")
    private void goToNewActivity () {

        try {
            TaskQueue.prepare().backgroundTask(new Runnable() {
                @Override
                public void run() {

                    String enteredPasscode = passcodeField.getText().toString();
                    String isPasscodeValid = Requester.request("/api/provjeri_pass.php", new HashMap<String, String>(), "passcode=" + enteredPasscode, context, getSupportFragmentManager());

                    if (isPasscodeValid.equals("true")) {
                        TaskQueue.prepare().guiTask(new Runnable() {
                            @Override
                            public void run() {
                                Effects.alterTextView(message, true,"LOZINKA ISPRAVNA",R.color.success);
                            }
                        }).subscribeMe();
                        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                        Trees.passcode = enteredPasscode;
                        startActivity(intent);
                    }
                    else {
                        TaskQueue.prepare().guiTask(new Runnable() {
                            @Override
                            public void run() {
                                Effects.alterTextView(message,true,"POGREŠNA LOZINKA",R.color.red);
                                Effects.fadeIn(message, 500);
                            }
                        }).subscribeMe();
                    }
                }
            }).subscribeMe();

        } catch (Exception e) {
            e.printStackTrace();
        }

        /*

        if (passcodeField.getText().toString().equals(getString(R.string.passcode))) {
            Effects.alterTextView(message, true,"LOZINKA ISPRAVNA",R.color.success);
            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
            startActivity(intent);
        }
        else {
            message.setText("POGREŠNA LOZINKA");
            Effects.fadeIn(message,500);
        }
        */
    }
}
