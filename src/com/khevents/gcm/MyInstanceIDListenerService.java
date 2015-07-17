package com.khevents.gcm;

import android.content.SharedPreferences;
import com.google.android.gms.iid.InstanceIDListenerService;
import com.khevents.EventsApp;
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
    private void obtainNewToken(String oldDeviceToken) {
        GCM.obtainNewToken(this, new GCM.OnNewTokenObtainFinished() {
            @Override
            public void onFinished(String token, IOException e) {
                EventsApp.getInstance().getRequestManager().loginGCMToken(token, oldDeviceToken,
                        VKSdk.getAccessToken().accessToken,
                        new OnFinish<IOException>() {
                            @Override
                            public void onFinish(IOException e) {
                                if (e == null) {
                                    saveTokenToSharedPreferences(token);
                                } else {
                                    e.printStackTrace();
                                }
                            }
                        });
            }
        });
    }

    private void saveTokenToSharedPreferences(String token) {
        GCM.saveTokenToSharedPreferences(this, token);
    }

    @Override
    public void onCreate() {
        String token = GCM.getTokenFromSharedPreferences(this);
        if (token == null) {
            obtainNewToken(null);
        }
        super.onCreate();
    }

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        obtainNewToken(GCM.getTokenFromSharedPreferences(this));
    }
}
