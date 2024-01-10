package it.lanos.eventbuddy.data.source.firebase.bucket;

import static it.lanos.eventbuddy.util.Constants.ON_UPLOAD_SUCCESS;
import static it.lanos.eventbuddy.util.Constants.PROFILE_PICTURES_BUCKET_REFERENCE;

import android.app.Application;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;

import androidx.appcompat.content.res.AppCompatResources;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

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
            if(bytes.length != 0) {
                userCallback.onImageDownloaded(bytes);
            }else{
                Bitmap bitmap = ((BitmapDrawable)AppCompatResources.getDrawable(application, R.drawable.logo)).getBitmap();
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                byte[] bitmapdata = stream.toByteArray();
                userCallback.onImageDownloaded(bitmapdata);
            }
        }).addOnFailureListener(e -> {
            userCallback.onImageDownloadFailed(e);
        });
    }

}
