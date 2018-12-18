package roadfood.sujae.com.roadfood;

import android.Manifest;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
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

    private AdView mAdView;
//    Document doc =null;
//    Elements contents;
    String st = "";
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MobileAds.initialize(this, "ca-app-pub-8740916255036569~3198671966");
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                Log.e("광고" ,"load됨");
            }
            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
                Log.e("광고" ,"load실패");
            }
            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
                Log.e("광고" ,"오픈됨");
            }
            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
                Log.e("광고" ,"어플끔");
            }
            @Override
            public void onAdClosed() {
                // Code to be executed when when the user is about to return
                // to the app after tapping on an ad.
                Log.e("광고" ,"광고닫음");
            }
        });

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
            if(grantResults.length>0 && grantResults[0]== PackageManager.PERMISSION_GRANTED){
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


//    public String parsing(final String parsename){
//        new AsyncTask() {//AsyncTask객체 생성
//            protected Object doInBackground(Object[] params) {
//                try {
//                    doc = Jsoup.connect("https://search.naver.com/search.naver?where=post&sm=tab_jum&query="+parsename).get(); //naver페이지를 불러옴
//                    contents = doc.select("span.ah_k");//셀렉터로 span태그중 class값이 ah_k인 내용을 가져옴
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                int cnt = 0;//숫자를 세기위한 변수
//                for(Element element: contents) {
//                    cnt++;
//                    st += cnt+". "+element.text() + "\n";
//                    if(cnt == 10)//10위까지 파싱하므로
//                        break;
//                    }
//                    return null;
//                    }
//                    protected void onPostExecute(Object o) {
//                        super.onPostExecute(o);
//                        Log.e("paring",st);
//                    }
//                }.execute();
//        return st;
//    }



    //맵에 알맞게 마커를 설정하고 마커 클릭시 알맞은 intent로 넘어감
    @Override
    public void onMapReady(final GoogleMap googleMap) {
        //현재 내 위치 지도에 넣기위해 추가한 코드
        this.googleMap = googleMap;
        this.googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        if(resultInt/100==1){
            resultInt=resultInt%100;
            //백종원의 골목식당 추가하기(RAW파일 사용했음)
            for(int k=0;k<RestaurantNM.size();k++){
                MarkerOptions markerOptions2 = new MarkerOptions();
                Log.e("array",RestaurantNM.get(k)+" "+RestaurantXCODE.get(k) + " "+ RestaurantYCODE.get(k));
                markerOptions2.position(new LatLng(RestaurantXCODE.get(k),RestaurantYCODE.get(k))).title(RestaurantNM.get(k));
//                BitmapDrawable bitmapdraw=(BitmapDrawable)getResources().getDrawable(R.drawable.baek);
                BitmapDrawable bitmapdraw=(BitmapDrawable) ContextCompat.getDrawable(getApplicationContext(),R.drawable.baek_p_compressor);

                Bitmap b=bitmapdraw.getBitmap();
                Bitmap smallMarker = Bitmap.createScaledBitmap(b, 60, 60, false);
                markerOptions2.icon(BitmapDescriptorFactory.fromBitmap(smallMarker));

                googleMap.addMarker(markerOptions2);

                googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    public boolean onMarkerClick(Marker marker) {
                        Intent intent = new Intent(getApplicationContext(),BlogActivity.class);
                        intent.putExtra("storename",marker.getTitle());
                        startActivity(intent);

                        return false;
                    }
                });
            }
        }
        if(resultInt/10==1){
            resultInt=resultInt%10;
            //골목길 추가하기
            for(int j = 0 ; j<COT_CONTS_NAME.size();j++){
                MarkerOptions markerOptions1 = new MarkerOptions();
                markerOptions1.position(new LatLng(COT_COORD_Y.get(j),COT_COORD_X.get(j))).title(COT_CONTS_NAME.get(j));
//                BitmapDrawable bitmapdraw=(BitmapDrawable)getResources().getDrawable(R.drawable.road);
                BitmapDrawable bitmapdraw=(BitmapDrawable) ContextCompat.getDrawable(getApplicationContext(),R.drawable.road_b_compressor);

                Bitmap b=bitmapdraw.getBitmap();
                Bitmap smallMarker = Bitmap.createScaledBitmap(b, 60, 60, false);
                markerOptions1.icon(BitmapDescriptorFactory.fromBitmap(smallMarker));
                googleMap.addMarker(markerOptions1);

                googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    public boolean onMarkerClick(Marker marker) {
                        Intent intent = new Intent(getApplicationContext(),BlogActivity.class);
                        intent.putExtra("storename",marker.getTitle());
                        startActivity(intent);

                        return false;
                    }
                });
            }
        }
        //푸드트럭 추가하기 좌표가 ITRF2000좌표라서 wgs84로 변경해야함

        if(resultInt/1==1) {
            for (int i = 0; i < NM.size(); i++) {
                MarkerOptions markerOptions = new MarkerOptions();
                BlogActivity.GeoPoint katec_pt = new BlogActivity.GeoPoint(XCODE.get(i), YCODE.get(i));
                BlogActivity.GeoPoint out_pt = GeoTrans.convert(GeoTrans.TM, GeoTrans.GEO, katec_pt);
                markerOptions.position(new LatLng(out_pt.getY(), out_pt.getX())).title(NM.get(i));
//                BitmapDrawable bitmapdraw=(BitmapDrawable)getResources().getDrawable(R.drawable.foodtruck_r);
//                BitmapDrawable bitmapdraw=(BitmapDrawable) ResourcesCompat.getDrawable(getResources(),R.drawable.foodtruck,null);

                BitmapDrawable bitmapdraw=(BitmapDrawable) ContextCompat.getDrawable(getApplicationContext(),R.drawable.foodtruck_r_compressor);
                Bitmap b=bitmapdraw.getBitmap();
                Bitmap smallMarker = Bitmap.createScaledBitmap(b, 60, 60, false);
                markerOptions.icon(BitmapDescriptorFactory.fromBitmap(smallMarker));
                googleMap.addMarker(markerOptions);

            googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                public boolean onMarkerClick(Marker marker) {
                    Intent intent = new Intent(getApplicationContext(),BlogActivity.class);
                    intent.putExtra("storename",marker.getTitle());
                    startActivity(intent);

                    return false;
                }
            });
            }
        }
        //현재 내 위치 지도에 넣기위해 추가한 코드
        LatLng position = new LatLng(mLatitude,mLongitude);
        this.googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 16));

//        map.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(latitude,longitude)));
//        map.animateCamera(CameraUpdateFactory.zoomTo(13));
    }
}