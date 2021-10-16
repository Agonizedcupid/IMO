package com.aariyan.imo_template.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.aariyan.imo_template.Interface.APISerrvice;
import com.aariyan.imo_template.Model.PaymentRequestModel;
import com.aariyan.imo_template.Notification.Client;
import com.aariyan.imo_template.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentAdapter extends RecyclerView.Adapter<PaymentAdapter.ViewHolder> {

    private Context context;
    private List<PaymentRequestModel> list;

    public PaymentAdapter(Context context, List<PaymentRequestModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.single_payment_layout,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        PaymentRequestModel model = list.get(position);
        holder.userPhone.setText(model.getPhoneNumber());
        holder.statusBox.setText(model.getStatus(), TextView.BufferType.EDITABLE);
        holder.requestedPoint.setText("Requested: "+model.getPoint()+" Points.");
        holder.dateTime.setText("Created: "+model.getDate()+"  "+model.getTime());
        holder.messageBox.setText(model.getMessage(), TextView.BufferType.EDITABLE);
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView userPhone,requestedPoint,dateTime;
        private EditText messageBox,statusBox;
        private Button updateUser;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            userPhone = itemView.findViewById(R.id.phoneNumber);
            requestedPoint = itemView.findViewById(R.id.points);
            dateTime = itemView.findViewById(R.id.dateTime);
            messageBox = itemView.findViewById(R.id.messageBox);
            statusBox = itemView.findViewById(R.id.statusBox);
            updateUser = itemView.findViewById(R.id.updateUserBtn);
        }
    }
}
