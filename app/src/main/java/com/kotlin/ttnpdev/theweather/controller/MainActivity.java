package com.kotlin.ttnpdev.theweather.controller;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.kotlin.ttnpdev.theweather.R;
import com.kotlin.ttnpdev.theweather.controller.display.Display;
import com.kotlin.ttnpdev.theweather.entity.Weather;

public class MainActivity extends Display {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity);
        display();
    }
}