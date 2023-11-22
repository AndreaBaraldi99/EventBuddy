package it.lanos.eventbuddy.data.source.local.dao;

import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

import it.lanos.eventbuddy.data.source.local.entities.EventWithUsers;

@Dao
public interface EventDao {
    @Transaction
    @Query("SELECT * FROM Event")
    List<EventWithUsers> getEventsWithUsers();
}
