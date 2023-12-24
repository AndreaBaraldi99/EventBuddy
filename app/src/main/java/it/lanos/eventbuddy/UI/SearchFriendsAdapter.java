package it.lanos.eventbuddy.UI;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import java.util.List;

import it.lanos.eventbuddy.R;
import it.lanos.eventbuddy.data.source.models.User;

public class SearchFriendsAdapter extends ArrayAdapter<User> {
    private List<User> searchingUsers;
    private final int layout;

    private FriendsFragment callback;



    public SearchFriendsAdapter(@NonNull Context context, int layout, List<User> searchingUsers, FriendsFragment frag) {
        super(context, layout, searchingUsers);
        this.searchingUsers = searchingUsers;
        this.layout = layout;
        this.callback = frag;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).
                    inflate(layout, parent, false);
        }

        TextView text = convertView.findViewById(R.id.usernameTextView);
        Button add = convertView.findViewById(R.id.add_guest_button);

        text.setText(searchingUsers.get(position).getUsername());
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user = searchingUsers.get(position);
                if(add.getText().equals("add")) {
                    add.setText("remove");
                    callback.onAddClick(user);
                }
                else {
                    add.setText("add");
                    callback.onRemoveClick(user);
                }


            }
        });


        return convertView;
    }
}
