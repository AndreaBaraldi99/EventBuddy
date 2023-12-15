package it.lanos.eventbuddy.data.source.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.UUID;

import it.lanos.eventbuddy.data.source.firebase.cloudDB.EventsCloudResponse;


@Entity(tableName = "Event", foreignKeys = @ForeignKey(entity = User.class,
        parentColumns = "userId",
        childColumns = "manager"))
public class Event {
    @PrimaryKey
    @NonNull
    final String eventId;
    @ColumnInfo(name = "manager", index = true)
    private String manager = "";
    @ColumnInfo(name = "name")
    final String name;
    @ColumnInfo(name = "date")
    final String date;
    @ColumnInfo(name = "location")
    final String location;
    @ColumnInfo(name = "description")
    final String description;

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
        return new Event(cloudResponse.getUid(), cloudResponse.getName(), cloudResponse.getDate(), cloudResponse.getLocation(), cloudResponse.getDescription());
    }
}


