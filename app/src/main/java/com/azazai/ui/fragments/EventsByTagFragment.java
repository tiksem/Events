package com.azazai.ui.fragments;

import android.app.Activity;
import com.azazai.R;
import com.azazai.data.Event;
import com.azazai.network.RequestManager;
import com.utils.framework.collections.LazyLoadingList;
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
    protected LazyLoadingList<Object> getLazyLoadingList(String filter) {
        return getRequestManager().getEventsByTag(tag);
    }

    @Override
    public String getActionBarTitle() {
        return tag + " " + getString(R.string.events);
    }

    @Override
    protected int getEmptyResultsHintText() {
        return R.string.no_events_found;
    }

    @Override
    protected int getEmptyEventsActionText() {
        return 0;
    }

    @Override
    protected boolean useCreateEventButton() {
        return false;
    }
}
