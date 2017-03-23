package com.azazai.ui.fragments;

import com.azazai.R;
import com.azazai.network.RequestManager;
import com.utils.framework.collections.LazyLoadingList;
import com.utilsframework.android.adapters.ViewArrayAdapter;
import com.utilsframework.android.fragments.Fragments;
import com.utilsframework.android.navdrawer.ActionBarTitleProvider;
import com.utilsframework.android.social.SocialUtils;
import com.vkandroid.VkUser;
import com.vkandroid.VkUsersListAdapter;

/**
 * Created by CM on 6/17/2015.
 */
public class EventSubscribersListFragment extends AbstractLazyLoadingListFragment<VkUser>
        implements ActionBarTitleProvider {
    private static final String EVENT_ID = "id";

    public static EventSubscribersListFragment create(long eventId) {
        return Fragments.createFragmentWith1Arg(new EventSubscribersListFragment(), EVENT_ID, eventId);
    }

    @Override
    protected ViewArrayAdapter<VkUser, ?> createAdapter() {
        return new VkUsersListAdapter(getActivity());
    }

    @Override
    protected LazyLoadingList<VkUser> getLazyLoadingList(String filter) {
        return getRequestManager().getSubscribers(getArguments().getLong(EVENT_ID));
    }

    @Override
    protected void onListItemClicked(VkUser vkUser, int position) {
        SocialUtils.openVkUserProfile(getActivity(), vkUser.id);
    }

    @Override
    protected int getRootLayout() {
        return R.layout.list_fragment;
    }

    @Override
    public String getActionBarTitle() {
        return getString(R.string.participants);
    }
}
