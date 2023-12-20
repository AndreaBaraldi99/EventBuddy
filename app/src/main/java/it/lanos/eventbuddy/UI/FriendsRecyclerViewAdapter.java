package it.lanos.eventbuddy.UI;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.imageview.ShapeableImageView;

import java.util.List;

import it.lanos.eventbuddy.R;
import it.lanos.eventbuddy.data.source.models.User;

public class FriendsRecyclerViewAdapter extends RecyclerView.Adapter<FriendsRecyclerViewAdapter.FriendViewHolder>{


    private final List<User> friends;

    public FriendsRecyclerViewAdapter(List<User> friends) {
        this.friends = friends;
    }

    @NonNull
    @Override
    public FriendViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.friend_list_item, parent, false);
        FriendViewHolder friendViewHolder = new FriendViewHolder(view);
        return friendViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull FriendViewHolder holder, int position) {
        holder.bind(friends.get(position));
    }

    @Override
    public int getItemCount() {
        if (friends != null) {
            return friends.size();
        }
        return 0;
    }

    public class FriendViewHolder extends RecyclerView.ViewHolder{

        ShapeableImageView profilePic;
        TextView nickname;

        public FriendViewHolder(@NonNull View itemView) {
            super(itemView);
            profilePic = itemView.findViewById(R.id.profilePic);
            nickname = itemView.findViewById(R.id.nickname);
        }

        public void bind(User user) {
            nickname.setText(user.getUsername());
            //TODO: Trovare il modo per settare anche l'immagine di profilo utente
        }
    }
}
