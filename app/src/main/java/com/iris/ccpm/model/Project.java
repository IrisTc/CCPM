package com.iris.ccpm.model;

import java.io.Serializable;

public class Project implements Serializable {
    private Integer project_uid;
    private String projectName;
    private Integer manager_uid;
    private String projectSynopsis;
    private String projectPlan;
    private Integer projectRate;
    private String projectStartTime;
    private String projectEndTime;

    public Integer getProject_uid() {
        return project_uid;
    }

    public void setProject_uid(Integer project_uid) {
        this.project_uid = project_uid;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public Integer getManager_uid() {
        return manager_uid;
    }

    public void setManager_uid(Integer manager_uid) {
        this.manager_uid = manager_uid;
    }

    public String getProjectSynopsis() {
        return projectSynopsis;
    }

    public void setProjectSynopsis(String projectSynopsis) {
        this.projectSynopsis = projectSynopsis;
    }

    public String getProjectPlan() {
        return projectPlan;
    }

    public void setProjectPlan(String projectPlan) {
        this.projectPlan = projectPlan;
    }

    public Integer getProjectRate() {
        return projectRate;
    }

    public void setProjectRate(Integer projectRate) {
        this.projectRate = projectRate;
    }

    public String getProjectStartTime() {
        return projectStartTime;
    }

    public void setProjectStartTime(String projectStartTime) {
        this.projectStartTime = projectStartTime;
    }

    public String getProjectEndTime() {
        return projectEndTime;
    }

    public void setProjectEndTime(String projectEndTime) {
        this.projectEndTime = projectEndTime;
    }
}
