package com.example.benetech.bbq.channel;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.benetech.bbq.R;


public class ChanneltwoActivity extends AppCompatActivity {

    public static TextView channel_two;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_channeltwo);
        channel_two=findViewById(R.id.channel_two);
    }
}
