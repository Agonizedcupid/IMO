package com.aariyan.imo_template.Auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.aariyan.imo_template.Activity.ChangeNameActivity;
import com.aariyan.imo_template.Constant.Constant;
import com.aariyan.imo_template.MainActivity;
import com.aariyan.imo_template.Model.UserModel;
import com.aariyan.imo_template.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

public class OTP_verification extends AppCompatActivity {

    private static String phoneNumber, otp, givenOTP;
    private EditText firstCode, secondCode, thirdCode, fourthCode, fifthCode, sixthCode;
    private TextView resendCode;
    private Button verificationOTP;

    private FirebaseAuth userAuth;

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_verification);
        userAuth = FirebaseAuth.getInstance();

        if (getIntent() != null) {
            otp = getIntent().getStringExtra("otp");
            phoneNumber = getIntent().getStringExtra("phone");
        }

        initUI();
    }

    private void initUI() {
        progressBar = findViewById(R.id.progressbar);

        firstCode = findViewById(R.id.firstCode);
        secondCode = findViewById(R.id.secondCode);
        thirdCode = findViewById(R.id.thirdCode);
        fourthCode = findViewById(R.id.fourthCode);
        fifthCode = findViewById(R.id.fifthCode);
        sixthCode = findViewById(R.id.sixthCode);
        resendCode = findViewById(R.id.resendText);
        verificationOTP = findViewById(R.id.verifyOtp);


        verificationOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                validation();
                verificationOTP.setEnabled(false);
            }
        });

        numberOTPMOve();

        resendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resendOTP();
            }
        });
    }

    private void resendOTP() {
        startActivity(new Intent(OTP_verification.this, Authentications.class));
    }

    private void numberOTPMOve() {
        firstCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (!s.toString().trim().isEmpty()) {
                    secondCode.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        secondCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (!s.toString().trim().isEmpty()) {
                    thirdCode.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        thirdCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (!s.toString().trim().isEmpty()) {
                    fourthCode.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        fourthCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (!s.toString().trim().isEmpty()) {
                    fifthCode.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        fifthCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (!s.toString().trim().isEmpty()) {
                    sixthCode.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void validation() {

        if (!TextUtils.isEmpty(firstCode.getText().toString().trim())
                && !TextUtils.isEmpty(secondCode.getText().toString().trim())
                && !TextUtils.isEmpty(thirdCode.getText().toString().trim())
                && !TextUtils.isEmpty(fourthCode.getText().toString().trim())
                && !TextUtils.isEmpty(fifthCode.getText().toString().trim())
                && !TextUtils.isEmpty(sixthCode.getText().toString().trim())
        ) {
            givenOTP = firstCode.getText().toString() + secondCode.getText().toString() + thirdCode.getText().toString() +
                    fourthCode.getText().toString() + fifthCode.getText().toString() + sixthCode.getText().toString();


            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(otp, givenOTP);
            if (givenOTP.equals(credential.getSmsCode())) {
                //Toast.makeText(this, "Matched", Toast.LENGTH_SHORT).show();

                //Create Sign up options:
               // signUp();
                //Toast.makeText(OTP_verification.this, "Log In Success", Toast.LENGTH_SHORT).show();
                userAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            saveDataOnDatabase();
                            //startActivity(new Intent(OTP_verification.this, MainActivity.class));
                            startActivity(new Intent(OTP_verification.this, ChangeNameActivity.class));
                            finish();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });

            } else {
                Toast.makeText(this, "not matched", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        } else {
            Toast.makeText(this, "Please enter the OTP", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
        }
    }

    private void saveDataOnDatabase() {
        UserModel model = new UserModel(
                userAuth.getCurrentUser().getUid(),
                "",
                phoneNumber,
                "",
                "100"
        );

        Constant.userRef.child(userAuth.getCurrentUser().getUid()).setValue(model);
    }
}