package com.example.sujae.infoism;

import android.app.FragmentManager;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.Toast;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import java.util.ArrayList;



public class MainActivity extends FragmentActivity implements OnMapReadyCallback {

    ArrayList<String> NM;
    ArrayList<Double> XCODE;
    ArrayList<Double> YCODE;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FragmentManager fragmentManager = getFragmentManager();
        MapFragment mapFragment = (MapFragment) fragmentManager
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        NM = (ArrayList<String>) getIntent().getSerializableExtra("NM");
        XCODE = (ArrayList<Double>) getIntent().getSerializableExtra("XCODE");
        YCODE = (ArrayList<Double>) getIntent().getSerializableExtra("YCODE");

    }
    //맵에 알맞게 마커를 설정하고 마커 클릭시 알맞은 intent로 넘어감
    @Override
    public void onMapReady(final GoogleMap map) {
        Toast.makeText(getApplicationContext(), "NM = "+NM.size(), Toast.LENGTH_SHORT).show();

        for (int i = 0; i < NM.size(); i++) {
            MarkerOptions markerOptions = new MarkerOptions();
            GeoPoint katec_pt = new GeoPoint(XCODE.get(i),YCODE.get(i));
            GeoPoint out_pt = GeoTrans.convert(GeoTrans.TM, GeoTrans.GEO, katec_pt);
            markerOptions.position(new LatLng(out_pt.getY(),out_pt.getX())).title(NM.get(i));
            map.addMarker(markerOptions);
            map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                public boolean onMarkerClick(Marker marker) {
                    if (marker.getTitle().equals("스테이킹")) {
                        Toast.makeText(getApplicationContext(), "스테이킹입니다.", Toast.LENGTH_SHORT).show();
                    }
                    return false;
                }
            });
        }
        map.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(36.3333,127.7333)));
        map.animateCamera(CameraUpdateFactory.zoomTo(10));
    }

}