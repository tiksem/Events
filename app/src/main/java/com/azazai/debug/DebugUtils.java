package com.azazai.debug;

import android.content.Context;
import com.azazai.R;
import com.azazai.network.RequestManager;
import com.utilsframework.android.view.Alerts;
import com.utilsframework.android.view.OnYes;
import com.utilsframework.android.view.YesNoAlertSettings;

/**
 * Created by stykhonenko on 22.10.15.
 */
public class DebugUtils {
    public static void showInternetConnectionDialog(Context context) {
        if (RequestManager.internetConnectionEnabled) {
            Alerts.showYesNoAlert(new YesNoAlertSettings(context) {
                {
                    setMessage(R.string.disable_internet_connection);
                }

                @Override
                public void onYes() {
                    RequestManager.internetConnectionEnabled = false;
                }
            });
        } else {
            Alerts.showYesNoAlert(new YesNoAlertSettings(context) {
                {
                    setMessage(R.string.enable_internet_connection);
                }

                @Override
                public void onYes() {
                    RequestManager.internetConnectionEnabled = true;
                }
            });
        }
    }
}
