package com.khevents.network;

import com.jsonandroid.JsonAsyncNavigationList;
import com.khevents.data.Tag;
import com.utils.framework.network.RequestExecutor;

/**
 * Created by CM on 7/1/2015.
 */
class TagsNavigationList extends JsonAsyncNavigationList<Tag> {
    public TagsNavigationList(RequestExecutor requestExecutor, String rootUrl) {
        super(Tag.class, rootUrl + "getTags", "Tags", null, requestExecutor);
    }
}
