package com.aariyan.imo_template;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.aariyan.imo_template.Adapter.CategoryAdapter;
import com.aariyan.imo_template.Constant.Constant;
import com.aariyan.imo_template.Fragment.PaymentFragment;
import com.aariyan.imo_template.Fragment.QuizAddFragment;
import com.aariyan.imo_template.Fragment.QuizHomeFragment;
import com.aariyan.imo_template.Fragment.QuizProfileFragment;
import com.aariyan.imo_template.Model.CategoryModel;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class QuizActivity extends AppCompatActivity {

    //private TabLayout tabLayout;

    private ImageView backBtn,addCategory;

    private RecyclerView recyclerView;
    private ProgressBar progressBar;

    private List<CategoryModel> list = new ArrayList<>();

    private View root;

    private Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        context = QuizActivity.this;

        initUI();
    }

    private void initUI() {
        recyclerView = findViewById(R.id.categoryRecyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));

        progressBar = findViewById(R.id.progressbar);

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

//    private void UI() {
//        tabLayout = findViewById(R.id.tabs);
//
//        Objects.requireNonNull(tabLayout.getTabAt(0)).getIcon().setColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC_IN);
//        setFragment(new QuizHomeFragment());
//
//        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//            @Override
//            public void onTabSelected(TabLayout.Tab tab) {
//                int tabIconColor = ContextCompat.getColor(QuizActivity.this, R.color.white);
//                tab.getIcon().setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);
//
//                if (tab.getPosition() == 0) {
//                    setFragment(new QuizHomeFragment());
//                } else if (tab.getPosition() == 1) {
//                    setFragment(new QuizAddFragment());
//                } else if (tab.getPosition() == 3) {
//                    setFragment(new QuizProfileFragment());
//                }else if (tab.getPosition() == 2) {
//                    setFragment(new PaymentFragment());
//                }
//            }
//
//            @Override
//            public void onTabUnselected(TabLayout.Tab tab) {
//                tab.getIcon().setColorFilter(Color.parseColor("#0D76BC"), PorterDuff.Mode.SRC_IN);
//
//            }
//
//            @Override
//            public void onTabReselected(TabLayout.Tab tab) {
//
//            }
//        });
//    }
//
//    private void setFragment(Fragment fragment) {
//        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainers, fragment).addToBackStack(null).commit();
//    }
}