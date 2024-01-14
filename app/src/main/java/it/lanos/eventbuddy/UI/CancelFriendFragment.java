package it.lanos.eventbuddy.UI;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import org.jetbrains.annotations.Nullable;

import it.lanos.eventbuddy.R;
import it.lanos.eventbuddy.data.IUserRepository;
import it.lanos.eventbuddy.data.source.models.User;
import it.lanos.eventbuddy.util.ServiceLocator;

public class CancelFriendFragment extends DialogFragment {
    private User friendToRemove;
    private View view;

    @NonNull
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());

        LayoutInflater inflater = requireActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.fragment_remove_friends, null);

        builder.setTitle(R.string.remove_friends_title)
                .setMessage(R.string.remove_friends_text)
                .setPositiveButton(R.string.confirm_text, (dialog, which) -> removeFriend())
                .setNegativeButton(R.string.cancel_text, (dialog, which) -> dialog.dismiss());
        return builder.create();
    }
    public View onCreateView(@NonNull LayoutInflater inflater, @androidx.annotation.Nullable ViewGroup container, @androidx.annotation.Nullable Bundle savedInstanceState) {
        return view;
    }

    public void removeFriend() {
        Bundle args = getArguments();
        if (args != null) {
            User friendToRemove = args.getParcelable("userToRemove");

            if (friendToRemove != null) {
                IUserRepository userRepository = ServiceLocator.getInstance().getUserRepository(requireActivity().getApplication());
                userRepository.removeFriend(friendToRemove);
                FriendsFragment friendsFragment = (FriendsFragment) getParentFragment();
                assert friendsFragment != null;
                friendsFragment.refreshFriends();
            }
        }

        dismiss();
    }

}
