package it.lanos.eventbuddy.UI;

import static it.lanos.eventbuddy.util.Constants.ENCRYPTED_DATA_FILE_NAME;

import android.app.Application;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import java.util.Iterator;
import java.util.List;

import it.lanos.eventbuddy.R;
import it.lanos.eventbuddy.UI.authentication.EventAdapterHelper;
import it.lanos.eventbuddy.data.EventRepository;
import it.lanos.eventbuddy.data.source.models.EventWithUsers;
import it.lanos.eventbuddy.data.source.models.User;
import it.lanos.eventbuddy.data.source.models.UserEventCrossRef;
import it.lanos.eventbuddy.util.DataEncryptionUtil;
import it.lanos.eventbuddy.util.Parser;
import it.lanos.eventbuddy.util.ServiceLocator;

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
    private EventViewModel eventViewModel;





    public EventRecyclerViewAdapter(EventViewModel eventViewModel, List<EventWithUsers> eventList, Application application, OnItemClickListener onItemClickListener) {
        this.eventList = eventList;
        this.application = application;
        this.onItemClickListener = onItemClickListener;
        this.eventViewModel = eventViewModel;

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
        private final TextView textDate;
        private final Button joinButton;
        private boolean joined = false;



        public NewViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.ItemNameTextView);
            textViewTime = itemView.findViewById(R.id.ItemTimeTextView);
            textViewLocation=itemView.findViewById(R.id.ItemLocationTextView);
            textDate = itemView.findViewById(R.id.itemDate);
            joinButton = itemView.findViewById(R.id.ItemCheckButton);

            itemView.setOnClickListener(this);

        }

        public void bind(EventWithUsers event) {
            textViewName.setText(event.getEvent().getName());
            String date_event = Parser.formatDateForEventList(event.getEvent().getDate());
            textDate.setText(date_event);
            String time = Parser.formatTime(event.getEvent().getDate());
            textViewTime.setText(time);
            handleConfigurationJoinButton(event);

            joinButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    configureButtonJoin(event);
                }
            });
            String address = event.getEvent().getLocation();
            String showAddress =  address.split(",")[0];
            textViewLocation.setText(showAddress);

        }


        @Override
        public void onClick(View v) {
            onItemClickListener.onEventItemClick(eventList.get(getAdapterPosition()));
        }

        private void configureButtonJoin(EventWithUsers event){
            if(joined == true){
                joinButton.setBackgroundColor(application.getResources().getColor(R.color.md_theme_light_onPrimary));
                eventViewModel.leaveEvent(event.getEvent().getEventId());
                joined = false;
            }
            else{
                joinButton.setBackgroundColor(application.getResources().getColor(R.color.md_theme_light_secondaryContainer));
                eventViewModel.joinEvent(event.getEvent().getEventId());
                joined = true;
            }

        }
        private User readUser(DataEncryptionUtil dataEncryptionUtil){
            User user = null;
            try {
                user = new Gson().fromJson(dataEncryptionUtil.readSecretDataOnFile(ENCRYPTED_DATA_FILE_NAME), User.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return user;
        }
        private void handleConfigurationJoinButton(EventWithUsers event) {
            User user = readUser(new DataEncryptionUtil(application));
            List<UserEventCrossRef> usersInfo = event.getUserEventCrossRefs();
            Iterator it = usersInfo.iterator();
            while(it.hasNext()){
                UserEventCrossRef current = (UserEventCrossRef) it.next();
                if(current.getUserId().equals(user.getUserId()) && current.getJoined()) {
                    joinButton.setBackgroundColor(application.getResources().getColor(R.color.md_theme_light_secondaryContainer));
                    this.joined = true;
                    break;
                }
                else if(current.getUserId().equals(user.getUserId()) && !current.getJoined()) {
                    joinButton.setBackgroundColor(application.getResources().getColor(R.color.md_theme_light_onPrimary));
                    this.joined = false;
                    break;
                }
            }

        }
    }




}
