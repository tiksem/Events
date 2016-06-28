package com.azazai.ui.fragments;

import com.azazai.R;
import com.azazai.data.Request;
import com.azazai.network.RequestManager;
import com.utils.framework.collections.NavigationList;
import com.vkandroid.VkApiUtils;

public class UserRequestsFragment extends AbstractRequestsFragment {
    @Override
    protected NavigationList<Request> getNavigationList(RequestManager requestManager,
                                                        String filter) {
        return requestManager.getRequestsByUserId(VkApiUtils.getUserId());
    }

    @Override
    protected void onEmptyListActionButtonClicked() {
        createEvent(true);
    }

    @Override
    protected int getEmptyEventsActionText() {
        return R.string.create_private_event;
    }
}
