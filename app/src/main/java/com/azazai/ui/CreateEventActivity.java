package com.azazai.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import com.jsonutils.RequestException;
import com.azazai.EventsApp;
import com.azazai.R;
import com.azazai.data.Event;
import com.azazai.network.EventArgs;
import com.azazai.network.IgnoreTagsProvider;
import com.azazai.network.OnEventCreationFinished;
import com.azazai.network.RequestManager;
import com.utils.framework.CollectionUtils;
import com.utils.framework.Objects;
import com.utils.framework.Transformer;
import com.utils.framework.strings.Strings;
import com.utilsframework.android.adapters.StringSuggestionsAdapter;
import com.utilsframework.android.resources.StringUtilities;
import com.utilsframework.android.threading.OnFinish;
import com.utilsframework.android.view.*;
import com.utilsframework.android.view.flowlayout.FlowLayout;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.*;
import com.vkandroid.VkActivity;

import java.io.IOException;
import java.util.Arrays;
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
    public static final String EDIT_EVENT = "EDIT_EVENT";

    private RequestManager requestManager;
    private NumberPickerButton peopleNumber;
    private TextView name;
    private TextView description;
    private DateTimePickerButton date;
    private EditTextWithSuggestions address;
    private ProgressDialog progressDialog;
    private FlowLayout tagsLayout;
    private EditTextWithSuggestions addTagEditText;
    private List<View> tagsLayoutChildren;
    private Event editEvent;

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
        date = (DateTimePickerButton) findViewById(R.id.date_and_time);

        requestManager = EventsApp.getInstance().createRequestManager();

        setupEditEvent();
        setupToolBar();
        setupTags();
        setupAddressEditText();
    }

    private void setupEditEvent() {
        editEvent = getIntent().getParcelableExtra(EDIT_EVENT);
        if (editEvent != null) {
            peopleNumber.setValue(editEvent.peopleNumber);
            name.setText(editEvent.name);
            description.setText(editEvent.description);
            date.setDate(editEvent.date * 1000l);
        }
    }

    private void setupToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.rgb(255, 255, 255));
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        if (editEvent == null) {
            actionBar.setTitle(R.string.event_creation_title);
        } else {
            String title = StringUtilities.getFormatString(this, R.string.event_edit_title, editEvent.name);
            actionBar.setTitle(title);
        }
    }

    private void setupAddressEditText() {
        address = (EditTextWithSuggestions) findViewById(R.id.address);
        if (editEvent != null) {
            address.setText(editEvent.address);
        }
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
        if (name.isEmpty()) {
            Toasts.error(this, R.string.enter_tag_name);
            return;
        }

        if (getTags().contains(name)) {
            Toasts.error(this, R.string.tag_already_added, name);
            return;
        }

        View tag = createTag(name);
        tagsLayout.addView(tag);
        addTagEditText.setText("");
        EditTextUtils.hideKeyboard(addTagEditText);
    }

    private void setupTags() {
        addTagEditText = (EditTextWithSuggestions) findViewById(R.id.add_tag_edit_text);
        StringSuggestionsAdapter suggestionsAdapter = new StringSuggestionsAdapter(this);
        suggestionsAdapter.setSuggestionsProvider(requestManager.getTagsSuggestionsProvider(new IgnoreTagsProvider() {
            @Override
            public List<String> getIgnoreTags() {
                return getTags();
            }
        }));
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

    private boolean shouldShowBackConfirm() {
        if (editEvent == null) {
            return Strings.containsAnyNotEmpty(name.getText(), address.getText(), description.getText());
        } else {
            return editChangesMade();
        }
    }

    private boolean editChangesMade() {
        List<CharSequence> current = Arrays.asList(name.getText(), address.getText(), description.getText());
        List<String> last = Arrays.asList(editEvent.name, editEvent.address, editEvent.description);
        return !Strings.stringListsEquals(current, last);
    }

    @Override
    public void onBackPressed() {
        if (shouldShowBackConfirm()) {
            Alerts.showYesNoAlert(this, new OnYes() {
                @Override
                public void onYes() {
                    CreateEventActivity.super.onBackPressed();
                }
            }, R.string.create_event_back_confirm);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.apply) {
            createOrEdit();
            return true;
        } else if(itemId == android.R.id.home) {
            onBackPressed();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private List<String> getTags() {
        return CollectionUtils.transform(tagsLayoutChildren, new Transformer<View, String>() {
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

    private void createOrEdit() {
        if (editEvent != null && !editChangesMade()) {
            finish();
        }

        EventArgs args;
        try {
            args = getEventArgs();
        } catch (ValidateException e) {
            return;
        }

        executeCreateEditEventRequest(args);
    }

    private EventArgs getEventArgs() throws ValidateException {
        EventArgs args = new EventArgs();
        args.name = name.getText().toString();
        validateField(args.name, R.string.name, MIN_EVENT_NAME_LENGTH);

        args.description = description.getText().toString();
        validateField(args.description, R.string.description, MIN_DESCRIPTION_LENGTH);

        args.address = address.getText().toString();
        validateField(args.address, R.string.address, MIN_ADDRESS_LENGTH);

        args.date = (int) (date.getDate() / 1000);
        args.peopleNumber = peopleNumber.getValue();
        args.tags = getTags();

        if (editEvent == null) {
            args.accessToken = VKSdk.getAccessToken().accessToken;
        } else {
            args.id = editEvent.id;
        }

        return args;
    }

    private void onEventCreated() {
        Toasts.message(name.getContext(), R.string.event_created);
        setResult(RESULT_OK);
        finish();
        //postToVK();
    }

    private void onEventEdited() {
        setResult(RESULT_OK);
        finish();
    }

    private void postToVK() {
        VKParameters parameters = new VKParameters();
        parameters.put("attachments", "https://www.youtube.com/watch?v=lJTzzVsUNAU");
        VKRequest request = VKApi.wall().post(parameters);
        request.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                super.onComplete(response);
            }

            @Override
            public void onError(VKError error) {
                super.onError(error);
            }
        });
    }

    void onEventCreateEditFinished(IOException error) {
        if (error == null) {
            if (editEvent == null) {
                onEventCreated();
            } else {
                onEventEdited();
            }
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

    private void executeCreateEditEventRequest(EventArgs args) {
        progressDialog = Alerts.showCircleProgressDialog(this, R.string.please_wait);

        if (editEvent == null) {
            requestManager.createEvent(args, new OnEventCreationFinished() {
                @Override
                public void onComplete(int id, IOException error) {
                    onEventCreateEditFinished(error);
                }
            });
        } else {
            requestManager.editEvent(args, new OnFinish<IOException>() {
                @Override
                public void onFinish(IOException e) {
                    onEventCreateEditFinished(e);
                }
            });
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        requestManager.cancelAll();
    }

    public static void edit(Fragment fragment, int requestCode, Event event) {
        Intent intent = new Intent(fragment.getActivity(), CreateEventActivity.class);
        intent.putExtra(EDIT_EVENT, event);
        fragment.startActivityForResult(intent, requestCode);
    }
}
