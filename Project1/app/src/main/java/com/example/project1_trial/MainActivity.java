package com.example.project1_trial;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {
    //Declaring both the buttons
    private Button button1;
    private Button button2;

    // Declaring a variable to check if the second activity 
    private Boolean validPhoneNumber;

    public TextView textView1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        button1 = (Button) findViewById(R.id.button1);
        button2 = (Button) findViewById(R.id.button2);
        textView1 = (TextView) findViewById(R.id.textView1);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity2();
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                callDial();
            }
        });
    }

    private void callDial() {
        String phoneNumber = textView1.getText().toString();
        if (phoneNumber.isEmpty()) {
            Toast.makeText(getApplicationContext(), "PLEASE ENTER A NUMBER", Toast.LENGTH_LONG).show();
        } else if (validPhoneNumber == false) {
            Toast.makeText(getApplicationContext(), "\""+phoneNumber+"\""+ " is an invalid number. PLEASE ENTER A VALID NUMBER", Toast.LENGTH_LONG).show();
        } else {

            Intent i = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNumber));
            startActivity(i);
        }
    }

    public void openActivity2() {

        Intent intent = new Intent(this, Activity2.class);
        startActivityForResult(intent, 1);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == 1) {
            String phoneNumber;
            if (data == null) {
                 phoneNumber = "";
            } else {
                 phoneNumber = data.getStringExtra("phoneNumber");
            }
            textView1.setText(phoneNumber);
            if (resultCode == RESULT_OK) {
                validPhoneNumber = true;
            }
            if (resultCode == RESULT_CANCELED) {
                validPhoneNumber = false;
            }
        }

    }
}