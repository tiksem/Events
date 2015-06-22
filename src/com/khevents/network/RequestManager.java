package com.khevents.network;

import com.khevents.data.Event;
import com.utils.framework.collections.NavigationList;
import com.utilsframework.android.IOErrorListener;
import com.utilsframework.android.network.GetRequestExecutor;
import com.utilsframework.android.network.IOErrorListenersSet;
import com.utilsframework.android.network.RequestExecutor;

/**
 * Created by CM on 6/21/2015.
 */
public class RequestManager implements IOErrorListenersSet {
    private RequestExecutor requestExecutor = new GetRequestExecutor();
    private String rootUrl;

    public RequestManager(String rootUrl) {
        this.rootUrl = rootUrl;
    }

    public NavigationList<Event> getEvents() {
        return new EventsNavigationList(rootUrl, requestExecutor);
    }

    @Override
    public void addIOErrorListener(IOErrorListener listener) {

    }

    @Override
    public void removeIOErrorListener(IOErrorListener listener) {

    }
}
