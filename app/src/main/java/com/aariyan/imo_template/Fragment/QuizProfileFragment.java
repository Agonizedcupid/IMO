package com.aariyan.imo_template.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.aariyan.imo_template.Constant.Constant;
import com.aariyan.imo_template.Model.UserModel;
import com.aariyan.imo_template.R;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import javax.sql.CommonDataSource;


public class QuizProfileFragment extends Fragment {

    private View root;
    private TextView pointTextBox, profilePhoneNumber;
    private EditText profileName, profileProfession;
    private Button profileUpdateBtn;

    private ProgressBar progressBar;

    private FirebaseAuth userAuth;

    public QuizProfileFragment() {
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
        root = inflater.inflate(R.layout.fragment_quiz_profile, container, false);
        userAuth = FirebaseAuth.getInstance();

        initUI();

        loadUserInfo();
        return root;
    }

    private void loadUserInfo() {
        Constant.userRef.orderByChild("userId").equalTo(userAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        UserModel model = dataSnapshot.getValue(UserModel.class);
                        pointTextBox.setText("You Have " + model.getUserPoints() + " Points");
                        profilePhoneNumber.setText(model.getUserPhone());
                        profileName.setText(model.getUserName(), TextView.BufferType.EDITABLE);
                        profileProfession.setText(model.getUserProfession(), TextView.BufferType.EDITABLE);
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
        progressBar = root.findViewById(R.id.progressbar);

        pointTextBox = root.findViewById(R.id.pointTextBox);
        profilePhoneNumber = root.findViewById(R.id.profilePhoneNumber);
        profileName = root.findViewById(R.id.profileName);
        profileProfession = root.findViewById(R.id.profileProfession);
        profileUpdateBtn = root.findViewById(R.id.updateProfile);

        profileUpdateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                updateProfile();
            }
        });
    }

    private void updateProfile() {
        Constant.userRef.child(userAuth.getCurrentUser().getUid()).child("userName").setValue(profileName.getText().toString());
        Constant.userRef.child(userAuth.getCurrentUser().getUid()).child("userProfession").setValue(profileProfession.getText().toString());

        Toast.makeText(requireContext(), "Profile Updated!", Toast.LENGTH_LONG).show();
        loadUserInfo();
        progressBar.setVisibility(View.GONE);
    }
}