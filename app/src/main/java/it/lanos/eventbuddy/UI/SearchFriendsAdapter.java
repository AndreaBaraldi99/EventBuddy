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

import java.util.Iterator;
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

        //SETUP LIST ITEM FOR CONSISTENCY
        add.setText("Add");
        Iterator it = callback.getUser().iterator();
        while(it.hasNext()){
            User iter = (User) it.next();
            if(iter.getUsername().equals(searchingUsers.get(position).getUsername())) {
                add.setText("Remove");
            }
        }
        //if(callback.getUser().contains(searchingUsers.get(position))){
            //add.setText("Remove");

        text.setText(searchingUsers.get(position).getUsername());
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user = searchingUsers.get(position);
                if(add.getText().toString().equals("Add")) {
                    add.setText("Remove");
                    callback.onAddClick(user);
                }
                else {
                    add.setText("Add");
                    callback.onRemoveClick(user);
                }


            }
        });


        return convertView;
    }
}
