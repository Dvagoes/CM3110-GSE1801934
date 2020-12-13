package com.example.cm3110_gse1801934;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Iterator;
import java.util.TimeZone;

public class LiveTrackActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final String TAG = "lta";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_track);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        getLocation(googleMap);
    }

    private void getLocation(final GoogleMap googleMap) {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://api.open-notify.org/iss-now.json";

// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                // Display the first 500 characters of the response string.

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject trackObject = jsonObject.getJSONObject("iss_position");
                    for (Iterator<String> iterator = trackObject.keys(); iterator.hasNext(); ) {
                        String id = iterator.next();
                        double lat = Double.parseDouble(trackObject.getString("latitude"));
                        double lon = Double.parseDouble(trackObject.getString("longitude"));
                        googleMap.addMarker(new MarkerOptions().position(new LatLng(lat, lon)).title("ISS"));
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