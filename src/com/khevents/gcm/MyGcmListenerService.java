package com.khevents.gcm;

import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;
import com.google.android.gms.gcm.GcmListenerService;

/**
 * Created by CM on 7/13/2015.
 */
public class MyGcmListenerService extends GcmListenerService {
    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onMessageReceived(String from, Bundle data) {
        super.onMessageReceived(from, data);
        new Handler(getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MyGcmListenerService.this, "message received", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
