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
    private final Application application;

    public ImageRemoteDataSource(Application application) {
        storageReference = FirebaseStorage.getInstance().getReference().child(PROFILE_PICTURES_BUCKET_REFERENCE);
        this.application = application;
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
                userCallback.onSuccessFromFirebase(user);
            }
        });
    }

    @Override
    public void downloadImage(String userID) {
        StorageReference imageRef = storageReference.child(userID);
        imageRef.getBytes(Long.MAX_VALUE).addOnSuccessListener(bytes -> {
            if(bytes.length != 0) {
                userCallback.onImageDownloaded(bytes);
            }else{
                Bitmap bitmap = ((BitmapDrawable) Objects.requireNonNull(AppCompatResources.getDrawable(application, R.drawable.logo))).getBitmap();
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                byte[] bitmapdata = stream.toByteArray();
                userCallback.onImageDownloaded(bitmapdata);
            }
        }).addOnFailureListener(e -> userCallback.onImageDownloadFailed(e));
    }

}
