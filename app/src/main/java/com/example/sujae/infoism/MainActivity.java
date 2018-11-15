package com.example.sujae.infoism;

import android.app.FragmentManager;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


public class MainActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fragmentManager = getFragmentManager();
        MapFragment mapFragment = (MapFragment)fragmentManager
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(final GoogleMap map) {

        for(int idx = 0;idx<10; idx++){
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(new LatLng(37.632930+(idx/1000), 126.924490)).title("치치하우스가는길");
            map.addMarker(markerOptions);
        }
        map.setOnMarkerClickListener(this);
        map.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(37.632930, 126.924490)));

        map.animateCamera(CameraUpdateFactory.zoomTo(10));
    }
    public boolean onMarkerClick(Marker marker){
        Intent intent1 = new Intent(getApplicationContext(), CrimeInfo.class);
        startActivity(intent1);
        return true;
    }
}