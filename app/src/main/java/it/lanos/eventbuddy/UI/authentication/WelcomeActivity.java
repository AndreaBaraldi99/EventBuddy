package it.lanos.eventbuddy.UI.authentication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;

import com.google.firebase.auth.FirebaseUser;

import it.lanos.eventbuddy.R;
import it.lanos.eventbuddy.UI.BottomNavigationBarActivity;
import it.lanos.eventbuddy.UI.PreferencesSettingsActivity;
import it.lanos.eventbuddy.data.IUserRepository;
import it.lanos.eventbuddy.util.ServiceLocator;

public class WelcomeActivity extends AppCompatActivity {
    Button signup_button, login_button;
    UserViewModel userViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        // Initialize the ViewModel
        IUserRepository userRepository =
                ServiceLocator.getInstance().getUserRepository(getApplication());

        userViewModel = new ViewModelProvider(
                this,
                new UserViewModelFactory(userRepository)).get(UserViewModel.class);
        rememberMe();

        setAppTheme();

        alreadyLoggedUser();

        signup_button = findViewById(R.id.signup_button);
        login_button = findViewById(R.id.login_button);

        // Navigate the user to RegistrationActivity
        signup_button.setOnClickListener(view -> {
            Intent intent = new Intent(this, RegistrationActivity.class);
            startActivity(intent);
            finish();
        });

        //Navigate the user to LoginActivity
        login_button.setOnClickListener(view -> {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void alreadyLoggedUser() {
        FirebaseUser currentUser = userViewModel.getCurrentUser();
        if(currentUser != null) {
            Intent intent = new Intent(this, BottomNavigationBarActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void setAppTheme() {
        SharedPreferences sharedPreferences = getSharedPreferences("Settings", MODE_PRIVATE);
        boolean isDarkThemeEnabled = sharedPreferences.getBoolean("isDarkThemeEnabled", false);

        PreferencesSettingsActivity.setAppTheme(isDarkThemeEnabled);
    }

    private void rememberMe() {
        SharedPreferences sharedPreferences = getSharedPreferences("RememberMeBoolean", MODE_PRIVATE);
        boolean rememberMeChecked = sharedPreferences.getBoolean("rememberMe", true);

        if(!rememberMeChecked) {
            userViewModel.signOut();
        }
    }
}