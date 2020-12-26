package com.iris.ccpm.model;

public class Apply {
    private String apply_uid;
    private String project_uid;
    private Integer applyAccountUID;
    private Integer targetManagerState;
    private String applyAccountNickName;
    private String projectName;

    public String getApplyAccountNickName() {
        return applyAccountNickName;
    }

    public void setApplyAccountNickName(String applyAccountNickName) {
        this.applyAccountNickName = applyAccountNickName;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getApply_uid() {
        return apply_uid;
    }

    public void setApply_uid(String apply_uid) {
        this.apply_uid = apply_uid;
    }

    public String getProject_uid() {
        return project_uid;
    }

    public void setProject_uid(String project_uid) {
        this.project_uid = project_uid;
    }

    public Integer getApplyAccountUID() {
        return applyAccountUID;
    }

    public void setApplyAccountUID(Integer applyAccountUID) {
        this.applyAccountUID = applyAccountUID;
    }

    public Integer getTargetManagerState() {
        return targetManagerState;
    }

    public void setTargetManagerState(Integer targetManagerState) {
        this.targetManagerState = targetManagerState;
    }
}
