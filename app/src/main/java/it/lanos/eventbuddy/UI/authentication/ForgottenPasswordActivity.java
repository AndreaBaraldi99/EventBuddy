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
import it.lanos.eventbuddy.util.ServiceLocator;

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

        // Initialize the ViewModel
        IUserRepository userRepository =
                ServiceLocator.getInstance().getUserRepository(getApplication());

        userViewModel = new ViewModelProvider(
                this,
                new UserViewModelFactory(userRepository)).get(UserViewModel.class);

        //End email icon listener
        emailTextInputLayout.setEndIconOnClickListener(view -> Objects.requireNonNull(emailTextInputLayout.getEditText()).setText(""));

        handleResetButton();
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
            String email = Objects.requireNonNull(emailTextInputLayout.getEditText()).getText().toString().trim();

            if(android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                userViewModel.resetPassword(email).observe(this, result -> {
                    if(result.isSuccess()) {
                        userViewModel.signOut();
                        navigateToLoginActivity(((Result.AuthSuccess) result).getMessage());
                    } else {
                        Snackbar.make(findViewById(android.R.id.content),
                                        ((Result.Error) result).getMessage(),
                                        Snackbar.LENGTH_LONG)
                                .show();
                    }
                });
            } else {
                emailTextInputLayout.setError(getString(R.string.not_valid_email));
                TextInputListenerHelper.setEmailTextInputLayoutListener(this, emailTextInputLayout);
            }
        });
    }
}