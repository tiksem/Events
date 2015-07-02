package com.khevents.network;

import com.jsonandroid.JsonAsyncNavigationList;
import com.khevents.data.Event;
import com.utilsframework.android.network.RequestExecutor;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by CM on 7/1/2015.
 */
class UserEventsNavigationList extends EventsNavigationList {
    public enum Mode {
        created,
        subscribed
    }

    public UserEventsNavigationList(String rootUrl, Mode mode,
                                    String token,
                                    RequestExecutor requestExecutor) {
        super(rootUrl + "getUserEvents", "subscribers",
                new HashMap<String, Object>(){
                    {
                        put("mod", mode);
                        put("token", token);
                    }
                },
                requestExecutor);
    }
}
