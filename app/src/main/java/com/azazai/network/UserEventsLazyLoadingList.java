package com.azazai.network;

import com.utils.framework.network.RequestExecutor;

import java.util.HashMap;

/**
 * Created by CM on 7/1/2015.
 */
class UserEventsLazyLoadingList extends EventsLazyLoadingList {
    public enum Mode {
        created,
        subscribed
    }

    public UserEventsLazyLoadingList(String rootUrl, final Mode mode,
                                     final long userId,
                                     RequestExecutor requestExecutor, RequestManager requestManager) {
        super(rootUrl + "getUserEvents", "Events",
                new HashMap<String, Object>(){
                    {
                        put("mod", mode);
                        put("userId", userId);
                    }
                },
                requestExecutor, requestManager);
    }
}
