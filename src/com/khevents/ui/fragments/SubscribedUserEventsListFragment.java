package com.khevents.ui.fragments;

import com.khevents.Level;
import com.khevents.R;
import com.khevents.data.Event;
import com.khevents.network.RequestManager;
import com.utils.framework.collections.NavigationList;
import com.vk.sdk.VKSdk;

/**
 * Created by CM on 7/1/2015.
 */
public class SubscribedUserEventsListFragment extends UserEventsListFragment {
    @Override
    protected NavigationList<Event> getNavigationList(RequestManager requestManager, String filter) {
        return requestManager.getSubscribedUserEvents(VKSdk.getAccessToken().accessToken);
    }

    @Override
    protected void onEventActionButtonClicked() {
        findEvents();
    }

    private void findEvents() {
        replaceFragment(new AllEventsListFragment(), Level.FIND_EVENTS);
    }

    @Override
    protected int getEventActionText() {
        return R.string.find_events;
    }

    @Override
    protected int getHintText() {
        return R.string.no_subscribed_events;
    }
}
