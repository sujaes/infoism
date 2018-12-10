package com.example.sujae.infoism;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

import org.jsoup.select.Elements;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.StringTokenizer;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Document;

public class StartActivity extends Activity {
    ArrayList<String> NM = new ArrayList<String>();
    ArrayList<String> COT_CONTS_NAME = new ArrayList<String>();
    ArrayList<String> RestaurantNM = new ArrayList<String>();
    ArrayList<Double> XCODE = new ArrayList<Double>();
    ArrayList<Double> YCODE = new ArrayList<Double>();
    ArrayList<Double> COT_COORD_X = new ArrayList<Double>();
    ArrayList<Double> COT_COORD_Y = new ArrayList<Double>();
    ArrayList<Double> RestaurantXCODE = new ArrayList<Double>();
    ArrayList<Double> RestaurantYCODE = new ArrayList<Double>();
    private AdView mAdView;
    Button button;

    String key="616d44704b776b6439366e78424248";
    CheckBox food,road,restaurant;
    private static String urlStr = "http://www.kma.go.kr/wid/queryDFS.jsp?gridx=61&gridy=125";
    private InterstitialAd interstitialAd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        button =(Button) findViewById(R.id.button);
        food = (CheckBox) findViewById(R.id.food);
        road = (CheckBox) findViewById(R.id.road);
        restaurant = (CheckBox) findViewById(R.id.restaurant);

        //전면광고
        MobileAds.initialize(this, "ca-app-pub-8740916255036569~3198671966");
        interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        interstitialAd.loadAd(new AdRequest.Builder().build());

        interstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded(){
                displayInterstitial();
            }
            @Override
            public void onAdClosed() {

            }

        });


        //날씨 파싱
        TextView textView = (TextView)findViewById(R.id.textView);
        WeatherConnection weatherConnection = new WeatherConnection();
        AsyncTask<String, String, String> result = weatherConnection.execute("기온 : ","\n날씨 : ");
        System.out.println("RESULT");
        try{
            String msg = result.get();
            System.out.println(msg);

            textView.setText(msg.toString());
        }catch (Exception e){
        }



    }
    //전면광고 보여주기
    public void displayInterstitial(){
        if(interstitialAd.isLoaded()){
            interstitialAd.show();
        }
    }

    public class WeatherConnection extends AsyncTask<String, String, String>{

        // 백그라운드에서 작업하게 한다
        @Override
        protected String doInBackground(String... params) {


            // Jsoup을 이용한 날씨데이터 Pasing하기.
            try{

                String path = "http://weather.naver.com/rgn/townWetr.nhn?naverRgnCd=09650510";

                org.jsoup.nodes.Document document = Jsoup.connect(path).get();

                Elements elements = document.select("em");
                Element targetElement = elements.get(2);
                String text = targetElement.text();
                return text;


                /*
                // URL접속하여 HTML 가져오기.
                URL url = new URL("http://weather.naver.com/rgn/townWetr.nhn?naverRgnCd=09650510");

                System.out.println("Call target URL try....");

                InputStream inputStream = url.openStream();
                Scanner scanner = new Scanner(inputStream, "UTF-8");

                 while (true){
                    try{
                        System.out.println(scanner.nextLine());
                    }catch (Exception e){
                        e.printStackTrace();
                        break;
                    }
                }
                */
            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }
    }



    public int Checked() {
        int resultInt = 0;
        if (food.isChecked()) {
            resultInt += 1;
        }
        if (road.isChecked()) {
            resultInt += 10;
        }
        if(restaurant.isChecked()){
            resultInt += 100;
        }
        return resultInt;
    }

// 버튼 누르면 MainActivitiy로 넘어가는 클릭함수
    public void mOnClick(View v){
        switch( v.getId() ){
            case R.id.button:
                new Thread(new Runnable() {
                    public void run() {  //아래 메소드를 호출하여 XML data를 파싱해서 String 객체로 얻어오기//
                        getFoodXmlData();
                        getRoadXmlData();
                        getRoadRestaurantData();
                        runOnUiThread(new Runnable() {
                            public void run() {
                                    // TODO Auto-generated method stub
                                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                                intent.putExtra("NM",NM);
                                intent.putExtra("XCODE",  XCODE);
                                intent.putExtra("YCODE",YCODE);
                                intent.putExtra("COT_CONTS_NAME",COT_CONTS_NAME);
                                intent.putExtra("COT_COORD_X",COT_COORD_X);
                                intent.putExtra("COT_COORD_Y",COT_COORD_Y);
                                intent.putExtra("RestaurantNM",RestaurantNM);
                                intent.putExtra("RestaurantXCODE",RestaurantXCODE);
                                intent.putExtra("RestaurantYCODE",RestaurantYCODE);
                                intent.putExtra("checked",Checked());
                                startActivity(intent);
                            }
                        });
                    }
                }).start();
                break;
        }
    }
        //서울시 푸드트럭 위치 불러오는 함수
        void getFoodXmlData() {
        StringBuffer buffer = new StringBuffer();
        String temp;
        String queryUrl = "http://openapi.seoul.go.kr:8088/" + key + "/xml/foodTruckInfo/1/100/";
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
    }
        //서울시 30선 골목길 불러오는 함수
        public void getRoadXmlData() {
            String temp;
            boolean lang=true;
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
                            if (tag.equals("MgisAllyWay")) {
                            } else if (tag.equals("COT_COORD_X")) {
                                xpp.next();
                                temp = xpp.getText();
                                if(COT_COORD_X.contains(Double.parseDouble(temp))){
                                    break;
                                }
                                COT_COORD_X.add(Double.parseDouble(temp));
                            } else if (tag.equals("COT_COORD_Y")) {
                                xpp.next();
                                temp = xpp.getText();
                                if(COT_COORD_Y.contains(Double.parseDouble(temp))){
                                    break;
                                }
                                COT_COORD_Y.add(Double.parseDouble(temp));
                            }
                            //30선 골목길 한글이름만 추출하기 위해서 넣은 else if문
                            else if (tag.equals("COT_CONTS_LAN_TYPE")) {
                                xpp.next();
                                temp = xpp.getText();
                                if(temp.equals("ENG")){
                                    lang=false;

                                }else{
                                    lang=true;
                                }
                                break;
                            }
                            else if (tag.equals("COT_CONTS_NAME")) {

                                xpp.next();
                                temp = xpp.getText();
                                if(lang==true){
                                    COT_CONTS_NAME.add(temp);
                                }else{
                                }
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



        //백종원의 골목식당 데이터 불러오는 함수
        public void getRoadRestaurantData(){
                BufferedReader in;
                Resources myResources = getResources();
                InputStream myFile = myResources.openRawResource(R.raw.road_restaurant);
                StringBuffer strBuffer = new StringBuffer();
                String str = null;
                try {
                    in = new BufferedReader(
                            new InputStreamReader(myFile, "UTF-8"));  // file이 utf-8 로 저장되어 있다면 "UTF-8"
                    while( (str = in.readLine()) != null)                      // file이 KSC5601로 저장되어 있다면 "KSC5601"
                    {
                        strBuffer.append(str + " ");
                    }
                    in.close();
                } catch (UnsupportedEncodingException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            StringTokenizer st = new StringTokenizer(strBuffer.toString(), " ");
            String [] array = new String[st.countTokens()];
            int i = 0;
            while(st.hasMoreElements()){
                array[i++] = st.nextToken();
            }
            for(i=0; i < array.length ; i=i+3){
                RestaurantNM.add(array[i]);
                RestaurantXCODE.add(Double.parseDouble(array[i+1]));
                RestaurantYCODE.add(Double.parseDouble(array[i+2]));
            }
    }
}