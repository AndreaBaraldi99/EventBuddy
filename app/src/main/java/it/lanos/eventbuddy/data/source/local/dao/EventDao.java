package it.lanos.eventbuddy.data.source.local.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

import it.lanos.eventbuddy.data.source.models.EventWithUsers;
import it.lanos.eventbuddy.data.source.models.Event;
import it.lanos.eventbuddy.data.source.models.UserEventCrossRef;

@Dao
public interface EventDao {
    @Transaction
    @Query("SELECT * FROM Event")
    List<EventWithUsers> getEventsWithUsers();
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertEvent(Event event);
    @Insert
    void insertEventWithUsers(UserEventCrossRef userEventCrossRef);
}
