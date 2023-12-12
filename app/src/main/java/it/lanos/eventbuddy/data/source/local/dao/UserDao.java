package it.lanos.eventbuddy.data.source.local.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

import it.lanos.eventbuddy.data.source.entities.User;
import it.lanos.eventbuddy.data.source.entities.UserEventCrossRef;
import it.lanos.eventbuddy.data.source.entities.UserWithEvents;

@Dao
public interface UserDao {
    @Transaction
    @Query("SELECT * FROM User")
    List<UserWithEvents> getUsersWithEvents();
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insertUser(User user);
    @Insert
    List<Long> insertUsers(List<User> users);
    @Insert
    void insertUserWithEvents(UserEventCrossRef userEventCrossRef);

}
