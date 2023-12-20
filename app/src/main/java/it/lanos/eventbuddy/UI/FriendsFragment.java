package it.lanos.eventbuddy.UI;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import it.lanos.eventbuddy.R;
import it.lanos.eventbuddy.data.IEventsRepository;
import it.lanos.eventbuddy.data.IUserRepository;
import it.lanos.eventbuddy.data.source.models.User;
import it.lanos.eventbuddy.util.ServiceLocator;

public class FriendsFragment extends Fragment {
    private List<User> user;
    private FriendsViewModel friendsViewModel;

    public FriendsFragment() {
        // Required empty public constructor
    }

    public static FriendsFragment newInstance(String param1, String param2) {
        FriendsFragment fragment = new FriendsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IUserRepository iUserRepository =
                ServiceLocator.getInstance().getUserRepository(requireActivity().getApplication());

        friendsViewModel = new ViewModelProvider(
                this,
                new FriendsViewModelFactory(iUserRepository)).get(FriendsViewModel.class);

        user = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_friends, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerViewFriends = view.findViewById(R.id.friendsRecyclerView);
        RecyclerView.LayoutManager layoutManager =
                new GridLayoutManager(requireContext(),4);
        recyclerViewFriends.setLayoutManager(layoutManager);
        recyclerViewFriends.setAdapter(new FriendsRecyclerViewAdapter(user));
    }
}