package com.aariyan.imo_template;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.aariyan.imo_template.Adapter.ChatAdapter;
import com.aariyan.imo_template.Model.MessageModel;

import java.util.ArrayList;
import java.util.List;

public class MessagingActivity extends AppCompatActivity {

    private String name;
    private TextView userName;

    String md = "The quick, brown fox jumps over a lazy dog. DJs flock by when MTV ax quiz prog. Junk MTV quiz graced by fox whelps. Bawds jog, flick quartz, vex nymphs. Waltz, bad nymph, for quick jigs";

    private RecyclerView recyclerView;

    List<MessageModel> list = new ArrayList<>();
    MessageModel model;

    private int[] types = {0,1,0,1,0,1,0,1};
    private String[] receiverM = {md,md,md,md,md,md,md,md};
    private String[] times = {"01.0 PM","02.0 PM","03.0 PM","04.0 PM","05.0 PM","06.0 PM","07.0 PM","08.0 PM",};



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messaging);

        userName = findViewById(R.id.userNameInMessagingActivity);

        if (getIntent() != null) {
            name = getIntent().getStringExtra("name");
            userName.setText(name);
        }
        initUI();
        loadMessage();
    }

    private void initUI() {
        recyclerView = findViewById(R.id.chatRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //go to the parent:
        findViewById(R.id.backBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void loadMessage() {
        list.clear();

        for (int i=0; i<types.length; i++) {
            model = new MessageModel(
                    i,
                    types[i],
                    ""+receiverM[i],
                    ""+receiverM[i],
                    ""+times[i],
                    ""+times[i]
            );

            list.add(model);
        }

        ChatAdapter adapter = new ChatAdapter(MessagingActivity.this,list);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}