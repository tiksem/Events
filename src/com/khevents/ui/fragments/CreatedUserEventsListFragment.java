package com.khevents.ui.fragments;

import android.os.Bundle;
import android.view.View;
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
    protected int getRootLayout() {
        return R.layout.created_event_list_fragment;
    }

    @Override
    protected boolean useCreateEventButton() {
        return true;
    }

    @Override
    protected boolean useUpdateBroadcastReceiver() {
        return true;
    }

    @Override
    protected int getEmptyListResourceId() {
        return R.id.no_created_events;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.create_event).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createEvent();
            }
        });
    }
}
