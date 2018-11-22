package com.example.sujae.infoism;

import android.app.FragmentManager;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
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
    ArrayList<String> COT_CONTS_NAME;
    ArrayList<Double> COT_COORD_X;
    ArrayList<Double> COT_COORD_Y;
    ArrayList<String> RestaurantNM;
    ArrayList<Double> RestaurantXCODE;
    ArrayList<Double> RestaurantYCODE;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FragmentManager fragmentManager = getFragmentManager();
        MapFragment mapFragment = (MapFragment) fragmentManager
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        //인텐트 객체로 데이터 넘겨받기
        NM = (ArrayList<String>) getIntent().getSerializableExtra("NM");
        XCODE = (ArrayList<Double>) getIntent().getSerializableExtra("XCODE");
        YCODE = (ArrayList<Double>) getIntent().getSerializableExtra("YCODE");
        COT_CONTS_NAME = (ArrayList<String>) getIntent().getSerializableExtra("COT_CONTS_NAME");
        COT_COORD_X = (ArrayList<Double>) getIntent().getSerializableExtra("COT_COORD_X");
        COT_COORD_Y = (ArrayList<Double>) getIntent().getSerializableExtra("COT_COORD_Y");
        RestaurantNM = (ArrayList<String>) getIntent().getSerializableExtra("RestaurantNM");
        RestaurantXCODE = (ArrayList<Double>) getIntent().getSerializableExtra("RestaurantXCODE");
        RestaurantYCODE = (ArrayList<Double>) getIntent().getSerializableExtra("RestaurantYCODE");
    }
    //맵에 알맞게 마커를 설정하고 마커 클릭시 알맞은 intent로 넘어감
    @Override
    public void onMapReady(final GoogleMap map) {
        Toast.makeText(getApplicationContext(), "NM = "+NM.size(), Toast.LENGTH_SHORT).show();
        //푸드트럭 추가하기 좌표가 ITRF2000좌표라서 wgs84로 변경해야함
        for (int i = 0; i < NM.size(); i++) {
            MarkerOptions markerOptions = new MarkerOptions();
            GeoPoint katec_pt = new GeoPoint(XCODE.get(i),YCODE.get(i));
            GeoPoint out_pt = GeoTrans.convert(GeoTrans.TM, GeoTrans.GEO, katec_pt);
            markerOptions.position(new LatLng(out_pt.getY(),out_pt.getX())).title(NM.get(i));
            map.addMarker(markerOptions);
//            map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
//                public boolean onMarkerClick(Marker marker) {
//                    if (marker.getTitle().equals("스테이킹")) {
//                        Toast.makeText(getApplicationContext(), "스테이킹입니다.", Toast.LENGTH_SHORT).show();
//                    }
//                    return false;
//                }
//            });
        }
        //골목길 추가하기
        for(int j = 0 ; j<COT_CONTS_NAME.size();j++){
            MarkerOptions markerOptions1 = new MarkerOptions();
            markerOptions1.position(new LatLng(COT_COORD_Y.get(j),COT_COORD_X.get(j))).title(COT_CONTS_NAME.get(j));
            map.addMarker(markerOptions1);
        }
        //백종원의 골목식당 추가하기(RAW파일 사용했음)
        for(int k=0;k<RestaurantNM.size();k++){
            MarkerOptions markerOptions2 = new MarkerOptions();
            Log.e("array",RestaurantNM.get(k)+" "+RestaurantXCODE.get(k) + " "+ RestaurantYCODE.get(k));
            markerOptions2.position(new LatLng(RestaurantXCODE.get(k),RestaurantYCODE.get(k))).title(RestaurantNM.get(k));
            map.addMarker(markerOptions2);
        }
        map.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(37.570983,126.976298)));
        map.animateCamera(CameraUpdateFactory.zoomTo(13));
    }
}