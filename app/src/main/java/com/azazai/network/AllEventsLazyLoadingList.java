package com.azazai.network;

import com.utils.framework.network.RequestExecutor;

import java.util.Collections;
import java.util.HashMap;

/**
 * Created by CM on 7/2/2015.
 */
class AllEventsLazyLoadingList extends EventsLazyLoadingList {

    public static final String KEY = "Events";
    public static final String METHOD_NAME = "getEventsList";

    public AllEventsLazyLoadingList(String rootUrl, RequestExecutor requestExecutor, RequestManager requestManager) {
        super(rootUrl + METHOD_NAME, KEY, null, requestExecutor, requestManager);
    }

    public AllEventsLazyLoadingList(String rootUrl, int date, RequestExecutor requestExecutor,
                                    RequestManager requestManager) {
        super(rootUrl + METHOD_NAME, KEY,
                Collections.<String, Object>singletonMap("dateFilter", date),
                requestExecutor, requestManager);
    }

    public AllEventsLazyLoadingList(String rootUrl, String query, RequestExecutor requestExecutor,
                                    RequestManager requestManager) {
        super(rootUrl + METHOD_NAME, KEY,
                Collections.<String, Object>singletonMap("query", query),
                requestExecutor, requestManager);
    }

    public AllEventsLazyLoadingList(String rootUrl, final String query, final int date, RequestExecutor requestExecutor,
                                    RequestManager requestManager) {
        super(rootUrl + METHOD_NAME, KEY,
                new HashMap<String, Object>(){
                    {
                        put("query", query);
                        put("dateFilter", date);
                    }
                },
                requestExecutor, requestManager);
    }
}
