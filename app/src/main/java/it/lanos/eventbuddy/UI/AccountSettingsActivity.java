package it.lanos.eventbuddy.UI;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseUser;

import org.w3c.dom.Text;

import it.lanos.eventbuddy.R;
import it.lanos.eventbuddy.UI.authentication.UserHelper;
import it.lanos.eventbuddy.UI.authentication.UserViewModel;
import it.lanos.eventbuddy.UI.authentication.WelcomeActivity;
import it.lanos.eventbuddy.data.source.models.Result;

public class AccountSettingsActivity extends AppCompatActivity {

    TextView account_email;
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

        // Handle the logout button press
        handleLogoutButton();

        // Handle the change_password button press
        handleChangePasswordButton();

        setUserEmail(account_email);

    }

    private void setViewsUp() {
        account_email = findViewById(R.id.account_email);

        current_password_text = findViewById(R.id.current_password_text);
        new_password_text = findViewById(R.id.new_password_text);
        confirm_password_text = findViewById(R.id.confirm_password_text);

        UserHelper.setPasswordTextInputLayoutListener(this, new_password_text);
        UserHelper.checkEqualTextInputLayout(this, new_password_text, confirm_password_text);

        change_password = findViewById(R.id.change_password);
        logout_button = findViewById(R.id.logout_button);
        delete_account = findViewById(R.id.delete_account);
    }

    private void handleChangePasswordButton() {
        change_password.setOnClickListener(view -> {
            String oldPassword = UserHelper.getString(current_password_text);
            String newPassword = UserHelper.getString(new_password_text);
            String confirm_password = UserHelper.getString(confirm_password_text);

            if(!oldPassword.isEmpty() && !newPassword.isEmpty() && !confirm_password.isEmpty()) {
                if(newPassword.equals(confirm_password)) {
                    userViewModel.changePassword(oldPassword, newPassword)
                            .observe(this, result -> {
                                if(UserHelper.isAuthSuccess(result)) {
                                    userViewModel.signOut();
                                    navigateUserToWelcomeScreen();
                                } else if(UserHelper.isError(result)) {
                                    Snackbar.make(findViewById(android.R.id.content),
                                                    ((Result.Error) result).getMessage(),
                                                    Snackbar.LENGTH_LONG)
                                            .show();
                                }
                            });
                }
            }

        });
    }
    private void handleLogoutButton() {
        logout_button.setOnClickListener(view -> {
            userViewModel.signOut();
            navigateUserToWelcomeScreen();
        });
    }

    // Navigate the user to WelcomeActivity
    private void navigateUserToWelcomeScreen() {
        Intent intent = new Intent(this, WelcomeActivity.class);
        startActivity(intent);
        finish();
    }

    //Set the user email into the textview
    private void setUserEmail(TextView textView) {
        String email = userViewModel.getCurrentUser().getEmail().toString().trim();
        textView.setText(email);
    }

    // Handle the press of DeleteButton, shows an AlertDialog
    private void handleDeleteButton() {
        delete_account.setOnClickListener(view -> {
            final EditText input = new EditText(this);
            input.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
            new AlertDialog.Builder(this)
                    .setTitle(getString(R.string.confirm_elimination))
                    .setMessage(getString(R.string.insert_password))
                    .setView(input)
                    .setPositiveButton(getString(R.string.confirm_alert), (dialog, whichButton) -> {
                        String password = input.getText().toString();
                        checkPassword(password);
                    })
                    .setNegativeButton(getString(R.string.cancel_alert), (dialog, whichButton) -> dialog.cancel())
                    .show();
        });
    }

    // Check if the entered password is correct
    private void checkPassword(String password) {
        FirebaseUser currentUser = userViewModel.getCurrentUser();

        AuthCredential credential = EmailAuthProvider
                .getCredential(currentUser.getEmail(), password);

        currentUser.reauthenticate(credential).addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                deleteAccount();
            } else {
                Snackbar.make(findViewById(android.R.id.content),
                                (getString(R.string.wrong_delete_password)),
                                Snackbar.LENGTH_LONG)
                        .show();
            }
        });
    }


    private void deleteAccount() {
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
    }
}