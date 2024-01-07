package it.lanos.eventbuddy.data.source.local.datasource;

import java.util.List;

import it.lanos.eventbuddy.data.source.models.User;
import it.lanos.eventbuddy.data.source.local.EventsRoomDatabase;
import it.lanos.eventbuddy.data.source.local.dao.UserDao;

public class UserLocalDataSource extends BaseUserLocalDataSource {
    private final UserDao userDao;

    public UserLocalDataSource(EventsRoomDatabase newsRoomDatabase) {
        this.userDao = newsRoomDatabase.userDao();
    }

    @Override
    public void addFriends(List<User> users) {
        EventsRoomDatabase.databaseWriteExecutor.execute(() -> {
            for (User user : users) {
                user.setIsFriend((short) 1);
            }
            userDao.insertUsers(users);
            userCallback.onSuccessFromLocalDB(users);
        });
    }

    @Override
    public void updateFriend(User user) {
        EventsRoomDatabase.databaseWriteExecutor.execute(() -> {
            if(userDao.updateUsers(user) != 0){
                userCallback.onUpdatedFriendFromLocal(user);
            } else {
                userCallback.onFailureFromLocal(new Exception("User not found"));
            }
        });
    }


}