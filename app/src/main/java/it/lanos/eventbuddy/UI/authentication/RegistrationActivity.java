package it.lanos.eventbuddy.UI.authentication;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

import it.lanos.eventbuddy.R;
import it.lanos.eventbuddy.UI.BottomNavigationBarActivity;
import it.lanos.eventbuddy.data.source.models.Result;

public class RegistrationActivity extends AppCompatActivity {
    TextInputLayout nameTextInputLayout, nicknameTextInputLayout,
            emailTextInputLayout, passwordTextInputLayout;
    Button registration_button, login_textButton;
    UserViewModel userViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        // Navigate the user to welcome activity when back button is pressed
        UserHelper.setupBackButtonHandling(this, WelcomeActivity.class);

        // Find views by ID
        setViewsUp();

        // Set required listeners for the text fields
        setTextFieldsListeners();

        // Initialize the ViewModel
        userViewModel = UserHelper.initializeAndGetViewModel(this);

        // Handle the registration button press
        handleRegistrationButton();

        // Navigate the user to the LoginActivity
        navigateToLoginScreen();
    }

    // Find views by ID
    private void setViewsUp() {
        nameTextInputLayout = findViewById(R.id.nameTextInputLayout);
        nicknameTextInputLayout = findViewById(R.id.nicknameTextInputLayout);
        emailTextInputLayout = findViewById(R.id.emailTextInputLayout);
        passwordTextInputLayout = findViewById(R.id.passwordTextInputLayout);

        registration_button = findViewById(R.id.registration_button);
        login_textButton = findViewById(R.id.login_TextButton);
    }

    // Set listeners for the end icons of text fields
    private void setEndIconsListeners() {
        nameTextInputLayout.setEndIconOnClickListener(view -> Objects.requireNonNull(nameTextInputLayout.getEditText()).setText(""));

        nicknameTextInputLayout.setEndIconOnClickListener(view -> Objects.requireNonNull(nicknameTextInputLayout.getEditText()).setText(""));

        emailTextInputLayout.setEndIconOnClickListener(view -> Objects.requireNonNull(emailTextInputLayout.getEditText()).setText(""));
    }

    // Set required listeners for the text fields
    private void setTextFieldsListeners() {
        setEndIconsListeners();

        UserHelper.setTextInputLayoutListener(this, nameTextInputLayout);
        UserHelper.setTextInputLayoutListener(this, nicknameTextInputLayout);
        UserHelper.setEmailTextInputLayoutListener(this, emailTextInputLayout);
        UserHelper.setPasswordTextInputLayoutListener(this, passwordTextInputLayout);
    }


    // Handle the registration button press
    private void handleRegistrationButton() {
        registration_button.setOnClickListener(view -> {
            String fullName = UserHelper.getString(nameTextInputLayout);
            String userName = UserHelper.getString(nameTextInputLayout);
            String email = UserHelper.getString(emailTextInputLayout);
            String password = UserHelper.getString(passwordTextInputLayout);

            if (UserHelper.isValidEmail(email) && password.length() >= 6 && !fullName.isEmpty() && !userName.isEmpty()) {
                userViewModel.register(fullName, userName, email, password).observe(this, result -> {
                    if (UserHelper.isAuthSuccess(result)) {
                        navigateToHomeScreen();
                    } else if (UserHelper.isError(result)) {
                        Snackbar.make(findViewById(android.R.id.content),
                                        ((Result.Error) result).getMessage(),
                                        Snackbar.LENGTH_LONG)
                                .show();
                    }
                });
            }
        });
    }


    // Navigate the user to the LoginActivity
    private void navigateToLoginScreen() {
        login_textButton.setOnClickListener(view -> {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        });
    }

    // Navigate the user to HomeScreen
    private void navigateToHomeScreen() {
        Intent intent = new Intent(this, BottomNavigationBarActivity.class);
        startActivity(intent);
        finish();
    }
}