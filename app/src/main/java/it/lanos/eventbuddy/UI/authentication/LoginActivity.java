package it.lanos.eventbuddy.UI.authentication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

import it.lanos.eventbuddy.R;
import it.lanos.eventbuddy.data.IUserRepository;
import it.lanos.eventbuddy.data.source.models.Result;
import it.lanos.eventbuddy.util.ServiceLocator;

public class LoginActivity extends AppCompatActivity {

    Button login_button, forgot_password_button, signup_textButton;
    TextInputLayout emailTextInputLayout, passwordTextInputLayout;
    CheckBox rememberMe_checkbox;
    UserViewModel userViewModel;
    static boolean rememberMeBoolean = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setViewsUp();

        setTextFieldsListeners();

        initializeViewModel();

        handleLoginButton();

        navigateToForgottenPasswordScreen();

        navigateToSignupScreen();
    }

    // Find views by ID
    private void setViewsUp(){
        login_button = findViewById(R.id.login_button);
        forgot_password_button = findViewById(R.id.forgot_password_button);
        signup_textButton = findViewById(R.id.signup_textButton);

        emailTextInputLayout = findViewById(R.id.email_text);
        passwordTextInputLayout = findViewById(R.id.new_password_text);

        rememberMe_checkbox = findViewById(R.id.rememberMe_checkBox);
    }

    // Initialize the view model
    private void initializeViewModel() {
        IUserRepository userRepository = ServiceLocator.getInstance().getUserRepository(getApplication());

        userViewModel = new ViewModelProvider(
                this,
                new UserViewModelFactory(userRepository)).get(UserViewModel.class);
    }

    // Handle the login button press
    private void handleLoginButton() {
        login_button.setOnClickListener(view -> {

            rememberMeBoolean = isRememberMeChecked();

            String email = Objects.requireNonNull(emailTextInputLayout.getEditText()).getText().toString().trim();
            String password = Objects.requireNonNull(passwordTextInputLayout.getEditText()).getText().toString().trim();

            if(Helper.isValidEmail(email) && !password.isEmpty()) {
                userViewModel.signIn(email, password).observe(this, result -> {
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

    // Navigate the user to RegistrationActivity
    private void navigateToSignupScreen() {
        signup_textButton.setOnClickListener(view -> {
            Intent intent = new Intent(this, RegistrationActivity.class);
            startActivity(intent);
        });
    }

    private boolean isRememberMeChecked() {
        if(rememberMe_checkbox.isChecked()) {
            return true;
        } else {
            return false;
        }
    }

    // Set required listeners for the text fields
    private void setTextFieldsListeners() {
        //End email icon listener
        emailTextInputLayout.setEndIconOnClickListener(view -> {
            Objects.requireNonNull(emailTextInputLayout.getEditText()).setText("");
        });

        Helper.setEmailTextInputLayoutListener(this, emailTextInputLayout);
        Helper.setTextInputLayoutListener(this, passwordTextInputLayout);
    }

    // Navigate the user to ForgottenPasswordActivity
    public void navigateToForgottenPasswordScreen() {
        forgot_password_button.setOnClickListener(view -> {
            Intent intent = new Intent(this, ForgottenPasswordActivity.class);
            startActivity(intent);
        });
    }

    public static boolean getRememberMeBoolean() {
        return rememberMeBoolean;
    }
}
