package ru.abelov.compassviewtestapplication;

import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import ru.abelov.compassview.GISensors;

public class MainActivity extends AppCompatActivity {

    ru.abelov.compassview.CompassView compass;
    ru.abelov.compassview.CompassView compass_new;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        compass = (ru.abelov.compassview.CompassView) findViewById(R.id.compass);
        compass_new = (ru.abelov.compassview.CompassView) findViewById(R.id.compass_new);
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

    }
}
