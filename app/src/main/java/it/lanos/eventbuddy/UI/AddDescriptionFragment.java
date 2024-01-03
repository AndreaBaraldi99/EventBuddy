package it.lanos.eventbuddy.UI;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import android.view.LayoutInflater;
import android.view.View;

import com.google.android.material.textfield.TextInputLayout;

import it.lanos.eventbuddy.R;

public class AddDescriptionFragment extends DialogFragment {

    TextInputLayout textInputLayout;
    public AddDescriptionFragment() {
        // Required empty public constructor
    }

    public static AddDescriptionFragment newInstance() {
        return new AddDescriptionFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater.
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_add_description, null);
        textInputLayout = view.findViewById(R.id.AddDescriptionTextInputLayout);
        String description = ((CreateEventActivity) getActivity()).getDescription();
        if(description!=null)
            textInputLayout.getEditText().setText(description);
        // Inflate and set the layout for the dialog.
        // Pass null as the parent view because it's going in the dialog layout.
        builder.setView(view)
                // Add action buttons
                .setPositiveButton(R.string.confirm_text,
                        (dialog, id) -> {((CreateEventActivity) getActivity()).onDescriptionDialogConfirmClick(textInputLayout.getEditText().getText().toString(), this);})
                .setNegativeButton(R.string.cancel_text,
                        (dialog, id) -> ((CreateEventActivity) getActivity()).onDialogCancelClick(this));

        return builder.create();
    }

}