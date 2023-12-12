package it.lanos.eventbuddy.data.source.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

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

    public Event(@NonNull String eventId, String name, String date, String location, String description) {
        this.eventId = eventId;
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
    public static Event fromCloudResponse(EventsCloudResponse cloudResponse){
        return new Event(cloudResponse.getId(), cloudResponse.getName(), cloudResponse.getDate(), cloudResponse.getLocation(), cloudResponse.getDescription());
    }
}


