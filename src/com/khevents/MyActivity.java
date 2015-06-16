package com.khevents;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import com.utilsframework.android.AndroidUtilities;

public class MyActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        AndroidUtilities.startActivity(this, VkUsersListActivity.class);
    }
}
