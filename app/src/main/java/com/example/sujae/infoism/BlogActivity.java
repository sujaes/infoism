package com.example.sujae.infoism;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class BlogActivity extends AppCompatActivity{

    private WebView mWebView;
    private WebSettings mWebSettings;
    private String storename;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog);
        Intent intent = getIntent(); /*데이터 수신*/

        String storename = intent.getExtras().getString("storename");

        mWebView = (WebView)findViewById(R.id.showblog);
        mWebView.setWebViewClient(new WebViewClient());
        mWebSettings = mWebView.getSettings();
        mWebSettings.setJavaScriptEnabled(true);

        mWebView.loadUrl("https://search.naver.com/search.naver?where=post&sm=tab_jum&query="+storename);
    }

    /**
     * @author aquilegia
     *
     */
    public static class GeoPoint {
        double x;
        double y;
        double z;

        /**
         *
         */
        public GeoPoint() {
            super();
        }

        /**
         * @param x
         * @param y
         */
        public GeoPoint(double x, double y) {
            super();
            this.x = x;
            this.y = y;
            this.z = 0;
        }

        /**
         * @param x
         * @param y
         * @param y
         */
        public GeoPoint(double x, double y, double z) {
            super();
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public double getX() {
            return x;
        }

        public double getY() {
            return y;
        }

        public double getZ() {
            return z;
        }
    }
}