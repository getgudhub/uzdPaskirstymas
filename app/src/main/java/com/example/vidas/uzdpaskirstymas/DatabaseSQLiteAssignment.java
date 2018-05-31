package com.example.vidas.uzdpaskirstymas;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DatabaseSQLiteAssignment extends SQLiteOpenHelper{
    private static final int DATABASE_VERSION   = 2;
    private static final String DATABASE_NAME   = "db";

    private static final String USERNAME            = "username";
    private static final String TABLE_ASSIGNMENTS   = "assignments";
    private static final String ASSIGNMENT_ID       = "id";
    private static final String ASSIGNMENT_NAME     = "assignment";
    private static final String ASSIGNMENT_GROUP    = "groupname";
    private static final String ASSIGNED_TO         = "names";
    private static final String ASSIGNMENT_DUE      = "duedate";
    private static final String ASSIGNMENT_DATE     = "startdate";
    private static final String ASSIGNMENT_ACOMMENT = "admcomment";
    private static final String ASSIGNMENT_COMMENT  = "comment";
    private static final String ASSIGNMENT_PROGRESS = "progress";

    String CREATE_ASSIGNMENTS_TABLE = "CREATE TABLE IF NOT EXISTS "+
            TABLE_ASSIGNMENTS
            + "("
            + ASSIGNMENT_ID         + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + ASSIGNMENT_GROUP      + " TEXT,"
            + ASSIGNED_TO           + " TEXT,"
            + ASSIGNMENT_NAME       + " TEXT,"
            + ASSIGNMENT_DUE        + " TEXT,"
            + ASSIGNMENT_DATE       + " TEXT,"
            + ASSIGNMENT_ACOMMENT   + " TEXT,"
            + ASSIGNMENT_COMMENT    + " TEXT,"
            + ASSIGNMENT_PROGRESS   + " INTEGER"
            + ")";


    public DatabaseSQLiteAssignment(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS "+ TABLE_ASSIGNMENTS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_ASSIGNMENTS);
        onCreate(db);
    }

    public void addAssignment(Assignment assignment){
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("INSERT INTO " + TABLE_ASSIGNMENTS + " " +
                "(" + ASSIGNMENT_GROUP + ", " + ASSIGNED_TO + ", " + ASSIGNMENT_NAME + ", " +
                ASSIGNMENT_DUE + ", " + ASSIGNMENT_DATE+ ", " + ASSIGNMENT_ACOMMENT + ") " +
                "VALUES ('"+ assignment.getGroup() +"', '"+ assignment.getNames() +"', '"+ assignment.getAssignment() +
                "', '"+ assignment.getEndDate() +"', '"+ assignment.getAssignDate() +"', '"+ assignment.getAdmComment() + "')" ) ;

        // Closing database connection
        db.close();
    }

    public boolean deleteAssignment(int id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_ASSIGNMENTS, ASSIGNMENT_ID + "=" + id, null) > 0;
    }

    public int updateAssignment(Assignment assignment){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ASSIGNMENT_GROUP,     assignment.getGroup());
        values.put(ASSIGNED_TO,          assignment.getNames());
        values.put(ASSIGNMENT_NAME,      assignment.getAssignment());
        values.put(ASSIGNMENT_DUE,       assignment.getEndDate());
        values.put(ASSIGNMENT_DATE,      assignment.getAssignDate());
        values.put(ASSIGNMENT_ACOMMENT,  assignment.getAdmComment());
        values.put(ASSIGNMENT_COMMENT,   assignment.getComment());
        values.put(ASSIGNMENT_PROGRESS,  assignment.getProgress());

        int i = db.update(TABLE_ASSIGNMENTS, values,
                ASSIGNMENT_ID + " = ?",
                new String[] {String.valueOf(assignment.getId())});


        db.close();
        return i;
    }

    public ArrayList<Assignment> getAllAssignments() {
        ArrayList<Assignment> assignments = new ArrayList<Assignment>();

        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(CREATE_ASSIGNMENTS_TABLE);

        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_ASSIGNMENTS;

        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Assignment assignment = new Assignment();
                assignment.setId(cursor.getInt(0));
                assignment.setGroup(cursor.getString(1));
                assignment.setNames(cursor.getString(2));
                assignment.setAssignment(cursor.getString(3));
                assignment.setEndDate(cursor.getString(4));
                assignment.setAssignDate(cursor.getString(5));
                assignment.setAdmComment(cursor.getString(6));
                assignment.setComment(cursor.getString(7));
                assignment.setProgress(cursor.getInt(8));

                assignments.add(assignment);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return assignments;
    }

    public ArrayList<Assignment> getUserAssignments(String userfullname) {
        ArrayList<Assignment> films = new ArrayList<Assignment>();

        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(CREATE_ASSIGNMENTS_TABLE);

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_ASSIGNMENTS + " WHERE "+ ASSIGNED_TO +" LIKE '%" + userfullname + "%'", null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Assignment assignment = new Assignment();

                assignment.setId(cursor.getInt(0));
                assignment.setGroup(cursor.getString(1));
                assignment.setNames(cursor.getString(2));
                assignment.setAssignment(cursor.getString(3));
                assignment.setEndDate(cursor.getString(4));
                assignment.setAssignDate(cursor.getString(5));
                assignment.setAdmComment(cursor.getString(6));
                assignment.setComment(cursor.getString(7));
                assignment.setProgress(cursor.getInt(8));

                films.add(assignment);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return films;
    }

}
