package com.example.vidas.uzdpaskirstymas;


import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AssignmentAdapter extends RecyclerView.Adapter<AssignmentAdapter.MyViewHolder> {

    private static ClickListener clickListener = null;

    private Context context;
    private LayoutInflater inflater;
    private List<Assignment> assignment = Collections.emptyList();
    private Assignment currentAssignment;
    private User user;
    ProgressBar progressBar;

    @Override
    public AssignmentAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.assignment_items, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(AssignmentAdapter.MyViewHolder vh, int position) {
        currentAssignment = assignment.get(position);
        vh.tvGroup.setText(currentAssignment.getGroup());
        vh.tvAssignment.setText("Užduotis: " + currentAssignment.getAssignment());
        vh.tvAssignedTo.setText("Kam priskirta: " + currentAssignment.getNames());
        vh.progressBar.setProgress(currentAssignment.getProgress());

        if(currentAssignment.getProgress() == 100) {
            vh.progressBar.setProgressTintList(ColorStateList.valueOf(Color.GREEN));
            vh.imgDone.setVisibility(View.VISIBLE);
        }else{
            vh.imgDone.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return assignment.size();
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    // susieti su esamu langu ir perduoti filmu sarasa is DB
    AssignmentAdapter(Context context, ArrayList<Assignment> assignments, User user) {
        this.context= context;
        this.assignment = assignments;
        this.user = user;
        inflater = LayoutInflater.from(context);
    }

    public interface ClickListener {
        void onItemClick(int position, View v);
    }

    void setFilter(ArrayList<Assignment> list){
        assignment = new ArrayList<>();
        assignment.addAll(list);
        notifyDataSetChanged();
    }

    void setOnItemClickListener(ClickListener clickListener){
        AssignmentAdapter.clickListener = clickListener;
    }


    class MyViewHolder extends RecyclerView.ViewHolder{

        private TextView tvAssignment, tvAssignedTo, tvGroup;
        private ProgressBar progressBar;
        private CardView cardView;
        private ImageView imgDone;


        MyViewHolder(View itemView) {
            super(itemView);
            tvAssignment = itemView.findViewById(R.id.tvAssigment);
            tvAssignedTo = itemView.findViewById(R.id.tvAssignedTo);
            tvGroup = itemView.findViewById(R.id.tvGroup);
            progressBar = itemView.findViewById(R.id.progressBar);
            cardView = itemView.findViewById(R.id.cardView);
            imgDone = itemView.findViewById(R.id.ivDone);

            cardView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    if(clickListener !=null){
                        clickListener.onItemClick(getAdapterPosition(),v);
                        int itemPos = getAdapterPosition();

                        int assignmentId = assignment.get(itemPos).getId();
                        String assignmentName = assignment.get(itemPos).getAssignment();
                        String assignmentGroup = assignment.get(itemPos).getGroup();
                        String assignmentEndDate = assignment.get(itemPos).getEndDate();
                        String assignmentAssignDate = assignment.get(itemPos).getAssignDate();
                        int progress = assignment.get(itemPos).getProgress();
                        String admComment = assignment.get(itemPos).getAdmComment();
                        String comment = assignment.get(itemPos).getComment();
                        String names = assignment.get(itemPos).getNames();
                        int userlevel = user.getUserlevel();
                        String username = user.getUsername();

                        Intent intent;
                        if(userlevel == 9) {
                            intent = new Intent(context, AdminAssignmentActivity.class);
                        }else{
                            intent = new Intent(context, AssignmentActivity.class);
                        }
                        intent.putExtra("id", assignmentId);
                        intent.putExtra("name", assignmentName);
                        intent.putExtra("group", assignmentGroup);
                        intent.putExtra("assignmentEndDate", assignmentEndDate);
                        intent.putExtra("assignmentDate", assignmentAssignDate);
                        intent.putExtra("progress", progress);
                        intent.putExtra("admComment", admComment);
                        intent.putExtra("comment", comment);
                        intent.putExtra("names", names);
                        intent.putExtra("userlevel", userlevel);
                        intent.putExtra("username", username);

                        context.startActivity(intent);
                    }
                }
            });
        }
    }
}
