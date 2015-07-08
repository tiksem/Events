package com.khevents.ui.fragments;

import android.app.Activity;
import com.khevents.R;
import com.khevents.data.Event;
import com.khevents.network.RequestManager;
import com.utils.framework.collections.NavigationList;
import com.utilsframework.android.fragments.Fragments;
import com.utilsframework.android.navdrawer.ActionBarTitleProvider;

/**
 * Created by CM on 7/2/2015.
 */
public class EventsByTagFragment extends EventsListFragment implements ActionBarTitleProvider {
    public static final String TAG = "tag";

    private String tag;

    public static EventsListFragment create(String tag) {
        return Fragments.createFragmentWith1Arg(new EventsByTagFragment(), TAG, tag);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        tag = getArguments().getString(TAG);
    }

    @Override
    protected NavigationList<Event> getNavigationList(RequestManager requestManager, String filter) {
        return requestManager.getEventsByTag(tag);
    }

    @Override
    protected int getRootLayout() {
        return R.layout.list_fragment;
    }

    @Override
    public String getActionBarTitle() {
        return tag + " " + getString(R.string.events);
    }
}
