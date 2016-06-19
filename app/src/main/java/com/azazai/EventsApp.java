package com.azazai;

import android.app.Application;
import android.os.Environment;

import com.azazai.network.RequestManager;
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
    private static final int MAX_DATABASE_CACHE_SIZE = 100;

    private static EventsApp instance;

    private VkUser currentUser;

    private String apiUrl;

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
        return new RequestManager(this, apiUrl, MAX_DATABASE_CACHE_SIZE);
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
