package com.khevents.gcm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.khevents.R;
import com.khevents.data.Event;
import com.khevents.ui.MainActivity;
import com.khevents.ui.WelcomeActivity;
import com.khevents.ui.fragments.EventFragment;
import com.utilsframework.android.fragments.OneFragmentActivity;
import com.utilsframework.android.sp.SharedPreferencesMap;

/**
 * Created by CM on 7/22/2015.
 */
public class GCMBroadCastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Event event = intent.getParcelableExtra(MainActivity.NOTIFICATION_EVENT);
        if (event != null) {
            if (MainActivity.isRunning()) {
                Bundle args = new Bundle();
                args.putParcelable(EventFragment.EVENT, event);
                intent = OneFragmentActivity.getStartIntent(context, EventFragment.class, args, R.layout.toolbar);
            } else {
                intent = new Intent(context, WelcomeActivity.class);
                intent.putExtra(MainActivity.NOTIFICATION_EVENT, event);
            }
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);

        new SharedPreferencesMap(context).remove(MyGcmListenerService.NOTIFICATIONS_COUNT +
                intent.getIntExtra(MyGcmListenerService.ID, MyGcmListenerService.COMMENT_ID));
    }
}
