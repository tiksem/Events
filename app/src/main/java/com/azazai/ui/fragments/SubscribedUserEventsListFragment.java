package com.azazai.ui.fragments;

import com.azazai.Level;
import com.azazai.R;
import com.azazai.data.Event;
import com.azazai.network.RequestManager;
import com.utils.framework.collections.LazyLoadingList;
import com.vk.sdk.VKSdk;
import com.vkandroid.VkApiUtils;

/**
 * Created by CM on 7/1/2015.
 */
public class SubscribedUserEventsListFragment extends UserEventsListFragment {
    @Override
    protected LazyLoadingList<Object> getLazyLoadingList(String filter) {
        return getRequestManager().getSubscribedUserEvents(VkApiUtils.getUserId());
    }

    @Override
    protected void onEmptyListActionButtonClicked() {
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
    protected int getEmptyResultsHintText() {
        return R.string.no_subscribed_events;
    }

    @Override
    protected void onSwipeRefresh() {
        getRequestManager().clearSubscribedEventsCache();
        super.onSwipeRefresh();
    }
}
