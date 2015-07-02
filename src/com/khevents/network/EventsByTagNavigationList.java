package com.khevents.network;

import com.utilsframework.android.network.RequestExecutor;

import java.util.Collections;
import java.util.Map;

/**
 * Created by CM on 7/2/2015.
 */
class EventsByTagNavigationList extends EventsNavigationList {
    public EventsByTagNavigationList(String rootUrl, String tag,
                                     RequestExecutor requestExecutor) {
        super(rootUrl + "getEventsByTag", "subscribers",
                Collections.singletonMap("tag", tag), requestExecutor);
    }
}
