package com.aariyan.imo_template.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aariyan.imo_template.Constant.Constant;
import com.aariyan.imo_template.Interface.CheckUserPointInterface;
import com.aariyan.imo_template.MainActivity;
import com.aariyan.imo_template.Model.AnsweredQuestionModel;
import com.aariyan.imo_template.Model.PointModel;
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
import java.util.UUID;

public class QuestionActivity extends AppCompatActivity {

    private FloatingActionButton addQ;
    private TextView questionBox;
    private TextView answerOne, answerTwo, answerThree, answerFour;

    private int position = 0;
    private String id = "";
    private static List<QuestionModel> questionList = new ArrayList<>();
    private static List<AnsweredQuestionModel> answeredList = new ArrayList<>();
    private String answer = "";
    private FirebaseAuth userAuth;

    private static int userRemainingPoints = 0;
    private LinearLayout mainLinearLayout;
    private Context context;
    private boolean check = false;
    private boolean faceQuestion = false;


    AnsweredQuestionModel ans;

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

        checkingAnsweredQuestion();

        checkAdminPoints();

    }

    private void checkAdminPoints() {
        Constant.pointRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        PointModel model = dataSnapshot.getValue(PointModel.class);
                        assert model != null;
                        Constant.wrongAnswerPoint = Integer.parseInt(model.getWrongAnswerPoint());
                        Constant.rightAnswerPoint = Integer.parseInt(model.getRightAnswerPoint());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void checkingAnsweredQuestion() {
        Constant.answeredQuestionRef.child(userAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    answeredList.clear();
                    for (DataSnapshot dat : snapshot.getChildren()) {
                        AnsweredQuestionModel answered = dat.getValue(AnsweredQuestionModel.class);
                        answeredList.add(answered);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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

                        if (model.getStatus().equals("accepted")) {
                            questionList.add(model);
                        }

                    }
                    if (questionList.size() > 0) {
                        if (answeredList.size() > 0) {
                            for (int i = 0; i < answeredList.size(); i++) {
                                AnsweredQuestionModel model = answeredList.get(i);
                                for (int j = 0; j < questionList.size(); j++) {
                                    QuestionModel question = questionList.get(j);
                                    if (model.getQuestionId().equals(question.getId())) {
                                        questionList.remove(j);
                                    }
                                }
                            }
                        }
                        if (questionList.size() > 0) {
                            loadSingleQuestion();
                        } else {
                            mainLinearLayout.setVisibility(View.GONE);
                            Toast.makeText(context, "No question found!", Toast.LENGTH_SHORT).show();
                        }

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
        faceQuestion = false;
        QuestionModel model = questionList.get(position);

//        if (answeredList.size() > 0) {
//            for (int i = 0; i < answeredList.size(); i++) {
//                if (model.getId().equals(answeredList.get(i).getQuestionId())) {
//                    check = true;
//                    faceQuestion = true;
//                }
//            }
//        }
//
//
//        if (!faceQuestion) {
//            questionBox.setText("Q: " + model.getQuestion());
//            answerOne.setText(model.getOptionOne());
//            answerTwo.setText(model.getOptionTwo());
//            answerThree.setText(model.getOptionThree());
//            answerFour.setText(model.getOptionFour());
//        }
//
//        if (faceQuestion) {
//            position++;
//            if (position < questionList.size()) {
//                loadSingleQuestion();
//            } else {
//                mainLinearLayout.setVisibility(View.GONE);
//                Toast.makeText(QuestionActivity.this, "No More Question!", Toast.LENGTH_SHORT).show();
//            }
//        }


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
                putOnQuestionAnswered(model.getId());
            }
        });
        answerTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                answer = model.getOptionTwo();
                answerValidationDialog(answer, model);
                putOnQuestionAnswered(model.getId());
            }
        });

        answerThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                answer = model.getOptionThree();
                answerValidationDialog(answer, model);
                putOnQuestionAnswered(model.getId());
            }
        });

        answerFour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                answer = model.getOptionFour();
                answerValidationDialog(answer, model);
                putOnQuestionAnswered(model.getId());
            }
        });

    }

    private void putOnQuestionAnswered(String id) {
        String UID = UUID.randomUUID().toString();
        AnsweredQuestionModel model = new AnsweredQuestionModel(UID, id);
        Constant.answeredQuestionRef.child(userAuth.getCurrentUser().getUid()).child(UID).setValue(model);
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
        TextView rightAnswer = dialog.findViewById(R.id.rightAnswer);

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
            //userRemainingPoints = userRemainingPoints + 5;
            userRemainingPoints = userRemainingPoints + Constant.rightAnswerPoint;
            rightOrWrongAnswer.setText("Right Answer!");
            rightAnswer.setVisibility(View.GONE);
        } else {
            rightAnswer.setVisibility(View.VISIBLE);
            rightAnswer.setText("Right Answer: " + model.getAnswer());
            icon.setImageResource(R.drawable.cross_icon_red);
            rightOrWrongAnswer.setText("Wrong Answer!");
            userRemainingPoints = userRemainingPoints - Constant.wrongAnswerPoint;
            //userRemainingPoints = userRemainingPoints - 5;
            Constant.userRef.orderByChild("userId").equalTo(model.getUploaderId()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        int uploaderPoints = 0;
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            UserModel model = dataSnapshot.getValue(UserModel.class);
                            //uploaderPoints = 5 + Integer.parseInt(model.getUserPoints());
                            uploaderPoints = Constant.wrongAnswerPoint + Integer.parseInt(model.getUserPoints());
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