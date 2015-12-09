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

    @Override
    protected int getEmptyListResourceId() {
        return R.id.no_events;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button eventActionButton = (Button) view.findViewById(R.id.event_action_button);
        eventActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onEventActionButtonClicked();
            }
        });
        eventActionButton.setText(getEventActionText());

        TextView hint = (TextView) view.findViewById(R.id.hint);
        hint.setText(getHintText());
    }

    protected abstract int getHintText();
    protected abstract void onEventActionButtonClicked();
    protected abstract int getEventActionText();

    @Override
    protected int getRootLayout() {
        return R.layout.user_events_list_fragment;
    }
}
