package com.example.sujae.infoism;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;

public class LoadingActivity extends FragmentActivity {

    public static Activity mLoadingActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        mLoadingActivity = LoadingActivity.this;

        new Handler().postDelayed(new Runnable() {
            public void run() {
                startActivity(new Intent(getApplicationContext(), StartActivity.class));
            }
        }, 2000);
    }
}
