package com.example.sujae.infoism;

import android.Manifest;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
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
    int resultInt;

    //현재 내 위치 지도에 넣기위해 추가한 코드
    GoogleMap googleMap;
    MapFragment mapFragment;
    LocationManager locationManager;
    //위도 경도 초기화 해주기 서울시청
    double mLatitude = 37.566886;  //위도
    double mLongitude=126.988317; //경도



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //현재 내 위치 지도에 넣기위해 추가한 코드
        locationManager = (LocationManager)getSystemService(LOCATION_SERVICE);
        //GPS가 켜져있는지 체크
        if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            //GPS 설정화면으로 이동
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            startActivity(intent);
            finish();
        }
        //마시멜로 이상이면 권한 요청하기
        if(Build.VERSION.SDK_INT >= 23){
            //권한이 없는 경우
            if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION , Manifest.permission.ACCESS_FINE_LOCATION} , 1);
            }
            //권한이 있는 경우
            else{
                requestMyLocation();
            }
        }
        //마시멜로 아래
        else{
            requestMyLocation();
        }




        FragmentManager fragmentManager = getFragmentManager();
        mapFragment = (MapFragment) fragmentManager
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
        Intent it=getIntent();	//인텐트 받기 선언
        resultInt = it.getIntExtra("checked",0);
    }



    //현재 내 위치 지도에 넣기위해 추가한 코드
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //ACCESS_COARSE_LOCATION 권한
        if(requestCode==1){
            //권한받음
            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                requestMyLocation();
            }
            //권한못받음
            else{
                Toast.makeText(this, "권한없음", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }
    public void requestMyLocation(){
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            return;
        }
        Location location = locationManager
                .getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (location != null) {
            mLatitude = location.getLatitude();
            mLongitude = location.getLongitude();
        }
        //요청
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100, 1, locationListener);
    }
    //위치정보 구하기 리스너
    LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                return;
            }
            //나의 위치를 한번만 가져오기 위해
            locationManager.removeUpdates(locationListener);
            //위도 경도
            mLatitude = location.getLatitude();   //위도
            mLongitude = location.getLongitude(); //경도
            //콜백클래스 설정
//            mapFragment.getMapAsync(MainActivity.this);
        }
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) { Log.d("gps", "onStatusChanged"); }
        @Override
        public void onProviderEnabled(String provider) { }
        @Override
        public void onProviderDisabled(String provider) { }
    };





    //맵에 알맞게 마커를 설정하고 마커 클릭시 알맞은 intent로 넘어감
    @Override
    public void onMapReady(final GoogleMap googleMap) {
        //현재 내 위치 지도에 넣기위해 추가한 코드
        this.googleMap = googleMap;
        this.googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        //푸드트럭 추가하기 좌표가 ITRF2000좌표라서 wgs84로 변경해야함
        if(resultInt/100==1){
            resultInt=resultInt%100;
            //백종원의 골목식당 추가하기(RAW파일 사용했음)
            for(int k=0;k<RestaurantNM.size();k++){
                MarkerOptions markerOptions2 = new MarkerOptions();
                Log.e("array",RestaurantNM.get(k)+" "+RestaurantXCODE.get(k) + " "+ RestaurantYCODE.get(k));
                markerOptions2.position(new LatLng(RestaurantXCODE.get(k),RestaurantYCODE.get(k))).title(RestaurantNM.get(k));
                googleMap.addMarker(markerOptions2);
            }
        }
        if(resultInt/10==1){
            resultInt=resultInt%10;
            //골목길 추가하기
            for(int j = 0 ; j<COT_CONTS_NAME.size();j++){
                MarkerOptions markerOptions1 = new MarkerOptions();
                markerOptions1.position(new LatLng(COT_COORD_Y.get(j),COT_COORD_X.get(j))).title(COT_CONTS_NAME.get(j));
                googleMap.addMarker(markerOptions1);
//                googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
//                public boolean onMarkerClick(Marker marker) {
//                    if (marker.getTitle().equals("남대문 칼국수골목")) {
//                        Toast.makeText(getApplicationContext(), "남대문 칼국수골목.", Toast.LENGTH_SHORT).show();
//                    }
//                    return false;
//                }
//            });
            }
        }
        if(resultInt/1==1) {
            for (int i = 0; i < NM.size(); i++) {
                MarkerOptions markerOptions = new MarkerOptions();
                GeoPoint katec_pt = new GeoPoint(XCODE.get(i), YCODE.get(i));
                GeoPoint out_pt = GeoTrans.convert(GeoTrans.TM, GeoTrans.GEO, katec_pt);
                markerOptions.position(new LatLng(out_pt.getY(), out_pt.getX())).title(NM.get(i));
                googleMap.addMarker(markerOptions);
//            map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
//                public boolean onMarkerClick(Marker marker) {
//                    if (marker.getTitle().equals("스테이킹")) {
//                        Toast.makeText(getApplicationContext(), "스테이킹입니다.", Toast.LENGTH_SHORT).show();
//                    }
//                    return false;
//                }
//            });
            }
        }
        //현재 내 위치 지도에 넣기위해 추가한 코드
        LatLng position = new LatLng(mLatitude,mLongitude);
        this.googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 15));

//        map.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(latitude,longitude)));
//        map.animateCamera(CameraUpdateFactory.zoomTo(13));
    }
}