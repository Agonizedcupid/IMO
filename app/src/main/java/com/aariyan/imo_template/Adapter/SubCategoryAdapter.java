package com.aariyan.imo_template.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.aariyan.imo_template.Activity.QuestionActivity;
import com.aariyan.imo_template.Constant.Constant;
import com.aariyan.imo_template.Fragment.QuestionFragment;
import com.aariyan.imo_template.Fragment.QuizSubCategoryFragment;
import com.aariyan.imo_template.Model.SubCategoryModel;
import com.aariyan.imo_template.R;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.List;

public class SubCategoryAdapter extends RecyclerView.Adapter<SubCategoryAdapter.ViewHolder> {

    private Context context;
    private List<SubCategoryModel> list;
    private Activity activity;
    public SubCategoryAdapter(Context context,List<SubCategoryModel>list, Activity activity) {
        this.context = context;
        this.list = list;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.single_category_layout,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SubCategoryModel model = list.get(position);
        holder.name.setText(model.getSubCategoryName());


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, QuestionActivity.class);
                intent.putExtra("id",model.getId());
                context.startActivity(intent);

//                AppCompatActivity activity = (AppCompatActivity) view.getContext();
//                Fragment fragment = new QuestionFragment();
//
//                Bundle args = new Bundle();
//                args.putString("id", model.getId());
//                fragment.setArguments(args);
//
//                activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainers, fragment).addToBackStack(null).commit();

            }
        });
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView name;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.categoryName);
        }
    }
}
