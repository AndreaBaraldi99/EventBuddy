package it.lanos.eventbuddy.data.source.firebase.cloudDB;

public class UsersCloudResponse {
    private String fullname;
    private String uid;
    private String username;
    private boolean isInvited;

    public UsersCloudResponse() {}

    public String getFullname() {
        return fullname;
    }

    public String getUid() {
        return uid;
    }

    public String getUsername() {
        return username;
    }

    public boolean getIsInvited() {
        return isInvited;
    }

    public void setIsInvited(boolean isInvited) {
        this.isInvited = isInvited;
    }
}
