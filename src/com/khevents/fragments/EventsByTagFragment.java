package com.khevents.fragments;

import android.os.Bundle;
import com.khevents.R;
import com.khevents.data.Event;
import com.khevents.network.RequestManager;
import com.utils.framework.collections.NavigationList;
import com.utilsframework.android.fragments.Fragments;

/**
 * Created by CM on 7/2/2015.
 */
public class EventsByTagFragment extends EventsListFragment {
    public static final String TAG = "tag";

    public static EventsListFragment create(String tag) {
        return Fragments.createFragmentWith1Arg(new EventsByTagFragment(), TAG, tag);
    }

    @Override
    protected NavigationList<Event> getNavigationList(RequestManager requestManager, String filter) {
        return requestManager.getEventsByTag(getArguments().getString(TAG));
    }

    @Override
    protected int getRootLayout() {
        return R.layout.list_fragment;
    }
}
