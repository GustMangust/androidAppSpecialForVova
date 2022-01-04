package com.example.companionfinder;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.companionfinder.Database.AppDatabase;
import com.example.companionfinder.Helpers.DatabaseUtility;
import com.example.companionfinder.Model.Trip;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TripsListActivity extends AppCompatActivity {

    private int userId;
    private Button searchButton;
    private AppDatabase db;
    private ListView listTrips;
    private List<Trip> trips = new ArrayList<Trip>();
    private Spinner categoriesSpinner;
    private EditText searchEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trips_activity);
        categoriesSpinner = findViewById(R.id.categoriesSpinner);
        listTrips = findViewById(R.id.listViewTrips);
        searchEdit = findViewById(R.id.searchEditText);
        searchButton = findViewById(R.id.searchButton);
        userId = Integer.parseInt(getIntent().getSerializableExtra("UserId").toString());
        db = AppDatabase.getInstance(this);
        trips = DatabaseUtility.GetActualTrips(getApplicationContext(),true,true);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, new String[]{"Transport type", "Cost less", "Destination point"});
        categoriesSpinner.setAdapter(adapter);
        TripListAdapter tripsListAdapter = new TripListAdapter(getApplicationContext(), trips);
        listTrips.setAdapter(tripsListAdapter);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String category = categoriesSpinner.getSelectedItem().toString();
                List<Trip> newTrips = new ArrayList<Trip>();
                if(!searchEdit.getText().toString().isEmpty()){
                    switch (category){
                        case "Transport type":
                            newTrips = trips.stream().filter(x->x.transportType.contains(searchEdit.getText().toString())).collect(Collectors.toList());
                            break;
                        case "Cost less":
                            try {
                                newTrips = trips.stream().filter(x->x.costForUser < Integer.parseInt(searchEdit.getText().toString())).collect(Collectors.toList());
                            } catch (NumberFormatException e) {
                                Toast.makeText(getApplicationContext(), "It's not a number!", Toast.LENGTH_SHORT).show();
                            }
                            break;
                        case "Destination point":
                            newTrips = trips.stream().filter(x->x.endAddress.contains(searchEdit.getText().toString())).collect(Collectors.toList());
                            break;
                    }
                } else{
                    newTrips = trips;
                }
                TripListAdapter newTripsListAdapter = new TripListAdapter(getApplicationContext(), newTrips);
                listTrips.setAdapter(newTripsListAdapter);
            }
        });
    }

    public class TripListAdapter extends BaseAdapter {

        private final Context context;
        private final List<Trip> trips;


        public TripListAdapter(Context context, List<Trip> trips) {
            this.context = context;
            this.trips = trips;
        }

        @Override
        public int getCount() {
            return trips.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        public void searchTrips(ArrayList<Trip> trips) {
            this.trips.clear();
            this.trips.addAll(trips);
            this.notifyDataSetChanged();
        }

        @Override
        public View getView(int pos, View convertView, ViewGroup viewGroup) {
            View view = getLayoutInflater().inflate(R.layout.trip_item, null);

            TextView startAddress = view.findViewById(R.id.txtStartAddress);
            TextView endAddress = view.findViewById(R.id.txtEndAddress);
            TextView cost = view.findViewById(R.id.txtCost);
            TextView date = view.findViewById(R.id.txtDate);

            startAddress.setText(trips.get(pos).startAddress);
            endAddress.setText(trips.get(pos).endAddress);
            cost.setText(trips.get(pos).costForUser + " BYN");
            date.setText(trips.get(pos).dateTime);

            view.setOnClickListener(v -> {
                Intent intent = new Intent(getApplicationContext(),SelectedTripActivity.class);
                intent.putExtra("TripId", trips.get(pos).tripId);
                intent.putExtra("UserId", userId);
                startActivity(intent);
            });

            return view;
        }
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("UserId",userId);
        startActivity(intent);
    }
}
