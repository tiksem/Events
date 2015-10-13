package com.khevents.vk;

import android.app.ProgressDialog;
import android.content.Context;
import com.khevents.EventsApp;
import com.khevents.R;
import com.khevents.gcm.GCM;
import com.khevents.network.RequestManager;
import com.utilsframework.android.threading.Threading;
import com.utilsframework.android.view.Alerts;
import com.utilsframework.android.view.Toasts;
import com.vk.sdk.VKSdk;
import com.vkandroid.VkUser;

import java.io.IOException;

/**
 * Created by CM on 7/22/2015.
 */
public class VkInitManager {
    private final RequestManager requestManager;
    private Context context;
    private boolean showProgressDialog = true;
    private ProgressDialog progressDialog;
    private boolean updateDeviceVkUser = false;

    public VkInitManager(Context context, RequestManager requestManager) {
        this.context = context;
        this.requestManager = requestManager;
    }

    public void execute(boolean showProgressDialog) {
        this.showProgressDialog = showProgressDialog;
        updateDeviceVkUser = !VkManager.initialize(context, R.string.vk_login_error,
                new VkManager.OnAccessTokenGot() {
                    @Override
                    public void onAccessToken() {
                        onAccessTokenGot();
                    }
                });
    }


    protected void onAccessTokenGot() {
        if (showProgressDialog) {
            progressDialog = Alerts.showCircleProgressDialog(context, R.string.please_wait);
        }
        Threading.executeAsyncTask(new Threading.Task<IOException, VkUser>() {
            @Override
            public VkUser runOnBackground() throws IOException {
                return getCurrentVkUser();
            }

            @Override
            public void onComplete(VkUser vkUser, IOException error) {
                if (vkUser != null) {
                    onVkUserReached(vkUser);
                } else {
                    Toasts.error(context, R.string.no_internet_connection);
                }
                if (showProgressDialog) {
                    progressDialog.dismiss();
                }
            }
        }, IOException.class);
    }

    protected void onVkUserReached(VkUser vkUser) {
        EventsApp eventsApp = EventsApp.getInstance();
        eventsApp.initVkUser(vkUser);
        String token = GCM.getTokenFromSharedPreferences(context);
        if (token == null) {
            GCM.obtainAndLoginNewToken(context, requestManager, null);
            return;
        }

        if (updateDeviceVkUser) {
            GCM.obtainAndLoginNewToken(context, requestManager, token);
        }
    }

    private VkUser getCurrentVkUser() throws IOException {
        return requestManager.getVkUserById(
                Long.valueOf(VKSdk.getAccessToken().userId));
    }
}
