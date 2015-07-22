package com.khevents.ui;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import com.khevents.EventsApp;
import com.khevents.R;
import com.khevents.gcm.GCM;
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
    private VkInitManager vkInitManager = new VkInitManager(this) {
        @Override
        protected void onVkUserReached(VkUser vkUser) {
            super.onVkUserReached(vkUser);
            startMainActivity();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome);

        vkInitManager.execute(true);

        findViewById(R.id.login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginVK();
            }
        });
    }

    private void loginVK() {
        VkManager.authorize();
    }

    private void startMainActivity() {
        AndroidUtilities.startActivity(WelcomeActivity.this, MainActivity.class);
        finish();
    }
}
