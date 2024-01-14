package it.lanos.eventbuddy.UI.authentication;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

import it.lanos.eventbuddy.R;
import it.lanos.eventbuddy.UI.BottomNavigationBarActivity;
import it.lanos.eventbuddy.data.IUserRepository;
import it.lanos.eventbuddy.data.source.models.Result;
import it.lanos.eventbuddy.util.ServiceLocator;

public class LoginActivity extends AppCompatActivity {

    private Button login_button, forgot_password_button, signup_textButton;
    private TextInputLayout emailTextInputLayout, passwordTextInputLayout;
    private CheckBox rememberMe_checkbox;
    private UserViewModel userViewModel;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Navigate the user to welcome activity when back button is pressed
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                startActivity(new Intent(LoginActivity.this, WelcomeActivity.class));
                finish();
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);

        // Find the views by ID
        setViewsUp();

        sharedPreferences = getSharedPreferences("RememberMeBoolean", MODE_PRIVATE);
        // Retrieve and set the "remember me" preference
        boolean rememberMeChecked = sharedPreferences.getBoolean("rememberMe", true);
        rememberMe_checkbox.setChecked(rememberMeChecked);

        setTextFieldsListeners();

        // Initialize the ViewModel
        IUserRepository userRepository =
                ServiceLocator.getInstance().getUserRepository(getApplication());

        userViewModel = new ViewModelProvider(
                this,
                new UserViewModelFactory(userRepository)).get(UserViewModel.class);

        handleLoginButton();

        // Navigate the user to ForgottenPasswordActivity
        navigateToForgottenPasswordScreen();

        navigateToSignupScreen();

        //Show a snack-bar with a message "Email sent" if the user reset his password
        showPopupIfComingFromForgottenPassword();
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


    // Handle the login button press
    private void handleLoginButton() {
        login_button.setOnClickListener(view -> {

            String email = Objects.requireNonNull(emailTextInputLayout.getEditText()).getText().toString().trim();
            String password = Objects.requireNonNull(passwordTextInputLayout.getEditText()).getText().toString().trim();

            if(android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                if(!password.isEmpty()) {
                    rememberMe();
                    userViewModel.signIn(email, password).observe(this, result -> {
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

    // Navigate the user to RegistrationActivity
    private void navigateToSignupScreen() {
        signup_textButton.setOnClickListener(view -> {
            Intent intent = new Intent(this, RegistrationActivity.class);
            startActivity(intent);
        });
    }

    // Set required listeners for the text fields
    private void setTextFieldsListeners() {
        //End email icon listener
        emailTextInputLayout.setEndIconOnClickListener(view -> Objects.requireNonNull(emailTextInputLayout.getEditText()).setText(""));
    }

    // Navigate the user to ForgottenPasswordActivity
    private void navigateToForgottenPasswordScreen() {
        forgot_password_button.setOnClickListener(view -> {
            Intent intent = new Intent(this, ForgottenPasswordActivity.class);
            startActivity(intent);
        });
    }

    // Navigate the user to HomeScreen
    private void navigateToHomeScreen() {
        Intent intent = new Intent(this, BottomNavigationBarActivity.class);
        startActivity(intent);
        finish();
    }

    private void showPopupIfComingFromForgottenPassword() {
        String message = getIntent().getStringExtra("EmailSentMessage");

        if(message != null) {
            Snackbar.make(findViewById(android.R.id.content),
                            message,
                            Snackbar.LENGTH_LONG)
                    .show();
        }
    }

    private void rememberMe() {
        boolean rememberMeChecked = rememberMe_checkbox.isChecked();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("rememberMe", rememberMeChecked);
        editor.apply();
    }
}
