package com.example.sujae.infoism;

import android.app.Activity;
import android.os.Bundle;
import android.os.StrictMode;
import android.widget.TextView;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;
import java.net.URL;

public class CrimeInfo extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crimeinfo);
        StrictMode.enableDefaults();
        TextView status1 = (TextView)findViewById(R.id.text); //파싱된 결과확인!
        boolean incontinent = false, inename = false, inname = false, innews = false, inwrtDt = false, initem=false;
        String continent = null, ename = null, name = null, news = null, wrtDt = null;

        try{
            URL url = new URL("http://apis.data.go.kr/1262000/AccidentService/getAccidentList?"
                    + "&isoCode1=CHN&isoCode2=IRQ&ServiceKey="
                    + "qVtieL2r0pCsDUULqxPRLfwEFx5bPYvlq0hn3ioPYbB6CTPJbmEemyGT27f6eqPCW%2By%2FrCvigpanjyoXPYJ7Wg%3D%3D"
            ); //검색 URL부분

            XmlPullParserFactory parserCreator = XmlPullParserFactory.newInstance();
            XmlPullParser parser = parserCreator.newPullParser();

            parser.setInput(url.openStream(), null);

            int parserEvent = parser.getEventType();
            System.out.println("파싱시작합니다.");

            while (parserEvent != XmlPullParser.END_DOCUMENT){
                switch(parserEvent){
                    case XmlPullParser.START_TAG://parser가 시작 태그를 만나면 실행
                        if(parser.getName().equals("continent")){ //title 만나면 내용을 받을수 있게 하자
                            incontinent = true;
                        }
                        if(parser.getName().equals("ename")){ //address 만나면 내용을 받을수 있게 하자
                            inename = true;
                        }
                        if(parser.getName().equals("name")){ //mapx 만나면 내용을 받을수 있게 하자
                            inname = true;
                        }
                        if(parser.getName().equals("news")){ //mapy 만나면 내용을 받을수 있게 하자
                            innews = true;
                        }
                        if(parser.getName().equals("wrtDt")) { //mapy 만나면 내용을 받을수 있게 하자
                            inwrtDt = true;

                        }
                        break;

                    case XmlPullParser.TEXT://parser가 내용에 접근했을때
                        if(incontinent){ //isTitle이 true일 때 태그의 내용을 저장.
                            continent = parser.getText();
                            incontinent = false;
                        }
                        if(inname){ //isAddress이 true일 때 태그의 내용을 저장.
                            name = parser.getText();
                            inname = false;
                        }
                        if(innews){ //isMapx이 true일 때 태그의 내용을 저장.
                            news = parser.getText();
                            innews = false;
                        }
                        if(inwrtDt){ //isMapy이 true일 때 태그의 내용을 저장.
                            wrtDt = parser.getText();
                            inwrtDt = false;
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if(parser.getName().equals("item")){
                            status1.setText(status1.getText()+"대륙 : "+ continent +"\n 이름: "+ name +"\n 뉴스내용: " + news
                                    +"\n 작성일 : " + wrtDt +"\n");
                            initem=false;
                        }
                        break;
                }
                parserEvent = parser.next();
            }
        } catch(Exception e){
            status1.setText("에러가..났습니다...");
        }
    }
}