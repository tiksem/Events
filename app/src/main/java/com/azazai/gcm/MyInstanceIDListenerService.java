package com.azazai.gcm;

import android.content.SharedPreferences;
import com.google.android.gms.iid.InstanceIDListenerService;
import com.azazai.EventsApp;
import com.azazai.network.RequestManager;
import com.utilsframework.android.AndroidUtilities;
import com.utilsframework.android.network.AndroidNetwork;
import com.utilsframework.android.network.NetworkStateReceiver;
import com.utilsframework.android.threading.OnFinish;
import com.vk.sdk.VKSdk;

import java.io.IOException;

/**
 * Created by CM on 7/13/2015.
 */
public class MyInstanceIDListenerService extends InstanceIDListenerService {

    private RequestManager requestManager;

    @Override
    public void onCreate() {
        super.onCreate();
        requestManager = EventsApp.getInstance().createRequestManager();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        requestManager.cancelAll();
    }

    private void obtainNewToken(String oldDeviceToken) {
        GCM.obtainAndLoginNewToken(this, requestManager, oldDeviceToken);
    }

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        obtainNewToken(GCM.getTokenFromSharedPreferences(this));
    }
}
