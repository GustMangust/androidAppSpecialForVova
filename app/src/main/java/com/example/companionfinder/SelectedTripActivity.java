package com.example.companionfinder;

import static androidx.core.content.PermissionChecker.PERMISSION_GRANTED;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.companionfinder.Database.AppDatabase;
import com.example.companionfinder.Helpers.DatabaseUtility;
import com.example.companionfinder.Model.Trip;

import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

public class SelectedTripActivity extends AppCompatActivity {

    private TextView startAddress;
    private TextView endAddress;
    private TextView seats;
    private TextView cost;
    private TextView datetime;
    private TextView transportType;
    private Button addUserToTrip;
    private Button deleteUserFromTrip;
    private AppDatabase db;
    private int tripId;
    private int userId;
    private Trip trip;
    private long seatsCount;
    boolean started = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selected_trip_activity);
        startAddress = findViewById(R.id.editTextStartPoint);
        endAddress = findViewById(R.id.editTextDestinationPoint);
        seats = findViewById(R.id.editTextSeats);
        cost = findViewById(R.id.editTextCost);
        datetime = findViewById(R.id.editTextDateTime);
        transportType = findViewById(R.id.editTextTransportType);
        addUserToTrip = findViewById(R.id.addUserToTripButton);
        deleteUserFromTrip = findViewById(R.id.deleteUserFromTripButton);
        db = AppDatabase.getInstance(this);
        tripId = (int)getIntent().getSerializableExtra("TripId");
        userId = (int)getIntent().getSerializableExtra("UserId");
        List<Trip> trips = DatabaseUtility.GetActualTrips(getApplicationContext(),false,false).stream().filter(x->x.tripId==tripId).collect(Collectors.toList());
        seatsCount = DatabaseUtility.GetSeatsByTripId(getApplicationContext(),tripId);
        trip = trips.get(0);
        startAddress.setText(trip.startAddress);
        endAddress.setText(trip.endAddress);
        seats.setText("Seats: "+ seatsCount);
        cost.setText("Cost: "+ trip.costForUser + " BYN");
        datetime.setText("Date and time: "+trip.dateTime);
        transportType.setText("Transport type: "+trip.transportType);

        addUserToTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(seatsCount>0 && db.userTripDao().getAll().stream().filter(x->x.tripId == tripId && x.userId == userId).count()==0 && db.tripDao().getTrip(tripId).userId != userId){
                    db.userTripDao().insert(userId,tripId);
                    Toast.makeText(getApplicationContext(),"Success!", Toast.LENGTH_SHORT).show();

                    final int callbackId = 42;
                    checkPermission(callbackId, Manifest.permission.READ_CALENDAR, Manifest.permission.WRITE_CALENDAR);
                    String[] date = trip.dateTime.substring(0,trip.dateTime.indexOf(" ")).split("/");
                    String[] time = trip.dateTime.substring(trip.dateTime.indexOf(" ")+1).split(":");
                    int hour = time[0].startsWith("0") ? Integer.parseInt(time[0].substring(1)) : Integer.parseInt(time[0]);
                    int minute = time[1].startsWith("0") ? Integer.parseInt(time[1].substring(1)) : Integer.parseInt(time[1]);
                    Calendar cal = Calendar.getInstance();
                    cal.set(Integer.parseInt(date[2]),Integer.parseInt(date[1])-1,Integer.parseInt(date[0]),hour,minute,0);

                    Intent intent = new Intent(Intent.ACTION_EDIT);
                    intent.setType("vnd.android.cursor.item/event");
                    intent.putExtra("beginTime", cal.getTimeInMillis()-60*60*1000);
                    intent.putExtra("allDay", false);
                    intent.putExtra("endTime", cal.getTimeInMillis());
                    intent.putExtra("title", "Trip to "+ trip.endAddress);
                    intent.putExtra("description", "Trip from "+ trip.startAddress + " to " + trip.endAddress+" . Cost: " + trip.cost + " BYN" );
                    startActivity(intent);

                } else if(db.tripDao().getMyTrips(userId).stream().filter(x->x.tripId == tripId).count() == 1){
                    Toast.makeText(getApplicationContext(),"You are the owner!", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplicationContext(),"Not enough place or you have already book the place!", Toast.LENGTH_SHORT).show();
                }
            }
            private void checkPermission(int callbackId, String... permissionsId) {
                boolean permissions = true;
                for (String p : permissionsId) {
                    permissions = permissions && ContextCompat.checkSelfPermission(getApplicationContext(), p) == PERMISSION_GRANTED;
                }

                if (!permissions)
                    ActivityCompat.requestPermissions(SelectedTripActivity.this, permissionsId, callbackId);
            }

            private void toMainActivity(){
                Intent intent1 = new Intent(getApplicationContext(),MainActivity.class);
                intent1.putExtra("UserId",userId);
                startActivity(intent1);
            }
        });
        deleteUserFromTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(db.userTripDao().getAll().stream().filter(x->x.tripId == tripId && x.userId == userId).count()!=0){
                    db.userTripDao().delete(userId,tripId);
                    Toast.makeText(getApplicationContext(),"Removed!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.putExtra("UserId",userId);
                    startActivity(intent);
                } else if(trip.userId == userId){
                    db.tripDao().deleteCascade(trip);
                    Toast.makeText(getApplicationContext(),"Deleted!", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.putExtra("UserId",userId);
                    startActivity(intent);
                }else{
                    Toast.makeText(getApplicationContext(),"Error!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if(started) {
            Intent intent1 = new Intent(getApplicationContext(),MainActivity.class);
            intent1.putExtra("UserId",userId);
            startActivity(intent1);
        } else {
            started = true;
        }

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("UserId",userId);
        startActivity(intent);
    }

}