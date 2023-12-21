package it.lanos.eventbuddy.UI.authentication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import it.lanos.eventbuddy.R;
import it.lanos.eventbuddy.UI.authentication.LoginActivity;
import it.lanos.eventbuddy.UI.authentication.RegistrationActivity;

public class WelcomeActivity extends AppCompatActivity {
    Button signup_button, login_button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        signup_button = findViewById(R.id.signup_button);
        login_button = findViewById(R.id.login_button);

        // Nvigate the user to RegistrationActivity
        signup_button.setOnClickListener(view -> {
            Intent intent = new Intent(this, RegistrationActivity.class);
            startActivity(intent);
        });

        //Navigate the user to LoginActivity
        login_button.setOnClickListener(view -> {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        });
    }
}