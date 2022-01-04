package com.example.companionfinder;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.companionfinder.Database.AppDatabase;
import com.example.companionfinder.Helpers.JSONHelper;
import com.example.companionfinder.Model.Trip;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    Button addCompanion;
    Button export;
    Button tripList;
    Button companions;
    Button importButton;
    Button bookedButton;
    Context context;
    AppDatabase db;
    int userId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;

        //Add companion
        addCompanion = findViewById(R.id.addCompanionButton);
        userId = Integer.parseInt(getIntent().getSerializableExtra("UserId").toString());
        addCompanion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),AddCompanionActivity.class);

                intent.putExtra("UserId", userId);
                startActivity(intent);
            }
        });
        AppDatabase db = AppDatabase.getInstance(this);
        companions = findViewById(R.id.companionsButton);
        companions.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),MyTripsActivity.class);
                intent.putExtra("UserId",getIntent().getSerializableExtra("UserId"));
                startActivity(intent);
            }
        });
        //Extract
        export = findViewById(R.id.exportButton);
        export.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 List<Trip> myTrips = db.tripDao().getMyTrips(userId);
                 JSONHelper.saveTripsJSON(context, myTrips);
                 Toast.makeText(context, "Trips are exported to JSON", Toast.LENGTH_SHORT).show();
                }
            });
        //Import
        importButton = findViewById(R.id.importButton);
        importButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.tripDao().insert(JSONHelper.getTripsJSON(getApplicationContext()));
                Toast.makeText(context, "Trips are imported", Toast.LENGTH_SHORT).show();
            }
        });
        //TripList
        tripList = findViewById(R.id.listButton);
        tripList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),TripsListActivity.class);
                intent.putExtra("UserId",getIntent().getSerializableExtra("UserId"));
                startActivity(intent);
            }
        });

        //BookedTrips
        bookedButton = findViewById(R.id.bookedButton);
        bookedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),BookedTripsActivity.class);
                intent.putExtra("UserId",getIntent().getSerializableExtra("UserId"));
                startActivity(intent);
            }
        });

    }
    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, LoginActivity.class));
    }

}