package com.aariyan.imo_template;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class CallActivity extends AppCompatActivity {

    String name = "";
    private TextView userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call);

        userName = findViewById(R.id.userName);

        if (getIntent() != null) {
            name = getIntent().getStringExtra("name");
            userName.setText(name);
        }

        findViewById(R.id.declineCall).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CallActivity.this,MainActivity.class));
                finish();
            }
        });
    }
}