package com.example.companionfinder.Model;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity()//foreignKeys = {@ForeignKey(entity = User.class, parentColumns = "user_id", childColumns = "user_id"),
        //@ForeignKey(entity = Trip.class, parentColumns  = "trip_id", childColumns = "trip_id")})
public class UserTrip {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "userTrip_id")
    public int userTripId;

    @ColumnInfo(name = "user_id")
    public int userId;

    @ColumnInfo(name = "trip_id")
    public int tripId;
}
