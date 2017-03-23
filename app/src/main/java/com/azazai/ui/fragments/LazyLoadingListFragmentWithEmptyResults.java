package com.azazai.ui.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.azazai.R;
import com.azazai.ui.CreateEventActivity;

/**
 * Created by stykhonenko on 09.12.15.
 */
public abstract class LazyLoadingListFragmentWithEmptyResults<T> extends AbstractLazyLoadingListFragment<T> {
    private Button emptyEventsActionButton;
    private TextView emptyEventsHint;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        emptyEventsActionButton = (Button) view.findViewById(R.id.event_action_button);
        emptyEventsActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onEmptyListActionButtonClicked();
            }
        });

        emptyEventsHint = (TextView) view.findViewById(R.id.hint);

        super.onViewCreated(view, savedInstanceState);
        onUpdateEmptyViews();
    }

    protected final void createEvent(boolean isPrivate) {
        CreateEventActivity.createEvent(this, CREATE_EVENT, isPrivate);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CREATE_EVENT && resultCode == Activity.RESULT_OK) {
            update();
        }
    }

    protected abstract int getEmptyResultsHintText();
    protected void onEmptyListActionButtonClicked() {
        createEvent(false);
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
        emptyEventsHint.setText(getEmptyResultsHintText());
    }

    @Override
    protected int getRootLayout() {
        return R.layout.list_fragment_with_empty_results;
    }
}
