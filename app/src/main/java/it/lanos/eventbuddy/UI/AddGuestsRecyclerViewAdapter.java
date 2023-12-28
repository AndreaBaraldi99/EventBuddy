package it.lanos.eventbuddy.UI;

import android.app.Application;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.selection.ItemDetailsLookup;
import androidx.recyclerview.selection.SelectionTracker;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import it.lanos.eventbuddy.R;
import it.lanos.eventbuddy.data.source.models.User;

public class AddGuestsRecyclerViewAdapter extends RecyclerView.Adapter<AddGuestsRecyclerViewAdapter.GuestViewHolder>{
    private final Application application;
    private final List<User> usersList;
    private SelectionTracker<Long> selectionTracker;

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
        holder.updateButtonState(isSelected);
    }

    @Override
    public int getItemCount() {
        if (usersList != null) {
            return usersList.size();
        }
        return 0;
    }

    public AddGuestsRecyclerViewAdapter(List <User> usersList, Application application) {
        this.usersList = usersList;
        this.application = application;
    }

    public class GuestViewHolder extends RecyclerView.ViewHolder {

        private TextView userNameTextView;
        private Button addButton;

        public GuestViewHolder(@NonNull View itemView) {
            super(itemView);
            userNameTextView = itemView.findViewById(R.id.usernameTextView);
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
            //TODO: implementare visualizzazione immagine di profilo
        }

        public boolean isItemSelected(int position) {
            return selectionTracker != null && selectionTracker.isSelected((long) position);
        }

        public void updateButtonState(boolean isSelected) {
            this.isItemSelected(getAdapterPosition());

            if (isSelected) {
                addButton.setText(R.string.create_event_button_remove_text);
            } else {
                addButton.setText(R.string.create_event_button_add_text);
            }
        }

        public ItemDetailsLookup.ItemDetails<Long> getItemDetails() {
            return new ItemDetailsLookup.ItemDetails<Long>() {
                @Override
                public int getPosition() {
                    return getAdapterPosition();
                }

                @Nullable
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
