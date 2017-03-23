package com.azazai.network;

import com.jsonandroid.JsonAsyncLazyLoadingList;
import com.azazai.data.Tag;
import com.utils.framework.KeyProvider;
import com.utils.framework.network.RequestExecutor;

/**
 * Created by CM on 7/1/2015.
 */
class TagsLazyLoadingList extends JsonAsyncLazyLoadingList<Tag> {
    public TagsLazyLoadingList(RequestExecutor requestExecutor, RequestManager requestManager,
                               String rootUrl) {
        super(Tag.class, rootUrl + "getTags", "Tags", null, requestExecutor, requestManager);
    }

    @Override
    protected KeyProvider<Object, Tag> getKeyProvider() {
        return new KeyProvider<Object, Tag>() {
            @Override
            public Object getKey(Tag tag) {
                return tag.name;
            }
        };
    }
}
