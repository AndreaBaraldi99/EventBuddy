package it.lanos.eventbuddy.UI.authentication;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

import it.lanos.eventbuddy.R;
import it.lanos.eventbuddy.UI.BottomNavigationBarActivity;
import it.lanos.eventbuddy.data.IUserRepository;
import it.lanos.eventbuddy.data.source.models.Result;
import it.lanos.eventbuddy.util.ServiceLocator;

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
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                startActivity(new Intent(RegistrationActivity.this, WelcomeActivity.class));
                finish();
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);

        // Find views by ID
        setViewsUp();

        // Set required listeners for the text fields
        setTextFieldsListeners();

        // Initialize the ViewModel
        IUserRepository userRepository =
                ServiceLocator.getInstance().getUserRepository(getApplication());

        userViewModel = new ViewModelProvider(
                this,
                new UserViewModelFactory(userRepository)).get(UserViewModel.class);

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

        TextInputListenerHelper.setPasswordTextInputLayoutListener(this, passwordTextInputLayout);
    }


    // Handle the registration button press
    private void handleRegistrationButton() {
        registration_button.setOnClickListener(view -> {
            String fullName =
                    Objects.requireNonNull(nameTextInputLayout.getEditText()).getText().toString().trim();
            String userName =
                    Objects.requireNonNull(nicknameTextInputLayout.getEditText()).getText().toString().trim();
            String email =
                    Objects.requireNonNull(emailTextInputLayout.getEditText()).getText().toString().trim();
            String password =
                    Objects.requireNonNull(passwordTextInputLayout.getEditText()).getText().toString().trim();

            if (android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                if(password.length() >= 6 && !fullName.isEmpty() && !userName.isEmpty()) {
                    userViewModel.register(fullName, userName, email, password).observe(this, result -> {
                        if (result.isSuccess()) {
                            navigateToHomeScreen();
                        } else {
                            Snackbar.make(findViewById(android.R.id.content),
                                            ((Result.Error) result).getMessage(),
                                            Snackbar.LENGTH_LONG)
                                    .show();
                        }
                    });
                }
            } else {
                emailTextInputLayout.setError(getString(R.string.not_valid_email));
                TextInputListenerHelper.setEmailTextInputLayoutListener(this, emailTextInputLayout);
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