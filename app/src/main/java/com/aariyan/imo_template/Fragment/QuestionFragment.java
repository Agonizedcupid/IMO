package com.aariyan.imo_template.Fragment;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.aariyan.imo_template.Constant.Constant;
import com.aariyan.imo_template.Model.QuestionModel;
import com.aariyan.imo_template.QuizActivity;
import com.aariyan.imo_template.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class QuestionFragment extends Fragment {

    private TextView questionBox;
    private RadioButton answerOne, answerTwo, answerThree, answerFour;
    private Button nextBtn,closeBtn;
    private ImageView answerIcon;
    private TextView rightAnswer;

    private LinearLayout rightAnswerLayout;

    private View root;

    private int position = 0;
    private String id = "";
    private static List<QuestionModel> questionList = new ArrayList<>();
    private static String answer = "";

    public QuestionFragment() {
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
        root = inflater.inflate(R.layout.fragment_question, container, false);

        assert getArguments() != null;
        id = getArguments().getString("id");

        initUI();
        loadQuestion(id);
        return root;
    }

    private void loadQuestion(String id) {
        Constant.questionRef.orderByChild("subCategoryId").equalTo(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    questionList.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        QuestionModel model = dataSnapshot.getValue(QuestionModel.class);
                        questionList.add(model);
                    }
                    loadSingleQuestion();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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

        answerOne.setChecked(false);
        answerTwo.setChecked(false);
        answerThree.setChecked(false);
        answerFour.setChecked(false);

        answerOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                answer = model.getOptionOne();
            }
        });
        answerTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                answer = model.getOptionTwo();
            }
        });

        answerThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                answer = model.getOptionThree();
            }
        });

        answerFour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                answer = model.getOptionFour();
            }
        });

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextBtn.setEnabled(false);
                openDialog();
            }
        });

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                rightAnswerLayout.setVisibility(View.VISIBLE);
                rightAnswer.setText(model.getAnswer());
                answerIcon.setVisibility(View.VISIBLE);
                rightAnswerLayout.setVisibility(View.VISIBLE);
                if (model.getAnswer().equals(answer)) {
                    answerIcon.setImageResource(R.drawable.rigt_tik_icon);
                } else {
                    answerIcon.setImageResource(R.drawable.cross_icon_red);
                }

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            answerIcon.setVisibility(View.GONE);
                            rightAnswerLayout.setVisibility(View.GONE);
                            position++;
                            if (position <= questionList.size()-1) {
                                loadSingleQuestion();
                            } else {
                                nextBtn.setEnabled(false);
                            }

                        }
                    }, 2000);
            }
        });
    }

    private void openDialog() {

        Dialog dialog = new Dialog(requireContext());
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setContentView(R.layout.dialog_layout);
        dialog.setCancelable(false);

        TextView okBtn = dialog.findViewById(R.id.okBtn);
        TextView cancelBtn = dialog.findViewById(R.id.cancelBtn);
        TextView totalQuestion = dialog.findViewById(R.id.totalAnsweredQuestion);

        totalQuestion.setText("You have answered "+position+" questions!");

        cancelBtn.setOnClickListener(v -> {
            nextBtn.setEnabled(true);
            dialog.dismiss();
        });

        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(requireContext(), QuizActivity.class));
                requireActivity().finish();
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void initUI() {
        questionBox = root.findViewById(R.id.questionText);
        answerOne = root.findViewById(R.id.answerOne);
        answerTwo = root.findViewById(R.id.answerTwo);
        answerThree = root.findViewById(R.id.answerThree);
        answerFour = root.findViewById(R.id.answerFour);
        nextBtn = root.findViewById(R.id.nextBtn);
        answerIcon = root.findViewById(R.id.answerIcon);
        rightAnswer = root.findViewById(R.id.rightAnswerText);
        rightAnswerLayout = root.findViewById(R.id.rightAnswerLayout);
        closeBtn = root.findViewById(R.id.closeQuizBtn);
    }
}