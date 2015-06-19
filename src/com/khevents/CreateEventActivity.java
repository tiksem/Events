package com.khevents;

import android.app.Activity;
import android.os.Bundle;
import android.widget.NumberPicker;
import com.utilsframework.android.view.NumberPickerButton;

/**
 * Created by CM on 6/19/2015.
 */
public class CreateEventActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_event_activity);

        NumberPickerButton peopleNumber = (NumberPickerButton) findViewById(R.id.people_number);
        peopleNumber.setMinValue(2);
        peopleNumber.setMaxValue(1000);
        peopleNumber.setValue(2);
        peopleNumber.setPickerDialogTitle(getString(R.string.people_number_dialog_title));
    }
}
