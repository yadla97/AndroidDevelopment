package com.example.app1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Button button;
    private BroadcastReceiver receiver;
    private IntentFilter filter;
    private static final String TOAST_INTENT = "edu.uic.cs478.BroadcastReceiver3.showToast";
    private static final String PERMISSION = "edu.uic.cs478.f20.kaboom";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = findViewById(R.id.A1_b1);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPermissions();
            }
        });
    }

    private void checkPermissions() {
        if (ContextCompat.checkSelfPermission(MainActivity.this, PERMISSION) == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "You already have the permission", Toast.LENGTH_SHORT).show();
            createBroadcastReceiverAndEnterA2();
        } else {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{PERMISSION}, 0);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                createBroadcastReceiverAndEnterA2();
            } else {
                Toast.makeText(this, "Bummer: No permission", Toast.LENGTH_SHORT)
                        .show();
            }
        }

    }




    private void createBroadcastReceiverAndEnterA2() {
        receiver = new Receiver1();
        filter = new IntentFilter(TOAST_INTENT);
        filter.setPriority(5);
        registerReceiver(receiver, filter);
        Intent intent = getPackageManager().getLaunchIntentForPackage("com.example.app2");
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }
}