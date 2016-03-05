package com.azazai.ui.fragments;

import com.azazai.R;
import com.azazai.data.Event;
import com.azazai.network.RequestManager;
import com.utils.framework.collections.NavigationList;
import com.vk.sdk.VKSdk;

/**
 * Created by CM on 7/1/2015.
 */
public class CreatedUserEventsListFragment extends UserEventsListFragment {
    @Override
    protected NavigationList<Event> getNavigationList(RequestManager requestManager, String filter) {
        return requestManager.getCreatedUserEvents(Integer.valueOf(VKSdk.getAccessToken().userId));
    }

    @Override
    protected boolean useCreateEventButton() {
        return true;
    }

    @Override
    protected int getEmptyEventsActionText() {
        return R.string.create_new_event;
    }

    @Override
    protected int getHintText() {
        return R.string.no_events_found;
    }

    @Override
    protected int getRootLayout() {
        return R.layout.events_list_fragment_with_fab;
    }
}
