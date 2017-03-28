package com.azazai.ui;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import com.azazai.EventsApp;
import com.azazai.R;
import com.azazai.network.RequestManager;
import com.azazai.vk.VkInitManager;
import com.azazai.vk.VkManager;
import com.utilsframework.android.AndroidUtilities;
import com.utilsframework.android.system.PermissionUtils;
import com.utilsframework.android.threading.Tasks;
import com.vkandroid.VkActivity;
import com.vkandroid.VkUser;

import java.util.ArrayDeque;
import java.util.Queue;

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

        initRequestAndVkManager();
        EventsApp eventsApp = EventsApp.getInstance();
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

    private void initRequestAndVkManager() {
        EventsApp eventsApp = EventsApp.getInstance();

        requestManager = eventsApp.createRequestManager();

        vkInitManager = new VkInitManager(WelcomeActivity.this, requestManager) {
            @Override
            protected void onVkUserReached(VkUser vkUser) {
                super.onVkUserReached(vkUser);
                startMainActivity();
            }
        };
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (requestManager != null) {
            requestManager.cancelAll();
        }
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
