package com.leticija.treeapp.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.leticija.treeapp.R;
import com.leticija.treeapp.net.Requester;

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

    private void goToNewActivity () {
        if (passcodeField.getText().toString().equals(getString(R.string.passcode))) {
            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
            startActivity(intent);
        }
        else {
            message.setText("Kriva lozinka");
            message.setVisibility(View.VISIBLE);
        }
    }
}
