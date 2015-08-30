package com.khevents;

import android.app.Application;
import android.os.Environment;
import com.khevents.gcm.GCM;
import com.khevents.network.RequestManager;
import com.utils.framework.io.IOUtilities;
import com.utils.framework.strings.Strings;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.utilsframework.android.UiLoopEvent;
import com.utilsframework.android.resources.LocaleUtils;
import com.vk.sdk.util.VKUtil;
import com.vkandroid.VkApiUtils;
import com.vkandroid.VkUser;

import java.io.IOException;
import java.util.Locale;

/**
 * Created by CM on 6/17/2015.
 */
public class EventsApp extends Application {
    private static EventsApp instance;

    private RequestManager requestManager;
    private VkUser currentUser;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        ImageLoaderConfiguration.Builder builder = new ImageLoaderConfiguration.Builder(this);
        builder.memoryCacheSize(2 * 1024 * 1024);
        builder.threadPoolSize(2);
        ImageLoader.getInstance().init(builder.build());

        String ipFile = Environment.getExternalStorageDirectory().getAbsolutePath() + "/EventsApp/ip.txt";
        try {
            String ip = IOUtilities.readStringFromUrl(ipFile);
            requestManager = new RequestManager(ip + "/api/");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String[] fingerprints = VKUtil.getCertificateFingerprint(this, this.getPackageName());
        Strings.join(fingerprints, ':');

        new UiLoopEvent(this).run(new Runnable() {
            @Override
            public void run() {

            }
        });
    }

    public static EventsApp getInstance() {
        return instance;
    }

    public RequestManager getRequestManager() {
        return requestManager;
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
