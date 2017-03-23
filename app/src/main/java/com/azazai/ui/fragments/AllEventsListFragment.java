package com.azazai.ui.fragments;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import com.azazai.R;
import com.azazai.data.Event;
import com.azazai.network.RequestManager;
import com.utils.framework.collections.LazyLoadingList;
import com.utilsframework.android.time.TimeUtils;
import com.utilsframework.android.view.DatePickerButton;

/**
 * Created by CM on 6/20/2015.
 */
public class AllEventsListFragment extends EventsListFragment {
    private CheckBox dateFilterCheckbox;
    private DatePickerButton datePickerButton;
    private boolean datePickerButtonCheckedListenerCalled = false;
    private long dateFilter;
    private TextView emptyEventsDate;

    public AllEventsListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        setupDateFilterViews(view);
        super.onViewCreated(view, savedInstanceState);
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
                    update();
                }

                datePickerButton.setVisibility(View.VISIBLE);
            }
        });

        datePickerButton.setOnDateChangedListener(new DatePickerButton.OnDateChangedListener() {
            @Override
            public void onDateChanged() {
                if (dateFilterCheckbox.isChecked()) {
                    update();
                }
            }
        });

        emptyEventsDate = (TextView) view.findViewById(R.id.date);
    }

    @Override
    protected LazyLoadingList<Event> getLazyLoadingList(String filter) {
        if (!dateFilterCheckbox.isChecked()) {
            dateFilter = -1;
            return getRequestManager().getEvents(filter);
        } else {
            dateFilter = datePickerButton.getDate();
            return getRequestManager().getEvents(dateFilter, filter);
        }
    }

    @Override
    protected int getRootLayout() {
        return R.layout.all_events_list_fragment;
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
    protected void onUpdateEmptyViews() {
        super.onUpdateEmptyViews();

        if (dateFilter > 0) {
            emptyEventsDate.setVisibility(View.VISIBLE);
            String date = TimeUtils.getAlternativeDisplayDate(getActivity(), dateFilter);
            emptyEventsDate.setText(date);
        } else {
            emptyEventsDate.setVisibility(View.GONE);
        }
    }

    @Override
    protected int getEmptyResultsHintText() {
        return dateFilter > 0 ? R.string.no_events_on : R.string.no_events_found;
    }

    @Override
    protected int getEmptyEventsActionText() {
        boolean isActionButtonVisible = getLastFilter() == null && dateFilter < 0;
        return isActionButtonVisible ? R.string.create_first_event : 0;
    }
}
