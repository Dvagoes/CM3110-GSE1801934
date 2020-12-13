package com.example.cm3110_gse1801934;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class ChangeLocActivity extends AppCompatActivity implements OnMapReadyCallback {

    private LatLng latLng;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_loc);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Intent intent = getIntent();
        double lat = intent.getDoubleExtra("latitude", 0);
        double lon = intent.getDoubleExtra("longitude", 0);
        latLng = new LatLng(lat, lon);
        setCurrLoc();

    }

    public void changeLocCancel(View view) {
        if (view.getId() == R.id.ChangeLocCancel) {
            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
            startActivity(intent);
        }
    }
    public void changeLocConfirm(View view) {
        if (view.getId() == R.id.ChangeLocConfirm) {

            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
            intent.putExtra("latitude", latLng.latitude);
            intent.putExtra("longitude", latLng.longitude);
            startActivity(intent);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.addMarker(new MarkerOptions().position(latLng).title("Target Location").draggable(true));
        googleMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {

            }

            @Override
            public void onMarkerDrag(Marker marker) {
                TextView textView = findViewById(R.id.coordLabel);
                Double lat = marker.getPosition().latitude;
                Double lon = marker.getPosition().longitude;
                String text = lat.toString().substring(0,5) + " N, " + lon.toString().substring(0,5) + " E";
                textView.setText(text);
            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                latLng = marker.getPosition();
            }
        });
    }

    public void setCurrLoc() {
        TextView textView = findViewById(R.id.coordLabel);
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


}