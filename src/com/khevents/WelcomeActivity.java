package com.khevents;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import com.khevents.vk.VkManager;
import com.utilsframework.android.AndroidUtilities;
import com.utilsframework.android.threading.OnFinish;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKSdk;

/**
 * Created by CM on 7/1/2015.
 */
public class WelcomeActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome);

        VkManager.initialize(this, R.string.vk_login_error, new VkManager.OnAccessTokenGot() {
            @Override
            public void onAccessToken() {
                startMainActivity();
            }
        });

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
