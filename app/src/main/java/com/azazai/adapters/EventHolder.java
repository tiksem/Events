package com.azazai.adapters;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.azazai.R;

/**
 * Created by CM on 6/21/2015.
 */
public class EventHolder {
    public TextView name;
    public TextView peopleCount;
    public TextView date;
    public TextView description;

    public EventHolder(View view) {
        name = (TextView) view.findViewById(R.id.name);
        date = (TextView) view.findViewById(R.id.date);
        description = (TextView) view.findViewById(R.id.description);
        peopleCount = (TextView) view.findViewById(R.id.people_number);
    }
}
