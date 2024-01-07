package it.lanos.eventbuddy.UI.authentication;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;

import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

import it.lanos.eventbuddy.R;

public class TextInputListenerHelper {

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
                if (!android.util.Patterns.EMAIL_ADDRESS.matcher(emailText).matches()) {
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
}
