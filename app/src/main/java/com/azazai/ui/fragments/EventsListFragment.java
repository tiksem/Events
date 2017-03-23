package com.azazai.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import com.azazai.Level;
import com.azazai.R;
import com.azazai.adapters.EventsAdapter;
import com.azazai.data.Event;
import com.azazai.network.RequestManager;
import com.utilsframework.android.adapters.ViewArrayAdapter;
import com.utilsframework.android.fab.FloatingActionButton;
import com.utilsframework.android.view.KeyboardIsShownListener;

/**
 * Created by CM on 7/1/2015.
 */
public abstract class EventsListFragment extends LazyLoadingListFragmentWithEmptyResults<Event> {
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
                    createEvent(false);
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (keyboardIsShownListener != null) {
            keyboardIsShownListener.destroy();
        }
    }

    @Override
    protected ViewArrayAdapter<Event, ?> createAdapter() {
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
