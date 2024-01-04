package it.lanos.eventbuddy.data.source.local.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

import it.lanos.eventbuddy.data.source.models.User;
import it.lanos.eventbuddy.data.source.models.UserEventCrossRef;
import it.lanos.eventbuddy.data.source.models.UserWithEvents;

@Dao
public interface UserDao {
    @Transaction
    @Query("SELECT * FROM User")
    List<UserWithEvents> getUsersWithEvents();
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUser(User user);
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUsers(List<User> users);
    @Insert
    void insertUserWithEvents(UserEventCrossRef userEventCrossRef);
    @Transaction
    @Query("DELETE FROM User WHERE userId IN (SELECT userId FROM UserEventCrossRef WHERE eventId = :eventId GROUP BY userId HAVING COUNT(*) = 1)")
    void deleteUsersWithNoEvents(String eventId);
}
