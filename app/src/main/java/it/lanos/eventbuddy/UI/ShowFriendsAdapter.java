package it.lanos.eventbuddy.UI;

import static it.lanos.eventbuddy.util.Constants.PROFILE_PICTURES_BUCKET_REFERENCE;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

import it.lanos.eventbuddy.R;
import it.lanos.eventbuddy.data.source.models.User;

public class ShowFriendsAdapter extends ArrayAdapter<User> {
    private List<User> partecipatingUsers;
    private final int layout;
    private final Context context;
    public ShowFriendsAdapter(@NonNull Context context, int layout, List<User> partecipatingUsers) {
        super(context, layout, partecipatingUsers);
        this.context = context;
        this.layout = layout;
        this.partecipatingUsers = partecipatingUsers;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).
                    inflate(layout, parent, false);
        }


        TextView nickUserPartecipating = convertView.findViewById(R.id.nickUserPartecipating);
        ImageView picUserPartecipating = convertView.findViewById(R.id.picUserPartecipating);

        nickUserPartecipating.setText(partecipatingUsers.get(position).getUsername());

        StorageReference storageReference = FirebaseStorage.getInstance().getReference()
                .child(PROFILE_PICTURES_BUCKET_REFERENCE).child(partecipatingUsers.get(position).getUserId());

        Glide.with(context)
                .load(storageReference)
                .placeholder(R.drawable.logo)
                .into(picUserPartecipating);


        return convertView;
    }

}
