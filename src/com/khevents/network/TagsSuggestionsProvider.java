package com.khevents.network;

import com.jsonandroid.StringListJsonSuggestionsProvider;
import com.utils.framework.network.RequestExecutor;
import com.utils.framework.suggestions.NetworkSuggestionsProvider;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by CM on 7/9/2015.
 */
class TagsSuggestionsProvider extends StringListJsonSuggestionsProvider {
    public TagsSuggestionsProvider(String rootUrl, RequestExecutor requestExecutor) {
        super(rootUrl + "searchTags", new HashMap<String, Object>(){
            {
                put("offset", 0);
                put("limit", 5);
            }
        }, requestExecutor, "Tags");
    }
}
