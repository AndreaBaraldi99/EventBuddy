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

import it.lanos.eventbuddy.R;
import it.lanos.eventbuddy.data.IUserRepository;
import it.lanos.eventbuddy.data.source.models.Result;

public class LoginActivity extends AppCompatActivity {

    Button login_button, forgot_password_button, signup_textButton;
    TextInputLayout email_text, password_text;
    CheckBox rememberMe_checkbox;

    UserViewModel userViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login_button = findViewById(R.id.login_button);
        forgot_password_button = findViewById(R.id.forgot_password_button);
        signup_textButton = findViewById(R.id.signup_textButton);

        email_text = findViewById(R.id.email_text);
        password_text = findViewById(R.id.password_text);

        rememberMe_checkbox = findViewById(R.id.rememberMe_checkBox);

        //End icon listener
        email_text.setEndIconOnClickListener(view -> {
            email_text.getEditText().setText("");
        });

        // Set up the IUserRepository and userViewModel
        IUserRepository userRepository = ServiceLocator.getInstance((Application) getApplicationContext()).getUserRepository();

        userViewModel = new ViewModelProvider(
                this,
                new UserViewModelFactory(userRepository)).get(UserViewModel.class);

        // Press the login button
        login_button.setOnClickListener(view -> {

            String email = email_text.getEditText().getText().toString().trim();
            String password = password_text.getEditText().getText().toString().trim();

            if(!email.isEmpty() && !password.isEmpty()) {
                userViewModel.signIn(email, password);
            }
        });

        // Response of the login button
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

        // Take the user to signUp activity
        signup_textButton.setOnClickListener(view -> {
            Intent intent = new Intent(this, RegistrationActivity.class);
            startActivity(intent);
        });
    }
}
