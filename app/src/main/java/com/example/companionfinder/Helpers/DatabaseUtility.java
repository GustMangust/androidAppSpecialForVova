package com.example.companionfinder.Helpers;

import android.content.Context;

import com.example.companionfinder.Database.AppDatabase;
import com.example.companionfinder.Model.Trip;

import java.time.Clock;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.List;
import java.util.stream.Collectors;

public class DatabaseUtility {
    static AppDatabase db;
    public static List<Trip> GetActualTrips(Context context, boolean filterFullTrips, boolean filterExpired){
        db = AppDatabase.getInstance(context);
        List<Trip> trips = filterFullTrips ? db.tripDao().getAll().stream().filter(x->GetSeatsByTripId(context,x.tripId)>0).collect(Collectors.toList()) : db.tripDao().getAll();
        List<Trip> filteredTrips = filterExpired ? trips.stream().filter(x->OffsetDateTime.now(Clock.systemUTC()).isBefore(ConvertToOffsetDateTime(x.dateTime))).collect(Collectors.toList()) : trips;

        for(Trip trip:filteredTrips){
            int amountOfTrips = (int)db.userTripDao().getAllByTripId(trip.tripId).stream().count();
            trip.costForUser = amountOfTrips > 0 ? trip.cost/amountOfTrips : trip.cost;
        }
        return filteredTrips;
    }

    private static OffsetDateTime ConvertToOffsetDateTime(String dateTime) {
        String[] date = dateTime.substring(0,dateTime.indexOf(" ")).split("/");
        String[] time = dateTime.substring(dateTime.indexOf(" ")+1).split(":");
        int hour = time[0].startsWith("0") ? Integer.parseInt(time[0].substring(1)) : Integer.parseInt(time[0]);
        int minute = time[1].startsWith("0") ? Integer.parseInt(time[1].substring(1)) : Integer.parseInt(time[1]);
        Instant instant = Instant.now();
        ZoneId systemZone = ZoneId.of("UTC+03:00");
        ZoneOffset zoneOffset = systemZone.getRules().getOffset(instant);
        return OffsetDateTime.of(Integer.parseInt(date[2]),Integer.parseInt(date[1]),Integer.parseInt(date[0]),hour,minute,0,0,zoneOffset);
    }

    public static int GetSeatsByTripId(Context context, int tripId){
        return (int)(db.tripDao().getTrip(tripId).seats - db.userTripDao().getAll().stream().filter(x->x.tripId == tripId).count());
    }
}
