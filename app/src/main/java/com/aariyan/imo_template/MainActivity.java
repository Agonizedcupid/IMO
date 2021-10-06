package com.aariyan.imo_template;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;

import com.aariyan.imo_template.Adapter.ListAdapter;
import com.aariyan.imo_template.Fragment.ContactFragment;
import com.aariyan.imo_template.Fragment.HomeFragment;
import com.aariyan.imo_template.Fragment.PrivateGroupFragment;
import com.aariyan.imo_template.Fragment.PublicGroupFragment;
import com.aariyan.imo_template.Model.LisModel;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {


    private TabLayout tabLayout;
    private CircleImageView profileIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initUI();
    }

    private void initUI() {

        profileIcon = findViewById(R.id.profileIcon);
        profileIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,ProfileActivity.class));
            }
        });

        tabLayout = findViewById(R.id.tabs);
        Objects.requireNonNull(tabLayout.getTabAt(0)).getIcon().setColorFilter(Color.parseColor("#ff0099cc"), PorterDuff.Mode.SRC_IN);
        openNavigationMenu(new HomeFragment());


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int tabIconColor = ContextCompat.getColor(MainActivity.this, android.R.color.holo_blue_dark);
                tab.getIcon().setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);

                if (tab.getPosition() == 0) {
                    openNavigationMenu(new HomeFragment());
                } else if (tab.getPosition() == 1) {
                    openNavigationMenu(new PrivateGroupFragment());
                } else if (tab.getPosition() == 2) {
                    openNavigationMenu(new PublicGroupFragment());
                } else if (tab.getPosition() == 3) {
                    openNavigationMenu(new ContactFragment());
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                tab.getIcon().setColorFilter(Color.parseColor("#a9a9a9"), PorterDuff.Mode.SRC_IN);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


    }

    private void openNavigationMenu(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, fragment).commit();
    }


}