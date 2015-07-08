package com.khevents.ui;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.TextAppearanceSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.khevents.R;
import com.khevents.data.Comment;
import com.khevents.data.Event;

/**
 * Created by CM on 7/7/2015.
 */
public class UiUtils {
    public static View createTopCommentLayout(Context context, Comment comment) {
        TextView textView = new TextView(context);
        setCommentMessage(context, comment, textView);
        return textView;
    }

    public static void setCommentMessage(Context context, Comment comment, TextView textView) {
        SpannableString text = new SpannableString(comment.userName + " " + comment.text);

        text.setSpan(new TextAppearanceSpan(context, R.style.header2), 0, comment.userName.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setText(text, TextView.BufferType.SPANNABLE);
    }

    public static void setPeopleNumber(TextView peopleNumber, Event event) {
        peopleNumber.setText(event.subscribersCount + "/" + event.peopleNumber);
    }
}
