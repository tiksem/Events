package com.khevents;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import com.khevents.network.EventArgs;
import com.khevents.network.OnEventCreationFinished;
import com.khevents.network.RequestManager;
import com.khevents.vk.VkManager;
import com.utilsframework.android.threading.OnFinish;
import com.utilsframework.android.view.Alerts;
import com.utilsframework.android.view.DateTimePickerButton;
import com.utilsframework.android.view.NumberPickerButton;
import com.utilsframework.android.view.UiMessages;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKSdk;
import com.vkandroid.VkActivity;

import java.io.IOException;
import java.util.Arrays;

/**
 * Created by CM on 6/19/2015.
 */
public class CreateEventActivity extends VkActivity {
    private RequestManager requestManager;
    private NumberPickerButton peopleNumber;
    private TextView name;
    private TextView description;
    private DateTimePickerButton date;
    private TextView address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_event_activity);

        peopleNumber = (NumberPickerButton) findViewById(R.id.people_number);
        peopleNumber.setMinValue(2);
        peopleNumber.setMaxValue(Integer.MAX_VALUE);
        peopleNumber.setValue(2);
        peopleNumber.setPickerDialogTitle(getString(R.string.people_number_dialog_title));

        name = (TextView) findViewById(R.id.name);
        description = (TextView) findViewById(R.id.description);
        address = (TextView) findViewById(R.id.address);
        date = (DateTimePickerButton) findViewById(R.id.date_and_time);

        VKAccessToken accessToken = VKSdk.getAccessToken();
        requestManager = EventsApp.getInstance().getRequestManager();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.creat_event, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.create) {
            create();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private void create() {
        VKSdk.authorize();

        EventArgs args = new EventArgs();
        args.name = name.getText().toString();
        args.description = description.getText().toString();
        args.address = address.getText().toString();
        args.date = (int) (date.getDate() / 1000);
        args.peopleNumber = peopleNumber.getValue();
        args.tags = Arrays.asList("gavno", "eblo"); //TODO Integrate tags

        VkManager.getAccessToken(this, R.string.create_event_vk_login_error, new OnFinish<VKAccessToken>() {
            @Override
            public void onFinish(VKAccessToken token) {
                args.accessToken = token.accessToken;
                executeCreateEventRequest(args);
            }
        });
    }

    private void executeCreateEventRequest(EventArgs args) {
        requestManager.createEvent(args, new OnEventCreationFinished() {
            @Override
            public void onComplete(int id, IOException error) {
                if (error == null) {
                    UiMessages.message(name.getContext(), R.string.event_created);
                    finish();
                } else {
                    Alerts.showOkButtonAlert(name.getContext(), error.getMessage());
                }
            }
        });
    }
}
