package com.khevents.ui.fragments;

import com.khevents.R;
import com.khevents.data.Event;
import com.khevents.network.RequestManager;
import com.utils.framework.collections.NavigationList;
import com.vk.sdk.VKSdk;

/**
 * Created by CM on 7/1/2015.
 */
public class CreatedUserEventsListFragment extends UserEventsListFragment {
    @Override
    protected NavigationList<Event> getNavigationList(RequestManager requestManager, String filter) {
        return requestManager.getCreatedUserEvents(VKSdk.getAccessToken().accessToken);
    }

    @Override
    protected boolean useCreateEventButton() {
        return true;
    }

    @Override
    protected void onEventActionButtonClicked() {
        createEvent();
    }

    @Override
    protected int getEventActionText() {
        return R.string.create_new_event;
    }

    @Override
    protected int getHintText() {
        return R.string.no_events_found;
    }
}
