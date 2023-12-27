package it.lanos.eventbuddy.UI.authentication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

import it.lanos.eventbuddy.R;
import it.lanos.eventbuddy.data.IUserRepository;
import it.lanos.eventbuddy.data.source.models.Result;

public class ForgottenPasswordActivity extends AppCompatActivity {

    TextInputLayout emailTextInputLayout;
    Button resetButton;
    UserViewModel userViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotten_password);

        emailTextInputLayout = findViewById(R.id.email_text);
        resetButton = findViewById(R.id.reset_button);

        initializeViewModel();

        setTextFieldsListeners();

        handleResetButton();
    }

    // Initialize the view model
    private void initializeViewModel() {
        IUserRepository userRepository = ServiceLocator.getInstance().getUserRepository(getApplication());

        userViewModel = new ViewModelProvider(
                this,
                new UserViewModelFactory(userRepository)).get(UserViewModel.class);
    }

    // Set required listeners for the text fields
    private void setTextFieldsListeners() {
        //End email icon listener
        emailTextInputLayout.setEndIconOnClickListener(view -> {
            Objects.requireNonNull(emailTextInputLayout.getEditText()).setText("");
        });

        Helper.setEmailTextInputLayoutListener(this, emailTextInputLayout);
    }

    // Navigate the user to LoginActivity
    public void navigateToLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    // Handle reset button press
    public void handleResetButton() {
        resetButton.setOnClickListener(view -> {
            String email = emailTextInputLayout.getEditText().getText().toString().trim();
            if(Helper.isValidEmail(email)) {
                userViewModel.resetPassword(email).observe(this, result -> {
                    if(Helper.isAuthSuccess(result)) {
                        Snackbar.make(findViewById(android.R.id.content),
                                        ((Result.AuthSuccess) result).getMessage(),
                                        Snackbar.LENGTH_LONG)
                                .show();
                        navigateToLoginActivity();
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
}