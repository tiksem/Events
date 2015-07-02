package com.khevents.fragments;

import android.support.v4.app.Fragment;
import com.khevents.Level;
import com.khevents.R;
import com.khevents.adapters.TagsAdapter;
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
        Fragment fragment = EventsByTagFragment.create(tag.name);
        replaceFragment(fragment, Level.TAG_EVENTS);
    }

    @Override
    protected int getRootLayout() {
        return R.layout.list_fragment;
    }
}
