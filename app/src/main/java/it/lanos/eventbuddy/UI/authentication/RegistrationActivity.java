package it.lanos.eventbuddy.UI.authentication;

import androidx.appcompat.app.AppCompatActivity;
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

        // Handle the registration response
        handleRegistrationResponse();

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

        setTextInputLayoutListener(nameTextInputLayout);
        setTextInputLayoutListener(nicknameTextInputLayout);
        setEmailTextInputLayoutListener();
        setPasswordTextInputLayoutListener();
    }

    // Listener used for validating email address
    private void setEmailTextInputLayoutListener() {
        Objects.requireNonNull(emailTextInputLayout.getEditText()).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String emailText = editable.toString().trim();
                if (!isValidEmail(emailText)) {
                    emailTextInputLayout.setError(getString(R.string.not_valid_email));
                } else {
                    emailTextInputLayout.setError(null);
                }
            }
        });
    }

    // Listener used for validating password
    private void setPasswordTextInputLayoutListener(){
        Objects.requireNonNull(passwordTextInputLayout.getEditText()).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String passwordText = editable.toString().trim();
                if (passwordText.length() < 6) {
                    passwordTextInputLayout.setError(getString(R.string.weak_registration_password));
                } else {
                    passwordTextInputLayout.setError(null);
                }
            }
        });
    }

    // Listener used if a field cannot be empty
    private void setTextInputLayoutListener(TextInputLayout textInputLayout) {
        Objects.requireNonNull(textInputLayout.getEditText()).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                String text = editable.toString().trim();
                if (text.isEmpty()) {
                    textInputLayout.setError(getString(R.string.required_field));
                } else {
                    textInputLayout.setError(null);
                }
            }
        });
    }

    // Initialize the view model
    private void initializeViewModel() {
        IUserRepository userRepository = ServiceLocator.getInstance((Application) getApplicationContext()).getUserRepository();

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

            if (isValidEmail(email) && password.length() >= 6 && !fullName.isEmpty() && !userName.isEmpty()) {
                userViewModel.register(fullName, userName, email, password);
            }
        });
    }

    // Handle the registration response
    private void handleRegistrationResponse() {
        userViewModel.getUserMutableLiveData().observe(this, result -> {
            if (result instanceof Result.AuthSuccess) {
                Snackbar.make(findViewById(android.R.id.content),
                                ((Result.AuthSuccess) result).getMessage(),
                                Snackbar.LENGTH_LONG)
                        .show();
            } else if (result instanceof Result.Error) {
                Snackbar.make(findViewById(android.R.id.content),
                                ((Result.Error) result).getMessage(),
                                Snackbar.LENGTH_LONG)
                        .show();
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

    // Check if a string is a valid email
    private boolean isValidEmail(String target) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }
}