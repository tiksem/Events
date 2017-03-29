package com.azazai;

import android.app.Application;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import com.azazai.network.RequestManager;
import com.squareup.picasso.LruCache;
import com.squareup.picasso.Picasso;
import com.utils.framework.io.IOUtilities;
import com.utils.framework.strings.Strings;
import com.utilsframework.android.UiLoopEvent;
import com.vk.sdk.util.VKUtil;
import com.vkandroid.VkApiUtils;
import com.vkandroid.VkUser;

import java.io.IOException;

/**
 * Created by CM on 6/17/2015.
 */
public class EventsApp extends Application {
    public static final boolean DEBUG = true;
    public static final int IMAGES_CACHE_SIZE = 24 * 1024 * 1024;

    private static EventsApp instance;

    private VkUser currentUser;

    private String apiUrl;

    private void setupPicasso() {
        Picasso.Builder picassoBuilder = new Picasso.Builder(this);
        picassoBuilder.listener(new Picasso.Listener() {
            @Override
            public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                Log.i(Picasso.class.getSimpleName(), "loading image " + uri + " failed", exception);
            }
        });
        picassoBuilder.memoryCache(new LruCache(IMAGES_CACHE_SIZE));
        Picasso.setSingletonInstance(picassoBuilder.build());
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        String[] fingerprints = VKUtil.getCertificateFingerprint(this, this.getPackageName());
        Strings.join(fingerprints, ':');

        if (DEBUG) {
            // This is for debug purposes Place breakpoint in run and evaluate expression in Intellij Idea
            new UiLoopEvent().run(new Runnable() {
                @Override
                public void run() {

                }
            });
        }

        RequestManager.init();

        setupPicasso();
        apiUrl = "http://azazai.com/api/";
    }

    public void initIp() {
        if (apiUrl != null) {
            return;
        }

        String ipFile = Environment.getExternalStorageDirectory().getAbsolutePath() +
                "/EventsApp/ip.txt";
        try {
            String ip = IOUtilities.readStringFromUrl(ipFile);
            apiUrl = ip + "/api/";
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static EventsApp getInstance() {
        return instance;
    }

    public RequestManager createRequestManager() {
        return new RequestManager(this, apiUrl);
    }

    public void logout() {
        VkApiUtils.logout(this);
        currentUser = null;
    }

    public void initVkUser(VkUser vkUser) {
        if (currentUser != null) {
            throw new IllegalStateException("currentUser is already set");
        }

        currentUser = vkUser;
    }

    public VkUser getCurrentUser() {
        return currentUser;
    }
}
