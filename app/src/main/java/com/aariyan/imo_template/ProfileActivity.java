package com.aariyan.imo_template;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.aariyan.imo_template.Activity.ChangeNameActivity;
import com.aariyan.imo_template.Activity.WalletActivity;
import com.aariyan.imo_template.Constant.Constant;
import com.aariyan.imo_template.Model.UserModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.core.view.Change;

public class ProfileActivity extends AppCompatActivity {

    private ImageView closeIcon;
    private TextView userName,phoneNumber;
    private FirebaseAuth userAuth;
    private ProgressBar progressBar;
    private LinearLayout imoWallet;

    //private LinearLayout changeName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        userAuth = FirebaseAuth.getInstance();

        initUI();
    }

    private void initUI() {

//        changeName = findViewById(R.id.changeName);
//        changeName.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(ProfileActivity.this, ChangeNameActivity.class));
//            }
//        });
        imoWallet = findViewById(R.id.imoWallet);
        imoWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this, WalletActivity.class));
            }
        });

        userName = findViewById(R.id.userNameInProfile);
        phoneNumber = findViewById(R.id.phoneNumberInProfile);
        progressBar = findViewById(R.id.progressbar);

        closeIcon = findViewById(R.id.closeIcon);
        closeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this,MainActivity.class));
            }
        });

        findViewById(R.id.settingsLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this,SettingActivity.class));
            }
        });

        loadUserInfo();
    }

    private void loadUserInfo() {
        Constant.userRef.orderByChild("userId").equalTo(userAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        UserModel model = dataSnapshot.getValue(UserModel.class);
                        phoneNumber.setText("Phone: "+model.getUserPhone());
                        userName.setText(model.getUserName());
                        progressBar.setVisibility(View.GONE);

                    }
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
}