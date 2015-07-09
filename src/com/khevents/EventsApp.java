package com.khevents;

import android.app.Application;
import android.os.Environment;
import com.khevents.network.RequestManager;
import com.khevents.vk.VkManager;
import com.utils.framework.io.IOUtilities;
import com.utils.framework.io.Network;
import com.utils.framework.strings.Strings;
import com.utilsframework.android.file.IoUtils;
import com.utilsframework.android.network.GetRequestExecutor;
import com.utilsframework.android.network.RequestExecutor;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.utilsframework.android.view.UiMessages;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKSdk;
import com.vk.sdk.VKSdkListener;
import com.vk.sdk.api.VKError;
import com.vk.sdk.util.VKUtil;
import com.vkandroid.VkUser;

import java.io.IOException;

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
    }

    public static EventsApp getInstance() {
        return instance;
    }

    public RequestManager getRequestManager() {
        return requestManager;
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
