package it.lanos.eventbuddy.data.source.local.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import java.util.List;

import it.lanos.eventbuddy.data.source.models.User;
import it.lanos.eventbuddy.data.source.models.UserEventCrossRef;

@Dao
public interface UserDao {
    @Transaction
    @Query("SELECT * FROM User WHERE isFriend = 1")
    List<User> getUserWithFriends();
    @Update
    int updateUsers(User... users);
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
