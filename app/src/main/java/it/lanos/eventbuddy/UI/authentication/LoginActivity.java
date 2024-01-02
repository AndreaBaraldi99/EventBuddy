package it.lanos.eventbuddy.UI.authentication;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

import it.lanos.eventbuddy.R;
import it.lanos.eventbuddy.UI.BottomNavigationBarActivity;
import it.lanos.eventbuddy.data.source.models.Result;

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

        setupBackButtonHandling();

        // Find the views by ID
        setViewsUp();

        setTextFieldsListeners();

        // Initialize the view model
        userViewModel = UserHelper.initializeAndGetViewModel(this);

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

            rememberMeBoolean = isRememberMeChecked();

            String email = UserHelper.getString(emailTextInputLayout);
            String password = UserHelper.getString(passwordTextInputLayout);

            if(UserHelper.isValidEmail(email) && !password.isEmpty()) {
                userViewModel.signIn(email, password).observe(this, result -> {
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

    // Navigate the user to RegistrationActivity
    private void navigateToSignupScreen() {
        signup_textButton.setOnClickListener(view -> {
            Intent intent = new Intent(this, RegistrationActivity.class);
            startActivity(intent);
        });
    }

    private boolean isRememberMeChecked() {
        return rememberMe_checkbox.isChecked();
    }

    // Set required listeners for the text fields
    private void setTextFieldsListeners() {
        //End email icon listener
        emailTextInputLayout.setEndIconOnClickListener(view -> Objects.requireNonNull(emailTextInputLayout.getEditText()).setText(""));

        UserHelper.setEmailTextInputLayoutListener(this, emailTextInputLayout);
        UserHelper.setTextInputLayoutListener(this, passwordTextInputLayout);
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
    public static boolean getRememberMeBoolean() {
        return rememberMeBoolean;
    }

    //Navigate the user to WelcomeActivity
    private void setupBackButtonHandling() {
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back button event
                Intent intent = new Intent(LoginActivity.this, WelcomeActivity.class);
                startActivity(intent);
                finish();
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);
    }
}
