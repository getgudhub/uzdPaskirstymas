package com.example.vidas.uzdpaskirstymas;


import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AdminAssignmentActivity extends FragmentActivity {

    Button btnAdd, btnUpdate, btnDelete, btnClean, btnIncrease, btnDecrease;
    DatabaseSQLiteAssignment dba;
    DatabaseSQLiteUser dbu;

    int userlevel, assignmentId, progress, newProgress;
    String username, assignmentName, assignmentGroup, assignmentEndDate,
            assignmentAssignDate, admComment, comment, names;

    boolean pridejimas = false;
    RelativeLayout rlDate;
    LinearLayout llProgress;
    TextView tvComment, tvAssignDate;
    EditText etAssignTo, etAssignment, etAdmComment, etGroup;
    @SuppressLint("StaticFieldLeak")
    static EditText etEndDate;
    ProgressBar progressBar;
    Animation btnProgressAnim;
    Spinner spinner;
    Assignment assignment;
    ArrayList<String> users;
    ArrayList<String> assignedToList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_assignment);

        progressBar =   findViewById(R.id.progressBar);
        etGroup =       findViewById(R.id.etGroup);
        etAssignTo =    findViewById(R.id.assignmentTo);
        etAssignment =  findViewById(R.id.assignment);
        etAdmComment =  findViewById(R.id.admComment);
        tvComment =     findViewById(R.id.comment);
        etEndDate =     findViewById(R.id.dptill);
        tvAssignDate =  findViewById(R.id.tvSince);

        btnAdd =        findViewById(R.id.btnAdd);
        btnDelete =     findViewById(R.id.btnDelete);
        btnUpdate =     findViewById(R.id.btnUpdate);
        btnClean =      findViewById(R.id.btnClean);
        btnIncrease =   findViewById(R.id.btnIncrease);
        btnDecrease =   findViewById(R.id.btnDecrease);

        rlDate = findViewById(R.id.rlNuoKada);
        llProgress = findViewById(R.id.llProgress);
        spinner = findViewById(R.id.spin);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            userlevel = bundle.getInt("userlevel");
            username = bundle.getString("username");
            pridejimas = bundle.getBoolean("pridejimas");
            if(pridejimas){
                btnDelete.setVisibility(View.GONE);
                btnUpdate.setVisibility(View.GONE);
                rlDate.setVisibility(View.GONE);
                llProgress.setVisibility(View.GONE);
                assignedToList = new ArrayList<String>();
            }else{
                btnAdd.setVisibility(View.GONE);
                btnDelete.setVisibility(View.VISIBLE);
                assignmentId = bundle.getInt("id");
                assignmentName = bundle.getString("name");
                assignmentGroup= bundle.getString("group");
                admComment = bundle.getString("admComment");
                comment = bundle.getString("comment");
                names = bundle.getString("names");
                assignmentEndDate = bundle.getString("assignmentEndDate");
                assignmentAssignDate = bundle.getString("assignmentDate");
                assignmentId = bundle.getInt("id");
                progress = bundle.getInt("progress");

                progressBar.setProgress(progress);
                etGroup.setText(assignmentGroup);
                etAssignment.setText(assignmentName);
                etAdmComment.setText(admComment);
                tvComment.setText(comment);
                etEndDate.setText(assignmentEndDate);
                tvAssignDate.setText(assignmentAssignDate);

                List<String> assignList = new ArrayList<String> (Arrays.asList(names.split(",")));
                String temporary ="";
                for(int i=0; i<assignList.size(); i++) {
                    if(Validation.isValidFullName(assignList.get(i)))
                    temporary += assignList.get(i)+"\n";
                }
                etAssignTo.setText(temporary);
                assignedToList = new ArrayList<String>(assignList);
            }
        }
        dbu = new DatabaseSQLiteUser(this);

        users = dbu.getAllUsers();
        dbu.close();
        final ArrayAdapter<String> adapter = new ArrayAdapter(this, R.layout.spinner_text, users);
        adapter.setDropDownViewResource(R.layout.sspinner_dropdown);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!etAssignTo.getText().toString().contains(spinner.getSelectedItem().toString())) {
                    etAssignTo.setText(etAssignTo.getText().toString() + spinner.getSelectedItem().toString() + "\n");
                    assignedToList.add(spinner.getSelectedItem().toString());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        btnClean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etAssignTo.setText("");
                assignedToList.clear();
                adapter.notifyDataSetChanged();
                spinner.setSelection(0);
            }
        });

        etEndDate.setShowSoftInputOnFocus(false);
        etEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTruitonDatePickerDialog(v);
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

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if(validAssignment()) {
                        assignment = createAssignment();
                        dba = new DatabaseSQLiteAssignment(AdminAssignmentActivity.this);
                        dba.addAssignment(assignment);
                        dba.close();

                        Intent intentAdd = new Intent(AdminAssignmentActivity.this, TableActivity.class);
                        intentAdd.putExtra("userlevel", userlevel);
                        startActivity(intentAdd);
                        AdminAssignmentActivity.this.finish();
                    }
                } catch (ParseException e) {
                    Log.e("ParseExc", "Failed to parse date");
                }
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                assignment = updatingAssignment();
                dba = new DatabaseSQLiteAssignment(AdminAssignmentActivity.this);

                dba.updateAssignment(assignment);
                dba.close();

                Intent intentUp = new Intent(AdminAssignmentActivity.this, TableActivity.class);
                intentUp.putExtra("userlevel", userlevel);
                intentUp.putExtra("username", username);
                startActivity(intentUp);
                AdminAssignmentActivity.this.finish();
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dba = new DatabaseSQLiteAssignment(AdminAssignmentActivity.this);
                dba.deleteAssignment(assignmentId);
                dba.close();

                Toast.makeText(AdminAssignmentActivity.this, "Užduotis pašalinta", Toast.LENGTH_SHORT).show();

                Intent goBack = new Intent(AdminAssignmentActivity.this, TableActivity.class);
                goBack.putExtra("userlevel", userlevel);
                goBack.putExtra("username", username);
                startActivity(goBack);
                AdminAssignmentActivity.this.finish();
            }
        });
    }

    public static class DatePickerFragment extends DialogFragment implements
            DatePickerDialog.OnDateSetListener {

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user
                etEndDate.setText(day + "/" + (month + 1) + "/" + year);
        }
    }

    public void showTruitonDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    @Override
    public void onBackPressed(){
        Intent goBack = new Intent(AdminAssignmentActivity.this, TableActivity.class);
        goBack.putExtra("userlevel", userlevel);
        goBack.putExtra("username", username);
        startActivity(goBack);
        AdminAssignmentActivity.this.finish();
    }

    public Assignment createAssignment(){
        Assignment assignment = new Assignment();
        String names ="";

        for(int i=0;i<assignedToList.size();i++){
            if(assignedToList.get(i) != ""){
                names += assignedToList.get(i) + ", ";
            }
        }

        assignment.setGroup(etGroup.getText().toString());
        assignment.setAssignment(etAssignment.getText().toString());
        assignment.setNames(names);
        assignment.setEndDate(etEndDate.getText().toString());
        assignment.setAssignDate(getDate());
        assignment.setAdmComment(etAdmComment.getText().toString());

        return assignment;
    }

    public Assignment updatingAssignment(){
        Assignment assignment = new Assignment();
        String names ="";
        int viensArDaugiau = 1;

        for(int i=0;i<assignedToList.size();i++){
            if(Validation.isValidFullName(assignedToList.get(i))){
                if(viensArDaugiau==1){  //nes vienam kablelio nereikia
                    names += assignedToList.get(i);
                    viensArDaugiau ++;
                }else {
                    names += ", " + assignedToList.get(i);
                }
            }
        }

        assignment.setId(assignmentId);
        assignment.setGroup(etGroup.getText().toString());
        assignment.setAssignment(etAssignment.getText().toString());
        assignment.setNames(names);
        assignment.setEndDate(etEndDate.getText().toString());
        assignment.setAssignDate(tvAssignDate.getText().toString());
        assignment.setAdmComment(etAdmComment.getText().toString());
        assignment.setProgress(progressBar.getProgress());

        return assignment;
    }

    public boolean validAssignment() throws ParseException {
        dbu = new DatabaseSQLiteUser(this);
        for(int i=0; i<assignedToList.size(); i++){
            if (!dbu.checkFullName(assignedToList.get(i))) {
                etAssignTo.requestFocus();
                etAssignTo.setError("Nerastas asmuo");
                return false;
            }
        }dbu.close();
        if(etGroup.getText().toString().isEmpty() || !Validation.isValidGroup(etGroup.getText().toString())){
            etGroup.requestFocus();
            etGroup.setError("Klaida grupės laukelyje");
            return false;
        }else if(etAssignment.getText().toString().isEmpty() || !Validation.isValidAssignment(etAssignment.getText().toString())){
            etAssignment.requestFocus();
            etAssignment.setError("Klaida užduoties pavadinimo laukelyje");
            return false;
        }else if(etEndDate.getText().toString().isEmpty() || !Validation.isValidDate(etEndDate.getText().toString()) ||
                !compareDates() ){
            etEndDate.requestFocus();
            etEndDate.setError("Klaida datoje. Iki kada numatyta turi būti bent diena toliau nei " + getDate());
            return false;
        }else {
            return true;
        }

    }
    public String getDate(){
        String date = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
        return date;
    }
    public static boolean compareDates()  {
        int checkIfTrue = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String newDate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
        try{
            Date assignDate = sdf.parse(newDate);
            Date endDate = sdf.parse(etEndDate.getText().toString());
            if(endDate.compareTo(assignDate)>0){
                checkIfTrue = 1; //assignDate is before endDate
            }else {
                checkIfTrue = 0;
            }
        }catch(ParseException e){
            Log.e("ParseExc", "Failed to parse date");
        }
        return checkIfTrue == 1;
    }

    public void setProgressBarColor(){
        if(newProgress == 100){
            progressBar.setProgressTintList(ColorStateList.valueOf(Color.GREEN));
        }else if(newProgress < 100){
            progressBar.setProgressTintList(ColorStateList.valueOf(Color.YELLOW));
        }
    }

}
