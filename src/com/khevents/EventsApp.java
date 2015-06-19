package com.khevents;

import android.app.Application;
import com.jsonandroid.GetRequestExecutor;
import com.jsonandroid.RequestExecutor;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

/**
 * Created by CM on 6/17/2015.
 */
public class EventsApp extends Application {
    private static EventsApp instance;

    private RequestExecutor requestExecutor;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        requestExecutor = new GetRequestExecutor();
        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(this));
    }

    public static EventsApp getInstance() {
        return instance;
    }

    public RequestExecutor getRequestExecutor() {
        return requestExecutor;
    }
}
