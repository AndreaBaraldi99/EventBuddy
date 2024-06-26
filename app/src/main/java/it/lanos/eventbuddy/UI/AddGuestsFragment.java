package it.lanos.eventbuddy.UI;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.selection.ItemKeyProvider;
import androidx.recyclerview.selection.SelectionPredicates;
import androidx.recyclerview.selection.SelectionTracker;
import androidx.recyclerview.selection.StorageStrategy;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

import it.lanos.eventbuddy.R;
import it.lanos.eventbuddy.adapter.AddGuestsRecyclerViewAdapter;
import it.lanos.eventbuddy.data.IUserRepository;
import it.lanos.eventbuddy.data.source.models.Result;
import it.lanos.eventbuddy.data.source.models.User;
import it.lanos.eventbuddy.util.ServiceLocator;

public class AddGuestsFragment extends DialogFragment {

    private List<User> userList;
    private List<User> alreadyInvitedList;
    private AddGuestsRecyclerViewAdapter addGuestsRecyclerViewAdapter;
    private FirebaseUser currentUser;
    private FriendsViewModel friendsViewModel;
    private View view;

    public AddGuestsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userList = new ArrayList<>();
        alreadyInvitedList = new ArrayList<>();
        Bundle args = getArguments();
        if(args != null){
            alreadyInvitedList = args.getParcelableArrayList("iUsers");
        }

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
                .setPositiveButton(R.string.confirm_text,
                        (dialog, id) -> ((CreateEventActivity) requireActivity()).onGuestDialogConfirmClick())
                .setNegativeButton(R.string.cancel_text,
                        (dialog, id) -> ((CreateEventActivity) requireActivity()).onDialogCancelClick(this));
        return builder.create();
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        IUserRepository iUserRepository = ServiceLocator.getInstance().getUserRepository(requireActivity().getApplication());
        friendsViewModel = new ViewModelProvider(
                this,
                new FriendsViewModelFactory(iUserRepository)).get(FriendsViewModel.class);

        currentUser = friendsViewModel.getCurrentUser();

        friendsViewModel.searchUsers("").observe(getViewLifecycleOwner(), result -> {
            if (result instanceof Result.UserSuccess) {

                userList.clear();
                userList.addAll(((Result.UserSuccess) result).getData());
                if (userList != null) {
                    String currentUserId = currentUser.getUid();
                    userList.removeIf(user -> user.getUserId().equals(currentUserId));
                }
                addGuestsRecyclerViewAdapter.notifyDataSetChanged();

            }});

        RecyclerView recyclerView = view.findViewById(R.id.add_guests_recycler_view);
        RecyclerView.LayoutManager layoutManager =
                new LinearLayoutManager(requireContext(),
                        LinearLayoutManager.VERTICAL, false);



        addGuestsRecyclerViewAdapter = new AddGuestsRecyclerViewAdapter(userList, alreadyInvitedList, requireActivity().getApplication(), requireContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(addGuestsRecyclerViewAdapter);

        SearchView searchView = view.findViewById(R.id.add_guest_search_view);

        MyItemKeyProvider myItemKeyProvider = new MyItemKeyProvider(ItemKeyProvider.SCOPE_MAPPED, recyclerView);
        MyItemDetailsLookup myItemDetailsLookup = new MyItemDetailsLookup(recyclerView);



        SelectionTracker<Long> selectionTracker = new SelectionTracker.Builder<>(
                "mySelection",
                recyclerView,
                new MyItemKeyProvider(ItemKeyProvider.SCOPE_MAPPED, recyclerView),
                myItemDetailsLookup,
                StorageStrategy.createLongStorage()
        ).withSelectionPredicate(SelectionPredicates.createSelectAnything()).build();
        addGuestsRecyclerViewAdapter.setSelectionTracker(selectionTracker);
        selectionTracker.addObserver(new SelectionTracker.SelectionObserver<Long>() {
            @Override
            public void onItemStateChanged(@NonNull Long key, boolean selected) {
                super.onItemStateChanged(key, selected);
                CreateEventActivity createEventActivity = (CreateEventActivity) getActivity();
                assert createEventActivity != null;

                // Ensure the key is within bounds of the userList
                if (key >= 0 && key < userList.size()) {
                    if (selected) {
                        createEventActivity.onGuestAddClick(userList.get(key.intValue()));
                    } else {
                        createEventActivity.onGuestRemoveClick(userList.get(key.intValue()));
                    }
                } else {
                    Log.e("AddGuestsFragment", "Invalid key: " + key);
                }
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                handleSearch(newText);
                return true;
            }
        });
        super.onViewCreated(view, savedInstanceState);
    }

    private void handleSearch(String text) {
        friendsViewModel.searchUsers(text);
        if(text.equals("")){
            userList.clear();
            addGuestsRecyclerViewAdapter.notifyDataSetChanged();
        }
    }

}