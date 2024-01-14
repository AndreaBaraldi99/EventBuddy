package it.lanos.eventbuddy.adapter;

import static it.lanos.eventbuddy.util.Constants.PROFILE_PICTURES_BUCKET_REFERENCE;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

import it.lanos.eventbuddy.R;
import it.lanos.eventbuddy.UI.FriendsFragment;
import it.lanos.eventbuddy.data.source.models.User;
import it.lanos.eventbuddy.util.Constants;

public class SearchFriendsAdapter extends ArrayAdapter<User> {
    private final List<User> searchingUsers;
    private final int layout;
    private final Context context;
    private final FriendsFragment callback;



    public SearchFriendsAdapter(@NonNull Context context, int layout, List<User> searchingUsers, FriendsFragment frag) {
        super(context, layout, searchingUsers);
        this.searchingUsers = searchingUsers;
        this.layout = layout;
        this.callback = frag;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).
                    inflate(layout, parent, false);
        }

        TextView text = convertView.findViewById(R.id.nickUserPartecipating);
        Button add = convertView.findViewById(R.id.add_guest_button);
        ImageView userImage = convertView.findViewById(R.id.picUserPartecipating);

        StorageReference storageReference = FirebaseStorage.getInstance().getReference()
                .child(PROFILE_PICTURES_BUCKET_REFERENCE).child(searchingUsers.get(position).getUserId());

        Glide.with(context)
                .load(storageReference)
                .error(Constants.PLACEHOLDER_IMAGE_URL)
                .into(userImage);

        //SETUP LIST ITEM FOR CONSISTENCY
        add.setText(R.string.add_text);
        for (User iter : callback.getUser()) {
            if (iter.getUsername().equals(searchingUsers.get(position).getUsername())) {
                add.setText(R.string.remove_text);
            }
        }

        text.setText(searchingUsers.get(position).getUsername());
        add.setOnClickListener(v -> {
            User user = searchingUsers.get(position);
            String buttonText = add.getText().toString();

            if (buttonText.equals(getContext().getString(R.string.add_text))) {
                add.setText(R.string.remove_text);
                callback.onAddClick(user);
            } else if (buttonText.equals(getContext().getString(R.string.remove_text))) {
                add.setText(R.string.add_text);
                callback.onRemoveClick(user);
            }


        });
        return convertView;
    }
}
