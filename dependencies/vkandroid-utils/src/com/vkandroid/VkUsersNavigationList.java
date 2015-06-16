package com.vkandroid;

import com.jsonandroid.JsonAsyncNavigationList;
import com.jsonandroid.RequestExecutor;
import com.jsonutils.Json;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created by CM on 6/16/2015.
 */
public class VkUsersNavigationList extends JsonAsyncNavigationList<VkUser> {
    public VkUsersNavigationList(String url, Map<String, String> args,
                                 RequestExecutor requestExecutor) {
        super(VkUser.class, url, args, requestExecutor);
    }

    public VkUsersNavigationList(String url, Map<String, String> args) {
        super(VkUser.class, url, args);
    }

    @Override
    protected List<VkUser> getElements(String url, Map<String, Object> args, RequestExecutor requestExecutor,
                                       Class<VkUser> aClass) throws IOException {
        String response = requestExecutor.executeRequest(url, args);
        List<Long> ides = Json.parseJsonArray(response, Long.class);
        return VkApiUtils.getUsers(ides, requestExecutor);
    }
}
