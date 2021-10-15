package com.aariyan.imo_template.Fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.aariyan.imo_template.Adapter.SubCategoryAdapter;
import com.aariyan.imo_template.Constant.Constant;
import com.aariyan.imo_template.Model.SubCategoryModel;
import com.aariyan.imo_template.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class QuizSubCategoryFragment extends Fragment {

    private String id = "";

    private ImageView backBtn,addCategory;

    private RecyclerView recyclerView;
    private static ProgressBar progressBar;

    private List<SubCategoryModel> list = new ArrayList<>();

    private Context context;

    private View root;

    public QuizSubCategoryFragment() {
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
        root = inflater.inflate(R.layout.fragment_quiz_sub_category, container, false);
        id = getArguments().getString("id");
        initUI();
        return root;
    }

    private void initUI() {
        backBtn = root.findViewById(R.id.backBtn);
        ///backBtn.setOnClickListener(view -> onBackPressed());



        recyclerView = root.findViewById(R.id.categoryRecyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(context,2));

        progressBar = root.findViewById(R.id.progressbar);

        //Load Category
        loadSubCategory();
    }

    public void loadSubCategory() {
        Constant.subCategoryRef.orderByChild("parentId").equalTo(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    list.clear();
                    for (DataSnapshot dataSnapshot:snapshot.getChildren()) {
                        SubCategoryModel model = dataSnapshot.getValue(SubCategoryModel.class);
                        list.add(model);
                    }
                    SubCategoryAdapter adapter = new SubCategoryAdapter(requireContext(),list,requireActivity());
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    progressBar.setVisibility(View.GONE);
                } else {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(requireContext(), "No data found!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressBar.setVisibility(View.GONE);
            }
        });
    }
}