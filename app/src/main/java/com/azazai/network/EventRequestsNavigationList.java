package com.azazai.network;

import com.azazai.data.Event;
import com.azazai.data.Request;
import com.jsonutils.Json;
import com.utils.framework.CollectionUtils;
import com.utils.framework.Lists;
import com.utils.framework.Transformer;
import com.utils.framework.network.RequestExecutor;
import com.vkandroid.VkApiUtils;
import com.vkandroid.VkUser;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventRequestsNavigationList extends RequestsNavigationList {
    private Event event;

    public EventRequestsNavigationList(String rootUrl,
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
                                       Class<Request> aClass) throws IOException {
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
