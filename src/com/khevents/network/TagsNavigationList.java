package com.khevents.network;

import com.jsonandroid.JsonAsyncNavigationList;
import com.khevents.data.Tag;
import com.utilsframework.android.network.RequestExecutor;

import java.util.Map;

/**
 * Created by CM on 7/1/2015.
 */
class TagsNavigationList extends JsonAsyncNavigationList<Tag> {
    public TagsNavigationList(RequestExecutor requestExecutor, String rootUrl) {
        super(Tag.class, rootUrl + "getTags", "Tags", null, requestExecutor);
    }
}
