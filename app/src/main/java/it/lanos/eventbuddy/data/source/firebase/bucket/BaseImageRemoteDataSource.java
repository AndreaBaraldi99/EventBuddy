package it.lanos.eventbuddy.data.source.firebase.bucket;

import it.lanos.eventbuddy.data.source.UserCallback;
import it.lanos.eventbuddy.data.source.models.User;

public abstract class BaseImageRemoteDataSource {
    protected UserCallback userCallback;
    public void setUserCallback(UserCallback userCallback) {
        this.userCallback = userCallback;
    }
    public abstract void uploadImage(User user, byte[] image);
    public abstract void downloadImage(String userID);
}
