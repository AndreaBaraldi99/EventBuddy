package it.lanos.eventbuddy.data.source.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "Event", foreignKeys = @ForeignKey(entity = User.class,
        parentColumns = "userId",
        childColumns = "manager"))
public class Event {
    @PrimaryKey(autoGenerate = true)
    private long eventId = 0;
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

    public Event(String name, String date, String location, String description) {
        this.name = name;
        this.date = date;
        this.location = location;
        this.description = description;
    }
    public void setEventId(long eventId) {
        this.eventId = eventId;
    }
    public long getEventId() {
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

}


