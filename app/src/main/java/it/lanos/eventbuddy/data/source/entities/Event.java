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
    @PrimaryKey
    final long eventId;
    @ColumnInfo(name = "manager", index = true)
    final long manager;
    @ColumnInfo(name = "name")
    final String name;
    @ColumnInfo(name = "date")
    final String date;
    @ColumnInfo(name = "location")
    final String location;
    @ColumnInfo(name = "description")
    final String description;

    public Event(long eventId, long manager, String name, String date, String location, String description) {
        this.eventId = eventId;
        this.manager = manager;
        this.name = name;
        this.date = date;
        this.location = location;
        this.description = description;
    }

    public long getEventId() {
        return eventId;
    }

    public long getManager() {
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


