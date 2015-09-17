package com.khevents.ui;

import com.khevents.network.OnEventCreationFinished;
import com.utilsframework.android.WeakActivityReferenceHolder;

import java.io.IOException;

/**
 * Created by CM on 9/18/2015.
 */
class EventCreationFinishedCallback extends WeakActivityReferenceHolder<CreateEventActivity>
        implements OnEventCreationFinished {
    EventCreationFinishedCallback(CreateEventActivity activity) {
        super(activity);
    }

    @Override
    public void onComplete(int id, IOException error) {
        CreateEventActivity activity = getActivity();
        if (activity != null) {
            activity.onEventCreationFinished(error);
        }
    }
}
