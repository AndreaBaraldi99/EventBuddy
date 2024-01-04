package it.lanos.eventbuddy.data.source.local.dao;

import androidx.room.Dao;
import androidx.room.Delete;
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
    @Query("SELECT * FROM Event ORDER BY eventId DESC")
    List<EventWithUsers> getEventsWithUsers();
    @Transaction
    @Query("SELECT * FROM Event WHERE eventId = :eventId")
    EventWithUsers getEventWithUsers(String eventId);
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertEvent(Event event);
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertEventWithUsers(UserEventCrossRef userEventCrossRef);
    @Delete
    void deleteEvent(Event event);
    @Transaction
    @Query("DELETE FROM UserEventCrossRef WHERE eventId = :eventId")
    void deleteUserEventCrossRef(String eventId);
}
