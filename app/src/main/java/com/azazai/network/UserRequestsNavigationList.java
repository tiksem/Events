package com.azazai.network;

import com.utils.framework.network.RequestExecutor;

import java.util.HashMap;

public class UserRequestsNavigationList extends RequestsNavigationList {
    public UserRequestsNavigationList(String rootUrl,
                                      final long userId,
                                      RequestExecutor requestExecutor,
                                      RequestManager requestManager) {
        super(rootUrl + "getAllRequests", new HashMap<String, Object>() {
            {
                put("userId", userId);
            }
        }, requestExecutor, requestManager);
    }
}
