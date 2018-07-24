package com.example.vidas.uzdpaskirstymas;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Space;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AssignmentActivity extends AppCompatActivity {

    DatabaseSQLiteAssignment db;

    private LinearLayout commentsLayout;
    Boolean commented = false;
    String commentText, userFullName;
    List<String> assignList;
    List<String> commentsList;
    List<String> adminCommentList;

    int userlevel, assignmentId, progress, newProgress;
    String username, assignmentName, assignmentGroup, assignmentEndDate,
            assignmentAssignDate, admComment, comment, names;
    Button btnUpdate, btnIncrease, btnDecrease, btnComment;
    TextView tvAssignmentTo, tvAssignment, tvAssignDate, tvDueDate, tvAdminName, tvAdmComment, tvGroup;
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

            commentsLayout = findViewById(R.id.commentsLayout);
            btnUpdate =      findViewById(R.id.btnUpdate);
            btnIncrease =    findViewById(R.id.btnIncrease);
            btnDecrease =    findViewById(R.id.btnDecrease);
            btnComment =     findViewById(R.id.btnComment);
            tvGroup =        findViewById(R.id.tvGroup);
            tvAssignmentTo = findViewById(R.id.assignmentTo);
            tvAssignment =   findViewById(R.id.assignment);
            tvAssignDate =   findViewById(R.id.tvSince);
            tvDueDate =      findViewById(R.id.dptill);
            tvAdminName =     findViewById(R.id.admName);
            tvAdmComment =   findViewById(R.id.admComment);
            progressBar =    findViewById(R.id.progressBar);

            getAndSetUserFullName();
            if(comment!=null) {
                settingUpComments();
            }
            if(admComment!=null){
                adminCommentList = new ArrayList<String> (Arrays.asList(admComment.split("~@")));
                tvAdminName.setText(adminCommentList.get(0));
                tvAdmComment.setText(adminCommentList.get(1));
            }

            tvAssignment.setText(assignmentName);
            tvGroup.setText(assignmentGroup);
            tvAssignmentTo.setText(names);
            tvDueDate.setText(assignmentEndDate);
            tvAssignDate.setText(assignmentAssignDate);
            progressBar.setProgress(progress);

        }

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

        if(comment == null || !commented){
            btnComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!commented) {
                        getUserCommentForEditTextView("");  // sukuriamas komentaro laukelis
                        btnComment.setTextColor(ContextCompat.getColor(AssignmentActivity.this, R.color.colorRed));
                        btnComment.setText("Ištrinti komentarą");
                        commented = true;
                    }
                }
            });
        }

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
        assignment.setProgress(progressBar.getProgress());

        if(commentsList != null) {  // komentaru saraso nera jei nera komentaru, tad reik patikrinti
            int index = 0;
            for(int i=0; i< commentsList.size(); i++){
                if(commentsList.get(index +1).equals("")) {
                    i+=2;   //persokti tuscia komentara ir visai istrinti laukeli kitam prisijungimui
                }else{
                    commentText += commentsList.get(i) + "~@";
                    i++;
                    commentText += commentsList.get(i) + "~@";  //isskirimui pasrinkti simboliai ~@
                }
            }
        }

        assignment.setComment(commentText);

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

    protected void getUserCommentForEditTextView( String comment) {
        final TextView tvc = new TextView(this);
        tvc.setText(userFullName);
        tvc.setGravity(View.TEXT_ALIGNMENT_CENTER);
        tvc.setTextColor(ContextCompat.getColor(this, R.color.colorGrey));
        commentsLayout.addView(tvc);

        final EditText etc = new EditText(this);
        etc.setText(comment);
        etc.setMinLines(1);
        etc.setMaxLines(3);
        commentsLayout.addView(etc);

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commentText = userFullName + "~@" + etc.getText().toString() + "~@";
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

        btnComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (commented) {
                    commentsLayout.removeView(tvc);
                    commentsLayout.removeView(etc);
                    commented = false;
                    btnComment.setTextColor(ContextCompat.getColor(AssignmentActivity.this, R.color.colorGreen));
                    btnComment.setText("Pridėti komentarą");
                } else if (!commented) {
                    getUserCommentForEditTextView("");
                    etc.requestFocus();
                    btnComment.setTextColor(ContextCompat.getColor(AssignmentActivity.this, R.color.colorRed));
                    btnComment.setText("Ištrinti komentarą");
                    commented = true;
                }
            }
        });

    }

    protected void getOthersCommentsForEditTextView(String name, String comment) {
        final TextView tvc = new TextView(this);
        tvc.setText(name);
        tvc.setGravity(View.TEXT_ALIGNMENT_CENTER);
        tvc.setTextColor(ContextCompat.getColor(this, R.color.colorGrey));
        commentsLayout.addView(tvc);

        final TextView tvComment = new TextView(this);
        tvComment.setText(comment);
        tvComment.setTextSize(18);
        commentsLayout.addView(tvComment);

    }

    public void settingUpComments(){
        int commentAt = 0;
        commentsList = new ArrayList<String>(Arrays.asList(comment.split("~@")));   //komentarai
        assignList = new ArrayList<String>(Arrays.asList(names.split(", ")));      //priskirti zmones
        ArrayList oldCommentsList = new ArrayList<String>(commentsList);                 //kopijuojamas Array, nes ankstesnis bus redaguojamas
        //kiek vartotoju priskirtu prie uzduoties; how many are assigned to the assignment
        for(int i=0; i<assignList.size(); i++) {
            //sarasas, kuriame isrenkami vartotojai ir ju komentarai; list of comment owners and the comments
            for(int j=0; j<oldCommentsList.size(); j=j+2){
                //kai komentaras yra, bet ne esancio vartotojo; when comment exists but it's not the users
                if(!assignList.get(i).equals(userFullName) && !oldCommentsList.get(j).equals(userFullName)){
                    getOthersCommentsForEditTextView(assignList.get(i), commentsList.get(j+1));
                    oldCommentsList.remove(j);
                    oldCommentsList.remove(j);
                //vartotojo komentarui
                }else if(assignList.get(i).equals(userFullName) && oldCommentsList.get(j).equals(userFullName)){
                    commented = true;
                    commentAt = j + 1;
                    btnComment.setTextColor(ContextCompat.getColor(this, R.color.colorRed));
                    btnComment.setText("Ištrinti komentarą");
                    getUserCommentForEditTextView(commentsList.get(commentAt));
                }
            }
        }
        //Ištrinamas vartotojo komentaras iš sąrašo, nes jis bus visuomet per naują patikrinamas
        if(commented) {
            commentsList.remove(commentAt);
            commentsList.remove(commentAt -1); //delete user from list
        }
    }

    //randamas, priskiriamas dabartinio vartotojo vardas

    private void getAndSetUserFullName(){
        DatabaseSQLiteUser dbu = new DatabaseSQLiteUser(this);
        userFullName = dbu.getFullName(username);
        dbu.close();
    }
}
