package com.khevents.ui.fragments;

import com.utilsframework.android.fragments.WeakFragmentReferenceHolder;
import com.utilsframework.android.threading.OnFinish;

import java.io.IOException;

/**
 * Created by CM on 9/18/2015.
 */
public class EventCancelCallback extends WeakFragmentReferenceHolder<EventFragment> implements OnFinish<IOException> {
    public EventCancelCallback(EventFragment fragment) {
        super(fragment);
    }

    @Override
    public void onFinish(IOException e) {
        EventFragment fragment = getFragment();
        if (fragment != null) {
            fragment.onEventCanceled(e);
        }
    }
}
