package com.example.sujae.infoism;

import android.app.Activity;
import android.os.Bundle;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class CrimeInfo extends Activity {
    List<String> NM = new ArrayList<String>();
    List<Double> XCODE = new ArrayList<Double>();
    List<Double> YCODE = new ArrayList<Double>();

    EditText edit;

    static TextView text;
    TextView text2;
    String key="616d44704b776b6439366e78424248";
    String data;

    @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_crimeinfo);
            edit= (EditText)findViewById(R.id.edit);
            text= (TextView)findViewById(R.id.text);
            text2= (TextView)findViewById(R.id.text2);
        }
        // 버튼 하나를 통해 2개의 쓰레드를 돌려서 각각의 api의 데이터를 가져와 출력
        public void mOnClick(View v){
            switch( v.getId() ){
                case R.id.button:
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            //아래 메소드를 호출하여 XML data를 파싱해서 String 객체로 얻어오기
                            data= getXmlData();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    // TODO Auto-generated method stub
                                    text.setText(data);
                                }
                            });
                        }
                    }).start();
                    break;
            }
        }
        String getXmlData() {
            StringBuffer buffer = new StringBuffer();

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
                                buffer.append(xpp.getText());
                                // MSRSTENAME 요소의 TEXT 읽어와서 문자열버퍼에 추가
                                buffer.append("\n"); // 줄바꿈 문자 추가
                            } else if (tag.equals("XCODE")) {
                                buffer.append("X좌표 : ");
                                xpp.next();
                                buffer.append(xpp.getText());
                                buffer.append("\n");
                            } else if (tag.equals("YCODE")) {
                                buffer.append("Y좌표 :");
                                xpp.next();
                                buffer.append(xpp.getText());//cpId
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

    }