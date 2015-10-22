package com.khevents.debug;

import android.content.Context;
import com.khevents.R;
import com.khevents.network.RequestManager;
import com.utilsframework.android.view.Alerts;
import com.utilsframework.android.view.OnYes;

/**
 * Created by stykhonenko on 22.10.15.
 */
public class DebugUtils {
    public static void showInternetConnectionDialog(Context context) {
        if (RequestManager.internetConnectionEnabled) {
            Alerts.showYesNoAlert(context, new OnYes() {
                @Override
                public void onYes() {
                    RequestManager.internetConnectionEnabled = false;
                }
            }, R.string.disable_internet_connection);
        } else {
            Alerts.showYesNoAlert(context, new OnYes() {
                @Override
                public void onYes() {
                    RequestManager.internetConnectionEnabled = true;
                }
            }, R.string.enable_internet_connection);
        }
    }
}
