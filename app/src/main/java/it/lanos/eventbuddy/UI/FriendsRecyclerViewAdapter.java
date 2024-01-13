package it.lanos.eventbuddy.UI;

import static it.lanos.eventbuddy.util.Constants.PROFILE_PICTURES_BUCKET_REFERENCE;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

import it.lanos.eventbuddy.R;
import it.lanos.eventbuddy.data.source.models.User;
import it.lanos.eventbuddy.util.Constants;

public class FriendsRecyclerViewAdapter extends RecyclerView.Adapter<FriendsRecyclerViewAdapter.FriendViewHolder>{

    private final Context context;
    private final List<User> friends;
    private OnGridItemClickListener onGridItemClickListener;

    public FriendsRecyclerViewAdapter(List<User> friends, Context context) {
        this.friends = friends;
        this.context = context;
    }

    @NonNull
    @Override
    public FriendViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.friend_list_item, parent, false);
        return new FriendViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendViewHolder holder, int position) {
        holder.bind(friends.get(position));

        User user = friends.get(position);
        holder.profilePic.setOnClickListener(v -> {
            if (onGridItemClickListener != null) {
                onGridItemClickListener.onGridItemClick(user);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (friends != null) {
            return friends.size();
        }
        return 0;
    }

    public interface OnGridItemClickListener {
        void onGridItemClick(User user);
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
            profilePic = itemView.findViewById(R.id.profilePic);

            StorageReference storageReference = FirebaseStorage.getInstance().getReference()
                    .child(PROFILE_PICTURES_BUCKET_REFERENCE).child(user.getUserId());

            Glide.with(context)
                    .load(storageReference)
                    .error(Constants.PLACEHOLDER_IMAGE_URL)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(profilePic);
        }
    }
}
