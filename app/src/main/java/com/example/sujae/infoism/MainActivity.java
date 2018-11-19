package com.example.sujae.infoism;

import android.app.FragmentManager;
import android.content.Intent;
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

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends FragmentActivity implements OnMapReadyCallback{
    String key="616d44704b776b6439366e78424248";
    List<String> NM = new ArrayList<String>();
    List<String> XCODE = new ArrayList<String>();
    List<String> YCODE = new ArrayList<String>();
    String temp="";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FragmentManager fragmentManager = getFragmentManager();
        MapFragment mapFragment = (MapFragment) fragmentManager
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        String tempa  = getXmlData();
        Toast.makeText(getApplicationContext(), tempa, Toast.LENGTH_LONG).show();


    }
    public String getXmlData() {
        StringBuffer buffer = new StringBuffer();
        String queryUrl = "http://openapi.seoul.go.kr:8088/" + key + "/xml/foodTruckInfo/1/50/";
        try {
            URL url = new URL(queryUrl); // 문자열로 된 요청 url을 URL 객체로 생성
            //여기까지는 받음
            InputStream is = url.openStream(); //url 위치로 인풋스트림 연결
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput(new InputStreamReader(is, "UTF-8"));
            // inputstream 으로부터 xml 입력받기

            String tag;
            xpp.next();
            int eventType = xpp.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                Toast.makeText(getApplicationContext(), "파싱시작!! ", Toast.LENGTH_LONG).show();
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                        tag = xpp.getName(); // 태그 이름 얻어오기
                        if (tag.equals("foodTruckInfo")){
                        }
                        else if (tag.equals("NM")) {
//                                buffer.append("사업장명 : ");
                            xpp.next();
                            temp = xpp.getText();
//                            NM.add(temp);
                            buffer.append(temp);
                            // MSRSTENAME 요소의 TEXT 읽어와서 문자열버퍼에 추가
//                                buffer.append("\n"); // 줄바꿈 문자 추가
                        } else if (tag.equals("XCODE")) {
//                                buffer.append("X좌표 : ");
                            xpp.next();
                            temp = xpp.getText();
//                            XCODE.add(temp);
                            buffer.append(temp);
//                                buffer.append("\n");
                        } else if (tag.equals("YCODE")) {
//                                buffer.append("Y좌표 :");
                            xpp.next();
                            temp = xpp.getText();
//                            YCODE.add(temp);
                                buffer.append(temp);//cpId
                                buffer.append("\n");
                        }
                        break;
                    case XmlPullParser.TEXT:
                        break;
                    case XmlPullParser.END_TAG:
                        tag = xpp.getName(); // 태그 이름 얻어오기
                        break;
                }
                eventType = xpp.next();
            }
        } catch (Exception e) {
            // TODO Auto-generated catch blocke.printStackTrace();
        }
        buffer.append("파싱 끝\n");
        return buffer.toString();//StringBuffer 문자열 객체 반환
    }

    //맵에 알맞게 마커를 설정하고 마커 클릭시 알맞은 intent로 넘어감
    @Override
    public void onMapReady(final GoogleMap map) {
        for (int i = 0; i < NM.size(); i++) {
//            Toast.makeText(getApplicationContext(), "XCODE = "+XCODE.get(i), Toast.LENGTH_SHORT).show();

//            MarkerOptions markerOptions = new MarkerOptions();
//            markerOptions.position(new LatLng(Double.parseDouble(XCODE.get(i)),Double.parseDouble(YCODE.get(i)))).title(NM.get(i));
//            map.addMarker(markerOptions);
//            map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
//                public boolean onMarkerClick(Marker marker) {
//                    if (marker.getTitle().equals("스테이킹")) {
//                        Intent intent1 = new Intent(getApplicationContext(), CrimeInfo.class);
//                        startActivity(intent1);
//                    }
//                    return false;
//                }
//            });
        }
//        map.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(Double.parseDouble(XCODE.get(0)),Double.parseDouble(YCODE.get(0)))));
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(new LatLng(30.3333,126.3333)).title("sasdasd");
        map.addMarker(markerOptions);
        map.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(30.3333,126.3333)));

        map.animateCamera(CameraUpdateFactory.zoomTo(10));
    }

}