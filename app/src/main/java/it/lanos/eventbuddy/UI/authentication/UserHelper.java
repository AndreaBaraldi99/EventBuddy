package it.lanos.eventbuddy.UI.authentication;

import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

import it.lanos.eventbuddy.R;
import it.lanos.eventbuddy.data.IUserRepository;
import it.lanos.eventbuddy.data.source.models.Result;
import it.lanos.eventbuddy.util.ServiceLocator;

public class UserHelper {

    // Listener used for validating email address
    static void setEmailTextInputLayoutListener(Context context, TextInputLayout emailTextInputLayout) {
        Objects.requireNonNull(emailTextInputLayout.getEditText()).addTextChangedListener(new TextWatcher() {
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
                    emailTextInputLayout.setError(context.getString(R.string.not_valid_email));
                } else {
                    emailTextInputLayout.setError(null);
                }
            }
        });
    }

    // Listener used for validating password
    public static void setPasswordTextInputLayoutListener(Context context, TextInputLayout passwordTextInputLayout){
        Objects.requireNonNull(passwordTextInputLayout.getEditText()).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String passwordText = editable.toString().trim();
                if (passwordText.length() < 6) {
                    passwordTextInputLayout.setError(context.getString(R.string.weak_registration_password));
                } else {
                    passwordTextInputLayout.setError(null);
                }
            }
        });
    }

    // Check if the text secondTextInputLayout is equal to the text in firstTextInputLayout
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
                String firstTextString = getString(firstTextInputLayout);
                String secondTextString = getString(secondTextInputLayout);

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
                String firstTextString = getString(firstTextInputLayout);
                String secondTextString = getString(secondTextInputLayout);

                if(!secondTextString.equals(firstTextString)) {
                    secondTextInputLayout.setError(context.getString(R.string.password_doesnt_match));
                } else {
                    secondTextInputLayout.setError(null);
                }
            }
        });
    }

    // Listener used for a field that cannot be empty
    static void setTextInputLayoutListener(Context context, TextInputLayout textInputLayout) {
        Objects.requireNonNull(textInputLayout.getEditText()).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                String text = editable.toString().trim();
                if (text.isEmpty()) {
                    textInputLayout.setError(context.getString(R.string.required_field));
                } else {
                    textInputLayout.setError(null);
                }
            }
        });
    }

    // Check if a string is a valid email
    static boolean isValidEmail(String target) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    public static boolean isAuthSuccess(Result result) {
        return result instanceof Result.AuthSuccess;
    }

     public static boolean isError(Result result) {
         return result instanceof Result.Error;
    }

    // Initialize the view model
    public static UserViewModel initializeAndGetViewModel(AppCompatActivity activity) {
        IUserRepository userRepository =
                ServiceLocator.getInstance().getUserRepository(activity.getApplication());

        return new ViewModelProvider(activity,
                new UserViewModelFactory(userRepository)).get(UserViewModel.class);
    }

    // Return a trimmed string from a TextInputLayout
    public static String getString(TextInputLayout textInputLayout) {
        return Objects.requireNonNull(textInputLayout.getEditText()).getText().toString().trim();
    }

    public static void setupBackButtonHandling(AppCompatActivity activity, Class<?> destinationActivity) {
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                activity.startActivity(new Intent(activity, destinationActivity));
                activity.finish();
            }
        };
        activity.getOnBackPressedDispatcher().addCallback(activity, callback);
    }


}
