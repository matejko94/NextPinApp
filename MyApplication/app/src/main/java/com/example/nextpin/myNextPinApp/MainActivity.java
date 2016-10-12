package com.example.nextpin.myNextPinApp;


import android.content.Intent;
import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import android.support.design.widget.FloatingActionButton;

import com.example.nextpin.myNextPinApp.preference.SharedPrefUtils;

import net.nextpin.geolib.NextPin;
import net.nextpin.geolib.NextPinListener;
import net.nextpin.geolib.types.GeoActivity;

import java.util.Calendar;
import java.util.List;
public class MainActivity extends AppCompatActivity implements NextPinListener,NextPin.GeoActivityReceiver {

    String nextPinToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NextPin nextPin= NextPin.getNextPinInstance(this);
        nextPinToken = SharedPrefUtils.getString(getApplicationContext(),
                PreferencesFragment.PREF_TOKEN);

        nextPin.start();
        nextPin.setToken(nextPinToken);
        nextPin.setNotificationGpsOff(true, R.mipmap.ic_launcher, MainActivity.class);
        nextPin.addListener(this);

        Calendar start = Calendar.getInstance();
        start.set(Calendar.DAY_OF_MONTH,1);
        start.set(Calendar.MONTH,9);
        start.set(Calendar.YEAR,2016);
        start.set(Calendar.HOUR_OF_DAY, 0);
        start.set(Calendar.MINUTE, 0);
        start.set(Calendar.SECOND, 0);
        start.set(Calendar.MILLISECOND, 0);

        Calendar end = Calendar.getInstance();
        end.set(Calendar.DAY_OF_MONTH,5);
        end.set(Calendar.MONTH,9);
        end.set(Calendar.YEAR,2016);
        end.set(Calendar.HOUR_OF_DAY, 23);
        end.set(Calendar.MINUTE, 59);
        end.set(Calendar.SECOND, 59);
        end.set(Calendar.MILLISECOND, 999);
        //example how to call Activities for specific days
        nextPin.getGeoActivities(this,start,end);




        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, PrefsActivity.class);
                startActivity(intent);
            }
        });


    }

    @Override
    public void receiveGeoActivities(List<GeoActivity> list) {


        TextView textView= (TextView) findViewById(R.id.testView);
        String test="";
        for(GeoActivity geoActivity:list)
            test=test+geoActivity.getName()+" "+geoActivity.getDuration()+" ";
        textView.setText(test);

    }

    @Override
    public void onLocalLocationDetected(Location location) {

    }

    @Override
    public void onDBChangeDetected(int i) {

    }

    @Override
    public void onLastSentLocation(Location location) {

    }

    @Override
    public void onWakeUp() {

    }

    @Override
    public void onLastRejectedLocation(Location location) {

    }

    @Override
    public void onNewStayPoint() {

    }

    @Override
    public void onEditStaypPoint() {

    }
}