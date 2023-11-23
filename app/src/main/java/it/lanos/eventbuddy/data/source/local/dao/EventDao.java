package it.lanos.eventbuddy.data.source.local.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

import it.lanos.eventbuddy.data.source.local.entities.EventWithUsers;
import it.lanos.eventbuddy.data.source.local.entities.LocalEvent;
import it.lanos.eventbuddy.data.source.local.entities.UserEventCrossRef;

@Dao
public interface EventDao {
    @Transaction
    @Query("SELECT * FROM Event")
    List<EventWithUsers> getEventsWithUsers();
    @Insert
    long insertEvent(LocalEvent event);
    @Insert
    void insertEventWithUsers(UserEventCrossRef userEventCrossRef);
}
