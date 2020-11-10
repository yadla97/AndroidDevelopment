package com.example.app1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

public class Activity2 extends AppCompatActivity {

    private static final String TOAST_INTENT = "edu.uic.cs478.BroadcastReceiver3.showToast";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2);


        Intent intent = getIntent();
        String url = intent.getStringExtra("URL");
        Intent i = new Intent(Intent.ACTION_VIEW);
        Uri uri = Uri.parse(url);
        i.setData(uri);
        startActivity(i);
    }


    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}