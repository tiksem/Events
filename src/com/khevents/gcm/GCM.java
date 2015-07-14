package com.khevents.gcm;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.utils.framework.network.RequestExecutor;
import com.utilsframework.android.threading.OnFinish;
import com.utilsframework.android.threading.Threading;

import java.io.IOException;

/**
 * Created by CM on 7/14/2015.
 */
public class GCM {
    public static final String APP_ID = "818758220238";
    public static final String TOKEN_KEY = "GCM_TOKEN_KEY";

    public interface OnNewTokenObtainFinished {
        void onFinished(String token, IOException e);
    }

    public static String getNewToken(Context context) throws IOException {
        InstanceID instanceID = InstanceID.getInstance(context);
        return instanceID.getToken(APP_ID,
                GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
    }

    public static void obtainNewToken(Context context, OnNewTokenObtainFinished obtainFinished) {
        Threading.executeAsyncTask(new Threading.Task<IOException, String>() {
            @Override
            public String runOnBackground() throws IOException {
                return getNewToken(context);
            }

            @Override
            public void onComplete(String token, IOException error) {
                obtainFinished.onFinished(token, error);
            }
        }, IOException.class);
    }

    public static void saveTokenToSharedPreferences(Context context, String token) {
        SharedPreferences sharedPreferences = getSharedPreferences(context);
        sharedPreferences.edit().putString(TOKEN_KEY, token).apply();
    }

    private static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(
                GCM.class.getSimpleName(),
                Context.MODE_PRIVATE);
    }


    public static String getTokenFromSharedPreferences(Context context) {
        SharedPreferences sharedPreferences = getSharedPreferences(context);
        return sharedPreferences.getString(TOKEN_KEY, null);
    }

    public static void initServices(Context context) {
        Intent intent = new Intent(context, MyInstanceIDListenerService.class);
        context.startService(intent);
        intent = new Intent(context, MyGcmListenerService.class);
        context.startService(intent);
    }
}
