package com.example.vidas.uzdpaskirstymas;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

public class AssignmentActivity extends AppCompatActivity {

    Button btnUpdate, btnIncrease, btnDecrease;
    DatabaseSQLiteAssignment db;

    int userlevel, assignmentId, progress, newProgress;
    String username, assignmentName, assignmentGroup, assignmentEndDate,
            assignmentAssignDate, admComment, comment, names;

    TextView tvAssignmentTo, tvAssignment, tvAssignDate, tvDueDate, tvAdmComment,tvGroup;
    EditText etComment;
    ProgressBar progressBar;
    Animation btnProgressAnim;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignment);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            userlevel = bundle.getInt("userlevel");
            username = bundle.getString("username");
            assignmentName = bundle.getString("name");
            assignmentGroup= bundle.getString("group");
            admComment = bundle.getString("admComment");
            comment = bundle.getString("comment");
            names = bundle.getString("names");
            assignmentEndDate = bundle.getString("assignmentEndDate");
            assignmentAssignDate = bundle.getString("assignmentDate");
            assignmentId = bundle.getInt("id");
            progress = bundle.getInt("progress");

            btnUpdate =      findViewById(R.id.btnUpdate);
            btnIncrease =    findViewById(R.id.btnIncrease);
            btnDecrease =    findViewById(R.id.btnDecrease);
            tvGroup =        findViewById(R.id.tvGroup);
            tvAssignmentTo = findViewById(R.id.assignmentTo);
            tvAssignment =   findViewById(R.id.assignment);
            tvAssignDate =   findViewById(R.id.tvSince);
            tvDueDate =      findViewById(R.id.dptill);
            tvAdmComment =   findViewById(R.id.admComment);
            etComment =      findViewById(R.id.comment);
            progressBar =    findViewById(R.id.progressBar);

            tvAssignment.setText(assignmentName);
            tvGroup.setText(assignmentGroup);
            tvAssignmentTo.setText(names);
            etComment.setText(comment);
            tvAdmComment.setText(admComment);
            tvDueDate.setText(assignmentEndDate);
            tvAssignDate.setText(assignmentAssignDate);
            progressBar.setProgress(progress);
        }

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db = new DatabaseSQLiteAssignment(AssignmentActivity.this);
                db.updateAssignment(updateAssignment());

                Intent intentUp = new Intent(AssignmentActivity.this, TableActivity.class);
                intentUp.putExtra("userlevel", userlevel);
                intentUp.putExtra("username", username);
                startActivity(intentUp);
                AssignmentActivity.this.finish();
                db.close();
            }
        });

        newProgress = progress;
        btnProgressAnim = AnimationUtils.loadAnimation(this, R.anim.btnanimation);
        setProgressBarColor();

        btnIncrease.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                v.startAnimation(btnProgressAnim);
                ObjectAnimator animation = ObjectAnimator.ofInt(progressBar, "progress", newProgress, newProgress+25);
                animation.setDuration(400);
                if(newProgress <= 75){
                    newProgress += 25;
                    progressBar.setProgress(newProgress);
                }
                setProgressBarColor();
                animation.start();
            }
        });

        btnDecrease.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                v.startAnimation(btnProgressAnim);
                ObjectAnimator animation = ObjectAnimator.ofInt(progressBar, "progress", newProgress, newProgress-25);
                animation.setDuration(400);
                if(newProgress >= 25){
                    newProgress -= 25;
                    progressBar.setProgress(newProgress);
                }
                setProgressBarColor();
                animation.start();
            }
        });

    }

    public Assignment updateAssignment(){
        Assignment assignment = new Assignment();
        assignment.setId(assignmentId);
        assignment.setAssignment(assignmentName);
        assignment.setGroup(assignmentGroup);
        assignment.setAssignDate(assignmentAssignDate);
        assignment.setEndDate(assignmentEndDate);
        assignment.setAdmComment(admComment);
        assignment.setNames(names);
        assignment.setComment(etComment.getText().toString());
        assignment.setProgress(progress);

        return assignment;
    }

    @Override
    public void onBackPressed(){
        Intent goBack = new Intent(AssignmentActivity.this, TableActivity.class);
        goBack.putExtra("userlevel", userlevel);
        goBack.putExtra("username", username);
        startActivity(goBack);
        AssignmentActivity.this.finish();
    }

    public void setProgressBarColor(){
        if(newProgress == 100){
            progressBar.setProgressTintList(ColorStateList.valueOf(Color.GREEN));
        }else if(newProgress < 100){
            progressBar.setProgressTintList(ColorStateList.valueOf(Color.YELLOW));
        }
    }
}
