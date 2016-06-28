package com.azazai.adapters;

import android.content.Context;
import android.text.Spanned;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.azazai.R;
import com.azazai.data.Request;
import com.squareup.picasso.Picasso;
import com.utilsframework.android.adapters.navigation.NavigationListAdapter;
import com.utilsframework.android.resources.StringUtilities;

public class RequestsAdapter extends NavigationListAdapter<Request, RequestHolder> {
    private final Picasso picasso;
    private Listener listener;

    public interface Listener {
        void onAcceptRequestTap(Request request);
        void onDeclineRequestTap(Request request);
    }

    public RequestsAdapter(Context context, Listener listener) {
        super(context);
        picasso = Picasso.with(context);
        this.listener = listener;
    }

    @Override
    protected int getRootLayoutId(int viewType) {
        return R.layout.request;
    }

    @Override
    protected RequestHolder createViewHolder(View view) {
        RequestHolder holder = new RequestHolder();
        holder.message = (TextView) view.findViewById(R.id.message);
        holder.avatar = (ImageView) view.findViewById(R.id.avatar);
        holder.accept = view.findViewById(R.id.accept);
        holder.decline = view.findViewById(R.id.decline);
        return holder;
    }

    @Override
    protected void reuseView(final Request request, RequestHolder holder,
                             int position, View view) {
        final Spanned message = StringUtilities.fromHtml(view.getContext(),
                R.string.request_message, request.userName, request.event.name);
        holder.message.setText(message);
        picasso.load(request.avatar).into(holder.avatar);
        holder.accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onAcceptRequestTap(request);
            }
        });
        holder.decline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onDeclineRequestTap(request);
            }
        });
    }
}
