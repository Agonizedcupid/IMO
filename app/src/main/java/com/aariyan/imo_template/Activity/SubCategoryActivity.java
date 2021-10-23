package com.aariyan.imo_template.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
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

public class SubCategoryActivity extends AppCompatActivity {

    private String id = "";

    private ImageView backBtn,addCategory;

    private RecyclerView recyclerView;
    private static ProgressBar progressBar;

    private List<SubCategoryModel> list = new ArrayList<>();

    private Context context;

    private View root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_category);
        context = SubCategoryActivity.this;
        id = getIntent().getStringExtra("id");
        initUI();
    }

    private void initUI() {

        backBtn = findViewById(R.id.backBtn);
        ///backBtn.setOnClickListener(view -> onBackPressed());



        recyclerView = findViewById(R.id.categoryRecyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(context,2));

        progressBar = findViewById(R.id.progressbar);

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
                    SubCategoryAdapter adapter = new SubCategoryAdapter(context,list,SubCategoryActivity.this);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    progressBar.setVisibility(View.GONE);
                } else {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(context, "No data found!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressBar.setVisibility(View.GONE);
            }
        });
    }
}