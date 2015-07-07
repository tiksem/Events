package com.khevents.ui.fragments;

import com.khevents.R;
import com.khevents.network.RequestManager;
import com.utils.framework.collections.NavigationList;
import com.utilsframework.android.adapters.ViewArrayAdapter;
import com.utilsframework.android.fragments.Fragments;
import com.utilsframework.android.social.SocialUtils;
import com.vkandroid.VkUser;
import com.vkandroid.VkUsersListAdapter;

/**
 * Created by CM on 6/17/2015.
 */
public class EventSubscribersListFragment extends AbstractNavigationListFragment<VkUser> {
    private static final String EVENT_ID = "id";

    public static EventSubscribersListFragment create(long eventId) {
        return Fragments.createFragmentWith1Arg(new EventSubscribersListFragment(), EVENT_ID, eventId);
    }

    @Override
    protected ViewArrayAdapter<VkUser, ?> createAdapter(RequestManager requestManager) {
        return new VkUsersListAdapter(getActivity());
    }

    @Override
    protected NavigationList<VkUser> getNavigationList(RequestManager requestManager, String filter) {
        return requestManager.getSubscribers(getArguments().getLong(EVENT_ID));
    }

    @Override
    protected void onListItemClicked(VkUser vkUser) {
        SocialUtils.openVkUserProfile(getActivity(), vkUser.id);
    }

    @Override
    protected int getRootLayout() {
        return R.layout.list_fragment;
    }
}
