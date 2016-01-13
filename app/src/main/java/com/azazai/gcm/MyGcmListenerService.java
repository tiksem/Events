package com.azazai.gcm;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import com.google.android.gms.gcm.GcmListenerService;
import com.jsonutils.Json;
import com.azazai.EventsApp;
import com.azazai.R;
import com.azazai.data.Comment;
import com.azazai.data.Event;
import com.azazai.data.GCMData;
import com.azazai.data.SubscribeInfo;
import com.azazai.network.RequestManager;
import com.azazai.ui.MainActivity;
import com.utilsframework.android.bitmap.BitmapUtilities;
import com.utilsframework.android.sp.SharedPreferencesMap;
import com.utilsframework.android.view.Notifications;
import com.vkandroid.VkUser;

import java.io.IOException;

/**
 * Created by CM on 7/13/2015.
 */
public class MyGcmListenerService extends GcmListenerService {
    public static final String NOTIFICATION_ACTION = "com.azazai.Notification";
    public static final String NOTIFICATIONS_COUNT = "NOTIFICATIONS_COUNT";
    public static final int COMMENT_ID = 1;
    public static final int SUBSCRIBE_ID = 2;
    public static final int CANCEL_ID = 3;
    public static final String ID = "ID";

    private SharedPreferencesMap sharedPreferences;
    private RequestManager requestManager;

    private void setupCommentNotification(Comment comment, Notification.Builder builder) throws IOException {
        setupVkUserNotification(comment.userId, builder, requestManager);
        builder.setContentText(comment.text);

        Event event = requestManager.getEventById(comment.eventId);
        setupEventNotificationIntent(builder, event, COMMENT_ID);
    }

    private void setupEventNotificationIntent(Notification.Builder builder, Event event, int id) {
        Intent intent = new Intent(NOTIFICATION_ACTION).putExtra(MainActivity.NOTIFICATION_EVENT, event);
        intent.putExtra(ID, id);
        builder.setContentIntent(PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT));
    }

    private VkUser setupVkUserNotification(long userId, Notification.Builder builder, RequestManager requestManager)
            throws IOException {
        VkUser vkUser = requestManager.getVkUserById(userId);
        builder.setLargeIcon(BitmapUtilities.getBitmapFromURL(vkUser.avatar));
        builder.setContentTitle(vkUser.name + " " + vkUser.lastName);
        return vkUser;
    }

    public int getCount(int id) {
        return sharedPreferences.getInt(NOTIFICATIONS_COUNT + id, 0);
    }

    private void postNotification(GCMData data) throws IOException {
        final Notification.Builder builder = new Notification.Builder(this);
        final int id;
        if (data.comment != null) {
            setupCommentNotification(data.comment, builder);
            id = COMMENT_ID;
        } else if(data.cancelEventId != null) {
            setupCancelEventNotification(data.cancelEventId, builder);
            id = CANCEL_ID;
        } else if(data.subscribe != null) {
            setupSubscribeEventNotification(data.subscribe, builder);
            id = SUBSCRIBE_ID;
        } else {
            return;
        }

        builder.setSmallIcon(R.drawable.ic_notification);
        int count = getCount(id) + 1;
        if (count > 1) {
            builder.setNumber(count);
        }
        sharedPreferences.putInt(NOTIFICATIONS_COUNT + id, count);

        new Handler(getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                postNotification(builder, id);
            }
        });
    }

    private void postNotification(Notification.Builder notification, int id) {
        Notifications.notify(this, id, notification);
    }

    private void setupSubscribeEventNotification(SubscribeInfo subscribe, Notification.Builder builder)
            throws IOException {
        Event event = requestManager.getEventById(subscribe.eventId);
        setupVkUserNotification(subscribe.userId, builder, requestManager);
        builder.setContentText(getString(R.string.subscribe_event_notification, event.name));
        setupEventNotificationIntent(builder, event, SUBSCRIBE_ID);
    }

    private void setupCancelEventNotification(long cancelEventId, Notification.Builder builder) throws IOException {
        Event event = requestManager.getEventById(cancelEventId);
        event.isCanceled = true;
        setupVkUserNotification(event.userId, builder, requestManager);
        builder.setContentText(getString(R.string.cancel_event_notification, event.name));
        setupEventNotificationIntent(builder, event, CANCEL_ID);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sharedPreferences = new SharedPreferencesMap(this);
        requestManager = EventsApp.getInstance().createRequestManager();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        requestManager.cancelAll();
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
            postNotification(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
