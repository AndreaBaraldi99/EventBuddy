package it.lanos.eventbuddy.UI;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.android.material.search.SearchBar;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import it.lanos.eventbuddy.R;
import it.lanos.eventbuddy.data.IEventsRepository;
import it.lanos.eventbuddy.data.IUserRepository;
import it.lanos.eventbuddy.data.source.models.Result;
import it.lanos.eventbuddy.data.source.models.User;
import it.lanos.eventbuddy.util.ServiceLocator;

public class FriendsFragment extends Fragment {
    private List<User> user;
    private List<User> searchingUsers;
    private FriendsViewModel friendsViewModel;

    private FriendsRecyclerViewAdapter friendsAdapter;

    private SearchFriendsAdapter searchAdapter;

    public List<User> getUser() {
        return user;
    }

    public FriendsFragment() {
        // Required empty public constructor
    }

    public static FriendsFragment newInstance(String param1, String param2) {
        FriendsFragment fragment = new FriendsFragment();
        return fragment;
    }

    public void onAddClick(User user){
        this.user.add(user);
        friendsAdapter.notifyDataSetChanged();
    }

    public void onRemoveClick(User user){
        Iterator it = this.user.iterator();
        while(it.hasNext()){
            User iter = (User) it.next();
            if(iter.getUsername().equals(user.getUsername())) {
                this.user.remove(iter);
                break;
            }
        }
        friendsAdapter.notifyDataSetChanged();
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
        searchingUsers = new ArrayList<>();
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

        ViewCompat.setOnApplyWindowInsetsListener(view, (v, windowInsets) -> {
            Insets insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars());
            // Apply the insets as a margin to the view. This solution sets only the
            // bottom, left, and right dimensions, but you can apply whichever insets are
            // appropriate to your layout. You can also update the view padding if that's
            // more appropriate.
            ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
            mlp.leftMargin = insets.left;
            mlp.topMargin = insets.top;
            mlp.rightMargin = insets.right;
            v.setLayoutParams(mlp);

            // Return CONSUMED if you don't want want the window insets to keep passing
            // down to descendant views.
            return WindowInsetsCompat.CONSUMED;
        });

        SearchView searchView = view.findViewById(R.id.searchBar);
        ListView listViewFriendsSearch = view.findViewById(R.id.listViewSearch);
        searchAdapter = new SearchFriendsAdapter(getContext(), R.layout.add_guest_item, searchingUsers, this);
        listViewFriendsSearch.setAdapter(searchAdapter);

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


        RecyclerView recyclerViewFriends = view.findViewById(R.id.friendsRecyclerView);
        RecyclerView.LayoutManager layoutManager =
                new GridLayoutManager(requireContext(),4);
        recyclerViewFriends.setLayoutManager(layoutManager);
        friendsAdapter = new FriendsRecyclerViewAdapter(user);
        recyclerViewFriends.setAdapter(friendsAdapter);
    }

    private boolean handleSearch(String text) {
        IUserRepository iUserRepository = ServiceLocator.getInstance().getUserRepository(requireActivity().getApplication());
        if(text.equals("")){
            searchingUsers.clear();
            searchAdapter.notifyDataSetChanged();
        }
        else{
            try {
                iUserRepository.searchUsers(text).observe(getViewLifecycleOwner(), result -> {
                    if (result instanceof Result.UserSuccess) {
                        searchingUsers.clear();
                        searchingUsers.addAll(((Result.UserSuccess) result).getData());
                        searchAdapter.notifyDataSetChanged();
                    }
                });
            } catch (Exception e) {
                String stampa = e.toString();
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }
}