package it.lanos.eventbuddy.UI;

import static it.lanos.eventbuddy.util.Constants.ENCRYPTED_DATA_FILE_NAME;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;

import java.util.Objects;

import it.lanos.eventbuddy.R;
import it.lanos.eventbuddy.UI.authentication.TextInputListenerHelper;
import it.lanos.eventbuddy.UI.authentication.UserViewModel;
import it.lanos.eventbuddy.UI.authentication.UserViewModelFactory;
import it.lanos.eventbuddy.UI.authentication.WelcomeActivity;
import it.lanos.eventbuddy.data.IUserRepository;
import it.lanos.eventbuddy.data.source.models.Result;
import it.lanos.eventbuddy.data.source.models.User;
import it.lanos.eventbuddy.util.DataEncryptionUtil;
import it.lanos.eventbuddy.util.ServiceLocator;

public class AccountSettingsActivity extends AppCompatActivity {

    TextView account_email, account_full_name;
    UserViewModel userViewModel;
    TextInputLayout current_password_text, new_password_text, confirm_password_text;
    Button change_password, logout_button, delete_account;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_settings);

        MaterialToolbar createEventToolbar = findViewById(R.id.toolbarAccountSettings);
        createEventToolbar.setNavigationOnClickListener(v -> NavUtils.navigateUpFromSameTask(AccountSettingsActivity.this));

        // Find the views by ID
        setViewsUp();

        // Initialize the ViewModel
        IUserRepository userRepository =
                ServiceLocator.getInstance().getUserRepository(getApplication());

        userViewModel = new ViewModelProvider(
                this,
                new UserViewModelFactory(userRepository)).get(UserViewModel.class);

        // Handle the delete button press
        handleDeleteButton();

        // Handle the logout button press
        handleLogoutButton();

        // Handle the change_password button press
        handleChangePasswordButton();

        //Set the user email into the textview
        setUserEmail(account_email);

        setUserFullName(account_full_name);

    }

    private void setViewsUp() {
        account_email = findViewById(R.id.account_email);
        account_full_name = findViewById(R.id.account_full_name);

        current_password_text = findViewById(R.id.current_password_text);
        new_password_text = findViewById(R.id.new_password_text);
        confirm_password_text = findViewById(R.id.confirm_password_text);

        TextInputListenerHelper.setPasswordTextInputLayoutListener(this, new_password_text);
        checkEqualTextInputLayout(this, new_password_text, confirm_password_text);

        change_password = findViewById(R.id.change_password);
        logout_button = findViewById(R.id.logout_button);
        delete_account = findViewById(R.id.delete_account);
    }

    private void handleChangePasswordButton() {
        change_password.setOnClickListener(view -> {
            String oldPassword = Objects.requireNonNull(current_password_text.getEditText()).getText().toString().trim();
            String newPassword = Objects.requireNonNull(new_password_text.getEditText()).getText().toString().trim();
            String confirm_password = Objects.requireNonNull(confirm_password_text.getEditText()).getText().toString().trim();

            if(!oldPassword.isEmpty() && !newPassword.isEmpty() && !confirm_password.isEmpty()) {
                if(newPassword.equals(confirm_password)) {
                    userViewModel.changePassword(oldPassword, newPassword)
                            .observe(this, result -> {
                                if(result.isSuccess()) {
                                    userViewModel.signOut();
                                    navigateUserToWelcomeScreen();
                                } else  {
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
            finish();
            navigateUserToWelcomeScreen();
        });
    }

    // Navigate the user to WelcomeActivity
    private void navigateUserToWelcomeScreen() {
        Intent intent = new Intent(this, WelcomeActivity.class);

        // Set flags to clear the activity stack and start a new task
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        // Start the welcome screen activity
        startActivity(intent);

        // Finish the current activity
        finish();
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
                    .setPositiveButton(getString(R.string.confirm_text), (dialog, whichButton) -> {
                        String password = input.getText().toString();
                        if(!password.isEmpty()) {
                            checkPassword(password);
                        }
                    })
                    .setNegativeButton(getString(R.string.cancel_text), (dialog, whichButton) -> dialog.cancel())
                    .show();
        });
    }

    // Check if the entered password is correct
    private void checkPassword(String password) {
        FirebaseUser currentUser = userViewModel.getCurrentUser();

        AuthCredential credential = EmailAuthProvider
                .getCredential(Objects.requireNonNull(currentUser.getEmail()), password);

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
                if(result.isSuccess()) {
                    // Account successfully deleted, navigate user to WelcomeActivity
                    navigateUserToWelcomeScreen();
                } else {
                    //Account not deleted
                    Snackbar.make(findViewById(android.R.id.content),
                                    ((Result.Error) result).getMessage(),
                                    Snackbar.LENGTH_LONG)
                            .show();
                }
            });
    }

    private void setUserEmail(TextView textView) {
        String email = Objects.requireNonNull(userViewModel.getCurrentUser().getEmail()).trim();
        textView.setText(email);
    }

    private void setUserFullName(TextView textView) {
        readUser(new DataEncryptionUtil(getApplication()));
        String fullName = user.getFullName();
        textView.setText(fullName);
    }

    private void readUser(DataEncryptionUtil dataEncryptionUtil){
        try {
            this.user = new Gson().fromJson(dataEncryptionUtil.readSecretDataOnFile(ENCRYPTED_DATA_FILE_NAME), User.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Check if the text in secondTextInputLayout is equal to the text in firstTextInputLayout
    public static void checkEqualTextInputLayout(
            Context context,
            TextInputLayout firstTextInputLayout,
            TextInputLayout secondTextInputLayout) {

        Objects.requireNonNull(firstTextInputLayout.getEditText()).addTextChangedListener(new TextWatcher() {

            // Listener for firstTextInputLayout
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String firstTextString = Objects.requireNonNull(firstTextInputLayout.getEditText()).getText().toString().trim();
                String secondTextString = Objects.requireNonNull(secondTextInputLayout.getEditText()).getText().toString().trim();

                if(!secondTextString.equals(firstTextString) && !secondTextString.isEmpty()) {
                    secondTextInputLayout.setError(context.getString(R.string.password_doesnt_match));
                } else {
                    secondTextInputLayout.setError(null);
                }
            }
        });

        // Listener for secondTextInputLayout
        Objects.requireNonNull(secondTextInputLayout.getEditText()).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String firstTextString = Objects.requireNonNull(firstTextInputLayout.getEditText()).getText().toString().trim();
                String secondTextString = Objects.requireNonNull(secondTextInputLayout.getEditText()).getText().toString().trim();

                if(!secondTextString.equals(firstTextString)) {
                    secondTextInputLayout.setError(context.getString(R.string.password_doesnt_match));
                } else {
                    secondTextInputLayout.setError(null);
                }
            }
        });
    }
}