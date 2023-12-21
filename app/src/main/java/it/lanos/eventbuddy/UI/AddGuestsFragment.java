package it.lanos.eventbuddy.UI;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


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
    private View view;

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
        view = inflater.inflate(R.layout.fragment_add_guests, null);

        builder.setView(view)
                // Add action buttons
                .setPositiveButton(R.string.add_description_confirm,
                        (dialog, id) -> {((CreateEventActivity) getActivity()).onGuestDialogConfirmClick();})
                .setNegativeButton(R.string.add_description_cancel,
                        (dialog, id) -> ((CreateEventActivity) getActivity()).onDialogCancelClick(this));
        return builder.create();
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        IUserRepository iUserRepository = ServiceLocator.getInstance().getUserRepository(requireActivity().getApplication());
        userList = new ArrayList<>();

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
                        // vedi https://developer.android.com/develop/ui/views/layout/recyclerview-custom?hl=it
                    }
                });
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(addGuestsRecyclerViewAdapter);

        SearchView searchView = view.findViewById(R.id.add_guest_search_view);
        //iUserRepository.searchUsers("mario");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                /*try {
                    iUserRepository.searchUsers(query).observe(getViewLifecycleOwner(), result -> {
                        if (result instanceof Result.UserSuccess) {
                            userList.clear();
                            userList.addAll(((Result.UserSuccess) result).getData());
                            addGuestsRecyclerViewAdapter.notifyDataSetChanged();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }
                return true;*/
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return handleSearch(newText);
            }
        });
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    private boolean handleSearch(String text) {
        String prova = text;
        IUserRepository iUserRepository = ServiceLocator.getInstance().getUserRepository(requireActivity().getApplication());
        try {
            iUserRepository.searchUsers(text).observe(getViewLifecycleOwner(), result -> {
                if (result instanceof Result.UserSuccess) {
                    userList.clear();
                    userList.addAll(((Result.UserSuccess) result).getData());
                    addGuestsRecyclerViewAdapter.notifyDataSetChanged();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

}