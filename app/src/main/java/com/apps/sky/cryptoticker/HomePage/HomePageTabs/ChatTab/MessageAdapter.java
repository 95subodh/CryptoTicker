package com.apps.sky.cryptoticker.HomePage.HomePageTabs.ChatTab;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.apps.sky.cryptoticker.R;

import java.util.List;

/**
 * Created by subyadav on 27/10/17.
 */

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    private List<Message> mMessages;
    private int[] mUsernameColors;
    private List<Boolean> fromMe;
    private Context context;

    public MessageAdapter(Context context, List<Message> messages, List<Boolean> from) {
        mMessages = messages;
        mUsernameColors = context.getResources().getIntArray(R.array.username_colors);
        fromMe = from;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layout = -1;
        context = parent.getContext();
        switch (viewType) {
            case Message.TYPE_MESSAGE:
                layout = R.layout.chat_item_message;
                break;
            case Message.TYPE_LOG:
                layout = R.layout.chat_item_log;
                break;
            case Message.TYPE_ACTION:
                layout = R.layout.chat_item_action;
                break;
        }
        View v = LayoutInflater
                .from(parent.getContext())
                .inflate(layout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        Message message = mMessages.get(position);
        viewHolder.setMessage(message.getMessage());
        viewHolder.setUsername(message.getUsername());
        View view =  viewHolder.itemView;
        RecyclerView.LayoutParams params = (RecyclerView.LayoutParams)view.getLayoutParams();
        int width = Resources.getSystem().getDisplayMetrics().widthPixels;
        if (fromMe.get(position)) {
            params.setMarginStart(width/4);
            params.setMarginEnd(0);
            viewHolder.itemView.findViewById(R.id.main_view).setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
            viewHolder.mMessageView.setTextColor(context.getResources().getColor(R.color.pure_white));
        }
        else {
            params.setMarginEnd(width/4);
            params.setMarginStart(0);
            viewHolder.itemView.findViewById(R.id.main_view).setBackgroundColor(context.getResources().getColor(R.color.dirty_white));
            viewHolder.mMessageView.setTextColor(context.getResources().getColor(R.color.dark_black));
        }
    }

    @Override
    public int getItemCount() {
        return mMessages.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mMessages.get(position).getType();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mUsernameView;
        private TextView mMessageView;

        public ViewHolder(View itemView) {
            super(itemView);

            mUsernameView = itemView.findViewById(R.id.username);
            mMessageView = itemView.findViewById(R.id.message);
        }

        public void setUsername(String username) {
            if (null == mUsernameView) return;
            mUsernameView.setText(username);
//            mUsernameView.setTextColor(getUsernameColor(username));
        }

        public void setMessage(String message) {
            if (null == mMessageView) return;
            mMessageView.setText(message);
        }

        private int getUsernameColor(String username) {
            int hash = 7;
            for (int i = 0, len = username.length(); i < len; i++) {
                hash = username.codePointAt(i) + (hash << 5) - hash;
            }
            int index = Math.abs(hash % mUsernameColors.length);
            return mUsernameColors[index];
        }
    }
}
