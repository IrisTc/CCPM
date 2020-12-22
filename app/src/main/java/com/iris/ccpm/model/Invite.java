package com.iris.ccpm.model;

public class Invite {
    private String inviteName;
    private Integer invite_uid;
    private Integer targetState;
    private Integer inviteUID;
    private String project_uid;
    private Integer targetUID;
    private String inviteContent;
    private String projectName;
    private String inviteNickName;

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getInviteNickName() {
        return inviteNickName;
    }

    public void setInviteNickName(String inviteNickName) {
        this.inviteNickName = inviteNickName;
    }

    public String getInviteContent() {
        return inviteContent;
    }

    public void setInviteContent(String inviteContent) {
        this.inviteContent = inviteContent;
    }

    public String getInviteName() {
        return inviteName;
    }

    public void setInviteName(String inviteName) {
        this.inviteName = inviteName;
    }

    public Integer getInvite_uid() {
        return invite_uid;
    }

    public void setInvite_uid(Integer invite_uid) {
        this.invite_uid = invite_uid;
    }

    public Integer getTargetState() {
        return targetState;
    }

    public void setTargetState(Integer targetState) {
        this.targetState = targetState;
    }

    public Integer getInviteUID() {
        return inviteUID;
    }

    public void setInviteUID(Integer inviteUID) {
        this.inviteUID = inviteUID;
    }

    public String getProject_uid() {
        return project_uid;
    }

    public void setProject_uid(String project_uid) {
        this.project_uid = project_uid;
    }

    public Integer getTargetUID() {
        return targetUID;
    }

    public void setTargetUID(Integer targetUID) {
        this.targetUID = targetUID;
    }
}
