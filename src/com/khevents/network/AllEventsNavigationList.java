package com.khevents.network;

import com.utils.framework.network.RequestExecutor;

import java.util.Collections;
import java.util.HashMap;

/**
 * Created by CM on 7/2/2015.
 */
class AllEventsNavigationList extends EventsNavigationList {
    public AllEventsNavigationList(String rootUrl, RequestExecutor requestExecutor, RequestManager requestManager) {
        super(rootUrl + "getEventsList", "events", null, requestExecutor, requestManager);
    }

    public AllEventsNavigationList(String rootUrl, int date, RequestExecutor requestExecutor,
                                   RequestManager requestManager) {
        super(rootUrl + "getEventsList", "events",
                Collections.<String, Object>singletonMap("dateFilter", date),
                requestExecutor, requestManager);
    }

    public AllEventsNavigationList(String rootUrl, String query, RequestExecutor requestExecutor,
                                   RequestManager requestManager) {
        super(rootUrl + "getEventsList", "events",
                Collections.<String, Object>singletonMap("query", query),
                requestExecutor, requestManager);
    }

    public AllEventsNavigationList(String rootUrl, final String query, final int date, RequestExecutor requestExecutor,
                                   RequestManager requestManager) {
        super(rootUrl + "getEventsList", "events",
                new HashMap<String, Object>(){
                    {
                        put("query", query);
                        put("dateFilter", date);
                    }
                },
                requestExecutor, requestManager);
    }
}
