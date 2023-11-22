package it.lanos.eventbuddy.data.source.local.dao;

import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

import it.lanos.eventbuddy.data.source.local.entities.UserWithEvents;

@Dao
public interface UserDao {
    @Transaction
    @Query("SELECT * FROM User")
    List<UserWithEvents> getUsersWithEvents();

}
