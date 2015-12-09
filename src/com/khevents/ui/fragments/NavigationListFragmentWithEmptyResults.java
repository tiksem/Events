package com.khevents.ui.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.khevents.R;
import com.khevents.ui.CreateEventActivity;
import com.utilsframework.android.AndroidUtilities;

/**
 * Created by stykhonenko on 09.12.15.
 */
public abstract class NavigationListFragmentWithEmptyResults<T> extends AbstractNavigationListFragment<T> {
    private Button emptyEventsActionButton;
    private TextView emptyEventsHint;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        emptyEventsActionButton = (Button) view.findViewById(R.id.event_action_button);
        emptyEventsActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onEmptyEventsActionButtonClicked();
            }
        });

        emptyEventsHint = (TextView) view.findViewById(R.id.hint);

        super.onViewCreated(view, savedInstanceState);
        onUpdateEmptyViews();
    }

    protected final void createEvent() {
        AndroidUtilities.startActivityForResult(this, CreateEventActivity.class, CREATE_EVENT);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CREATE_EVENT && resultCode == Activity.RESULT_OK) {
            updateNavigationListWithLastFilter();
        }
    }

    protected abstract int getHintText();
    protected void onEmptyEventsActionButtonClicked() {
        createEvent();
    }
    protected abstract int getEmptyEventsActionText();

    @Override
    protected int getEmptyListResourceId() {
        return R.id.no_events;
    }

    @Override
    protected final void onEmptyViewIsShown() {
        onUpdateEmptyViews();
    }

    protected void onUpdateEmptyViews() {
        int eventsActionText = getEmptyEventsActionText();
        if (eventsActionText != 0) {
            emptyEventsActionButton.setText(eventsActionText);
            emptyEventsActionButton.setVisibility(View.VISIBLE);
        } else {
            emptyEventsActionButton.setVisibility(View.GONE);
        }
        emptyEventsHint.setText(getHintText());
    }

    @Override
    protected int getRootLayout() {
        return R.layout.list_fragment_with_empty_results;
    }
}
