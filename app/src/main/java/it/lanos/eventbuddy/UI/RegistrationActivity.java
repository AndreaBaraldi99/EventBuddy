package it.lanos.eventbuddy.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;

import com.google.android.material.textfield.TextInputLayout;

import it.lanos.eventbuddy.R;

public class RegistrationActivity extends AppCompatActivity {

    TextInputLayout nameTextInputLayout, nicknameTextInputLayout,
            emailTextInputLayout, passwordTextInputLayout;

    Button registration_button, login_TextButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        nameTextInputLayout = findViewById(R.id.nameTextInputLayout);
        nicknameTextInputLayout = findViewById(R.id.nicknameTextInputLayout);
        emailTextInputLayout = findViewById(R.id.emailTextInputLayout);
        passwordTextInputLayout = findViewById(R.id.passwordTextInputLayout);

        registration_button = findViewById(R.id.registration_button);
        login_TextButton = findViewById(R.id.login_TextButton);

    }
}