package com.vkandroid;

import com.jsonandroid.JsonAsyncLazyLoadingList;
import com.utils.framework.network.RequestExecutor;
import com.jsonutils.Json;
import com.utilsframework.android.network.LegacyRequestManager;
import com.utilsframework.android.network.RequestManager;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by CM on 6/16/2015.
 */
public class VkUsersLazyLoadingList extends JsonAsyncLazyLoadingList<VkUser> {
    public VkUsersLazyLoadingList(String url, Map<String, Object> args,
                                  String jsonKey,
                                  RequestExecutor requestExecutor,
                                  LegacyRequestManager requestManager) {
        super(VkUser.class, url, jsonKey, args, requestExecutor, requestManager);
    }

    public VkUsersLazyLoadingList(String url, String jsonKey, Map<String, Object> args) {
        super(VkUser.class, url, jsonKey, args);
    }

    @Override
    protected List<VkUser> getElements(String url, Map<String, Object> args, RequestExecutor requestExecutor,
                                       Class<? extends VkUser> aClass) throws IOException {
        String response = requestExecutor.executeRequest(url, args);
        List<Long> ides = Json.parseLongArray(response, getJsonKey());
        return VkApiUtils.getUsers(ides, requestExecutor);
    }
}
