package com.example.cm3110_gse1801934;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Iterator;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity {
    LatLng latLng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();

        double lat = intent.getDoubleExtra("latitude", 100);
        double lon = intent.getDoubleExtra("longitude", 100);
        // as 100 is out of range, this will make it so that only on launch will the app ask ask for your location
        if (lat == 100 && lon == 100) {
            getLoc();
        } else {
            latLng = new LatLng(lat, lon);
        }

        setCurrLoc();
        setTime();

    }

    public void changeLocation(View view) {
        if (view.getId() == R.id.ChangeLocButn) {
            Intent intent = new Intent(getApplicationContext(), ChangeLocActivity.class);
            intent.putExtra("latitude", latLng.latitude);
            intent.putExtra("longitude", latLng.longitude);
            startActivity(intent);
        }
    }

    public void viewAllTimes(View view) {
        if (view.getId() == R.id.ViewTimeBtn) {
            Intent intent = new Intent(getApplicationContext(), ViewTimes.class);
            intent.putExtra("latitude", latLng.latitude);
            intent.putExtra("longitude", latLng.longitude);
            startActivity(intent);
        }
    }

    public void liveTrack(View view) {
        if (view.getId() == R.id.LiveTrackButn) {
            Intent intent = new Intent(getApplicationContext(), LiveTrackActivity.class);
            startActivity(intent);
        }
    }

    public void setCurrLoc() {
        TextView textView = findViewById(R.id.currLocLabel);
        Double lat = latLng.latitude;
        Double lon = latLng.longitude;
        String slat = lat.toString();
        String slon = lon.toString();
        while (slat.length() < 5) {
            slat += "0";
        }
        while (slon.length() < 5) {
            slon += "0";
        }
        String text = slat.substring(0, 5) + " N, " + slon.substring(0, 5) + " E";
        textView.setText(text);
    }

    public void getLoc() {
        LocationManager locationManager = (LocationManager)
                getSystemService(Context.LOCATION_SERVICE);
        MainActivity.MyLocationListener loc = new MainActivity.MyLocationListener(locationManager);
        try {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
            } else {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, loc);
                latLng = new LatLng(loc.latitude, loc.longitude);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class MyLocationListener implements LocationListener {

        private double latitude;
        private double longitude;

        public MyLocationListener(LocationManager locationManager) {
            Location loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            onLocationChanged(loc);
        }

        @Override
        public void onLocationChanged(Location loc) {
            latitude = loc.getLatitude();
            longitude = loc.getLongitude();
        }

        @Override
        public void onProviderDisabled(String provider) {
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        public double getLatitude() {
            return latitude;
        }

        public double getLongitude() {
            return longitude;
        }
    }

    public void setTime() {

        RequestQueue queue = Volley.newRequestQueue(this);


        String url = "http://api.open-notify.org/iss-pass.json?lat=" + latLng.latitude + "&lon=" + latLng.longitude;

// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                // Display the first 500 characters of the response string.
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray timeArray = jsonObject.getJSONArray("response");
                    JSONObject timeObject = timeArray.getJSONObject(0);
                    String text = "";
                    for (Iterator<String> iterator = timeObject.keys(); iterator.hasNext(); ) {
                        String id = iterator.next();
                        LocalTime time = LocalDateTime.ofInstant(
                                Instant.ofEpochMilli(Long.parseLong(timeObject.getString("risetime"))),
                                TimeZone.getDefault().toZoneId()
                        ).toLocalTime();
                        text = time.getHour() + ":" + time.getMinute();
                    }
                    TextView textView = findViewById(R.id.nextPassTime);
                    textView.setText(text);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

// Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

}