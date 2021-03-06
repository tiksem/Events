package com.azazai.network;

import com.azazai.data.Event;
import com.azazai.data.Request;
import com.jsonutils.Json;
import com.utils.framework.CollectionUtils;
import com.utils.framework.Transformer;
import com.utils.framework.network.RequestExecutor;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventRequestsLazyLoadingList extends RequestsLazyLoadingList {
    private Event event;

    public EventRequestsLazyLoadingList(String rootUrl,
                                        final Event event,
                                        RequestExecutor requestExecutor,
                                        RequestManager requestManager) {
        super(rootUrl + "getRequests", new HashMap<String, Object>() {
            {
                put("id", event.id);
            }
        }, requestExecutor, requestManager);
        this.event = event;
    }

    @Override
    protected List<Request> getElements(String url, Map<String, Object> args,
                                       RequestExecutor requestExecutor,
                                       Class<? extends Request> aClass) throws IOException {
        String response = requestExecutor.executeRequest(url, args);
        List<Long> ides = Json.parseLongArray(response, getJsonKey());
        final List<Request> result = CollectionUtils.transform(
                ides, new Transformer<Long, Request>() {
            @Override
            public Request get(Long id) {
                Request request = new Request();
                request.event = event;
                request.userId = id;
                return request;
            }
        });
        Requests.updateUserData(requestExecutor, result);
        return result;
    }
}
