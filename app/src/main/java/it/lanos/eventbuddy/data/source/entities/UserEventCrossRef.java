package it.lanos.eventbuddy.data.source.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;

@Entity(primaryKeys = {"userId", "eventId"})
public class UserEventCrossRef {

    private long userId;
    @ColumnInfo(index = true)
    private long eventId;
    @NonNull
    private Boolean joined;

    public UserEventCrossRef(long userId, long eventId,@NonNull Boolean joined) {
        this.userId = userId;
        this.eventId = eventId;
        this.joined = joined;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getEventId() {
        return eventId;
    }

    public void setEventId(long eventId) {
        this.eventId = eventId;
    }

    @NonNull
    public Boolean getJoined() {
        return joined;
    }

    public void setJoined(@NonNull Boolean joined) {
        this.joined = joined;
    }

}
