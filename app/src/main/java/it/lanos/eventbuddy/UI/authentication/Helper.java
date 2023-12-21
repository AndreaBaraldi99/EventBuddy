package it.lanos.eventbuddy.UI.authentication;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;

import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

import it.lanos.eventbuddy.R;
import it.lanos.eventbuddy.data.source.models.Result;

public class Helper {

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
    static void setPasswordTextInputLayoutListener(Context context, TextInputLayout passwordTextInputLayout){
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

    static boolean isAuthSuccess(Result result) {
        if(result instanceof Result.AuthSuccess) {
            return true;
        } else {
            return false;
        }
    }

     static boolean isError(Result result) {
        if(result instanceof Result.Error) {
            return true;
        } else {
            return false;
        }
    }
}