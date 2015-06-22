package com.khevents;

import android.app.Application;
import android.os.Environment;
import com.khevents.network.RequestManager;
import com.utils.framework.io.IOUtilities;
import com.utils.framework.io.Network;
import com.utilsframework.android.file.IoUtils;
import com.utilsframework.android.network.GetRequestExecutor;
import com.utilsframework.android.network.RequestExecutor;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.io.IOException;

/**
 * Created by CM on 6/17/2015.
 */
public class EventsApp extends Application {
    private static EventsApp instance;

    private RequestManager requestManager;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(this));
        String ipFile = Environment.getExternalStorageDirectory().getAbsolutePath() + "/EventsApp/ip.txt";
        try {
            String ip = IOUtilities.readStringFromUrl(ipFile);
            requestManager = new RequestManager(ip + "/api/");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static EventsApp getInstance() {
        return instance;
    }

    public RequestManager getRequestManager() {
        return requestManager;
    }
}
