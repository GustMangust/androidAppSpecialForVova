package com.example.companionfinder.Dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import com.example.companionfinder.Model.Trip;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Dao
public interface TripDao {
    @Query("SELECT * FROM [Trip]")
    List<Trip> getAll();

    @Transaction
    default List<Trip> getTrips(int userId){
        List<Integer> listTripIds = new ArrayList<Integer>(getTripIds(userId).length);
        for (int i : getTripIds(userId))
        {
            listTripIds.add(i);
        }
        return getAll().stream().filter(x-> listTripIds.contains(x.tripId)).collect(Collectors.toList());
    }
    @Query("SELECT * FROM [Trip] WHERE user_id = :userId")
    List<Trip> getMyTrips(int userId);

    @Query("SELECT trip_id FROM [UserTrip] WHERE user_id = :userId")
    int[] getTripIds(int userId);

    @Query("SELECT * FROM [Trip] WHERE trip_id = :tripId")
    Trip getTrip(int tripId);

    @Insert
    void insert(Trip trip);

    @Insert
    void insert(List<Trip> trip);

    @Delete
    void delete(Trip trip);

    @Transaction
    default void deleteCascade(Trip trip){
        delete(trip);
        deleteUserTrips(trip.tripId);
    }

    @Query("DELETE FROM [UserTrip] WHERE trip_id =:tripId")
    void deleteUserTrips(int tripId);
}
