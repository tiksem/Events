package com.khevents;

import android.app.Fragment;
import com.khevents.adapters.TagsAdapter;
import com.khevents.data.Event;
import com.khevents.data.Tag;
import com.khevents.network.RequestManager;
import com.utils.framework.collections.NavigationList;
import com.utilsframework.android.adapters.ViewArrayAdapter;

/**
 * Created by CM on 7/1/2015.
 */
public class TagsListFragment extends AbstractNavigationListFragment<Tag> {
    @Override
    protected ViewArrayAdapter<Tag, ?> createAdapter(RequestManager requestManager) {
        return new TagsAdapter(getActivity());
    }

    @Override
    protected NavigationList<Tag> getNavigationList(RequestManager requestManager, String filter) {
        return requestManager.getTags();
    }

    @Override
    protected void onListItemClicked(Tag tag) {

    }

    @Override
    protected int getRootLayout() {
        return R.layout.tags_list_fragment;
    }


}
