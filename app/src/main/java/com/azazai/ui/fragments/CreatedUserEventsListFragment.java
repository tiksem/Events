package com.azazai.ui.fragments;

import com.azazai.R;
import com.azazai.data.Event;
import com.azazai.network.RequestManager;
import com.utils.framework.collections.LazyLoadingList;
import com.vk.sdk.VKSdk;
import com.vkandroid.VkApiUtils;

/**
 * Created by CM on 7/1/2015.
 */
public class CreatedUserEventsListFragment extends UserEventsListFragment {
    @Override
    protected LazyLoadingList<Event> getLazyLoadingList(String filter) {
        return getRequestManager().getCreatedUserEvents(VkApiUtils.getUserId());
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
    protected int getEmptyResultsHintText() {
        return R.string.no_events_found;
    }

    @Override
    protected int getRootLayout() {
        return R.layout.events_list_fragment_with_fab;
    }
}
