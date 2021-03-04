package com.example.project1_trial;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;

import java.util.regex.*;

import android.os.Bundle;
import android.preference.EditTextPreference;
import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class Activity2 extends AppCompatActivity {

    private EditText editText1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2);

        editText1 = (EditText) findViewById(R.id.editTextPhone);

        editText1.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                Intent i = new Intent();
                String enteredValue = editText1.getText().toString();
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    //do what you want on the press of 'done'
                    enteredValue = editText1.getText().toString();
                    Boolean valid = Validate(enteredValue);

                    i.putExtra("phoneNumber", enteredValue);
                    if (valid) {
                        setResult(RESULT_OK, i);
                        finish();
                    } else {
                        /* Toast.makeText(getApplicationContext(),"Please enter valid phone number", Toast.LENGTH_LONG).show(); */
                        setResult(RESULT_CANCELED, i);
                        finish();
                    }
                }
                return false;
            }
        });
    }

    private Boolean Validate(String enteredValue) {

        String r = "^\\+?[1]? ?\\(?[0-9]{3}\\)? ?-?[0-9]{3} ?-?[0-9]{4}";
        Pattern p = Pattern.compile(r);
        if (enteredValue == null) {
            return false;
        }
        Matcher m = p.matcher(enteredValue);
        return m.matches();
    }
}