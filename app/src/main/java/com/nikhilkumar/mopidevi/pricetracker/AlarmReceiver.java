package com.nikhilkumar.mopidevi.pricetracker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by NIKHIL on 08-Feb-15.
 */

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {


        //Toast.makeText(context, "I'm running", Toast.LENGTH_LONG).show();

        Intent i = new Intent(context.getApplicationContext(), Checking.class);
        context.getApplicationContext().startService(i);
    }


    }

