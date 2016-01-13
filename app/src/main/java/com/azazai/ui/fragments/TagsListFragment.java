package com.azazai.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import com.azazai.Level;
import com.azazai.R;
import com.azazai.adapters.TagsAdapter;
import com.azazai.data.Tag;
import com.azazai.network.RequestManager;
import com.utils.framework.collections.NavigationList;
import com.utilsframework.android.adapters.ViewArrayAdapter;

/**
 * Created by CM on 7/1/2015.
 */
public class TagsListFragment extends NavigationListFragmentWithEmptyResults<Tag> {
    @Override
    protected ViewArrayAdapter<Tag, ?> createAdapter(RequestManager requestManager) {
        return new TagsAdapter(getActivity());
    }

    @Override
    protected NavigationList<Tag> getNavigationList(RequestManager requestManager, String filter) {
        return requestManager.getTags();
    }

    @Override
    protected void onListItemClicked(Tag tag, int position) {
        Fragment fragment = EventsByTagFragment.create(tag.name);
        replaceFragment(fragment, Level.TAG_EVENTS);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    protected int getRootLayout() {
        return R.layout.list_fragment_with_empty_results;
    }

    @Override
    protected int getEmptyListResourceId() {
        return R.id.no_events;
    }

    @Override
    protected int getHintText() {
        return R.string.no_tags_found;
    }

    @Override
    protected int getEmptyEventsActionText() {
        return R.string.create_first_event;
    }
}
