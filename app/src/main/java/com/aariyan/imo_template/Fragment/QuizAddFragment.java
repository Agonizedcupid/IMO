package com.aariyan.imo_template.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.aariyan.imo_template.Model.CategoryModel;
import com.aariyan.imo_template.Model.QuestionModel;
import com.aariyan.imo_template.Model.SubCategoryModel;
import com.aariyan.imo_template.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class QuizAddFragment extends Fragment {

    private View root;
    private EditText questionBox, optionOneBox, optionTwoBox, optionThreeBox, optionFourBox;
    private RadioButton optionOne, optionTwo, optionThree, optionFour;
    private Button addQuestionBtn;

    private Spinner spinner, subCategorySpinner;
    private EditText categoryNameBox, subCategoryNameBox;
    private DatabaseReference categoryRef, subCategoryRef,questionRef;
    private List<CategoryModel> list = new ArrayList<>();
    private List<SubCategoryModel> subCategoryList = new ArrayList<>();
    ArrayAdapter<CategoryModel> dataAdapter;
    ArrayAdapter<SubCategoryModel> dataAdapter2;

    private static String categorySpinnerItemSelection = "";
    private static String categorySpinnerItemId = "";

    private static String subCategorySpinnerItemSelection = "";
    private static String subCategorySpinnerItemId = "";
    private static String subCategorySpinnerItemParent = "";

    private static String selectedOption = "";

    private ProgressBar progressBar;
    private FirebaseAuth userAuth;

    public QuizAddFragment() {
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
        root = inflater.inflate(R.layout.fragment_quiz_add, container, false);
        userAuth = FirebaseAuth.getInstance();
        categoryRef = FirebaseDatabase.getInstance().getReference().child("QUIZ").child("Category");
        subCategoryRef = FirebaseDatabase.getInstance().getReference().child("QUIZ").child("SubCategory");
        questionRef = FirebaseDatabase.getInstance().getReference().child("QUIZ").child("Questions");
        initUI();
        loadCategorySpinner();
        return root;
    }

    private void loadCategorySpinner() {
        categoryRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    list.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        CategoryModel model = dataSnapshot.getValue(CategoryModel.class);
                        list.add(model);
                    }
                    progressBar.setVisibility(View.GONE);
                    dataAdapter = new ArrayAdapter<CategoryModel>(requireContext(),
                            android.R.layout.simple_spinner_item, list);
                    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner.setAdapter(dataAdapter);
                    spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            CategoryModel model = list.get(position);
                            categorySpinnerItemSelection = model.getCategoryName();
                            if (model.getCategoryName().equals("New")) {
                                categoryNameBox.setVisibility(View.VISIBLE);
                                loadSubCategory("NAI");
                                categorySpinnerItemId = "0";
                            } else {
                                loadSubCategory(model.getId());
                                categorySpinnerItemId = model.getId();
                                categoryNameBox.setVisibility(View.GONE);
                                progressBar.setVisibility(View.VISIBLE);
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });

                } else {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(requireContext(), "No Category Found!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void loadSubCategory(String id) {
        subCategoryRef.orderByChild("parentId").equalTo(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    subCategorySpinner.setVisibility(View.VISIBLE);
                    subCategoryNameBox.setVisibility(View.GONE);
                    subCategoryList.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        SubCategoryModel model = dataSnapshot.getValue(SubCategoryModel.class);
                        subCategoryList.add(model);
                    }
                    progressBar.setVisibility(View.GONE);
                    dataAdapter2 = new ArrayAdapter<SubCategoryModel>(requireContext(),
                            android.R.layout.simple_spinner_item, subCategoryList);
                    dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    subCategorySpinner.setAdapter(dataAdapter2);
                    subCategorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            SubCategoryModel model = subCategoryList.get(position);
                            subCategorySpinnerItemId = model.getId();
                            subCategorySpinnerItemParent = model.getParentId();
                            //Toast.makeText(requireContext(), "" + model.getSubCategoryName(), Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });

                } else {
                    subCategorySpinnerItemId = "0";
                    progressBar.setVisibility(View.GONE);
                    subCategoryNameBox.setVisibility(View.VISIBLE);
                    subCategorySpinner.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void initUI() {
        questionBox = root.findViewById(R.id.addQuestionBox);
        optionOneBox = root.findViewById(R.id.optionOneBox);
        optionTwoBox = root.findViewById(R.id.optionTwoBox);
        optionThreeBox = root.findViewById(R.id.optionThreeBox);
        optionFourBox = root.findViewById(R.id.optionFourBox);
        optionOne = root.findViewById(R.id.optionOne);
        optionTwo = root.findViewById(R.id.optionTwo);
        optionThree = root.findViewById(R.id.optionThree);
        optionFour = root.findViewById(R.id.optionFour);

        spinner = root.findViewById(R.id.spinner);
        categoryNameBox = root.findViewById(R.id.categoryNameBox);

        subCategorySpinner = root.findViewById(R.id.subCategorySpinner);
        subCategoryNameBox = root.findViewById(R.id.subCategoryNameBox);

        progressBar = root.findViewById(R.id.progressbar);

        addQuestionBtn = root.findViewById(R.id.addQuestionBtn);
        addQuestionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                validation();
            }
        });

    }

    private void validation() {
        //checking category spinner items:
        String newCategoryId,subcategoryId = "";
        if (categorySpinnerItemSelection.equals("New") && categorySpinnerItemId.equals("0")) {
            if (!TextUtils.isEmpty(categoryNameBox.getText().toString())) {
                newCategoryId = generatingId();
                CategoryModel model = new CategoryModel(newCategoryId, categoryNameBox.getText().toString());
                categoryRef.child(newCategoryId).setValue(model);
            } else {
                categoryNameBox.setError("Enter somethings!");
                categoryNameBox.requestFocus();
                return;
            }
        } else {
            newCategoryId = categorySpinnerItemId;
        }

        //checking the subCategory;
        if (subCategorySpinnerItemId.equals("0")) {
            if (!TextUtils.isEmpty(subCategoryNameBox.getText().toString())) {
                String id = generatingId();
                subcategoryId = id;
                SubCategoryModel model = new SubCategoryModel(id, newCategoryId, subCategoryNameBox.getText().toString());
                subCategoryRef.child(id).setValue(model);
            }
        } else {
            subcategoryId = subCategorySpinnerItemId;
        }

        //Upload Question Section:
        if (TextUtils.isEmpty(questionBox.getText().toString())) {
            questionBox.setError("Enter Question");
            questionBox.requestFocus();
            progressBar.setVisibility(View.GONE);
            return;
        }

        if (TextUtils.isEmpty(optionOneBox.getText().toString())) {
            optionOneBox.setError("Enter Option");
            optionOneBox.requestFocus();
            progressBar.setVisibility(View.GONE);
            return;
        }

        if (TextUtils.isEmpty(optionTwoBox.getText().toString())) {
            optionTwoBox.setError("Enter Option");
            optionTwoBox.requestFocus();
            progressBar.setVisibility(View.GONE);
            return;
        }

        if (TextUtils.isEmpty(optionThreeBox.getText().toString())) {
            optionThreeBox.setError("Enter Option");
            optionThreeBox.requestFocus();
            progressBar.setVisibility(View.GONE);
            return;
        }
        if (TextUtils.isEmpty(optionFourBox.getText().toString())) {
            optionFourBox.setError("Enter Option");
            optionFourBox.requestFocus();
            progressBar.setVisibility(View.GONE);
            return;
        }

        optionOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                optionOne.setChecked(true);
                optionTwo.setChecked(false);
                optionThree.setChecked(false);
                optionFour.setChecked(false);

                selectedOption = optionOneBox.getText().toString();
            }
        });

        optionTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                optionOne.setChecked(false);
                optionTwo.setChecked(true);
                optionThree.setChecked(false);
                optionFour.setChecked(false);

                selectedOption = optionTwoBox.getText().toString();
            }
        });

        optionThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                optionOne.setChecked(false);
                optionTwo.setChecked(false);
                optionThree.setChecked(true);
                optionFour.setChecked(false);

                selectedOption = optionThreeBox.getText().toString();
            }
        });

        optionFour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                optionOne.setChecked(false);
                optionTwo.setChecked(false);
                optionThree.setChecked(false);
                optionFour.setChecked(true);

                selectedOption = optionFourBox.getText().toString();
            }
        });

        if (selectedOption.equals("")) {
            Toast.makeText(requireContext(), "Please select a correct answer!", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
            return;
        }

        String questionId = generatingId();
        QuestionModel model = new QuestionModel(
                questionId,
                "",
                subcategoryId,
                questionBox.getText().toString(),
                optionOneBox.getText().toString(),
                optionTwoBox.getText().toString(),
                optionThreeBox.getText().toString(),
                optionFourBox.getText().toString(),
                selectedOption,
                userAuth.getCurrentUser().getUid()
        );

        questionRef.child(questionId).setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(requireContext(), "Question Uploaded!", Toast.LENGTH_SHORT).show();
                questionBox.setText("", TextView.BufferType.EDITABLE);
                optionOne.setChecked(false);
                optionTwo.setChecked(false);
                optionFour.setChecked(false);
                optionThree.setChecked(false);
                optionOneBox.setText("", TextView.BufferType.EDITABLE);
                optionTwoBox.setText("", TextView.BufferType.EDITABLE);
                optionThreeBox.setText("", TextView.BufferType.EDITABLE);
                optionFourBox.setText("", TextView.BufferType.EDITABLE);
            }
        });

        progressBar.setVisibility(View.GONE);
    }

    //generate category id:
    private String generatingId() {
        return UUID.randomUUID().toString() + "_N_" + System.currentTimeMillis();
    }
}