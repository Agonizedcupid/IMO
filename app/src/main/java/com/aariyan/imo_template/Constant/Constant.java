package com.aariyan.imo_template.Constant;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Constant {
    public static DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("QUIZ").child("User");

    public static DatabaseReference categoryRef = FirebaseDatabase.getInstance().getReference().child("QUIZ").child("Category");
    public static DatabaseReference subCategoryRef = FirebaseDatabase.getInstance().getReference().child("QUIZ").child("SubCategory");
    public static DatabaseReference questionRef = FirebaseDatabase.getInstance().getReference().child("QUIZ").child("Questions");

}
