package it.lanos.eventbuddy.data.source.firebase.bucket;

import static it.lanos.eventbuddy.util.Constants.ON_UPLOAD_SUCCESS;
import static it.lanos.eventbuddy.util.Constants.PROFILE_PICTURES_BUCKET_REFERENCE;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import it.lanos.eventbuddy.data.source.models.User;

public class ImageRemoteDataSource extends BaseImageRemoteDataSource{
    private final StorageReference storageReference;

    public ImageRemoteDataSource() {
        storageReference = FirebaseStorage.getInstance().getReference().child(PROFILE_PICTURES_BUCKET_REFERENCE);
    }

    @Override
    public void uploadImage(User user, byte[] image) {
        StorageReference imageRef = storageReference.child(user.getUserId());
        UploadTask uploadTask = imageRef.putBytes(image);
        uploadTask.addOnSuccessListener(taskSnapshot -> {
            userCallback.onImageUploaded(ON_UPLOAD_SUCCESS);
        }).addOnFailureListener(e -> {
            userCallback.onImageUploadFailed(e);
        });
    }

    @Override
    public void downloadImage(String userID) {
        StorageReference imageRef = storageReference.child(userID);
        imageRef.getBytes(Long.MAX_VALUE).addOnSuccessListener(bytes -> {
            userCallback.onImageDownloaded(bytes);
        }).addOnFailureListener(e -> {
            userCallback.onImageDownloadFailed(e);
        });
    }

}
