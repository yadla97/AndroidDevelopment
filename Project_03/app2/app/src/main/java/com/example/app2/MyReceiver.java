package com.example.app2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class MyReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        String place_name = intent.getStringExtra("PLACENAME");
        Log.i("hellloooooooooo", String.valueOf(intent.getIntExtra("POSITION",-1)));
        Toast.makeText(context, "Toast from A2 : "+ place_name , Toast.LENGTH_SHORT).show();
    }

}
