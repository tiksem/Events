package com.khevents.ui;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import com.jsonutils.RequestException;
import com.khevents.EventsApp;
import com.khevents.R;
import com.khevents.network.EventArgs;
import com.khevents.network.OnEventCreationFinished;
import com.khevents.network.RequestManager;
import com.utils.framework.CollectionUtils;
import com.utils.framework.Objects;
import com.utilsframework.android.adapters.StringSuggestionsAdapter;
import com.utilsframework.android.resources.StringUtilities;
import com.utilsframework.android.view.*;
import com.utilsframework.android.view.flowlayout.FlowLayout;
import com.vk.sdk.VKSdk;
import com.vkandroid.VkActivity;

import java.io.IOException;
import java.util.List;

/**
 * Created by CM on 6/19/2015.
 */
public class CreateEventActivity extends VkActivity {
    public static final int MAX_PEOPLE_NUMBER = 9999;
    public static final int MIN_PEOPLE_NUMBER = 2;
    public static final int MIN_EVENT_NAME_LENGTH = 5;
    public static final int MIN_DESCRIPTION_LENGTH = 5;
    public static final int MIN_ADDRESS_LENGTH = 5;
    public static final String INVALID_DATE = "InvalidDate";
    private RequestManager requestManager;
    private NumberPickerButton peopleNumber;
    private TextView name;
    private TextView description;
    private DateTimePickerButton date;
    private TextView address;
    private ProgressDialog progressDialog;
    private FlowLayout tagsLayout;
    private EditTextWithSuggestions addTagEditText;
    private List<View> tagsLayoutChildren;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_event_activity);

        peopleNumber = (NumberPickerButton) findViewById(R.id.people_number);
        peopleNumber.setMinValue(MIN_PEOPLE_NUMBER);
        peopleNumber.setMaxValue(MAX_PEOPLE_NUMBER);
        peopleNumber.setValue(MIN_PEOPLE_NUMBER);
        peopleNumber.setPickerDialogTitle(getString(R.string.people_number_dialog_title));

        name = (TextView) findViewById(R.id.name);
        description = (TextView) findViewById(R.id.description);
        address = (TextView) findViewById(R.id.address);
        date = (DateTimePickerButton) findViewById(R.id.date_and_time);

        requestManager = EventsApp.getInstance().getRequestManager();

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setupTags();
    }

    private View createTag(String name) {
        View root = View.inflate(this, R.layout.tag, null);
        TextView nameView = (TextView) root.findViewById(R.id.tag);
        nameView.setText(name);
        root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GuiUtilities.removeView(v);
            }
        });
        return root;
    }

    private void addTag() {
        String name = addTagEditText.getText().toString();
        View tag = createTag(name);
        tagsLayout.addView(tag);
        addTagEditText.setText("");
        EditTextUtils.hideKeyboard(addTagEditText);
    }

    private void setupTags() {
        addTagEditText = (EditTextWithSuggestions) findViewById(R.id.add_tag_edit_text);
        StringSuggestionsAdapter suggestionsAdapter = new StringSuggestionsAdapter(this);
        suggestionsAdapter.setSuggestionsProvider(requestManager.getTagsSuggestionsProvider());
        addTagEditText.setAdapter(suggestionsAdapter);

        tagsLayout = (FlowLayout) findViewById(R.id.tags);
        tagsLayoutChildren = GuiUtilities.getChildrenAsListNonCopy(tagsLayout);

        View addTagButton = findViewById(R.id.add_tag_button);
        addTagButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTag();
            }
        });
        addTagButton.setEnabled(addTagEditText.length() > 0);

        addTagEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                addTagButton.setEnabled(s.length() > 0);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        addTagEditText.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                addTag();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.creat_event, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.create) {
            create();
            return true;
        } else if(itemId == android.R.id.home) {
            onBackPressed();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private List<String> getTags() {
        return CollectionUtils.transform(tagsLayoutChildren, new CollectionUtils.Transformer<View, String>() {
            @Override
            public String get(View view) {
                return ((TextView) view.findViewById(R.id.tag)).getText().toString();
            }
        });
    }

    private static class ValidateException extends Exception {}

    private void validateField(String field, int fieldNameId, int min) throws ValidateException {
        String fieldName = getString(fieldNameId);

        if (field.isEmpty()) {
            Alerts.showOkButtonAlert(this,
                    StringUtilities.getFormatString(this, R.string.empty_event_field, fieldName));
            throw new ValidateException();
        } else if(field.length() < min) {
            Alerts.showOkButtonAlert(this,
                    StringUtilities.getFormatString(this, R.string.small_event_field, fieldName, min));
            throw new ValidateException();
        }
    }

    private void create() {
        EventArgs args = new EventArgs();
        try {
            args.name = name.getText().toString();
            validateField(args.name, R.string.name, MIN_EVENT_NAME_LENGTH);

            args.description = description.getText().toString();
            validateField(args.description, R.string.description, MIN_DESCRIPTION_LENGTH);

            args.address = address.getText().toString();
            validateField(args.address, R.string.address, MIN_ADDRESS_LENGTH);
        } catch (ValidateException e) {
            return;
        }

        args.date = (int) (date.getDate() / 1000);
        args.peopleNumber = peopleNumber.getValue();
        args.tags = getTags();
        args.accessToken = VKSdk.getAccessToken().accessToken;

        executeCreateEventRequest(args);
    }

    private void executeCreateEventRequest(EventArgs args) {
        progressDialog = Alerts.showCircleProgressDialog(this, R.string.please_wait);

        requestManager.createEvent(args, new OnEventCreationFinished() {
            @Override
            public void onComplete(int id, IOException error) {
                if (error == null) {
                    UiMessages.message(name.getContext(), R.string.event_created);
                    setResult(RESULT_OK);
                    finish();
                } else {
                    if (error instanceof RequestException) {
                        RequestException requestException = (RequestException) error;
                        if (Objects.equals(requestException.getExceptionInfo().getError(), INVALID_DATE)) {
                            Alerts.showOkButtonAlert(name.getContext(), R.string.invalid_date);
                        } else {
                            Alerts.showOkButtonAlert(name.getContext(), error.getMessage());
                        }
                    } else {
                        Alerts.showOkButtonAlert(name.getContext(), error.getMessage());
                    }
                }
                progressDialog.dismiss();
            }
        });
    }
}
