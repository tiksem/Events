package com.azazai.gcm;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.azazai.network.RequestManager;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.utilsframework.android.threading.OnFinish;
import com.utilsframework.android.threading.Threading;
import com.vk.sdk.VKSdk;

import java.io.IOException;

/**
 * Created by CM on 7/14/2015.
 */
public class GCM {
    public static final String TAG = "GCM";

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

    public static void obtainNewToken(final Context context, final OnNewTokenObtainFinished obtainFinished) {
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

    public static void obtainAndLoginNewToken(final Context context, final RequestManager requestManager,
                                              final String oldDeviceToken) {
        GCM.obtainNewToken(context, new GCM.OnNewTokenObtainFinished() {
            @Override
            public void onFinished(final String token, IOException e) {
                if (e != null) {
                    Log.e(TAG, "token obtain failed", e);
                    return;
                }

                requestManager.loginGCMToken(token, oldDeviceToken,
                        VKSdk.getAccessToken().accessToken,
                        new OnFinish<IOException>() {
                            @Override
                            public void onFinish(IOException e) {
                                if (e == null) {
                                    saveTokenToSharedPreferences(context, token);
                                } else {
                                    e.printStackTrace();
                                }
                            }
                        });
            }
        });
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
}
