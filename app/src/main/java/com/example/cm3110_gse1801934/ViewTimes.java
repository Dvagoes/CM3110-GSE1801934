package com.example.cm3110_gse1801934;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.internal.ILocationSourceDelegate;
import com.google.android.gms.maps.internal.zzah;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TimeZone;

public class ViewTimes extends AppCompatActivity {

    static private String TAG = "vt_tag";
    private List<PassTime> passTimes;
    private LatLng latLng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_times);
        passTimes = new ArrayList<>();

        Intent intent = getIntent();
        double lat = intent.getDoubleExtra("latitude", 0);
        double lon = intent.getDoubleExtra("longitude", 0);
        latLng = new LatLng(lat, lon);

        getTimes();

    }

    public void getTimes() {

        RequestQueue queue = Volley.newRequestQueue(this);


        String url = "http://api.open-notify.org/iss-pass.json?lat=" + latLng.latitude + "&lon=" + latLng.longitude;

// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                // Display the first 500 characters of the response string.
                Log.i(TAG, "Response " + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray timeArray = jsonObject.getJSONArray("response");
                    Log.i(TAG, String.valueOf(timeArray.length()));
                    passTimes.clear();
                    for (int i = 0; i < timeArray.length(); i++) {
                        JSONObject timeObject = timeArray.getJSONObject(i);
                        for (Iterator<String> iterator = timeObject.keys(); iterator.hasNext(); ) {
                            String id = iterator.next();
                            long duration = Long.parseLong(timeObject.getString("duration"));
                            LocalTime time = LocalDateTime.ofInstant(
                                    Instant.ofEpochMilli(Long.parseLong(timeObject.getString("risetime"))),
                                    TimeZone.getDefault().toZoneId()
                            ).toLocalTime();
                            PassTime passTime = new PassTime(time, duration);
                            Log.i(TAG, passTime.toString());
                            passTimes.add(passTime);
                            RecyclerView recyclerView = findViewById(R.id.rv_viewtimes);
                            RecyclerView.Adapter adapter = new ViewTimesAdapter(getApplicationContext(), passTimes);
                            recyclerView.setAdapter(adapter);
                            recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG, "error " + error.getLocalizedMessage());
            }
        });

// Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

}
