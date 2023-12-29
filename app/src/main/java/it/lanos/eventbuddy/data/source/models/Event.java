package it.lanos.eventbuddy.data.source.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.UUID;


@Entity(tableName = "Event", foreignKeys = @ForeignKey(entity = User.class,
        parentColumns = "userId",
        childColumns = "manager"))
public class Event implements Parcelable {
    @PrimaryKey
    @NonNull
    private String eventId;
    @ColumnInfo(name = "manager", index = true)
    private String manager = "";
    @ColumnInfo(name = "name")
    private String name;
    @ColumnInfo(name = "date")
    private String date;
    @ColumnInfo(name = "location")
    private String location;
    @ColumnInfo(name = "description")
    private String description;

    /**
     * Constructor to create an event with a given id (typically from the cloud db)
     *
     * @param eventId       id of the event
     * @param name          name of the event
     * @param date          date of the event
     * @param location      location of the event
     * @param description   description of the event
     */

    public Event(@NonNull String eventId, String name, String date, String location, String description) {
        this.eventId = eventId;
        this.name = name;
        this.date = date;
        this.location = location;
        this.description = description;
    }

    /**
     * Constructor to create an event with a random id (typically created from local user)
     *
     * @param name          name of the event
     * @param date          date of the event
     * @param location      location of the event
     * @param description   description of the event
     */
    @Ignore
    public Event(String name, String date, String location, String description){
        this.eventId = UUID.randomUUID().toString();
        this.name = name;
        this.date = date;
        this.location = location;
        this.description = description;
    }
    @NonNull
    public String getEventId() {
        return eventId;
    }
    public void setManager(String manager) {
        this.manager = manager;
    }
    public String getManager() {
        return manager;
    }
    @NonNull
    public String getName() {
        return name;
    }

    @NonNull
    public String getDate() {
        return date;
    }

    @NonNull
    public String getLocation() {
        return location;
    }

    public String getDescription() {
        return description;
    }

    /**
     * Static method to create an event from a cloud response
     *
     * @param cloudResponse     cloud response to create the event from
     * @return                  the event created from the cloud response
     */
    public static Event fromCloudResponse(EventsCloudResponse cloudResponse){
        Event event = new Event(cloudResponse.getUid(), cloudResponse.getName(), cloudResponse.getDate(), cloudResponse.getLocation(), cloudResponse.getDescription());
        event.setManager(cloudResponse.getManager());
        return event;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.eventId);
        dest.writeString(this.manager);
        dest.writeString(this.name);
        dest.writeString(this.date);
        dest.writeString(this.location);
        dest.writeString(this.description);
    }

    public void readFromParcel(Parcel source) {
        this.eventId = source.readString();
        this.manager = source.readString();
        this.name = source.readString();
        this.date = source.readString();
        this.location = source.readString();
        this.description = source.readString();
    }

    protected Event(Parcel in) {
        this.eventId = in.readString();
        this.manager = in.readString();
        this.name = in.readString();
        this.date = in.readString();
        this.location = in.readString();
        this.description = in.readString();
    }

    public static final Parcelable.Creator<Event> CREATOR = new Parcelable.Creator<Event>() {
        @Override
        public Event createFromParcel(Parcel source) {
            return new Event(source);
        }

        @Override
        public Event[] newArray(int size) {
            return new Event[size];
        }
    };

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Event){
            Event event = (Event) obj;
            return this.eventId.equals(event.getEventId());
        }
        return false;
    }
}


