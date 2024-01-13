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
import androidx.appcompat.content.res.AppCompatResources;

import com.bumptech.glide.Glide;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

import it.lanos.eventbuddy.R;
import it.lanos.eventbuddy.data.source.models.User;
import it.lanos.eventbuddy.data.source.models.UserEventCrossRef;

public class ShowFriendsAdapter extends ArrayAdapter<User> {
    private final List<User> partecipatingUsers;
    private final int layout;
    private final Context context;
    private final List<UserEventCrossRef> joinedUsers;

    public ShowFriendsAdapter(@NonNull Context context, int layout, List<User> partecipatingUsers, List<UserEventCrossRef> joinedUsers) {
        super(context, layout, partecipatingUsers);
        this.context = context;
        this.layout = layout;
        this.partecipatingUsers = partecipatingUsers;
        this.joinedUsers = joinedUsers;
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
        ImageView isUserJoining = convertView.findViewById(R.id.checkUserPartecipating);

        nickUserPartecipating.setText(partecipatingUsers.get(position).getUsername());

        StorageReference storageReference = FirebaseStorage.getInstance().getReference()
                .child(PROFILE_PICTURES_BUCKET_REFERENCE).child(partecipatingUsers.get(position).getUserId());

        isUserJoining.setImageDrawable(AppCompatResources.getDrawable(context, R.drawable.baseline_close_24));
        for(UserEventCrossRef cross : joinedUsers){
            if(cross.getUserId().equals(partecipatingUsers.get(position).getUserId())){
                if(cross.getJoined()){
                    isUserJoining.setImageDrawable(AppCompatResources.getDrawable(context, R.drawable.baseline_check_24));
                }
            }
        }


        Glide.with(context)
                .load(storageReference)
                .placeholder(R.drawable.logo)
                .into(picUserPartecipating);


        return convertView;
    }

}
