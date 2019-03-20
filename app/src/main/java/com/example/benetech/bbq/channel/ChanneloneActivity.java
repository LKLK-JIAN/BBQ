package com.example.benetech.bbq.channel;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.benetech.bbq.R;


public class ChanneloneActivity extends AppCompatActivity {
    public TextView channelone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_channelone);
        channelone=findViewById(R.id.channel_one);
    }
}
