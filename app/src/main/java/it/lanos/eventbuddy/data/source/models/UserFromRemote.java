package it.lanos.eventbuddy.data.source.models;

import java.util.List;

public class UserFromRemote{
    private String userId;
    private String username;
    private String fullName;
    private List<String> friends;
    private String profilePictureUrl;

    public UserFromRemote() {}

    public UserFromRemote(String userId, String username, String fullName, List<String> friends, String profileImage) {
        this.userId = userId;
        this.username = username;
        this.fullName = fullName;
        this.friends = friends;
        this.profilePictureUrl = profileImage;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public List<String> getFriends() {
        return friends;
    }

    public void setFriends(List<String> friends) {
        this.friends = friends;
    }

    public String getProfilePictureUrl() {
        return profilePictureUrl;
    }

    public void setProfilePictureUrl(String profilePictureUrl) {
        this.profilePictureUrl = profilePictureUrl;
    }

}
