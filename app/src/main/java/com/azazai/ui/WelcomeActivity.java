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
    private Queue<Runnable> onContentStorageGruntedQueue = new ArrayDeque<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome);

        final EventsApp eventsApp = EventsApp.getInstance();

        vkInitManager = new VkInitManager(this, requestManager) {
            @Override
            protected void onVkUserReached(VkUser vkUser) {
                super.onVkUserReached(vkUser);
                startMainActivity();
            }
        };

        requestReadStoragePermissionExecuteWhenGranted(new Runnable() {
            @Override
            public void run() {
                if (eventsApp.getCurrentUser() == null) {
                    vkInitManager.execute(true);
                } else {
                    startMainActivity();
                }
            }
        });

        findViewById(R.id.login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginVK();
            }
        });
    }

    private boolean requestReadStoragePermissionExecuteWhenGranted(final Runnable runnable) {
        Runnable whenGrunted = new Runnable() {
            @Override
            public void run() {
                EventsApp eventsApp = EventsApp.getInstance();
                eventsApp.initIp();
                requestManager = eventsApp.createRequestManager();
                runnable.run();
            }
        };

        if (!PermissionUtils.shouldRequestReadStoragePermission(this)) {
            whenGrunted.run();
            return true;
        } else {
            PermissionUtils.requestReadStoragePermission(this, R.string.read_storage_permission);
            onContentStorageGruntedQueue.add(whenGrunted);
            return false;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == PermissionUtils.READ_STORAGE_PERMISSION_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Tasks.executeAndClearQueue(onContentStorageGruntedQueue);
            }
        }
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
        requestReadStoragePermissionExecuteWhenGranted(new Runnable() {
            @Override
            public void run() {
                VkManager.authorize();
            }
        });
    }

    private void startMainActivity() {
        AndroidUtilities.startActivityWithExtras(this, MainActivity.class, getIntent());
        finish();
    }
}
