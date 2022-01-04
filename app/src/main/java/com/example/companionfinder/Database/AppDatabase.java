package com.example.companionfinder.Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.companionfinder.Dao.TripDao;
import com.example.companionfinder.Dao.UserDao;
import com.example.companionfinder.Dao.UserTripDao;
import com.example.companionfinder.Model.Trip;
import com.example.companionfinder.Model.User;
import com.example.companionfinder.Model.UserTrip;

@Database(entities = {User.class, Trip.class, UserTrip.class}, version = 13,exportSchema = false)
public abstract class AppDatabase extends RoomDatabase{
    private static final String DB_NAME = "database";
    private static volatile AppDatabase appDatabase;

    public abstract UserDao userDao();
    public abstract TripDao tripDao();
    public abstract UserTripDao userTripDao();

    public static synchronized AppDatabase getInstance(Context context){
        if (appDatabase == null) appDatabase = create(context);
        return appDatabase;
    }

    private static AppDatabase create(final Context context){
        return Room.databaseBuilder(context,AppDatabase.class,DB_NAME).allowMainThreadQueries().fallbackToDestructiveMigration().build();
    }
}
