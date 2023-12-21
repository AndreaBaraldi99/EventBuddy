package it.lanos.eventbuddy.UI.authentication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

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

        setViewsUp();

        setEndIconsListeners();

        setEmailTextInputLayoutListener();

        initializeViewModel();

        handleRegistrationButton();

        handleRegistrationResponse();

        navigateToLoginScreen();
    }

    private void setViewsUp() {
        nameTextInputLayout = findViewById(R.id.nameTextInputLayout);
        nicknameTextInputLayout = findViewById(R.id.nicknameTextInputLayout);
        emailTextInputLayout = findViewById(R.id.emailTextInputLayout);
        passwordTextInputLayout = findViewById(R.id.passwordTextInputLayout);

        registration_button = findViewById(R.id.registration_button);
        login_textButton = findViewById(R.id.login_TextButton);
    }
    private void setEndIconsListeners() {
        //End icon listeners
        nameTextInputLayout.setEndIconOnClickListener(view -> {
            nameTextInputLayout.getEditText().setText("");
        });

        nicknameTextInputLayout.setEndIconOnClickListener(view -> {
            nicknameTextInputLayout.getEditText().setText("");
        });

        emailTextInputLayout.setEndIconOnClickListener(view -> {
            emailTextInputLayout.getEditText().setText("");
        });
    }
    private void setEmailTextInputLayoutListener() {
        emailTextInputLayout.getEditText().addTextChangedListener(new TextWatcher() {
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
                    emailTextInputLayout.setError("Email non valida");
                } else {
                    emailTextInputLayout.setError(null);
                }
            }
        });
    }
    private void initializeViewModel() {
        IUserRepository userRepository = ServiceLocator.getInstance((Application) getApplicationContext()).getUserRepository();

        userViewModel = new ViewModelProvider(
                this,
                new UserViewModelFactory(userRepository)).get(UserViewModel.class);
    }
    private void handleRegistrationButton() {
        registration_button.setOnClickListener(view -> {
            String fullName = nameTextInputLayout.getEditText().getText().toString().trim();
            String userName = nicknameTextInputLayout.getEditText().getText().toString().trim();
            String email = emailTextInputLayout.getEditText().getText().toString().trim();
            String password = passwordTextInputLayout.getEditText().getText().toString().trim();

            // TODO: 21/12/2023  Settare gli errori negli EditText
            if (isValidEmail(email)) {
                if (password.length() >= 8) {
                    if (!fullName.isEmpty() && !userName.isEmpty()) {
                        userViewModel.register(fullName, userName, email, password);
                    }
                }
            }
        });
    }
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
    private void navigateToLoginScreen() {
        login_textButton.setOnClickListener(view -> {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        });
    }
    private boolean isValidEmail(String target) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }
}