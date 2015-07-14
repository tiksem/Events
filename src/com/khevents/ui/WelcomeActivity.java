package com.khevents.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import com.khevents.EventsApp;
import com.khevents.R;
import com.khevents.gcm.GCM;
import com.khevents.vk.VkManager;
import com.utilsframework.android.AndroidUtilities;
import com.utilsframework.android.threading.AsyncOperationCallback;
import com.utilsframework.android.threading.Threading;
import com.utilsframework.android.view.Alerts;
import com.utilsframework.android.view.UiMessages;
import com.vk.sdk.VKSdk;
import com.vkandroid.VkActivity;
import com.vkandroid.VkUser;

import java.io.IOException;

/**
 * Created by CM on 7/1/2015.
 */
public class WelcomeActivity extends VkActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome);

        initVk();

        findViewById(R.id.login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginVK();
            }
        });
    }

    private void initVk() {
        VkManager.initialize(this, R.string.vk_login_error,
                new VkManager.OnAccessTokenGot() {
                    @Override
                    public void onAccessToken() {
                        onAccessTokenGot();
                    }
                });
    }

    public void onAccessTokenGot() {
        ProgressDialog progressDialog = Alerts.showCircleProgressDialog(this, R.string.please_wait);
        Threading.executeAsyncTask(new Threading.Task<IOException, VkUser>() {
            @Override
            public VkUser runOnBackground() throws IOException {
                return getCurrentVkUser();
            }

            @Override
            public void onComplete(VkUser vkUser, IOException error) {
                if (vkUser != null) {
                    onVkUserReached(vkUser);
                } else {
                    UiMessages.error(WelcomeActivity.this, R.string.no_internet_connection);
                }
                progressDialog.dismiss();
            }
        }, IOException.class);
    }

    public void onVkUserReached(VkUser vkUser) {
        EventsApp.getInstance().initVkUser(vkUser);
        GCM.initServices(this);
        startMainActivity();
    }

    private VkUser getCurrentVkUser() throws IOException {
        return EventsApp.getInstance().getRequestManager().getVkUserById(
                Long.valueOf(VKSdk.getAccessToken().userId));
    }

    private void loginVK() {
        VkManager.authorize();
    }

    private void startMainActivity() {
        AndroidUtilities.startActivity(WelcomeActivity.this, MainActivity.class);
        finish();
    }
}
