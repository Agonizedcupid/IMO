package com.aariyan.imo_template.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.aariyan.imo_template.Constant.Constant;
import com.aariyan.imo_template.Interface.APISerrvice;
import com.aariyan.imo_template.Model.NotificationModel;
import com.aariyan.imo_template.Model.PaymentRequestModel;
import com.aariyan.imo_template.Model.UserModel;
import com.aariyan.imo_template.Notification.Client;
import com.aariyan.imo_template.Notification.Data;
import com.aariyan.imo_template.Notification.MyResponse;
import com.aariyan.imo_template.Notification.Sender;
import com.aariyan.imo_template.Notification.Token;
import com.aariyan.imo_template.ProfileActivity;
import com.aariyan.imo_template.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.NotNull;

import java.util.HashMap;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WalletActivity extends AppCompatActivity {

    private View root;
    private TextView pointTextBox;

    private ProgressBar progressBar;

    private FirebaseAuth userAuth;

    private EditText pointBox;
    private Button withdrawBtn;

    private Context context;

    private RadioButton bKashBtn, payPalBtn;
    private EditText paymentId;
    private String selectedPaymentMethod = "";

    //Notification Part:
    APISerrvice apiSerrvice;
    private static String whatTypes = "";

    private String invitationMessage = "Your Question";
    private String receiverId;
    public static boolean notify = false;

    private String notificationStatus = "Payment Request Created!";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);
        context = WalletActivity.this;

        userAuth = FirebaseAuth.getInstance();
        apiSerrvice = Client.getClient("https://fcm.googleapis.com/").create(APISerrvice.class);

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
                        pointTextBox.setText("You Have " + model.getUserPoints() + " Points");
                        progressBar.setVisibility(View.GONE);

                        int po = Integer.parseInt(model.getUserPoints());

                        withdrawBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                progressBar.setVisibility(View.VISIBLE);
                                String point = pointBox.getText().toString();
                                if (!point.equals("")) {
                                    if (Integer.parseInt(point) < 150 || po < 150) {
                                        Toast.makeText(context, "At least 150 points needed!", Toast.LENGTH_SHORT).show();
                                        progressBar.setVisibility(View.GONE);
                                    } else {
                                        if (Integer.parseInt(point) > po) {
                                            Toast.makeText(context, "Invalid point", Toast.LENGTH_SHORT).show();
                                        } else {
                                            createPaymentRequest(model, point);
                                            startActivity(new Intent(WalletActivity.this, ProfileActivity.class));
                                            finish();
                                        }
                                        progressBar.setVisibility(View.GONE);
                                    }
                                } else {
                                    progressBar.setVisibility(View.GONE);
                                    Toast.makeText(context, "Please enter point!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

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

    private void createPaymentRequest(UserModel user, String point) {

        if (TextUtils.isEmpty(paymentId.getText().toString())) {
            paymentId.setError("Can't be empty!");
            paymentId.requestFocus();
            return;
        }
        if (selectedPaymentMethod.equals("")) {
            Toast.makeText(WalletActivity.this, "Select Payment Method!", Toast.LENGTH_SHORT).show();
            return;
        }

        String id = UUID.randomUUID().toString();
        PaymentRequestModel model = new PaymentRequestModel(
                id,
                point,
                user.getUserId(),
                Constant.getCurrentDate(),
                Constant.getCurrentTime(),
                user.getUserPhone(),
                "",
                "pending",
                selectedPaymentMethod, paymentId.getText().toString()
        );
        Constant.paymentRef.child(id).setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(context, "Payment Request Created!", Toast.LENGTH_SHORT).show();
                int p = Integer.parseInt(point);
                p = Integer.parseInt(user.getUserPoints()) - p;
                Constant.userRef.child(userAuth.getCurrentUser().getUid()).child("userPoints").setValue("" + p);
                pointBox.setText("", TextView.BufferType.EDITABLE);
                withdrawBtn.setEnabled(false);
                notification();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, "Unable to create request!", Toast.LENGTH_SHORT).show();
                pointBox.setText("", TextView.BufferType.EDITABLE);
            }
        });
    }

    private void notification() {
        HashMap<String, Object> hashMap = new HashMap<>();

        hashMap.put("sender", userAuth.getCurrentUser().getUid());
        hashMap.put("receiver", "");
        hashMap.put("message", invitationMessage);

        final String msg = invitationMessage;
        notify = true;
        if (notify) {
            //assert users != null;
            sendNotification(receiverId, notificationStatus, msg, "");
        }

        notify = false;
    }

    private void sendNotification(final String receiver, final String notificationStatus, final String msg, String idForNotification) {

        Log.d("NOTIFICATION_TEST", "sendNotification");

        FirebaseDatabase.getInstance().getReference().child("Token_Admin").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Token token = snapshot.getValue(Token.class);

                        //Here data will be change

                        Data data = new Data(
                                userAuth.getCurrentUser().getUid(),
                                R.mipmap.ic_launcher,
                                "Please check your notification tab for more details!",
                                notificationStatus,
                                receiverId,
                                whatTypes,
                                ""
                        );

                        Sender sender = new Sender(data, token.getToken());

                        Log.d("TOKEN_RESULT", token.getToken());

                        apiSerrvice.sendNotification(sender).enqueue(new Callback<MyResponse>() {
                            @Override
                            public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {

                                //Toast.makeText(context, "" + response.code(), Toast.LENGTH_SHORT).show();

                                if (response.isSuccessful()) {
                                    //Toast.makeText(LoanEnquiryDetails.this, "Notification Sent", Toast.LENGTH_SHORT).show();
                                    saveNotification(receiverId, notificationStatus, "Payment Request Created!", idForNotification);
                                    //Toast.makeText(context, "Invitation sent.", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(context, "" + response.message(), Toast.LENGTH_SHORT).show();
                                }


                            }

                            @Override
                            public void onFailure(Call<MyResponse> call, Throwable t) {

                                Toast.makeText(context, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                                Log.d("TEST_RESULT", t.getMessage());
                            }
                        });

                    }
                } else {
                    Toast.makeText(context, "Not find!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("NOTIFICATION_TEST", "" + error.getMessage());
            }
        });
    }

    private void saveNotification(String receiverId, String notificationStatus, String title, String idForNotification) {

        String id = UUID.randomUUID().toString();
        NotificationModel model = new NotificationModel(
                id,
                idForNotification,
                Constant.getCurrentDate(),
                Constant.getCurrentTime(),
                receiverId,
                "Point: " + pointBox.getText().toString() + " Method: " + selectedPaymentMethod + " PaymentID: " + paymentId.getText().toString(),
                title,
                notificationStatus
        );

        Constant.notificationRef.child(id).setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(context, "Notification Sent", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                Toast.makeText(context, "Unable to send notification", Toast.LENGTH_SHORT).show();
            }
        });

    }


    private void initUI() {
        progressBar = findViewById(R.id.progressbar);

        pointBox = findViewById(R.id.pointBoxForWithdrawMoney);
        withdrawBtn = findViewById(R.id.withdrawMoneyBtn);

        pointTextBox = findViewById(R.id.pointTextBox);

        bKashBtn = findViewById(R.id.bKashRadioButton);
        payPalBtn = findViewById(R.id.paypalRadioButton);
        paymentId = findViewById(R.id.paymentId);

        bKashBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bKashBtn.setChecked(true);
                payPalBtn.setChecked(false);
                selectedPaymentMethod = "Bkash";
            }
        });

        payPalBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bKashBtn.setChecked(false);
                payPalBtn.setChecked(true);
                selectedPaymentMethod = "PayPal";
            }
        });
    }
}