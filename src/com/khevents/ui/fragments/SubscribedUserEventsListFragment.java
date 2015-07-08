package com.khevents.ui.fragments;

import com.khevents.R;
import com.khevents.data.Event;
import com.khevents.network.RequestManager;
import com.utils.framework.collections.NavigationList;
import com.utilsframework.android.navdrawer.ActionBarTitleProvider;
import com.vk.sdk.VKSdk;

/**
 * Created by CM on 7/1/2015.
 */
public class SubscribedUserEventsListFragment extends UserEventsListFragment {
    @Override
    protected NavigationList<Event> getNavigationList(RequestManager requestManager, String filter) {
        return requestManager.getSubscribedUserEvents(VKSdk.getAccessToken().accessToken);
    }
}
