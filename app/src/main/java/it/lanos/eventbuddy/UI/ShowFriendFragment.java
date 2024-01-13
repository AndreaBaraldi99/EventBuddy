package it.lanos.eventbuddy.UI;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import it.lanos.eventbuddy.R;
import it.lanos.eventbuddy.data.IUserRepository;
import it.lanos.eventbuddy.data.source.models.User;
import it.lanos.eventbuddy.data.source.models.UserEventCrossRef;
import it.lanos.eventbuddy.util.ServiceLocator;


public class ShowFriendFragment extends DialogFragment {
    private User friendToRemove;
    private View view;
    private List<User> invatedUsers;
    private List<UserEventCrossRef> joinedUsers;
    private ShowFriendsAdapter showFriendsAdapter;


    @NonNull
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());

        LayoutInflater inflater = requireActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.fragment_show_friend, null);

        Bundle args = getArguments();
        if(args != null){
            invatedUsers = args.getParcelableArrayList("iUsers");
            joinedUsers = args.getParcelableArrayList("pUsers");
        }

        builder.setView(view).setNegativeButton(R.string.cancel_text, (dialog, which) -> dialog.dismiss());
        return builder.create();
    }
    public View onCreateView(@NonNull LayoutInflater inflater, @androidx.annotation.Nullable ViewGroup container, @androidx.annotation.Nullable Bundle savedInstanceState) {
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @androidx.annotation.Nullable Bundle savedInstanceState) {
        ListView listViewPartecipatingFriends = view.findViewById(R.id.listFriendsPartecipating);
        List<User> partecipatingUsersforAdapter = new ArrayList<>();
        for(UserEventCrossRef cross : joinedUsers){
            for(User realUser : invatedUsers){
                if(cross.getUserId().equals(realUser.getUserId()))
                    partecipatingUsersforAdapter.add(realUser);

            }
        }

        showFriendsAdapter = new ShowFriendsAdapter(requireContext(), R.layout.show_friend_list_item, partecipatingUsersforAdapter);
        listViewPartecipatingFriends.setAdapter(showFriendsAdapter);
        super.onViewCreated(view, savedInstanceState);
    }



}
