package com.khevents.gcm;

import android.app.Notification;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;
import com.google.android.gms.gcm.GcmListenerService;
import com.jsonutils.Json;
import com.khevents.R;
import com.khevents.data.GCMData;
import com.utilsframework.android.bitmap.BitmapUtilities;
import com.utilsframework.android.view.Notifications;

import java.io.IOException;

/**
 * Created by CM on 7/13/2015.
 */
public class MyGcmListenerService extends GcmListenerService {
    private Notification.Builder createNotification(GCMData data) {
        Notification.Builder builder = new Notification.Builder(this);
        builder.setLargeIcon(BitmapUtilities.getBitmapFromURL(data.icon));
        builder.setContentText(data.message);
        builder.setContentTitle(data.title);
        builder.setSmallIcon(R.drawable.add_icon);
        return builder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onMessageReceived(String from, Bundle bundle) {
        super.onMessageReceived(from, bundle);
        String json = bundle.getString("data");
        Log.i("GCM", "message received: " + json);
        GCMData data = Json.readNoThrow(json, GCMData.class);
        if (data == null) {
            return;
        }

        Notification.Builder notification = createNotification(data);

        new Handler(getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                postNotification(notification);
            }
        });
    }

    private void postNotification(Notification.Builder builder) {
        Notifications.notify(this, 1, builder);
    }
}
