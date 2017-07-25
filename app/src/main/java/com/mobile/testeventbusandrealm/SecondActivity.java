package com.mobile.testeventbusandrealm;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import org.greenrobot.eventbus.EventBus;

public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
    }

    @Override
    public void onStart()
    {
        super.onStart();
        Button interruptCount = (Button) findViewById(R.id.stop_button);

        interruptCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }


    @Override
    public void onDestroy(){
        super.onDestroy();
    }



}
