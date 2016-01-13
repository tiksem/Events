package com.azazai.vk;

import android.content.Context;
import android.content.SharedPreferences;
import com.utilsframework.android.threading.OnFinish;
import com.utilsframework.android.threading.Tasks;
import com.utilsframework.android.view.Alerts;
import com.utilsframework.android.view.Toasts;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKScope;
import com.vk.sdk.VKSdk;
import com.vk.sdk.VKSdkListener;
import com.vk.sdk.api.VKError;
import com.vk.sdk.dialogs.VKCaptchaDialog;
import com.vkandroid.VkApiUtils;

import java.util.ArrayDeque;

/**
 * Created by CM on 6/23/2015.
 */
public class VkManager {
    private static final String APP_ID = "4969665";
    private static final String[] SCOPES = new String[]{VKScope.OFFLINE, VKScope.WALL};

    public static void getAccessToken(final Context context, final int errorMessageId, final OnFinish<VKAccessToken> onFinish) {
        VkApiUtils.getAccessToken(context, APP_ID, SCOPES, new VkApiUtils.AuthorizationListener() {
            @Override
            public void onSuccess(VKAccessToken token) {
                onFinish.onFinish(token);
            }

            @Override
            public void onError(VKError authorizationError) {
                Alerts.showOkButtonAlert(context, context.getString(errorMessageId));
                onFinish.onFinish(null);
            }
        });
    }

    public interface OnAccessTokenGot {
        public void onAccessToken();
    }

    public static boolean initialize(final Context context, final int errorMessageId, final OnAccessTokenGot onAccessTokenGot) {
        VKAccessToken accessToken = VkApiUtils.getAccessTokenFromSharedPreferences(context);

        VkApiUtils.initialize(context, APP_ID, SCOPES, new VkApiUtils.AuthorizationListener() {
            @Override
            public void onSuccess(VKAccessToken token) {
                onAccessTokenGot.onAccessToken();
            }

            @Override
            public void onError(VKError authorizationError) {

            }
        }, accessToken);

        return accessToken != null;
    }

    public static void authorize() {
        VKSdk.authorize(SCOPES);
    }
}
