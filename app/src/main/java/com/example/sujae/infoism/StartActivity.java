package com.example.sujae.infoism;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class StartActivity extends Activity {
    ArrayList<String> NM = new ArrayList<String>();
    ArrayList<String> COT_CONTS_NAME = new ArrayList<String>();
    ArrayList<Double> XCODE = new ArrayList<Double>();
    ArrayList<Double> YCODE = new ArrayList<Double>();
    ArrayList<Double> COT_COORD_X = new ArrayList<Double>();
    ArrayList<Double> COT_COORD_Y = new ArrayList<Double>();


    Button button;
    TextView textView;
    String key="616d44704b776b6439366e78424248";
    String data;

    @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_start);
            button =(Button) findViewById(R.id.button);
            textView = (TextView) findViewById(R.id.textView);
        }

        public void mOnClick(View v){
            switch( v.getId() ){
                case R.id.button:
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            //아래 메소드를 호출하여 XML data를 파싱해서 String 객체로 얻어오기
                            data= getFoodXmlData();
                            getRoadXmlData();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    // TODO Auto-generated method stub
//                                    textView.setText(data);
                                    Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                                    intent.putExtra("NM",NM);
                                    intent.putExtra("XCODE",  XCODE);
                                    intent.putExtra("YCODE",YCODE);
                                    intent.putExtra("COT_CONTS_NAME",COT_CONTS_NAME);
                                    intent.putExtra("COT_COORD_X",COT_COORD_X);
                                    intent.putExtra("COT_COORD_Y",COT_COORD_Y);
                                    startActivity(intent);
                                }
                            });
                        }
                    }).start();
                    break;
            }
        }
        String getFoodXmlData() {
            StringBuffer buffer = new StringBuffer();
            String temp;
            String queryUrl = "http://openapi.seoul.go.kr:8088/" + key + "/xml/foodTruckInfo/1/50/";
            try {
                URL url = new URL(queryUrl); // 문자열로 된 요청 url을 URL 객체로 생성
                InputStream is = url.openStream(); //url 위치로 인풋스트림 연결
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                XmlPullParser xpp = factory.newPullParser();
                xpp.setInput(new InputStreamReader(is, "UTF-8"));
                // inputstream 으로부터 xml 입력받기
                String tag;
                xpp.next();
                int eventType = xpp.getEventType();
                while (eventType != XmlPullParser.END_DOCUMENT) {
                    switch (eventType) {
                        case XmlPullParser.START_DOCUMENT:
                            break;
                        case XmlPullParser.START_TAG:
                            tag = xpp.getName(); // 태그 이름 얻어오기
                            if (tag.equals("foodTruckInfo")){

                            }
                            else if (tag.equals("NM")) {
                                buffer.append("사업장명 : ");
                                xpp.next();
                                temp = xpp.getText();

                                buffer.append(temp);
                                buffer.append("\n");

                                NM.add(temp);

                            } else if (tag.equals("XCODE")) {
                                buffer.append("X좌표 : ");
                                xpp.next();
                                temp = xpp.getText();

                                buffer.append(temp);
                                buffer.append("\n");

                                XCODE.add(Double.parseDouble(temp));
                            } else if (tag.equals("YCODE")) {
                                buffer.append("Y좌표 :");
                                xpp.next();
                                temp = xpp.getText();

                                buffer.append(temp);
                                buffer.append("\n");

                                YCODE.add(Double.parseDouble(temp));

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

        public void getRoadXmlData(){
            String temp;
            String queryUrl = "http://openapi.seoul.go.kr:8088/" + key + "/xml/MgisAllyWay/1/50/";
            try {
                URL url = new URL(queryUrl); // 문자열로 된 요청 url을 URL 객체로 생성
                InputStream is = url.openStream(); //url 위치로 인풋스트림 연결
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                XmlPullParser xpp = factory.newPullParser();
                xpp.setInput(new InputStreamReader(is, "UTF-8"));
                // inputstream 으로부터 xml 입력받기
                String tag;
                xpp.next();
                int eventType = xpp.getEventType();
                while (eventType != XmlPullParser.END_DOCUMENT) {
                    switch (eventType) {
                        case XmlPullParser.START_DOCUMENT:
                            break;
                        case XmlPullParser.START_TAG:
                            tag = xpp.getName(); // 태그 이름 얻어오기
                            if (tag.equals("MgisAllyWay")){
                            }
                            else if (tag.equals("COT_COORD_X")) {
                                xpp.next();
                                temp = xpp.getText();
//                                if(COT_COORD_X.contains(Double.parseDouble(temp))){
//                                    break;
//                                }
                                COT_COORD_X.add(Double.parseDouble(temp));
                            } else if (tag.equals("COT_COORD_Y")) {
                                xpp.next();
                                temp = xpp.getText();
//                                if(COT_COORD_Y.contains(Double.parseDouble(temp))){
//                                    break;
//                                }
                                COT_COORD_Y.add(Double.parseDouble(temp));
                            }
                            if(tag.equals("COT_CONTS_LAN_TYPE")){
                                xpp.next();
                                temp = xpp.getText();
//                                if(temp.equals("ENG")){
//                                    break;
//                                }
                            }
                            else if (tag.equals("COT_CONTS_NAME")) {
                                xpp.next();
                                temp = xpp.getText();
                                COT_CONTS_NAME.add(temp);
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
        }

    }