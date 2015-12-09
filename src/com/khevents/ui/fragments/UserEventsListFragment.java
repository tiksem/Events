package com.khevents.ui.fragments;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.khevents.R;

/**
 * Created by CM on 7/1/2015.
 */
public abstract class UserEventsListFragment extends EventsListFragment {
    @Override
    protected boolean hasSearchMenu() {
        return false;
    }
}
