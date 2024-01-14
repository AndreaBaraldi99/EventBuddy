package it.lanos.eventbuddy.adapter;

import static it.lanos.eventbuddy.util.Constants.PROFILE_PICTURES_BUCKET_REFERENCE;

import android.app.Application;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.selection.ItemDetailsLookup;
import androidx.recyclerview.selection.SelectionTracker;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

import it.lanos.eventbuddy.R;
import it.lanos.eventbuddy.data.source.models.User;
import it.lanos.eventbuddy.util.Constants;

public class AddGuestsRecyclerViewAdapter extends RecyclerView.Adapter<AddGuestsRecyclerViewAdapter.GuestViewHolder>{
    private final Application application;
    private final List<User> usersList;
    private List<User> alreadyInvitedList;
    private SelectionTracker<Long> selectionTracker;
    private final Context context;

    public void setSelectionTracker(SelectionTracker<Long> selectionTracker) {
        this.selectionTracker = selectionTracker;
    }

    @NonNull
    @Override
    public GuestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.add_guest_item, parent, false);
        return new GuestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AddGuestsRecyclerViewAdapter.GuestViewHolder holder, int position) {
        holder.bind(usersList.get(position));
        boolean isSelected = selectionTracker != null && selectionTracker.isSelected((long) position);
        holder.itemView.setActivated(isSelected);
        holder.itemView.setOnClickListener(view -> {
            if (selectionTracker != null) {
                selectionTracker.select((long) position);
            }
        });
        holder.bind(usersList.get(position));
    }

    @Override
    public int getItemCount() {
        if (usersList != null) {
            return usersList.size();
        }
        return 0;
    }

    public AddGuestsRecyclerViewAdapter(List <User> usersList, List<User> alreadyInvitedList, Application application, Context context) {
        this.usersList = usersList;
        this.alreadyInvitedList = alreadyInvitedList;
        this.application = application;
        this.context = context;
    }



    public class GuestViewHolder extends RecyclerView.ViewHolder {

        private final TextView userNameTextView;
        private final Button addButton;
        private final ImageView userImage;

        public GuestViewHolder(@NonNull View itemView) {
            super(itemView);
            userNameTextView = itemView.findViewById(R.id.nickUserPartecipating);
            userImage = itemView.findViewById(R.id.picUserPartecipating);
            addButton = itemView.findViewById(R.id.add_guest_button);
            addButton.setOnClickListener(view -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    boolean isSelected = !isItemSelected(position);
                    updateButtonState(isSelected);
                    setItemSelection(position, isSelected);
                }
            });
        }

        public void bind(User user) {
            userNameTextView.setText(user.getUsername());

            addButton.setText(R.string.add_text);
            for (User iter : alreadyInvitedList) {
                if (iter.getUsername().equals(user.getUsername())) {
                    addButton.setText(R.string.remove_text);
                }
            }

            addButton.setOnClickListener(v -> {
                String buttonText = addButton.getText().toString();

                if (buttonText.equals(context.getString(R.string.add_text))) {
                    addButton.setText(R.string.remove_text);
                    alreadyInvitedList.add(usersList.get(getAdapterPosition()));
                } else if (buttonText.equals(context.getString(R.string.remove_text))) {
                    addButton.setText(R.string.add_text);
                    alreadyInvitedList.remove(usersList.get(getAdapterPosition()));
                }


            });

            StorageReference storageReference = FirebaseStorage.getInstance().getReference()
                    .child(PROFILE_PICTURES_BUCKET_REFERENCE).child(user.getUserId());

            Glide.with(context)
                    .load(storageReference)
                    .error(Constants.PLACEHOLDER_IMAGE_URL)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(userImage);

        }

        public boolean isItemSelected(int position) {
            return selectionTracker != null && selectionTracker.isSelected((long) position);
        }

        public void updateButtonState(boolean isSelected) {
            this.isItemSelected(getAdapterPosition());

            if (isSelected) {
                addButton.setText(R.string.remove_text);
                alreadyInvitedList.add(usersList.get(getAdapterPosition()));


            } else {
                addButton.setText(R.string.add_text);
                alreadyInvitedList.remove(usersList.get(getAdapterPosition()));
            }
        }

        public ItemDetailsLookup.ItemDetails<Long> getItemDetails() {
            return new ItemDetailsLookup.ItemDetails<Long>() {
                @Override
                public int getPosition() {
                    return getAdapterPosition();
                }
                @Override
                public Long getSelectionKey() {
                    return (long) getAdapterPosition();
                }
            };
        }

        public void setItemSelection(int position, boolean isSelected) {
            if (selectionTracker != null) {
                if (isSelected) {
                    selectionTracker.select((long) position);
                } else {
                    selectionTracker.deselect((long) position);
                }
            }
            notifyItemChanged(position);
        }
    }
}
