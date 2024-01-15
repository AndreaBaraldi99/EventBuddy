package it.lanos.eventbuddy.data.source.firebase.bucket;

import static it.lanos.eventbuddy.util.Constants.PROFILE_PICTURES_BUCKET_REFERENCE;

import android.app.Application;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;

import androidx.appcompat.content.res.AppCompatResources;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.Objects;

import it.lanos.eventbuddy.R;
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
        uploadTask.continueWithTask(task -> {
            if (!task.isSuccessful()) {
                throw Objects.requireNonNull(task.getException());
            }
            // Continue with the task to get the download URL
            return imageRef.getDownloadUrl();
        }).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Uri downloadUri = task.getResult();
                user.setProfilePictureUrl(downloadUri.toString());
                userCallback.onImageUploaded(user);
            }
        });
    }


}
