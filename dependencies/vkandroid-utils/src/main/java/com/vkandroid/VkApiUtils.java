package com.vkandroid;

import android.content.Context;

import com.utils.framework.CollectionUtils;
import com.utils.framework.KeyProvider;
import com.utils.framework.Maps;
import com.utils.framework.Predicate;
import com.utils.framework.Transformer;
import com.utils.framework.network.GetRequestExecutor;
import com.utils.framework.network.RequestExecutor;
import com.jsonutils.Json;
import com.utils.framework.ArrayUtils;
import com.utils.framework.strings.Strings;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKSdk;
import com.vk.sdk.VKSdkListener;
import com.vk.sdk.api.VKError;
import com.vk.sdk.dialogs.VKCaptchaDialog;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created by CM on 6/17/2015.
 */
public class VkApiUtils {
    private static final String VK_ACCESS_TOKEN = "VK_ACCESS_TOKEN";

    public static String getUsersRequestUrl(List<Long> ides, List<String> fields) {
        if (ides.isEmpty()) {
            throw new IllegalArgumentException("Empty ides");
        }
        ides = CollectionUtils.unique(ides);

        String url = "https://api.vk.com/method/users.get?user_ids=" + Strings.joinObjects(ides, ',');
        if (!fields.isEmpty()) {
            url += "&fields=" + Strings.join(fields, ',');
        }

        return url;
    }

    public static List<VkUser> getUsers(List<Long> ides, RequestExecutor requestExecutor)
            throws IOException {
        if (ides.isEmpty()) {
            return new ArrayList<>();
        }

        String vkUrl = VkApiUtils.getUsersRequestUrl(ides, Collections.singletonList("photo_100"));
        String response = requestExecutor.executeRequest(vkUrl, null);
        final List<VkUser> users = Json.readList(response, "response", VkUser.class);

        final Map<Long, VkUser> usersMap = Maps.fromList(users, new KeyProvider<Long, VkUser>() {
            @Override
            public Long getKey(VkUser value) {
                return value.id;
            }
        });

        return CollectionUtils.transform(ides, new Transformer<Long, VkUser>() {
            @Override
            public VkUser get(final Long id) {
                return usersMap.get(id);
            }
        });
    }

    public static List<VkUser> getUsers(List<Long> ides)
            throws IOException {
        return getUsers(ides, new GetRequestExecutor());
    }

    public static VkUser getUser(long id, RequestExecutor requestExecutor) throws IOException {
        List<VkUser> users = getUsers(Collections.singletonList(id), requestExecutor);
        if (users.isEmpty()) {
            return null;
        }

        return users.get(0);
    }

    public static List<VkUser> getUsers(long... ides) throws IOException {
        return getUsers(ArrayUtils.asList(ides));
    }

    public interface AuthorizationListener {
        void onSuccess(VKAccessToken token);
        void onError(VKError authorizationError);
    }

    public static void getAccessToken(Context context, String appId, String[] scopes,
                                      AuthorizationListener listener) {
        VKAccessToken token = VKSdk.getAccessToken();
        if (token != null) {
            listener.onSuccess(token);
            return;
        }

        token = getAccessTokenFromSharedPreferences(context);
        initialize(context, appId, scopes, listener, token);

        if (token == null) {
            VKSdk.authorize(scopes);
        }
    }

    public static void initialize(final Context context, String appId, final String[] scopes,
                                  final AuthorizationListener listener, VKAccessToken token) {
        VKSdk.initialize(new VKSdkListener() {
            @Override
            public void onCaptchaError(VKError captchaError) {
                new VKCaptchaDialog(captchaError).show(context);
            }

            @Override
            public void onTokenExpired(VKAccessToken expiredToken) {
                VKSdk.authorize(scopes);
            }

            @Override
            public void onAccessDenied(VKError authorizationError) {
                listener.onError(authorizationError);
            }

            @Override
            public void onReceiveNewToken(VKAccessToken newToken) {
                newToken.saveTokenToSharedPreferences(context, VK_ACCESS_TOKEN);
                listener.onSuccess(newToken);
            }

            @Override
            public void onAcceptUserToken(VKAccessToken token) {
                listener.onSuccess(token);
            }
        }, appId, token);
    }

    public static VKAccessToken getAccessTokenFromSharedPreferences(Context context) {
        return VKAccessToken.tokenFromSharedPreferences(context, VK_ACCESS_TOKEN);
    }

    public static void logout(Context context) {
        VKAccessToken.removeTokenAtKey(context, VK_ACCESS_TOKEN);
        VKSdk.logout();
    }

    public static long getUserId() {
        return Long.valueOf(VKSdk.getAccessToken().userId);
    }
}
