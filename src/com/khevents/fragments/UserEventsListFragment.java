package com.khevents.fragments;

import com.khevents.R;

/**
 * Created by CM on 7/1/2015.
 */
public abstract class UserEventsListFragment extends EventsListFragment {
    @Override
    protected boolean hasSearchMenu() {
        return false;
    }

    @Override
    protected int getRootLayout() {
        return R.layout.list_fragment;
    }
}
