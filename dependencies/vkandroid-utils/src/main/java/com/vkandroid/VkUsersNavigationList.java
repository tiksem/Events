package com.vkandroid;

import com.jsonandroid.JsonAsyncNavigationList;
import com.utils.framework.network.RequestExecutor;
import com.jsonutils.Json;
import com.utilsframework.android.network.RequestManager;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by CM on 6/16/2015.
 */
public class VkUsersNavigationList extends JsonAsyncNavigationList<VkUser> {
    public VkUsersNavigationList(String url, Map<String, Object> args,
                                 String jsonKey,
                                 RequestExecutor requestExecutor, RequestManager requestManager) {
        super(VkUser.class, url, jsonKey, args, requestExecutor, requestManager);
    }

    public VkUsersNavigationList(String url, String jsonKey, Map<String, Object> args) {
        super(VkUser.class, url, jsonKey, args);
    }

    @Override
    protected List<VkUser> getElements(String url, Map<String, Object> args, RequestExecutor requestExecutor,
                                       Class<VkUser> aClass) throws IOException {
        String response = requestExecutor.executeRequest(url, args);
        List<Long> ides = Json.parseLongArray(response, getJsonKey());
        return VkApiUtils.getUsers(ides, requestExecutor);
    }
}
