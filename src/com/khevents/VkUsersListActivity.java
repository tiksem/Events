package com.khevents;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import com.utils.framework.ArrayUtils;
import com.utilsframework.android.threading.OnFinish;
import com.utilsframework.android.threading.Threading;
import com.vkandroid.VkApiUtils;
import com.vkandroid.VkUser;
import com.vkandroid.VkUsersListAdapter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by CM on 6/17/2015.
 */
public class VkUsersListActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ListView listView = new ListView(this);
        VkUsersListAdapter adapter = new VkUsersListAdapter(this);

        Threading.getResultAsync(new Threading.ResultProvider<List<VkUser>>() {
            @Override
            public List<VkUser> get() {
                try {
                    return VkApiUtils.getUsers(1, 2, 3, 4, 6, 7, 8, 121);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }, new OnFinish<List<VkUser>>() {
            @Override
            public void onFinish(List<VkUser> vkUsers) {
                adapter.setElements(vkUsers);
                listView.setAdapter(adapter);
                setContentView(listView);
            }
        });
    }
}
