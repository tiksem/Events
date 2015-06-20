package com.khevents;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import com.utilsframework.android.view.DatePickerButton;

/**
 * Created by CM on 6/20/2015.
 */
public class EventsListActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.events_list_activity);

        CheckBox dateFilterCheckbox = (CheckBox) findViewById(R.id.date_filter_checkbox);
        DatePickerButton datePickerButton = (DatePickerButton) findViewById(R.id.filter_date);
        dateFilterCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (datePickerButton.getVisibility() == View.INVISIBLE) {
                    datePickerButton.setVisibility(View.VISIBLE);
                    datePickerButton.performClick();
                }
            }
        });
    }
}
