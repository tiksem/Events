package com.azazai.network;

import com.utils.framework.network.RequestExecutor;

import java.util.HashMap;

/**
 * Created by CM on 7/1/2015.
 */
class UserEventsNavigationList extends EventsNavigationList {
    public enum Mode {
        created,
        subscribed
    }

    public UserEventsNavigationList(String rootUrl, final Mode mode,
                                    final int userId,
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
