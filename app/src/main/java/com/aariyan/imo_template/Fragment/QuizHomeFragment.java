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

import com.aariyan.imo_template.Adapter.CategoryAdapter;
import com.aariyan.imo_template.Constant.Constant;
import com.aariyan.imo_template.Model.CategoryModel;
import com.aariyan.imo_template.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class QuizHomeFragment extends Fragment {

    private ImageView backBtn,addCategory;

    private RecyclerView recyclerView;
    private ProgressBar progressBar;

    private List<CategoryModel> list = new ArrayList<>();

    private View root;

    private Context context;
    public QuizHomeFragment() {
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
        root = inflater.inflate(R.layout.fragment_quiz_home, container, false);
        context = requireContext();
        initUI();
        return root;
    }

    private void initUI() {

        recyclerView = root.findViewById(R.id.categoryRecyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(requireContext(),2));

        progressBar = root.findViewById(R.id.progressbar);

        //Load Category
        loadCategory();
    }

    private void loadCategory() {
        Constant.categoryRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    list.clear();
                    for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                        CategoryModel model = dataSnapshot.getValue(CategoryModel.class);
                        list.add(model);
                    }
                    CategoryAdapter adapter = new CategoryAdapter(context,list);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    progressBar.setVisibility(View.GONE);

                } else {
                    Toast.makeText(context, "No Category Found!", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        });
    }
}