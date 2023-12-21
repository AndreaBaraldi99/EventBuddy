package it.lanos.eventbuddy.UI.authentication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

import it.lanos.eventbuddy.R;
import it.lanos.eventbuddy.data.IUserRepository;
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

        // Find views by ID
        setViewsUp();

        // Set required listeners for the text fields
        setTextFieldsListeners();

        // Initialize the ViewModel
        initializeViewModel();

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
        nameTextInputLayout.setEndIconOnClickListener(view -> {
            Objects.requireNonNull(nameTextInputLayout.getEditText()).setText("");
        });

        nicknameTextInputLayout.setEndIconOnClickListener(view -> {
            Objects.requireNonNull(nicknameTextInputLayout.getEditText()).setText("");
        });

        emailTextInputLayout.setEndIconOnClickListener(view -> {
            Objects.requireNonNull(emailTextInputLayout.getEditText()).setText("");
        });
    }

    // Set required listeners for the text fields
    private void setTextFieldsListeners() {
        setEndIconsListeners();

        Helper.setTextInputLayoutListener(this, nameTextInputLayout);
        Helper.setTextInputLayoutListener(this, nicknameTextInputLayout);
        Helper.setEmailTextInputLayoutListener(this, emailTextInputLayout);
        Helper.setPasswordTextInputLayoutListener(this, passwordTextInputLayout);
    }

    // Initialize the view model
    private void initializeViewModel() {
        IUserRepository userRepository = ServiceLocator.getInstance().getUserRepository(getApplication());

        userViewModel = new ViewModelProvider(
                this,
                new UserViewModelFactory(userRepository)).get(UserViewModel.class);
    }

    // Handle the registration button press
    private void handleRegistrationButton() {
        registration_button.setOnClickListener(view -> {
            String fullName = Objects.requireNonNull(nameTextInputLayout.getEditText()).getText().toString().trim();
            String userName = Objects.requireNonNull(nicknameTextInputLayout.getEditText()).getText().toString().trim();
            String email = Objects.requireNonNull(emailTextInputLayout.getEditText()).getText().toString().trim();
            String password = Objects.requireNonNull(passwordTextInputLayout.getEditText()).getText().toString().trim();

            if (Helper.isValidEmail(email) && password.length() >= 6 && !fullName.isEmpty() && !userName.isEmpty()) {
                userViewModel.register(fullName, userName, email, password).observe(this, result -> {
                    if (Helper.isAuthSuccess(result)) {
                        Snackbar.make(findViewById(android.R.id.content),
                                        ((Result.AuthSuccess) result).getMessage(),
                                        Snackbar.LENGTH_LONG)
                                .show();
                    } else if (Helper.isError(result)) {
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


}