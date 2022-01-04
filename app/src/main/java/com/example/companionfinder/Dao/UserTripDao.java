package com.example.companionfinder.Dao;


import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Transaction;

import com.example.companionfinder.Model.UserTrip;

import java.util.List;


@Dao
public interface UserTripDao {
    @Query("SELECT * FROM [UserTrip]")
    List<UserTrip> getAll();

    @Query("SELECT * FROM [UserTrip] WHERE trip_id = :tripId")
    List<UserTrip> getAllByTripId(int tripId);

    @Transaction
    default void insert(int userId, int tripId){
        insertUserTrip(userId,tripId);



    }

    @Query("INSERT INTO [UserTrip] (user_id,trip_id) " +
            "values(:userId,:tripId)")
     void insertUserTrip(int userId, int tripId);

    @Query("DELETE FROM [USERTRIP] where user_id = :userId and trip_id = :tripId")
    void delete(int userId, int tripId);
}

