package com.example.vidas.uzdpaskirstymas;


import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.ArrayList;

public class TableActivity extends AppCompatActivity implements SearchView.OnQueryTextListener{

    DatabaseSQLiteAssignment dba;
    DatabaseSQLiteUser dbUser;

    RecyclerView mRecyclerView;
    SearchView searchView;
    RecyclerView.LayoutManager layoutManager;
    private ArrayList<Assignment> assignmentList;
    private AssignmentAdapter adapter;

    Button btnPrideti, btnDaugiau, btnPerziureti;
    Toolbar toolbar;
    User user;
    String username ;
    int userlevel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table);

        mRecyclerView = findViewById(R.id.assignment_list);
        mRecyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(TableActivity.this);
        mRecyclerView.setLayoutManager(layoutManager);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.search_label);
        setSupportActionBar(toolbar);

        btnPrideti = findViewById(R.id.btnPrideti);
        btnDaugiau = findViewById(R.id.btnDaugiau);
        btnPerziureti = findViewById(R.id.btnPerziuretSenesnius);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if(bundle !=null){
            userlevel = bundle.getInt("userlevel");
            username = bundle.getString("username");
        }

        dba = new DatabaseSQLiteAssignment(TableActivity.this);
        dbUser = new DatabaseSQLiteUser(TableActivity.this);

        user = dbUser.getUser(username);
        if(userlevel == 9) {
            assignmentList = dba.getAllAssignments();
            adapter = new AssignmentAdapter(this, assignmentList, user);
            mRecyclerView.setAdapter(adapter);
            btnDaugiau.setVisibility(View.VISIBLE);
            btnPrideti.setVisibility(View.VISIBLE);
        }else{
            assignmentList = dba.getUserAssignments(user.getFullNameForRegister());
            btnPerziureti.setVisibility(View.VISIBLE);
            adapter = new AssignmentAdapter(this, assignmentList, user);
            mRecyclerView.setAdapter(adapter);
        }
        dba.close();
        dbUser.close();

        adapter.setOnItemClickListener(new AssignmentAdapter.ClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                    TableActivity.this.finish();
            }
        });


        btnPrideti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TableActivity.this, AdminAssignmentActivity.class);
                intent.putExtra("userlevel", userlevel);
                intent.putExtra("pridejimas", true);
                intent.putExtra("username", username);
                startActivity(intent);
                TableActivity.this.finish();
            }
        });

        btnDaugiau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TableActivity.this, OptionsActivity.class);
                intent.putExtra("userlevel", userlevel);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        newText = newText.toLowerCase();
        ArrayList<Assignment> newList = new ArrayList<>();
        for(Assignment film : assignmentList){
            String group = film.getGroup().toLowerCase();
            if(group.contains(newText)) newList.add(film);
        }
        adapter.setFilter(newList);
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.assignment_table_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.actionSearch);

        SearchManager sm = (SearchManager) TableActivity.this.getSystemService(Context.SEARCH_SERVICE);
        if(searchItem != null){
            searchView = (SearchView) searchItem.getActionView();
            searchView.setOnQueryTextListener(TableActivity.this);
        }
        return true;
    }

    @Override
    public void onBackPressed(){
        Intent goBack = new Intent(TableActivity.this, MainActivity.class);
        startActivity(goBack);
        TableActivity.this.finish();
    }
}
