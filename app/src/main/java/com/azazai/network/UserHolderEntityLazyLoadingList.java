package com.azazai.network;

import com.azazai.data.VkUserHolderEntity;
import com.jsonandroid.JsonAsyncLazyLoadingList;
import com.utils.framework.network.RequestExecutor;
import com.utilsframework.android.network.LegacyRequestManager;
import com.utilsframework.android.network.RequestManager;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class UserHolderEntityLazyLoadingList<T extends VkUserHolderEntity>
        extends JsonAsyncLazyLoadingList<T> {
    public UserHolderEntityLazyLoadingList(Class<T> aClass, String url,
                                           String jsonKey,
                                           Map<String, Object> args,
                                           RequestExecutor requestExecutor,
                                           LegacyRequestManager requestManager,
                                           List<T> preloadedElements) {
        super(aClass, url, jsonKey, args, requestExecutor, requestManager, preloadedElements);
    }

    public UserHolderEntityLazyLoadingList(Class<T> aClass, String url, String jsonKey,
                                           Map<String, Object> args,
                                           RequestExecutor requestExecutor,
                                           LegacyRequestManager requestManager) {
        super(aClass, url, jsonKey, args, requestExecutor, requestManager);
    }

    public UserHolderEntityLazyLoadingList(Class<T> aClass, String url,
                                           String jsonKey,
                                           Map<String, Object> args) {
        super(aClass, url, jsonKey, args);
    }

    @Override
    protected List<T> getElements(String url, Map<String, Object> args,
                                  RequestExecutor requestExecutor,
                                  Class<T> aClass) throws IOException {
        List<T> list = super.getElements(url, args, requestExecutor, aClass);
        Requests.updateUserData(requestExecutor, list);
        return list;
    }
}
