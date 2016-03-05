package com.azazai.ui.fragments;

import com.azazai.Level;
import com.azazai.R;
import com.azazai.data.Event;
import com.azazai.network.RequestManager;
import com.utils.framework.collections.NavigationList;
import com.vk.sdk.VKSdk;

/**
 * Created by CM on 7/1/2015.
 */
public class SubscribedUserEventsListFragment extends UserEventsListFragment {
    @Override
    protected NavigationList<Event> getNavigationList(RequestManager requestManager, String filter) {
        return requestManager.getSubscribedUserEvents(Integer.valueOf(
                VKSdk.getAccessToken().userId));
    }

    @Override
    protected void onEmptyEventsActionButtonClicked() {
        findEvents();
    }

    private void findEvents() {
        replaceFragment(new AllEventsListFragment(), Level.FIND_EVENTS);
    }

    @Override
    protected int getEmptyEventsActionText() {
        return R.string.find_events;
    }

    @Override
    protected int getHintText() {
        return R.string.no_subscribed_events;
    }
}
