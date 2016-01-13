package com.azazai.network;

import com.jsonandroid.StringListJsonSuggestionsProvider;
import com.utils.framework.network.RequestExecutor;
import com.utils.framework.strings.Strings;
import com.utils.framework.suggestions.NetworkSuggestionsProvider;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by CM on 7/9/2015.
 */
class TagsSuggestionsProvider extends StringListJsonSuggestionsProvider {
    private final IgnoreTagsProvider ignoreTagsProvider;

    public TagsSuggestionsProvider(String rootUrl, IgnoreTagsProvider ignoreTagsProvider,
                                   RequestExecutor requestExecutor) {
        super(rootUrl + "searchTags", new HashMap<String, Object>(){
            {
                put("offset", 0);
                put("limit", 5);
            }
        }, requestExecutor, "Tags");

        this.ignoreTagsProvider = ignoreTagsProvider;
    }

    @Override
    public List<String> getSuggestions(String query) {
        getArgs().put("ignore", Strings.join(ignoreTagsProvider.getIgnoreTags(), ','));
        return super.getSuggestions(query);
    }
}
