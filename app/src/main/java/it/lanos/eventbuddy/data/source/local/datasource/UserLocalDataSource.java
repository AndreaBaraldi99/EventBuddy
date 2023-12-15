package it.lanos.eventbuddy.data.source.local.datasource;

import it.lanos.eventbuddy.data.source.entities.User;
import it.lanos.eventbuddy.data.source.local.EventsRoomDatabase;
import it.lanos.eventbuddy.data.source.local.dao.UserDao;

public class UserLocalDataSource extends BaseUserLocalDataSource {
    private final UserDao userDao;

    public UserLocalDataSource(EventsRoomDatabase newsRoomDatabase) {
        this.userDao = newsRoomDatabase.userDao();
    }

    @Override
    public void addUser(User user) {
        EventsRoomDatabase.databaseWriteExecutor.execute(() -> {
            userDao.insertUser(user);
            userCallback.onSuccessFromLocalDB(user);
        });
    }
}