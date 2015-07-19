package com.khevents.gcm;

import android.app.Notification;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;
import com.google.android.gms.gcm.GcmListenerService;
import com.jsonutils.Json;
import com.khevents.EventsApp;
import com.khevents.R;
import com.khevents.data.Comment;
import com.khevents.data.GCMData;
import com.utilsframework.android.bitmap.BitmapUtilities;
import com.utilsframework.android.view.Notifications;
import com.vkandroid.VkApiUtils;
import com.vkandroid.VkUser;

import java.io.IOException;

/**
 * Created by CM on 7/13/2015.
 */
public class MyGcmListenerService extends GcmListenerService {
    private void setupCommentNotification(Comment comment, Notification.Builder builder) throws IOException {
        VkUser vkUser = EventsApp.getInstance().getRequestManager().getVkUserById(comment.userId);
        builder.setLargeIcon(BitmapUtilities.getBitmapFromURL(vkUser.avatar));
        builder.setContentTitle(vkUser.name + " " + vkUser.lastName);
        builder.setContentText(comment.text);
    }

    private Notification.Builder createNotification(GCMData data) throws IOException {
        Notification.Builder builder = new Notification.Builder(this);
        if (data.comment != null) {
            setupCommentNotification(data.comment, builder);
        }
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

        try {
            Notification.Builder notification = createNotification(data);

            new Handler(getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    postNotification(notification);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void postNotification(Notification.Builder builder) {
        Notifications.notify(this, 1, builder);
    }
}
