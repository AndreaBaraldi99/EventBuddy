package it.lanos.eventbuddy.data.source.local.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

import it.lanos.eventbuddy.data.source.local.entities.LocalUser;
import it.lanos.eventbuddy.data.source.local.entities.UserEventCrossRef;
import it.lanos.eventbuddy.data.source.local.entities.UserWithEvents;

@Dao
public interface UserDao {
    @Transaction
    @Query("SELECT * FROM User")
    List<UserWithEvents> getUsersWithEvents();
    @Insert
    long insertUser(LocalUser user);
    @Insert
    void insertUserWithEvents(UserEventCrossRef userEventCrossRef);

}
