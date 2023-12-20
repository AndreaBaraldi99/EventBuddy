package it.lanos.eventbuddy.UI;

import android.app.Application;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import it.lanos.eventbuddy.R;
import it.lanos.eventbuddy.data.source.models.EventWithUsers;

/**
 * Custom adapter that extends RecyclerView.Adapter to show an ArrayList of News
 * with a RecyclerView.
 */
public class EventRecyclerViewAdapter extends
        RecyclerView.Adapter<EventRecyclerViewAdapter.NewViewHolder> {

    /**
     * Interface to associate a click listener with
     * a RecyclerView item.
     */
    public interface OnItemClickListener {
        void onEventItemClick(EventWithUsers event);
    }

    private final List<EventWithUsers> eventList;
    private final Application application;
    private final OnItemClickListener onItemClickListener;





    public EventRecyclerViewAdapter(List<EventWithUsers> eventList, Application application, OnItemClickListener onItemClickListener) {
        this.eventList = eventList;
        this.application = application;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public NewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.event_list_item, parent, false);

        return new NewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewViewHolder holder, int position) {
        holder.bind(eventList.get(position));
    }

    @Override
    public int getItemCount() {
        if (eventList != null) {
            return eventList.size();
        }
        return 0;
    }

    /**
     * Custom ViewHolder to bind data to the RecyclerView items.
     */
    public class NewViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private final TextView textViewName;
        private final TextView textViewTime;
        private final TextView textViewLocation;
        private final Button dateButton;

        public NewViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.ItemNameTextView);
            textViewTime = itemView.findViewById(R.id.ItemTimeTextView);
            textViewLocation=itemView.findViewById(R.id.ItemLocationTextView);
            dateButton = itemView.findViewById(R.id.ItemDateButton);
            itemView.setOnClickListener(this);
        }

        public void bind(EventWithUsers event) {
            textViewName.setText(event.getEvent().getName());
            dateButton.setText(event.getEvent().getDate().substring(0,8));
            textViewTime.setText(event.getEvent().getDate().substring(9));
            textViewLocation.setText(event.getEvent().getLocation());

        }


        @Override
        public void onClick(View v) {
            onItemClickListener.onEventItemClick(eventList.get(getAdapterPosition()));
        }
    }
    }
