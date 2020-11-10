package com.example.app1;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class Receiver1 extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        // throw new UnsupportedOperationException("Not yet implemented");
        int position = intent.getIntExtra("POSITION", -1);
        String url = intent.getStringExtra("URL");
        if (position == -1) {
            Toast.makeText(context, "Choose a vacation spot!!! ", Toast.LENGTH_SHORT).show();
        } else {

            //Log.i("checkkkk", context.toString());
            Intent i = new Intent(context, Activity2.class);
            i.putExtra("URL", url);
            context.startActivity(i);

        }
    }
}
