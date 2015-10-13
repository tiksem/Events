package com.khevents.ui;

import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.khevents.EventsApp;
import com.khevents.R;
import com.khevents.gcm.GCM;
import com.khevents.network.RequestManager;
import com.khevents.vk.VkInitManager;
import com.khevents.vk.VkManager;
import com.utilsframework.android.AndroidUtilities;
import com.utilsframework.android.threading.Threading;
import com.utilsframework.android.view.Alerts;
import com.utilsframework.android.view.Toasts;
import com.vk.sdk.VKSdk;
import com.vkandroid.VkActivity;
import com.vkandroid.VkUser;

import java.io.IOException;

/**
 * Created by CM on 7/1/2015.
 */
public class WelcomeActivity extends VkActivity {
    private VkInitManager vkInitManager;
    private RequestManager requestManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome);

        EventsApp eventsApp = EventsApp.getInstance();

        requestManager = eventsApp.createRequestManager();

        vkInitManager = new VkInitManager(this, requestManager) {
            @Override
            protected void onVkUserReached(VkUser vkUser) {
                super.onVkUserReached(vkUser);
                startMainActivity();
            }
        };

        if (eventsApp.getCurrentUser() == null) {
            vkInitManager.execute(true);
        } else {
            startMainActivity();
        }

        findViewById(R.id.login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginVK();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        requestManager.cancelAll();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        getIntent().putExtras(intent);
    }

    private void loginVK() {
        VkManager.authorize();
    }

    private void startMainActivity() {
        AndroidUtilities.startActivityWithExtras(this, MainActivity.class, getIntent());
        finish();
    }
}
