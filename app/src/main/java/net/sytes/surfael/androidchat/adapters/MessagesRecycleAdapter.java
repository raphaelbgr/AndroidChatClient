package net.sytes.surfael.androidchat.adapters;

/**
 * Created by Raphael on 24/12/2015.
 */

import android.app.Activity;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.sytes.surfael.androidchat.R;
import net.sytes.surfael.api.model.messages.Message;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MessagesRecycleAdapter extends  RecyclerView.Adapter<MessagesRecycleAdapter.ViewHolder> {
    private static List<Message> messages;
    protected static Activity mContext;
    protected final FragmentManager supportFragmentManager;

    public MessagesRecycleAdapter(List<Message> messages, Activity mContext, FragmentManager supportFragmentManager){
        this.messages = messages;
        this.mContext = mContext;
        this.supportFragmentManager = supportFragmentManager;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mTxtHeader, mTxtDate, mTxtAmount;

        public ViewHolder(final View itemView) {
            super(itemView);

            mTxtHeader = (TextView) itemView.findViewById(R.id.txt_header);
            mTxtDate = (TextView) itemView.findViewById(R.id.txt_date);
            mTxtAmount = (TextView) itemView.findViewById(R.id.txt_amount);
        }

        public TextView getTxtHeader() {
            return mTxtHeader;
        }

        public TextView getTxtDate() {
            return mTxtDate;
        }

        public TextView getTxtAmount() {
            return mTxtAmount;
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());

        View view = layoutInflater.inflate(R.layout.list_item_message, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());

        Date date =  null;
        if (messages.get(position).getMsgCreationDate() != null) {
            date = messages.get(position).getMsgCreationDate();
            viewHolder.getTxtDate().setText(dateFormat.format(date));
        }

        viewHolder.getTxtHeader().setText(messages.get(position).getOwnerName());
        viewHolder.getTxtAmount().setText(messages.get(position).getText());
    }
}
