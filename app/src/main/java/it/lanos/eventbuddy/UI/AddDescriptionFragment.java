package it.lanos.eventbuddy.UI;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.textfield.TextInputEditText;
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

    //Dato che l'inflate si fa in onCreateDialog non serve fare l'override di questo metodo
    //Si gestisce tutto sotto
    /*@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_description, container, false);
        textInputLayout = view.findViewById(R.id.AddDescriptionTextInputLayout);
        return inflater.inflate(R.layout.fragment_add_description, container, false);
    }*/

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
                .setPositiveButton(R.string.add_description_confirm,
                        (dialog, id) -> {((CreateEventActivity) getActivity()).onDialogConfirmClick(textInputLayout.getEditText().getText().toString(), this);})
                .setNegativeButton(R.string.add_description_cancel,
                        (dialog, id) -> ((CreateEventActivity) getActivity()).onDialogCancelClick(this));

        return builder.create();
    }

}