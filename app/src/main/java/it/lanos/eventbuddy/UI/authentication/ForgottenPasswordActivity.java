package it.lanos.eventbuddy.UI.authentication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

import it.lanos.eventbuddy.R;
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

        userViewModel = UserHelper.initializeAndGetViewModel(this);

        setTextFieldsListeners();

        handleResetButton();
    }

    // Set required listeners for the text fields
    private void setTextFieldsListeners() {
        //End email icon listener
        emailTextInputLayout.setEndIconOnClickListener(view -> Objects.requireNonNull(emailTextInputLayout.getEditText()).setText(""));

        UserHelper.setEmailTextInputLayoutListener(this, emailTextInputLayout);
    }

    // Navigate the user to LoginActivity
    public void navigateToLoginActivity(String message) {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.putExtra("EmailSentMessage", message);
        startActivity(intent);
        finish();
    }

    // Handle reset button press
    public void handleResetButton() {
        resetButton.setOnClickListener(view -> {
            String email = UserHelper.getString(emailTextInputLayout);
            if(UserHelper.isValidEmail(email)) {
                userViewModel.resetPassword(email).observe(this, result -> {
                    if(UserHelper.isAuthSuccess(result)) {
                        userViewModel.signOut();
                        navigateToLoginActivity(((Result.AuthSuccess) result).getMessage());
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
}