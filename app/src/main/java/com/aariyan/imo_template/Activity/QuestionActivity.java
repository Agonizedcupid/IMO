package com.aariyan.imo_template.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aariyan.imo_template.Constant.Constant;
import com.aariyan.imo_template.Interface.CheckUserPointInterface;
import com.aariyan.imo_template.MainActivity;
import com.aariyan.imo_template.Model.QuestionModel;
import com.aariyan.imo_template.Model.UserModel;
import com.aariyan.imo_template.QuizActivity;
import com.aariyan.imo_template.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class QuestionActivity extends AppCompatActivity {

    private FloatingActionButton addQ;
    private TextView questionBox;
    private TextView answerOne, answerTwo, answerThree, answerFour;

    private int position = 0;
    private String id = "";
    private static List<QuestionModel> questionList = new ArrayList<>();
    private String answer = "";
    private FirebaseAuth userAuth;

    private static int userRemainingPoints = 0;
    private LinearLayout mainLinearLayout;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        context = QuestionActivity.this;

        userAuth = FirebaseAuth.getInstance();

        id = getIntent().getStringExtra("id");
        initUI();

        checkUserPoint(new CheckUserPointInterface() {
            @Override
            public void remainingUserPoint(int point) {
                userRemainingPoints = point;
                if (userRemainingPoints >= 5) {
                    mainLinearLayout.setVisibility(View.VISIBLE);
                    loadQuestion(id);
                } else {
                    mainLinearLayout.setVisibility(View.GONE);
                    Toast.makeText(context, "You don't have enough point!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void loadQuestion(String id) {
        Constant.questionRef.orderByChild("subCategoryId").equalTo(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    mainLinearLayout.setVisibility(View.VISIBLE);
                    questionList.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        QuestionModel model = dataSnapshot.getValue(QuestionModel.class);
                        if (model.getStatus().equals("accepted"))
                            questionList.add(model);
                    }
                    if (questionList.size() > 0) {
                        loadSingleQuestion();
                    } else {
                        mainLinearLayout.setVisibility(View.GONE);
                        Toast.makeText(context, "No question found!", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    mainLinearLayout.setVisibility(View.GONE);
                    Toast.makeText(context, "No Question!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                mainLinearLayout.setVisibility(View.GONE);
            }
        });
    }

    private void loadSingleQuestion() {
        QuestionModel model = questionList.get(position);
        questionBox.setText("Q: " + model.getQuestion());
        answerOne.setText(model.getOptionOne());
        answerTwo.setText(model.getOptionTwo());
        answerThree.setText(model.getOptionThree());
        answerFour.setText(model.getOptionFour());

        answerOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                answer = model.getOptionOne();
                answerValidationDialog(answer, model);
            }
        });
        answerTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                answer = model.getOptionTwo();
                answerValidationDialog(answer, model);
            }
        });

        answerThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                answer = model.getOptionThree();
                answerValidationDialog(answer, model);
            }
        });

        answerFour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                answer = model.getOptionFour();
                answerValidationDialog(answer, model);
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(QuestionActivity.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
        finish();
    }

    private void answerValidationDialog(String ans, QuestionModel model) {
        String answer = ans;
        Dialog dialog = new Dialog(context);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setContentView(R.layout.right_wrong_dialog);
        dialog.setCancelable(false);

        ImageView icon = dialog.findViewById(R.id.icons);
        TextView rightOrWrongAnswer = dialog.findViewById(R.id.rightOrWrongAnswer);
        TextView closeBtn = dialog.findViewById(R.id.closeBtn);
        TextView nextBtn = dialog.findViewById(R.id.nextBtn);

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                startActivity(new Intent(QuestionActivity.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
                finish();
            }
        });

        if (model.getAnswer().equals(answer)) {
            icon.setImageResource(R.drawable.rigt_tik_icon);
            userRemainingPoints = userRemainingPoints + 5;
            rightOrWrongAnswer.setText("Right Answer!");
        } else {
            icon.setImageResource(R.drawable.cross_icon_red);
            rightOrWrongAnswer.setText("Wrong Answer!");
            userRemainingPoints = userRemainingPoints - 5;
            Constant.userRef.orderByChild("userId").equalTo(model.getUploaderId()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        int uploaderPoints = 0;
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            UserModel model = dataSnapshot.getValue(UserModel.class);
                            uploaderPoints = 5 + Integer.parseInt(model.getUserPoints());
                        }
                        Constant.userRef.child(model.getUploaderId()).child("userPoints").setValue("" + uploaderPoints);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

        //Updating the points:
        Constant.userRef.child(userAuth.getCurrentUser().getUid()).child("userPoints").setValue("" + userRemainingPoints);

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                position++;
                if (position <= questionList.size() - 1 && userRemainingPoints >= 5) {
                    loadSingleQuestion();
                } else {
                    startActivity(new Intent(context, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
                    finish();
                }

                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void checkUserPoint(CheckUserPointInterface checkUserPointInterface) {
        Constant.userRef.orderByChild("userId").equalTo(userAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        UserModel model = dataSnapshot.getValue(UserModel.class);
                        assert model != null;
                        checkUserPointInterface.remainingUserPoint(Integer.parseInt(model.getUserPoints()));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void initUI() {

        addQ = findViewById(R.id.addQ);
        addQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(QuestionActivity.this, AddQuestionActivity.class));
            }
        });
        mainLinearLayout = findViewById(R.id.mainLinearLayout);

        questionBox = findViewById(R.id.questionText);
        answerOne = findViewById(R.id.answerOne);
        answerTwo = findViewById(R.id.answerTwo);
        answerThree = findViewById(R.id.answerThree);
        answerFour = findViewById(R.id.answerFour);

    }
}