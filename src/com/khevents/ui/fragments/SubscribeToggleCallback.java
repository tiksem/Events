package com.khevents.ui.fragments;

import com.utilsframework.android.fragments.WeakFragmentReferenceHolder;
import com.utilsframework.android.threading.OnFinish;

import java.io.IOException;

/**
 * Created by CM on 9/18/2015.
 */
public class SubscribeToggleCallback extends WeakFragmentReferenceHolder<EventFragment>
        implements OnFinish<IOException> {
    public SubscribeToggleCallback(EventFragment fragment) {
        super(fragment);
    }

    @Override
    public void onFinish(IOException e) {
        if (getView() != null) {
            getFragment().onSubscribeToggleFinished(e);
        }
    }
}
