package com.aariyan.imo_template.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.aariyan.imo_template.Constant.Constant;
import com.aariyan.imo_template.MainActivity;
import com.aariyan.imo_template.Model.UserModel;
import com.aariyan.imo_template.ProfileActivity;
import com.aariyan.imo_template.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.core.view.Change;

public class ChangeNameActivity extends AppCompatActivity {

    private EditText changeName;
    private Button updateBtn;
    private ProgressBar progressBar;
    private FirebaseAuth userAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_name);
        userAuth = FirebaseAuth.getInstance();

        initUI();

        loadUserInfo();
    }

    private void loadUserInfo() {
        Constant.userRef.orderByChild("userId").equalTo(userAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        UserModel model = dataSnapshot.getValue(UserModel.class);
                        changeName.setText(model.getUserName(), TextView.BufferType.EDITABLE);
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

    private void initUI() {
        changeName = findViewById(R.id.changeName);
        updateBtn = findViewById(R.id.updateProfileBtn);
        progressBar = findViewById(R.id.progressbar);


        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                updateProfile();
            }
        });
    }

    private void updateProfile() {
        Constant.userRef.child(userAuth.getCurrentUser().getUid()).child("userName").setValue(changeName.getText().toString());
        Toast.makeText(ChangeNameActivity.this, "Profile Updated!", Toast.LENGTH_LONG).show();
        loadUserInfo();
        progressBar.setVisibility(View.GONE);
        startActivity(new Intent(ChangeNameActivity.this, MainActivity.class));
        finish();
    }
}