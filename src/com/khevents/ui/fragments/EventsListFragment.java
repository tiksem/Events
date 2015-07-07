package com.khevents.ui.fragments;

import android.support.v4.app.Fragment;
import com.khevents.Level;
import com.khevents.adapters.EventsAdapter;
import com.khevents.data.Event;
import com.khevents.network.RequestManager;
import com.utilsframework.android.adapters.ViewArrayAdapter;

/**
 * Created by CM on 7/1/2015.
 */
public abstract class EventsListFragment extends AbstractNavigationListFragment<Event> {
    @Override
    protected ViewArrayAdapter<Event, ?> createAdapter(RequestManager requestManager) {
        return new EventsAdapter(getActivity());
    }

    @Override
    protected void onListItemClicked(Event event) {
        Fragment fragment = EventFragment.create(event);
        replaceFragment(fragment, Level.EVENT_PAGE);
    }
}
