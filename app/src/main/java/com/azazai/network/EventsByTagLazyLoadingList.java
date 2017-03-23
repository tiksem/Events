package com.azazai.network;

import com.utils.framework.network.RequestExecutor;

import java.util.Collections;

/**
 * Created by CM on 7/2/2015.
 */
class EventsByTagLazyLoadingList extends EventsLazyLoadingList {
    public EventsByTagLazyLoadingList(String rootUrl, String tag,
                                      RequestExecutor requestExecutor, RequestManager requestManager) {
        super(rootUrl + "getEventsByTag", "Events",
                Collections.<String, Object>singletonMap("tag", tag), requestExecutor, requestManager);
    }
}
