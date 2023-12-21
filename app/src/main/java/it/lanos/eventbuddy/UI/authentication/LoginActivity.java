package it.lanos.eventbuddy.UI.authentication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.app.Application;
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

public class LoginActivity extends AppCompatActivity {

    Button login_button, forgot_password_button, signup_textButton;
    TextInputLayout email_text, password_text;
    CheckBox rememberMe_checkbox;
    UserViewModel userViewModel;
    static boolean rememberMeBoolean = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setViewsUp();

        //End email icon listener
        email_text.setEndIconOnClickListener(view -> {
            Objects.requireNonNull(email_text.getEditText()).setText("");
        });

        initializeViewModel();

        handleLoginButton();

        handleLoginResponse();

        navigateToSignupScreen();
    }

    // Find views by ID
    private void setViewsUp(){
        login_button = findViewById(R.id.login_button);
        forgot_password_button = findViewById(R.id.forgot_password_button);
        signup_textButton = findViewById(R.id.signup_textButton);

        email_text = findViewById(R.id.email_text);
        password_text = findViewById(R.id.password_text);

        rememberMe_checkbox = findViewById(R.id.rememberMe_checkBox);
    }

    // Initialize the view model
    private void initializeViewModel() {
        IUserRepository userRepository = ServiceLocator.getInstance((Application) getApplicationContext()).getUserRepository();

        userViewModel = new ViewModelProvider(
                this,
                new UserViewModelFactory(userRepository)).get(UserViewModel.class);
    }

    // Handle the login button press
    private void handleLoginButton() {
        login_button.setOnClickListener(view -> {

            rememberMeBoolean = isRememberMeChecked();

            String email = Objects.requireNonNull(email_text.getEditText()).getText().toString().trim();
            String password = Objects.requireNonNull(password_text.getEditText()).getText().toString().trim();

            if(!email.isEmpty() && !password.isEmpty()) {
                userViewModel.signIn(email, password);
            } else {
                Snackbar.make(findViewById(android.R.id.content),
                                getString(R.string.generic_login_error),
                                Snackbar.LENGTH_LONG)
                        .show();
            }
        });
    }

    // Handle the login button response
    private void handleLoginResponse() {
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

    public static boolean getRememberMeBoolean() {
        return rememberMeBoolean;
    }
}
