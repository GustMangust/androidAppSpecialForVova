package com.example.companionfinder.Model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class Trip {
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "trip_id")
    public int tripId;

    @ColumnInfo(name = "datetime")
    public String dateTime;

    @ColumnInfo(name = "start_address")
    public String startAddress;

    @ColumnInfo(name="end_address")
    public String endAddress;

    @ColumnInfo(name="seats")
    public int seats;

    @ColumnInfo(name="transport_type")
    public String transportType;

    @ColumnInfo(name="cost")
    public double cost;

    @ColumnInfo(name="user_id")
    public int userId;

    @Ignore
    public double costForUser;

}
