package it.lanos.eventbuddy.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;

import com.google.android.material.textfield.TextInputLayout;

import it.lanos.eventbuddy.R;

public class LoginActivity extends AppCompatActivity {

    Button login_button, forgot_password_button, signup_textButton;
    TextInputLayout email_text, password_text;
    CheckBox rememberMe_checkbox;
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
    }
}