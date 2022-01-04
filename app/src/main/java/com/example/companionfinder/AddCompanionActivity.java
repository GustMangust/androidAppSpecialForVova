package com.example.companionfinder;

import static android.content.ContentValues.TAG;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.companionfinder.Database.AppDatabase;
import com.example.companionfinder.Model.Trip;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.material.textfield.TextInputEditText;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class AddCompanionActivity extends AppCompatActivity
{
    EditText editStartPoint;
    EditText editDestinationPoint;
    int START_PLACE_PICKER_REQUEST = 1;
    int DESTINATION_PICKER_REQUEST = 2;
    Context context = this;
    EditText editDate;
    EditText editTime;
    TextInputEditText costInput;
    TextInputEditText transportTypeInput;
    TextInputEditText seatsInput;
    DatePickerDialog.OnDateSetListener setListener;
    TimePickerDialog.OnTimeSetListener setTimeListener;
    Button addTrip;
    int userId;
    AppDatabase db;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_companion_activity);
        editStartPoint = findViewById(R.id.editTextStartPoint);
        editDestinationPoint = findViewById(R.id.editTextDestinationPoint);
        costInput = findViewById(R.id.costInput);
        transportTypeInput = findViewById(R.id.transportInput);
        seatsInput = findViewById(R.id.placesInput);
        editStartPoint.setFocusable(false);
        editStartPoint.setKeyListener(null);
        addTrip = findViewById(R.id.addTripButton);
        db = AppDatabase.getInstance(this);
        userId = Integer.parseInt(getIntent().getSerializableExtra("UserId").toString());
        //Start point
        editStartPoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                try {
                    Intent intent = builder.build(AddCompanionActivity.this);
                    startActivityForResult(intent, START_PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });

        //Destination point
        editDestinationPoint = findViewById(R.id.editTextDestinationPoint);
        editDestinationPoint.setFocusable(false);
        editDestinationPoint.setKeyListener(null);
        editDestinationPoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                try {
                    Intent intent = builder.build(AddCompanionActivity.this);
                    startActivityForResult(intent, DESTINATION_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });

        //Date picker
        editDate = findViewById(R.id.editTextDatePicker);
        editDate.setFocusable(false);
        editDate.setKeyListener(null);
        editDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(context, android.R.style.Theme_DeviceDefault_Dialog, setListener, year, month, day);
                datePickerDialog.show();
            }
        });
        setListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month +1;
                Log.d(TAG,"onDateSet: dd/mm/yyyy: " + dayOfMonth + "/" + month + "/"+ year);
                String date = dayOfMonth + "/" + month + "/" + year;
                editDate.setText(date);
            }
        };

        //Time picker
        editTime = findViewById(R.id.editTextTimePicker);
        editTime.setFocusable(false);
        editTime.setKeyListener(null);
        editTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);
                TimePickerDialog timePickerDialog = new TimePickerDialog(context,android.R.style.Theme_DeviceDefault_Dialog, setTimeListener, hour, minute, android.text.format.DateFormat.is24HourFormat(context));
                timePickerDialog.show();
            }
        });
        setTimeListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                String min = Integer.toString(i1);
                String hour= Integer.toString(i);
                if(min.length() == 1){
                    min = "0"+min;
                }
                if(hour.length() == 1){
                    hour = "0"+hour;
                }
                String time = hour + ":" + min;
                editTime.setText(time);
            }
        };
        addTrip.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(seatsInput.getText().length() > 0 && costInput.getText().length() > 0 && editTime.getText().length() > 0 && editDate.getText().length() > 0 && transportTypeInput.getText().length() > 0 && editStartPoint.getText().length() > 0 && editDestinationPoint.getText().length() > 0)
                {
                    Trip trip = new Trip();
                    trip.seats = Integer.parseInt(seatsInput.getText().toString());
                    trip.cost = Double.parseDouble(costInput.getText().toString());
                    trip.dateTime = editDate.getText().toString() +" "+ editTime.getText().toString();
                    trip.transportType = transportTypeInput.getText().toString();
                    trip.startAddress = editStartPoint.getText().toString();
                    trip.endAddress = editDestinationPoint.getText().toString();
                    trip.userId = userId;
                    db.tripDao().insert(trip);
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.putExtra("UserId",userId);
                    startActivity(intent);
                } else{
                    Toast.makeText(getApplicationContext(),"Fill all fields!",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == START_PLACE_PICKER_REQUEST){
            if (resultCode == AddCompanionActivity.RESULT_OK){
                try {
                    Place place = PlacePicker.getPlace(context,data);
                    StringBuilder stringBuilder = new StringBuilder();
                    Double latitude = Double.valueOf(place.getLatLng().latitude);
                    Double longitude = Double.valueOf(place.getLatLng().longitude);
                    Geocoder geocoder;
                    geocoder = new Geocoder(context, Locale.getDefault());
                    List<Address> addresses;
                    addresses = geocoder.getFromLocation(latitude, longitude,1 );
                    String locationAdress = addresses.get(0).getAddressLine(0);
                    editStartPoint.setText(locationAdress);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        if (requestCode == DESTINATION_PICKER_REQUEST){
            if (resultCode == AddCompanionActivity.RESULT_OK){
                try {
                    Place place = PlacePicker.getPlace(context,data);
                    StringBuilder stringBuilder = new StringBuilder();
                    Double latitude = Double.valueOf(place.getLatLng().latitude);
                    Double longitude = Double.valueOf(place.getLatLng().longitude);
                    Geocoder geocoder;
                    geocoder = new Geocoder(context, Locale.getDefault());
                    List<Address> addresses;
                    addresses = geocoder.getFromLocation(latitude, longitude,1 );
                    String locationAdress = addresses.get(0).getAddressLine(0);
                    editDestinationPoint.setText(locationAdress);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("UserId",userId);
        startActivity(intent);
    }
}
