package it.lanos.eventbuddy.UI;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import it.lanos.eventbuddy.R;
import it.lanos.eventbuddy.data.IUserRepository;
import it.lanos.eventbuddy.data.source.models.Result;
import it.lanos.eventbuddy.data.source.models.User;
import it.lanos.eventbuddy.util.ServiceLocator;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddGuestsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddGuestsFragment extends DialogFragment {

    private List<User> userList;
    private AddGuestsRecyclerViewAdapter addGuestsRecyclerViewAdapter;

    public AddGuestsFragment() {
        // Required empty public constructor
    }

    public static AddGuestsFragment newInstance() {
        AddGuestsFragment fragment = new AddGuestsFragment();
        return fragment;
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
        View view = inflater.inflate(R.layout.fragment_add_guests, null);

        SearchView searchView = view.findViewById(R.id.add_guest_search_view);
        builder.setView(view)
                // Add action buttons
                .setPositiveButton(R.string.add_description_confirm,
                        (dialog, id) -> {((CreateEventActivity) getActivity()).onGuestDialogConfirmClick();})
                .setNegativeButton(R.string.add_description_cancel,
                        (dialog, id) -> ((CreateEventActivity) getActivity()).onDialogCancelClick(this));
        return builder.create();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        IUserRepository iUserRepository = ServiceLocator.getInstance().getUserRepository(requireActivity().getApplication());
        userList = new ArrayList<>();

        Button addGuestButton = view.findViewById(R.id.GuestsIconButton);
        TextView guestUsername = view.findViewById(R.id.usernameTextView);
        RecyclerView recyclerView = view.findViewById(R.id.add_guests_recycler_view);
        RecyclerView.LayoutManager layoutManager =
                new LinearLayoutManager(requireContext(),
                        LinearLayoutManager.VERTICAL, false);

        addGuestsRecyclerViewAdapter = new AddGuestsRecyclerViewAdapter(userList, requireActivity().getApplication(),
                new AddGuestsRecyclerViewAdapter.OnItemClickListener() {
                    @Override
                    public void onGuestItemClick(User user) {
                        //aggiungere il guest
                        //cambiare il colore del pulsante
                    }
                });
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(addGuestsRecyclerViewAdapter);

        SearchView searchView = view.findViewById(R.id.add_guest_search_view);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                iUserRepository.searchUsers(query).observe(getViewLifecycleOwner(), result -> {
                    if (result instanceof Result.UserSuccess) {
                        userList.clear();
                        userList.addAll(((Result.UserSuccess) result).getData());
                        addGuestsRecyclerViewAdapter.notifyDataSetChanged();
                    }
                });
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });



    }
}