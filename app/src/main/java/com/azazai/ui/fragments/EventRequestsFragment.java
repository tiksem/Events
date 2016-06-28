package com.azazai.ui.fragments;

import android.app.Activity;

import com.azazai.data.Event;
import com.azazai.data.Request;
import com.azazai.network.RequestManager;
import com.utils.framework.collections.NavigationList;
import com.utilsframework.android.fragments.Fragments;

public class EventRequestsFragment extends AbstractRequestsFragment {
    public static final String EVENT = "event";
    private Event event;

    public static EventRequestsFragment create(Event event) {
        return Fragments.createFragmentWith1Arg(new EventRequestsFragment(), EVENT, event);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        event = Fragments.getParcelable(this, EVENT);
    }

    @Override
    protected NavigationList<Request> getNavigationList(RequestManager requestManager,
                                                        String filter) {
        return requestManager.getRequestsByEvent(event);
    }

    @Override
    protected int getEmptyEventsActionText() {
        return 0;
    }
}
