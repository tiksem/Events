package com.azazai.ui;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.TextAppearanceSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.azazai.R;
import com.azazai.data.Comment;
import com.azazai.data.Event;
import com.azazai.data.Icon;
import com.squareup.picasso.Picasso;

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
        String commentText = comment.text.replaceAll("\\n+", " ");
        SpannableString text = new SpannableString(comment.userName + " " + commentText);

        text.setSpan(new TextAppearanceSpan(context, R.style.header3), 0, comment.userName.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setText(text, TextView.BufferType.SPANNABLE);
    }

    public static void setPeopleNumber(TextView peopleNumber, Event event) {
        peopleNumber.setText(event.subscribersCount + "/" + event.peopleNumber);
    }

    public static void loadEventIcon(Picasso picasso, ImageView view, Icon icon) {
        picasso.load(icon.getUrl()).resize(view.getMeasuredWidth(),
                view.getMeasuredHeight()).
                transform(new CircleTransform()).
                centerCrop().into(view);
    }
}
