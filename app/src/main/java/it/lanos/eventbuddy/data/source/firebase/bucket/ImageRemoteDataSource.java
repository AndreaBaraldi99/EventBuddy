package it.lanos.eventbuddy.data.source.firebase.bucket;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import it.lanos.eventbuddy.data.source.models.User;

public class ImageRemoteDataSource extends BaseImageRemoteDataSource{
    private StorageReference storageReference;

    public ImageRemoteDataSource() {
        storageReference = FirebaseStorage.getInstance().getReference().child("images");
    }

    @Override
    public void uploadImage(User user, byte[] image) {
        StorageReference imageRef = storageReference.child(user.getUserId());
        UploadTask uploadTask = imageRef.putBytes(image);
        uploadTask.addOnSuccessListener(taskSnapshot -> {
            userCallback.onImageUploaded("Upload Success");
        }).addOnFailureListener(e -> {
            userCallback.onImageUploadFailed(e);
        });
    }

}
