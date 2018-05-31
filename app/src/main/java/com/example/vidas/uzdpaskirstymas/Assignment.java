package com.example.vidas.uzdpaskirstymas;

import java.util.ArrayList;

public class Assignment {

    private int id;
    private String group;
    private String names;
    private String assignment;
    private String endDate;
    private String assignDate;
    private String admComment;
    private String comment;
    private int progress;

    Assignment() {
    }

    public Assignment(int id, String group,String names, String assignment, String endDate, String assignDate,
                      String admComment, String comment, int progress) {
        this.id = id;
        this.group = group;
        this.names = names;
        this.assignment = assignment;
        this.endDate = endDate;
        this.assignDate = assignDate;
        this.admComment = admComment;
        this.comment = comment;
        this.progress = progress;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    String getGroup() {
        return group;
    }

    void setGroup(String group) {
        this.group = group;
    }

    String getNames() {
        return names;
    }

    void setNames(String names) {
        this.names = names;
    }

    public String getAssignment() {
        return assignment;
    }

    public void setAssignment(String assignment) {
        this.assignment = assignment;
    }

    String getEndDate() {
        return endDate;
    }

    void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    String getAssignDate() {
        return assignDate;
    }

    void setAssignDate(String assignDate) {
        this.assignDate = assignDate;
    }

    String getAdmComment() {
        return admComment;
    }

    void setAdmComment(String admComment) {
        this.admComment = admComment;
    }

    String getComment() {
        return comment;
    }

    void setComment(String comment) {
        this.comment = comment;
    }

    int getProgress() {
        return progress;
    }

    void setProgress(int progress) {
        this.progress = progress;
    }

    @Override
    public String toString() {
        return "Assignment{" +
                "id=" + id +
                ", group='" + group + '\'' +
                ", names='" + names + '\'' +
                ", assignment='" + assignment + '\'' +
                ", endDate='" + endDate + '\'' +
                ", assignDate='" + assignDate + '\'' +
                ", admComment='" + admComment + '\'' +
                ", comment='" + comment + '\'' +
                ", progress=" + progress + "/100" +
                '}';
    }
}
