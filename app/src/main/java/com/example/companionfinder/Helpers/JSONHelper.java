package com.example.companionfinder.Helpers;

import android.content.Context;

import com.example.companionfinder.Model.Trip;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class JSONHelper {
    private static final String FILE_NAME = "myTrips.json";

    public static ArrayList<Trip> getTripsJSON(Context context) {
        if(!isExist(context))
            return new ArrayList<>();

        Gson gson = new Gson();
        File file = new File(context.getFilesDir(), FILE_NAME);

        try {
            FileInputStream fis = new FileInputStream(file);
            InputStreamReader isr = new InputStreamReader(fis);

            DataItems trips = gson.fromJson(isr, DataItems.class);

            fis.close();
            isr.close();
            return trips.getTrips();
        } catch (Exception e) { }

        return new ArrayList<>();
    }

    public static void saveTripsJSON(Context context, List<Trip> trips) {
        File file = new File(context.getFilesDir(), FILE_NAME);
        trips.forEach(x->x.tripId = 0);
        Gson gson = new Gson();
        DataItems dataItems = new DataItems();

        dataItems.setTrips(trips);
        String jsonStr = gson.toJson(dataItems);

        try {
            FileOutputStream fos = new FileOutputStream(file, false);

            fos.write(jsonStr.getBytes());
            fos.close();
        } catch (Exception e) { }
    }

    private static boolean isExist(Context context) {
        File file = new File(context.getFilesDir(), FILE_NAME);
        return file.exists();
    }

    private static class DataItems {
        private ArrayList<Trip> trips;

        public ArrayList<Trip> getTrips() { return trips; }
        public void setTrips(List<Trip> trips) { this.trips = (ArrayList<Trip>)trips; }
    }
}
