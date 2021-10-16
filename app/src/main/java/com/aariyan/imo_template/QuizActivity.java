package com.aariyan.imo_template;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;

import com.aariyan.imo_template.Fragment.PaymentFragment;
import com.aariyan.imo_template.Fragment.QuizAddFragment;
import com.aariyan.imo_template.Fragment.QuizHomeFragment;
import com.aariyan.imo_template.Fragment.QuizProfileFragment;
import com.google.android.material.tabs.TabLayout;

import java.util.Objects;

public class QuizActivity extends AppCompatActivity {

    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        initUI();
    }

    private void initUI() {
        tabLayout = findViewById(R.id.tabs);

        Objects.requireNonNull(tabLayout.getTabAt(0)).getIcon().setColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC_IN);
        setFragment(new QuizHomeFragment());

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int tabIconColor = ContextCompat.getColor(QuizActivity.this, R.color.white);
                tab.getIcon().setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);

                if (tab.getPosition() == 0) {
                    setFragment(new QuizHomeFragment());
                } else if (tab.getPosition() == 1) {
                    setFragment(new QuizAddFragment());
                } else if (tab.getPosition() == 3) {
                    setFragment(new QuizProfileFragment());
                }else if (tab.getPosition() == 2) {
                    setFragment(new PaymentFragment());
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                tab.getIcon().setColorFilter(Color.parseColor("#0D76BC"), PorterDuff.Mode.SRC_IN);

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void setFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainers, fragment).addToBackStack(null).commit();
    }
}