package com.khevents;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import com.khevents.adapters.EventsAdapter;
import com.khevents.data.Event;
import com.khevents.network.RequestManager;
import com.utils.framework.collections.NavigationList;
import com.utilsframework.android.AndroidUtilities;
import com.utilsframework.android.adapters.ViewArrayAdapter;
import com.utilsframework.android.fab.FloatingActionButton;
import com.utilsframework.android.navigation.NavigationListFragment;
import com.utilsframework.android.view.DatePickerButton;

/**
 * Created by CM on 6/20/2015.
 */
public class EventsListFragment extends AbstractNavigationListFragment<Event> {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        CheckBox dateFilterCheckbox = (CheckBox) view.findViewById(R.id.date_filter_checkbox);
        DatePickerButton datePickerButton = (DatePickerButton) view.findViewById(R.id.filter_date);
        dateFilterCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (datePickerButton.getVisibility() == View.INVISIBLE) {
                    datePickerButton.setVisibility(View.VISIBLE);
                    datePickerButton.performClick();
                }
            }
        });

        FloatingActionButton floatingActionButton = (FloatingActionButton) view.findViewById(R.id.fab);
        floatingActionButton.attachToListView(listView);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AndroidUtilities.startActivity(v.getContext(), CreateEventActivity.class);
            }
        });
    }

    @Override
    protected ViewArrayAdapter<Event, ?> createAdapter(RequestManager requestManager) {
        return new EventsAdapter(getActivity());
    }

    @Override
    protected NavigationList<Event> getNavigationList(RequestManager requestManager, String filter) {
        return requestManager.getEvents();
    }

    @Override
    protected void onListItemClicked(Event item) {

    }

    @Override
    protected int getRootLayout() {
        return R.layout.events_list_fragment;
    }
}
