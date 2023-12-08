package it.lanos.eventbuddy.data.source.local.datasource;

import java.util.List;

import it.lanos.eventbuddy.data.source.entities.EventWithUsers;
import it.lanos.eventbuddy.data.source.entities.UserEventCrossRef;
import it.lanos.eventbuddy.data.source.local.EventsRoomDatabase;
import it.lanos.eventbuddy.data.source.local.dao.EventDao;
import it.lanos.eventbuddy.data.source.local.dao.UserDao;
import it.lanos.eventbuddy.data.source.local.datasource.BaseEventsLocalDataSource;

public class EventsLocalDataSource extends BaseEventsLocalDataSource {
    private final EventDao eventDao;
    private final UserDao userDao;
    //private final SharedPreferencesUtil sharedPreferencesUtil;

    public EventsLocalDataSource(EventsRoomDatabase newsRoomDatabase) {
        this.eventDao = newsRoomDatabase.eventDao();
        this.userDao = newsRoomDatabase.userDao();
        //this.sharedPreferencesUtil = sharedPreferencesUtil;
    }
    @Override
    public void getEvents() {
        EventsRoomDatabase.databaseWriteExecutor.execute(() -> {
            eventsCallback.onSuccessFromLocal(eventDao.getEventsWithUsers());
        });
    }
    @Override
    public void insertEvent(EventWithUsers eventWithUsers){
        EventsRoomDatabase.databaseWriteExecutor.execute(() -> {
            List<Long> insertedEventsIds = userDao.insertUsers(eventWithUsers.getUsers());
            long insertedEventId = eventDao.insertEvent(eventWithUsers.getEvent());
            for (Long userId : insertedEventsIds) {
                eventDao.insertEventWithUsers(new UserEventCrossRef(userId, insertedEventId, false));
            }
            eventsCallback.onSuccessFromLocal(eventDao.getEventsWithUsers());
        });
    }


    /*@Override
    public void insertNews(List<News> newsList) {
        NewsRoomDatabase.databaseWriteExecutor.execute(() -> {
            // Reads the news from the database
            List<News> allNews = newsDao.getAll();

            if (newsList != null) {

                // Checks if the news just downloaded has already been downloaded earlier
                // in order to preserve the news status (marked as favorite or not)
                for (News news : allNews) {
                    // This check works because News and NewsSource classes have their own
                    // implementation of equals(Object) and hashCode() methods
                    if (newsList.contains(news)) {
                        // The primary key and the favorite status is contained only in the News objects
                        // retrieved from the database, and not in the News objects downloaded from the
                        // Web Service. If the same news was already downloaded earlier, the following
                        // line of code replaces the News object in newsList with the corresponding
                        // line of code replaces the News object in newsList with the corresponding
                        // News object saved in the database, so that it has the primary key and the
                        // favorite status.
                        newsList.set(newsList.indexOf(news), news);
                    }
                }

                // Writes the news in the database and gets the associated primary keys
                List<Long> insertedNewsIds = newsDao.insertNewsList(newsList);
                for (int i = 0; i < newsList.size(); i++) {
                    // Adds the primary key to the corresponding object News just downloaded so that
                    // if the user marks the news as favorite (and vice-versa), we can use its id
                    // to know which news in the database must be marked as favorite/not favorite
                    newsList.get(i).setId(insertedNewsIds.get(i));
                }

                sharedPreferencesUtil.writeStringData(SHARED_PREFERENCES_FILE_NAME,
                        LAST_UPDATE, String.valueOf(System.currentTimeMillis()));

                newsCallback.onSuccessFromLocal(newsList);
            }
        });
    }*/
}
