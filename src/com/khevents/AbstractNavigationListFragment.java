package com.khevents;

import com.khevents.network.RequestManager;
import com.utilsframework.android.navigation.NavigationListFragment;

/**
 * Created by CM on 6/21/2015.
 */
public abstract class AbstractNavigationListFragment<T> extends NavigationListFragment<T, RequestManager> {
    @Override
    protected RequestManager obtainRequestManager() {
        return EventsApp.getInstance().getRequestManager();
    }

    @Override
    protected int getListResourceId() {
        return R.id.list;
    }

    @Override
    protected int getLoadingResourceId() {
        return R.id.loading;
    }

    @Override
    protected int getNoInternetConnectionViewId() {
        return R.id.no_connection;
    }
}
