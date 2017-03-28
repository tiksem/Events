package com.azazai.ui.fragments;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;

import com.azazai.R;
import com.azazai.adapters.IconsAdapter;
import com.azazai.data.Icon;
import com.utils.framework.collections.LazyLoadingList;
import com.utilsframework.android.adapters.ViewArrayAdapter;
import com.utilsframework.android.navdrawer.ActionBarTitleProvider;
import com.utilsframework.android.system.Intents;

/**
 * Created by stikhonenko on 3/28/17.
 */
public class IconsFragment extends AbstractLazyLoadingListFragment<Icon> implements ActionBarTitleProvider {

    public static final String ICON = "icon";

    @Override
    protected ViewArrayAdapter<Icon, ?> createAdapter() {
        return new IconsAdapter(getContext());
    }

    @Override
    protected LazyLoadingList<Icon> getLazyLoadingList(String filter) {
        return getRequestManager().getIcons();
    }

    @Override
    protected void onListItemClicked(Icon item, int position) {
        Intent intent = Intents.intentWithParcelableExtra(ICON, item);
        FragmentActivity activity = getActivity();
        activity.setResult(Activity.RESULT_OK, intent);
        activity.finish();
    }

    @Override
    protected int getRootLayout() {
        return R.layout.icons_list;
    }

    @Override
    public String getActionBarTitle() {
        return getString(R.string.icons_fragment_title);
    }
}
