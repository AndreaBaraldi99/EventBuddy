package it.lanos.eventbuddy.UI;

import android.app.Application;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import it.lanos.eventbuddy.R;
import it.lanos.eventbuddy.data.source.models.User;

public class AddGuestsRecyclerViewAdapter extends RecyclerView.Adapter<AddGuestsRecyclerViewAdapter.NewViewHolder>{
    private final Application application;
    private final OnItemClickListener onItemClickListener;
    private final List<User> usersList;

    public interface OnItemClickListener {
        void onGuestItemClick(User user);
    }

    @NonNull
    @Override
    public AddGuestsRecyclerViewAdapter.NewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.add_guest_item, parent, false);

        return new AddGuestsRecyclerViewAdapter.NewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AddGuestsRecyclerViewAdapter.NewViewHolder holder, int position) {
        holder.bind(usersList.get(position));
    }

    @Override
    public int getItemCount() {
        if (usersList != null) {
            return usersList.size();
        }
        return 0;
    }

    public AddGuestsRecyclerViewAdapter(List <User> usersList, Application application, AddGuestsRecyclerViewAdapter.OnItemClickListener onItemClickListener) {
        this.usersList = usersList;
        this.application = application;
        this.onItemClickListener = onItemClickListener;
    }

    public class NewViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView userNameTextView;
        private Button addButton;

        public NewViewHolder(@NonNull View itemView) {
            super(itemView);
            userNameTextView = itemView.findViewById(R.id.usernameTextView);
            addButton = itemView.findViewById(R.id.add_guest_button);
            itemView.setOnClickListener(this);
        }

        public void bind(User user) {
            userNameTextView.setText(user.getUsername());
            //TODO: implementare visualizzazione immagine di profilo
        }


        @Override
        public void onClick(View v) {
            onItemClickListener.onGuestItemClick(usersList.get(getAdapterPosition()));
        }
    }
}
