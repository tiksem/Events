package com.azazai.network;

import com.azazai.data.Icon;
import com.jsonandroid.JsonAsyncLazyLoadingList;
import com.utils.framework.network.RequestExecutor;
import com.utilsframework.android.network.LegacyRequestManager;

/**
 * Created by stikhonenko on 3/28/17.
 */
public class IconsLazyLoadingList extends JsonAsyncLazyLoadingList<Icon> {
    public IconsLazyLoadingList(String rootUrl,
                                RequestExecutor requestExecutor,
                                LegacyRequestManager requestManager) {
        super(Icon.class, rootUrl + "getIcons", "Icons", null, requestExecutor, requestManager);
    }
}
