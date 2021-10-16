package com.aariyan.imo_template.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.aariyan.imo_template.Adapter.PaymentAdapter;
import com.aariyan.imo_template.Constant.Constant;
import com.aariyan.imo_template.Model.PaymentRequestModel;
import com.aariyan.imo_template.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PaymentFragment extends Fragment {

    private View root;


    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private List<PaymentRequestModel> list = new ArrayList<>();
    private FirebaseAuth userAuth;

    public PaymentFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root =  inflater.inflate(R.layout.fragment_payment, container, false);

        userAuth = FirebaseAuth.getInstance();
        initUI();
        loadRequest();
        return root;
    }

    private void loadRequest() {
        Constant.paymentRef.orderByChild("userId").equalTo(userAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    list.clear();
                    for (DataSnapshot dataSnapshot:snapshot.getChildren()) {
                        PaymentRequestModel model = dataSnapshot.getValue(PaymentRequestModel.class);
                        list.add(model);
                    }
                    PaymentAdapter adapter = new PaymentAdapter(requireContext(),list);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    progressBar.setVisibility(View.GONE);
                } else {
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void initUI() {
        recyclerView = root.findViewById(R.id.paymentRecyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        progressBar = root.findViewById(R.id.progressbar);
    }
}