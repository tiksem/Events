package com.azazai.adapters;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.azazai.R;

public class IconHolder {
    public ImageView icon;
    public TextView tag;

    public IconHolder(View view) {
        icon = (ImageView) view.findViewById(R.id.icon);
        tag = (TextView) view.findViewById(R.id.tag);
    }
}
