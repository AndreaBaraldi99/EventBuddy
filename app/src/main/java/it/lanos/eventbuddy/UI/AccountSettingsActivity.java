package it.lanos.eventbuddy.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import it.lanos.eventbuddy.R;
import it.lanos.eventbuddy.UI.authentication.UserHelper;
import it.lanos.eventbuddy.UI.authentication.UserViewModel;
import it.lanos.eventbuddy.UI.authentication.WelcomeActivity;
import it.lanos.eventbuddy.data.source.models.Result;

public class AccountSettingsActivity extends AppCompatActivity {

    UserViewModel userViewModel;
    TextInputLayout current_password_text, new_password_text, confirm_password_text;
    Button change_password, logout_button, delete_account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_settings);
        MaterialToolbar toolbar = findViewById(R.id.toolbarAccountSettings);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        // Find the views by ID
        setViewsUp();

        // Initialize the view model
        userViewModel = UserHelper.initializeAndGetViewModel(this);

        // Handle the delete button press
        handleDeleteButton();

        // handle the logout button press
        handleLogoutButton();

    }

    private void setViewsUp() {
        current_password_text = findViewById(R.id.current_password_text);
        new_password_text = findViewById(R.id.new_password_text);
        confirm_password_text = findViewById(R.id.confirm_password_text);

        UserHelper.setPasswordTextInputLayoutListener(this, new_password_text);

        change_password = findViewById(R.id.change_password);
        logout_button = findViewById(R.id.logout_button);
        delete_account = findViewById(R.id.delete_account);
    }

    private void handleChangePasswordButton() {
        change_password.setOnClickListener(view -> {

        });
    }
    private void handleLogoutButton() {
        logout_button.setOnClickListener(view -> {
            userViewModel.signOut();
            navigateUserToWelcomeScreen();
        });
    }

    private void handleDeleteButton() {
        delete_account.setOnClickListener(view -> {
            userViewModel.deleteUser().observe(this, result -> {

                if(UserHelper.isAuthSuccess(result)) {
                    // Account successfully deleted, navigate user to WelcomeActivity
                    navigateUserToWelcomeScreen();
                } else if(UserHelper.isError(result)) {
                    //Account not deleted
                    Snackbar.make(findViewById(android.R.id.content),
                                    ((Result.Error) result).getMessage(),
                                    Snackbar.LENGTH_LONG)
                            .show();
                }
            });

        });
    }

    // Navigate the user to WelcomeActivity
    private void navigateUserToWelcomeScreen() {
        Intent intent = new Intent(this, WelcomeActivity.class);
        startActivity(intent);
        finish();
    }

}