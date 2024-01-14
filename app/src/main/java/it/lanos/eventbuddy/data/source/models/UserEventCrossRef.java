package it.lanos.eventbuddy.data.source.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;

@Entity(primaryKeys = {"userId", "eventId"})
public class UserEventCrossRef implements Parcelable {
    @NonNull
    private String userId;
    @NonNull
    @ColumnInfo(index = true)
    private String eventId;
    @NonNull
    private Boolean joined;

    public UserEventCrossRef(@NonNull String userId, @NonNull String eventId, @NonNull Boolean joined) {
        this.userId = userId;
        this.eventId = eventId;
        this.joined = joined;
    }

    @NonNull
    public String getUserId() {
        return userId;
    }

    public void setUserId(@NonNull String userId) {
        this.userId = userId;
    }

    @NonNull
    public String getEventId() {
        return eventId;
    }

    public void setEventId(@NonNull String eventId) {
        this.eventId = eventId;
    }

    @NonNull
    public Boolean getJoined() {
        return joined;
    }

    public void setJoined(@NonNull Boolean joined) {
        this.joined = joined;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.userId);
        dest.writeString(this.eventId);
        dest.writeValue(this.joined);
    }

    public void readFromParcel(Parcel source) {
        this.userId = source.readString();
        this.eventId = source.readString();
        this.joined = (Boolean) source.readValue(Boolean.class.getClassLoader());
    }

    protected UserEventCrossRef(Parcel in) {
        this.userId = in.readString();
        this.eventId = in.readString();
        this.joined = (Boolean) in.readValue(Boolean.class.getClassLoader());
    }

    public static final Parcelable.Creator<UserEventCrossRef> CREATOR = new Parcelable.Creator<UserEventCrossRef>() {
        @Override
        public UserEventCrossRef createFromParcel(Parcel source) {
            return new UserEventCrossRef(source);
        }

        @Override
        public UserEventCrossRef[] newArray(int size) {
            return new UserEventCrossRef[size];
        }
    };
}
