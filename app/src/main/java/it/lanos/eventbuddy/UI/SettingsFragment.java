package it.lanos.eventbuddy.UI;

import static it.lanos.eventbuddy.util.Constants.ENCRYPTED_DATA_FILE_NAME;
import static it.lanos.eventbuddy.util.Constants.PROFILE_PICTURES_BUCKET_REFERENCE;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;

import java.io.FileNotFoundException;
import java.io.InputStream;

import it.lanos.eventbuddy.R;
import it.lanos.eventbuddy.UI.authentication.UserViewModel;
import it.lanos.eventbuddy.UI.authentication.UserViewModelFactory;
import it.lanos.eventbuddy.data.IUserRepository;
import it.lanos.eventbuddy.data.source.models.Result;
import it.lanos.eventbuddy.data.source.models.User;
import it.lanos.eventbuddy.util.DataEncryptionUtil;
import it.lanos.eventbuddy.util.ServiceLocator;

public class SettingsFragment extends Fragment {
    private ActivityResultLauncher<Intent> imagePickerLauncher;
    private User user;
    private UserViewModel userViewModel;
    private Button nicknameTextButton;
    private ImageView userImage;

    public SettingsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ViewCompat.setOnApplyWindowInsetsListener(view, (v, windowInsets) -> {
            Insets insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars());
            // Apply the insets as a margin to the view. This solution sets only the
            // bottom, left, and right dimensions, but you can apply whichever insets are
            // appropriate to your layout. You can also update the view padding if that's
            // more appropriate.
            ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
            mlp.leftMargin = insets.left;
            mlp.topMargin = insets.top;
            mlp.rightMargin = insets.right;
            v.setLayoutParams(mlp);

            // Return CONSUMED if you don't want want the window insets to keep passing
            // down to descendant views.
            return WindowInsetsCompat.CONSUMED;

        });

        // Initialize the ViewModel
        IUserRepository userRepository =
                ServiceLocator.getInstance().getUserRepository(requireActivity().getApplication());

        userViewModel = new ViewModelProvider(
                this,
                new UserViewModelFactory(userRepository)).get(UserViewModel.class);

        //Download user profile pic and set it into the imageview
        userImage = view.findViewById(R.id.profilePic);
        downloadUserImage();

        // The user selects a new image from the gallery
        userImage.setOnClickListener(view1 -> {
            setUserImage();
        });
        initializeImagePickerLauncher();

        // Handle the button that sets user nickname
        nicknameTextButton = view.findViewById(R.id.nicknameTextButton);
        setUserNickname();
        nicknameTextButton.setOnClickListener(view1 -> changeNicknameAlert());

        // Navigate the user to Account Settings
        MaterialCardView accountConstraint = view.findViewById(R.id.card_account);
        accountConstraint.setOnClickListener(v ->
                Navigation.findNavController(v).navigate(R.id.action_settingsFragment_to_accountSettingsActivity));

        // Navigate the user to Preference Settings
        MaterialCardView preferencesConstraint = view.findViewById(R.id.card_preferences);
        preferencesConstraint.setOnClickListener(v ->
                Navigation.findNavController(v).navigate(R.id.action_settingsFragment_to_preferencesSettingsActivity));

        // Navigate the user to notification settings
        MaterialCardView notificationsConstraint = view.findViewById(R.id.card_notifications);
        notificationsConstraint.setOnClickListener(v -> {
            // Intent to open the specific App Info page:
            Intent intent = new Intent();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                // Android Oreo (8.0) and above versions
                intent.setAction(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
                intent.putExtra(Settings.EXTRA_APP_PACKAGE, v.getContext().getPackageName());
            } else {
                // Android Lollipop (5.0) - Android Nougat (7.1)
                intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
                intent.putExtra("app_package", v.getContext().getPackageName());
                intent.putExtra("app_uid", v.getContext().getApplicationInfo().uid);
            }

            try {
                v.getContext().startActivity(intent);
            } catch (ActivityNotFoundException e) {
                e.printStackTrace();
            }
        });

        // Navigate the user to permission settings
        MaterialCardView permissionsConstraint = view.findViewById(R.id.card_privacy);
        permissionsConstraint.setOnClickListener(v -> {
            // Intent to open the App Info page of your app
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);

            // Create a Uri from the package name of your app
            Uri uri = Uri.fromParts("package", v.getContext().getPackageName(), null);
            intent.setData(uri);

            try {
                v.getContext().startActivity(intent);
            } catch (ActivityNotFoundException e) {
                e.printStackTrace();
            }
        });
    }

    private void readUser(DataEncryptionUtil dataEncryptionUtil){
        try {
            this.user = new Gson().fromJson(dataEncryptionUtil.readSecretDataOnFile(ENCRYPTED_DATA_FILE_NAME), User.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Set the user nickname into the text-field
    private void setUserNickname() {
        readUser(new DataEncryptionUtil(requireActivity().getApplication()));
        String nickname = user.getUsername();
        nicknameTextButton.setText(nickname);
    }

    private void changeNicknameAlert() {
        Context context = requireContext();
        final EditText input = new EditText(context);
        new AlertDialog.Builder(context)
                .setTitle(getString(R.string.change_nickname))
                .setMessage(getString(R.string.insert_new_nickname))
                .setView(input)
                .setPositiveButton(getString(R.string.confirm_text), (dialog, whichButton) -> {
                    String newNickname = input.getText().toString();
                    if(!newNickname.isEmpty()) {
                        changeNickname(newNickname);
                    } else {
                        Snackbar snackbar = Snackbar.make(
                                requireView(),
                                getString(R.string.nickname_not_empty),
                                Snackbar.LENGTH_SHORT);
                        snackbar.show();
                    }
                })
                .setNegativeButton(getString(R.string.cancel_text), (dialog, whichButton) -> dialog.cancel())
                .show();
    }

    private void changeNickname(@NonNull String newNickname) {
        // TODO: 07/01/2024 changeUsername ritorna un live data
        userViewModel.changeUsername(newNickname);
        readUser(new DataEncryptionUtil(requireActivity().getApplication()));
        user.setUsername(newNickname);
        setUserNickname();
    }

    // Download the user image from Cloud Storage and set it into imageview
    private void downloadUserImage() {
        readUser(new DataEncryptionUtil(requireActivity().getApplication()));

        StorageReference storageReference = FirebaseStorage.getInstance().getReference()
                .child(PROFILE_PICTURES_BUCKET_REFERENCE).child(user.getUserId());

        Glide.with(requireContext())
                .load(storageReference)
                .into(userImage);
    }

    // The user selects a profile pic in the gallery
    private void setUserImage() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        imagePickerLauncher.launch(intent);
    }

    private void initializeImagePickerLauncher() {
        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null && data.getData() != null) {
                            Uri selectedImageUri = data.getData();
                            uploadUserImage(selectedImageUri);
                        }
                    }
                }
        );
    }

    // Upload user image to database
    private void uploadUserImage(Uri imageUri) {
        try {
            InputStream inputStream = requireContext().getContentResolver().openInputStream(imageUri);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            userViewModel.uploadProfileImage(bitmap).observe(getViewLifecycleOwner(), result -> {
                if(result.isSuccess()) {
                    downloadUserImage();
                } else {
                    Snackbar snackbar = Snackbar.make(
                            requireView(),
                            "Failed",
                            Snackbar.LENGTH_SHORT);
                    snackbar.show();
                }
            });
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}