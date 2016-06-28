package com.azazai.network;

import com.azazai.data.Request;
import com.utils.framework.network.RequestExecutor;

import java.util.Map;

public class RequestsNavigationList extends UserHolderEntityNavigationList<Request> {
    public RequestsNavigationList(String url,
                                  Map<String, Object> args,
                                  RequestExecutor requestExecutor,
                                  RequestManager requestManager) {
        super(Request.class, url,
                "Requests", args, requestExecutor, requestManager);
    }
}
