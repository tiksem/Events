package com.khevents.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import com.khevents.CreateEventActivity;
import com.khevents.R;
import com.khevents.adapters.EventsAdapter;
import com.khevents.data.Event;
import com.khevents.network.RequestManager;
import com.utils.framework.collections.NavigationList;
import com.utilsframework.android.AndroidUtilities;
import com.utilsframework.android.adapters.ViewArrayAdapter;
import com.utilsframework.android.fab.FloatingActionButton;
import com.utilsframework.android.view.DatePickerButton;

/**
 * Created by CM on 6/20/2015.
 */
public class AllEventsListFragment extends EventsListFragment {
    public static final int CREATE_EVENT = 1;
    private CheckBox dateFilterCheckbox;
    private DatePickerButton datePickerButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        dateFilterCheckbox = (CheckBox) view.findViewById(R.id.date_filter_checkbox);
        datePickerButton = (DatePickerButton) view.findViewById(R.id.filter_date);
        dateFilterCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (datePickerButton.getVisibility() == View.INVISIBLE) {
                    datePickerButton.setVisibility(View.VISIBLE);
                    datePickerButton.performClick();
                }
            }
        });

        datePickerButton.setOnDateChangedListener(new DatePickerButton.OnDateChangedListener() {
            @Override
            public void onDateChanged() {
                updateNavigationList(null);
            }
        });

        FloatingActionButton floatingActionButton = (FloatingActionButton) view.findViewById(R.id.fab);
        floatingActionButton.attachToListView(listView);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AndroidUtilities.startActivityForResult(AllEventsListFragment.this,
                        CreateEventActivity.class, CREATE_EVENT);
            }
        });
    }

    @Override
    protected NavigationList<Event> getNavigationList(RequestManager requestManager, String filter) {
        if (!dateFilterCheckbox.isChecked()) {
            return requestManager.getEvents();
        } else {
            long date = datePickerButton.getDate();
            return requestManager.getEvents(date);
        }
    }

    @Override
    protected int getRootLayout() {
        return R.layout.events_list_fragment;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CREATE_EVENT && resultCode == Activity.RESULT_OK) {
            updateNavigationList(null);
        }
    }
}
