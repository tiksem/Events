package com.khevents.ui.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import com.khevents.Level;
import com.khevents.R;
import com.khevents.adapters.EventsAdapter;
import com.khevents.data.Event;
import com.khevents.network.RequestManager;
import com.khevents.ui.CreateEventActivity;
import com.utilsframework.android.AndroidUtilities;
import com.utilsframework.android.adapters.ViewArrayAdapter;
import com.utilsframework.android.fab.FloatingActionButton;
import com.utilsframework.android.view.KeyboardIsShownListener;

/**
 * Created by CM on 7/1/2015.
 */
public abstract class EventsListFragment extends AbstractNavigationListFragment<Event> {
    public static final int CREATE_EVENT = 1;

    private KeyboardIsShownListener keyboardIsShownListener;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (useCreateEventButton()) {
            final FloatingActionButton floatingActionButton = (FloatingActionButton) view.findViewById(R.id.fab);
            floatingActionButton.attachToListView(getListView());
            floatingActionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    createEvent();
                }
            });

            keyboardIsShownListener = new KeyboardIsShownListener(getActivity()) {
                @Override
                protected void onKeyboardStateChanged(boolean isShown) {
                    floatingActionButton.setVisibility(isShown ? View.INVISIBLE : View.VISIBLE);
                }
            };
        }
    }

    protected final void createEvent() {
        AndroidUtilities.startActivityForResult(EventsListFragment.this,
                CreateEventActivity.class, CREATE_EVENT);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (keyboardIsShownListener != null) {
            keyboardIsShownListener.destroy();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CREATE_EVENT && resultCode == Activity.RESULT_OK) {
            updateNavigationListWithLastFilter();
        }
    }

    @Override
    protected ViewArrayAdapter<Event, ?> createAdapter(RequestManager requestManager) {
        return new EventsAdapter(getActivity());
    }

    @Override
    protected void onListItemClicked(Event event, int position) {
        Fragment fragment = EventFragment.create(event);
        replaceFragment(fragment, Level.EVENT_PAGE);
    }

    @Override
    protected boolean useUpdateBroadcastReceiver() {
        return true;
    }

    protected boolean useCreateEventButton() {
        return false;
    }
}
