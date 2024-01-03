package it.lanos.eventbuddy.UI;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import it.lanos.eventbuddy.R;
import it.lanos.eventbuddy.data.source.models.User;
import it.lanos.eventbuddy.data.source.models.mapbox.Suggestion;


public class suggestionAdapter extends ArrayAdapter<Suggestion> {
    private final List<Suggestion> suggestions;
    private final int layout;

    public suggestionAdapter(@NonNull Context context, int layout, List<Suggestion> suggestions) {
        super(context, layout, suggestions);
        this.suggestions = suggestions;
        this.layout = layout;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).
                    inflate(layout, parent, false);
        }

        TextView text = convertView.findViewById(R.id.text1);
        text.setText(suggestions.get(position).full_address);

        return convertView;
    }
}
