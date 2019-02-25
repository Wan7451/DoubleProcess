package com.wan7451.doubleprocess;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startService(new Intent(this,LocalService.class));
        startService(new Intent(this,RemoteService.class));
        //定时执行任务
        startService(new Intent(this, JobHandleService.class));
    }
}
