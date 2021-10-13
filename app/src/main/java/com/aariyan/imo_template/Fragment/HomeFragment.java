package com.aariyan.imo_template.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aariyan.imo_template.Adapter.ListAdapter;
import com.aariyan.imo_template.Model.LisModel;
import com.aariyan.imo_template.QuizActivity;
import com.aariyan.imo_template.R;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {

    private List<LisModel> userList = new ArrayList<>();
    private RecyclerView recyclerView;

    String[] name = {"Abul","Babul","Kabul","Dabul","Chabul","Ebul","Fibul","Mabul","Nabul","Tabul"};
    String[] message = {"is back on imo!"};
    String[] time =  {"2:49 PM","Yesterday", "11 Jun", "5 Jul", "27 Feb", "22 Feb", "31 Jan","15 Jan", "5 Jan", "31 Dec"};

    private CardView quizCard;

    private View root;

    public HomeFragment() {
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
        root = inflater.inflate(R.layout.fragment_home, container, false);
        initUI();
        return root;
    }

    private void initUI() {

        quizCard = root.findViewById(R.id.quizCard);
        quizCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(requireContext(), QuizActivity.class));
            }
        });

        recyclerView = root.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        populateData();
    }

    private void populateData() {

        userList.clear();

        for (int i = 0; i<10; i++)  {
            userList.add(new LisModel(
                    name[i],
                    message[0],
                    time[i]
            ));
        }

        ListAdapter adapter = new ListAdapter(requireContext(),userList);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}