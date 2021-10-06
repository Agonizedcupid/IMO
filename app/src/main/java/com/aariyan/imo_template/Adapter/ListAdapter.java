package com.aariyan.imo_template.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aariyan.imo_template.CallActivity;
import com.aariyan.imo_template.MessagingActivity;
import com.aariyan.imo_template.Model.LisModel;
import com.aariyan.imo_template.R;

import java.util.List;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {

    private Context context;
    private List<LisModel> list;

    public ListAdapter(Context context,List<LisModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.single_user_list_layout,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        LisModel model = list.get(position);

        holder.activeTime.setText(model.getTime());
        holder.userName.setText(model.getName());
        holder.userMessage.setText(model.getMessage());

        holder.callIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, CallActivity.class)
                .putExtra("name",model.getName()));

            }
        });

        holder.expand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, MessagingActivity.class)
                        .putExtra("name",model.getName()));

            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView userName,userMessage,activeTime;
        private ImageView callIcon;

        private LinearLayout expand;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            userMessage = itemView.findViewById(R.id.userMessage);
            userName = itemView.findViewById(R.id.userName);
            activeTime = itemView.findViewById(R.id.activeTime);
            callIcon = itemView.findViewById(R.id.callIcon);
            expand = itemView.findViewById(R.id.expand);
        }
    }
}
