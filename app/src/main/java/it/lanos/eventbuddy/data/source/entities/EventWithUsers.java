package it.lanos.eventbuddy.data.source.entities;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import java.util.ArrayList;
import java.util.List;

public class EventWithUsers implements Parcelable {
    @Embedded
    private Event event;
    @Relation(
            parentColumn = "eventId",
            entityColumn = "userId",
            associateBy = @Junction(UserEventCrossRef.class)
    )
    private List<User> users;

    public EventWithUsers(Event event, List<User> users) {
        this.event = event;
        this.users = users;
    }

    public Event getEvent() {
        return event;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable((Parcelable) this.event, flags);
        dest.writeList(this.users);
    }

    public void readFromParcel(Parcel source) {
        this.event = source.readParcelable(Event.class.getClassLoader());
        this.users = new ArrayList<User>();
        source.readList(this.users, User.class.getClassLoader());
    }

    protected EventWithUsers(Parcel in) {
        this.event = in.readParcelable(Event.class.getClassLoader());
        this.users = new ArrayList<User>();
        in.readList(this.users, User.class.getClassLoader());
    }

    public static final Parcelable.Creator<EventWithUsers> CREATOR = new Parcelable.Creator<EventWithUsers>() {
        @Override
        public EventWithUsers createFromParcel(Parcel source) {
            return new EventWithUsers(source);
        }

        @Override
        public EventWithUsers[] newArray(int size) {
            return new EventWithUsers[size];
        }
    };
}
