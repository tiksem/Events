package com.khevents.ui.fragments;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import com.khevents.R;
import com.khevents.data.Event;
import com.khevents.network.RequestManager;
import com.utils.framework.collections.NavigationList;
import com.utilsframework.android.time.TimeUtils;
import com.utilsframework.android.view.DatePickerButton;

/**
 * Created by CM on 6/20/2015.
 */
public class AllEventsListFragment extends EventsListFragment {
    private CheckBox dateFilterCheckbox;
    private DatePickerButton datePickerButton;
    private boolean datePickerButtonCheckedListenerCalled = false;
    private View emptyEventActionButton;
    private long dateFilter;
    private TextView emptyEventsHint;
    private TextView emptyEventsDate;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        setupDateFilterViews(view);
        super.onViewCreated(view, savedInstanceState);

        emptyEventActionButton = view.findViewById(R.id.event_action_button);
        emptyEventActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createEvent();
            }
        });

        emptyEventsHint = (TextView) view.findViewById(R.id.hint);
        emptyEventsDate = (TextView) view.findViewById(R.id.date);
    }

    protected void setupDateFilterViews(View view) {
        dateFilterCheckbox = (CheckBox) view.findViewById(R.id.date_filter_checkbox);
        datePickerButton = (DatePickerButton) view.findViewById(R.id.filter_date);
        dateFilterCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!datePickerButtonCheckedListenerCalled) {
                    datePickerButton.performClick();
                    datePickerButtonCheckedListenerCalled = true;
                } else if (datePickerButton.getVisibility() == View.VISIBLE) {
                    updateNavigationListWithLastFilter();
                }

                datePickerButton.setVisibility(View.VISIBLE);
            }
        });

        datePickerButton.setOnDateChangedListener(new DatePickerButton.OnDateChangedListener() {
            @Override
            public void onDateChanged() {
                if (dateFilterCheckbox.isChecked()) {
                    updateNavigationListWithLastFilter();
                }
            }
        });
    }

    @Override
    protected NavigationList<Event> getNavigationList(RequestManager requestManager, String filter) {
        if (!dateFilterCheckbox.isChecked()) {
            dateFilter = -1;
            return requestManager.getEvents(filter);
        } else {
            dateFilter = datePickerButton.getDate();
            return requestManager.getEvents(dateFilter, filter);
        }
    }

    @Override
    protected int getRootLayout() {
        return R.layout.events_list_fragment;
    }

    @Override
    protected boolean hasSearchMenu() {
        return true;
    }

    @Override
    protected boolean useCreateEventButton() {
        return true;
    }

    @Override
    protected int getEmptyListResourceId() {
        return R.id.no_events;
    }

    @Override
    protected void onEmptyViewIsShown() {
        boolean isActionButtonVisible = getLastFilter() == null && dateFilter < 0;
        emptyEventActionButton.setVisibility(isActionButtonVisible ? View.VISIBLE : View.GONE);
        if (dateFilter > 0) {
            emptyEventsDate.setVisibility(View.VISIBLE);
            String date = TimeUtils.getAlternativeDisplayDate(getActivity(), dateFilter);
            emptyEventsDate.setText(date);
            emptyEventsHint.setText(R.string.no_events_on);
        } else {
            emptyEventsHint.setText(R.string.no_events_found);
            emptyEventsDate.setVisibility(View.GONE);
        }
    }
}
