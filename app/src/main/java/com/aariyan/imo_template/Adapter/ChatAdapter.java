package com.aariyan.imo_template.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.aariyan.imo_template.Model.MessageModel;
import com.aariyan.imo_template.R;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {

    private Context context;
    private List<MessageModel> list;

    public ChatAdapter(Context context, List<MessageModel> list) {
        this.list = list;
        this.context = context;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.single_messaging_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        MessageModel model = list.get(position);

        if (model.getType() == 0) {
            holder.receiverLayout.setVisibility(View.VISIBLE);
            holder.senderLayout.setVisibility(View.GONE);
            holder.receiverMessage.setText(model.getReceiverMessage());
            holder.receiverTime.setText(model.getReceiverTime());
        }
        if (model.getType() == 1) {
            holder.receiverLayout.setVisibility(View.GONE);
            holder.senderLayout.setVisibility(View.VISIBLE);

            holder.senderMessage.setText(model.getSenderMessage());
            holder.senderTime.setText(model.getSenderTime());
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView senderMessage,receiverMessage;
        private TextView senderTime,receiverTime;
        private LinearLayout senderLayout,receiverLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            senderMessage = itemView.findViewById(R.id.senderText);
            receiverMessage = itemView.findViewById(R.id.receiverText);
            senderTime = itemView.findViewById(R.id.senderTime);
            receiverTime = itemView.findViewById(R.id.receiverTime);
            senderLayout = itemView.findViewById(R.id.senderLayout);
            receiverLayout = itemView.findViewById(R.id.receiverLayout);
        }
    }
}
