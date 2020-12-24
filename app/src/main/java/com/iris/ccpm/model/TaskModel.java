package com.iris.ccpm.model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class TaskModel implements Serializable {
    private int claimState;
    private String claimNickName;
    private int claim_uid;
    private String project_uid;
    private int taskEmergent;
    private String taskEndTime;
    private String taskName;
    private String taskPredictHours;
    private String taskRestHours;
    private String taskStartTime;
    private int taskState;
    private String taskSynopsis;
    private int task_uid;

    public TaskModel(){
        Date date=new Date();
        Calendar calendar = new GregorianCalendar();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        taskStartTime = formatter.format(date);
        calendar.setTime(date);
        calendar.add(calendar.DATE,1);
        date=calendar.getTime();
        taskEndTime = formatter.format(date);
        System.out.println("taskDate:"+formatter.format(date));
        claimState=2;
        claim_uid=0;
        project_uid="0";
        taskEmergent=0;
        taskName="taskName";
        taskPredictHours="0";
        taskRestHours="0";
        taskState=0;
        taskSynopsis="taskSynopsis";
        task_uid=0;
    }

    public String getClaimNickName() {
        return claimNickName;
    }

    public void setClaimNickName(String claimNickName) {
        this.claimNickName = claimNickName;
    }

    public int getClaimState() {
        return claimState;
    }

    public void setClaimState(int claimState) {
        this.claimState = claimState;
    }

    public int getClaim_uid() {
        return claim_uid;
    }

    public void setClaim_uid(int claim_uid) {
        this.claim_uid = claim_uid;
    }

    public String getProject_uid() {
        return project_uid;
    }

    public void setProject_uid(String project_uid) {
        this.project_uid = project_uid;
    }

    public int getTaskEmergent() {
        return taskEmergent;
    }

    public void setTaskEmergent(int taskEmergent) {
        this.taskEmergent = taskEmergent;
    }

    public String getTaskEndTime() {
        return taskEndTime;
    }

    public void setTaskEndTime(String taskEndTime) {
        this.taskEndTime = taskEndTime;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskPredictHours() {
        return taskPredictHours;
    }

    public void setTaskPredictHours(String taskPredictHours) {
        this.taskPredictHours = taskPredictHours;
    }

    public String getTaskRestHours() {
        return taskRestHours;
    }

    public void setTaskRestHours(String taskRestHours) {
        this.taskRestHours = taskRestHours;
    }

    public String getTaskStartTime() {
        return taskStartTime;
    }

    public void setTaskStartTime(String taskStartTime) {
        this.taskStartTime = taskStartTime;
    }

    public int getTaskState() {
        return taskState;
    }

    public void setTaskState(int taskState) {
        this.taskState = taskState;
    }

    public String getTaskSynopsis() {
        return taskSynopsis;
    }

    public void setTaskSynopsis(String taskSynopsis) {
        this.taskSynopsis = taskSynopsis;
    }

    public int getTask_uid() {
        return task_uid;
    }

    public void setTask_uid(int task_uid) {
        this.task_uid = task_uid;
    }
}
