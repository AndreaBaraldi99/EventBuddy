package it.lanos.eventbuddy.data.source.local.datasource;

import static it.lanos.eventbuddy.util.Constants.LAST_UPDATE;
import static it.lanos.eventbuddy.util.Constants.LAST_UPDATE_FRIENDS;
import static it.lanos.eventbuddy.util.Constants.SHARED_PREFERENCES_FILE_NAME;
import static it.lanos.eventbuddy.util.Constants.USER_NOT_FOUND;

import java.util.List;

import it.lanos.eventbuddy.data.source.models.User;
import it.lanos.eventbuddy.data.source.local.EventsRoomDatabase;
import it.lanos.eventbuddy.data.source.local.dao.UserDao;
import it.lanos.eventbuddy.util.SharedPreferencesUtil;

public class UserLocalDataSource extends BaseUserLocalDataSource {
    private final UserDao userDao;
    private final SharedPreferencesUtil sharedPreferencesUtil;

    public UserLocalDataSource(EventsRoomDatabase newsRoomDatabase, SharedPreferencesUtil sharedPreferencesUtil) {
        this.userDao = newsRoomDatabase.userDao();
        this.sharedPreferencesUtil = sharedPreferencesUtil;
    }

    @Override
    public void getFriends() {
        EventsRoomDatabase.databaseWriteExecutor.execute(() -> {
            List<User> friends = userDao.getFriends();
            sharedPreferencesUtil.writeStringData(SHARED_PREFERENCES_FILE_NAME,
                    LAST_UPDATE_FRIENDS, String.valueOf(System.currentTimeMillis()));
            userCallback.onSuccessFromLocalDB(friends);
        });
    }

    @Override
    public void addFriends(List<User> users) {
        EventsRoomDatabase.databaseWriteExecutor.execute(() -> {
            for (User user : users) {
                user.setIsFriend(1);
            }
            userDao.insertUsers(users);
            userCallback.onSuccessFromLocalDB(users);
        });
    }

    @Override
    public void updateFriend(User user) {
        EventsRoomDatabase.databaseWriteExecutor.execute(() -> {
            userDao.updateUsers(user);
            userCallback.onUpdatedFriendFromLocal(user);
        });
    }

    @Override
    public void updateUser(User user) {
        EventsRoomDatabase.databaseWriteExecutor.execute(() -> {
            userDao.updateUsers(user);
        });
    }


}