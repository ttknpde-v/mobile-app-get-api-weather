package com.kotlin.ttnpdev.theweather.entity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Weather {
    private Float temp;
    private Float tempMin;
    private Float tempMax;
    private String date = new SimpleDateFormat("yyyy-MM-dd",   Locale.getDefault()).format(new Date());
    private String description;
    private String main;
    private String icon;


    public Weather(Float temp, Float tempMin, Float tempMax, String description, String main , String icon) {
        this.temp = temp;
        this.tempMin = tempMin;
        this.tempMax = tempMax;
        this.description = description;
        this.main = main;
        this.icon = icon;
    }

    public String getTemp() {
        float kelvin = Float.parseFloat(String.format("%.2f", this.temp));

        return String.format("%.2f",(kelvin - 273.15F))+"° C";
    }

    public String getTempMin() {
        float kelvin = Float.parseFloat(String.format("%.2f", this.tempMin));
        return String.format("%.2f",(kelvin - 273.15F))+"° C";
    }

    public String getTempMax() {
        float kelvin = Float.parseFloat(String.format("%.2f", this.tempMax));
        return String.format("%.2f",(kelvin - 273.15F))+"° C";
    }

    public String getDate() {
        return "Prediction today "+date;
    }

    public String getDescription() {
        return description;
    }

    public String getMain() {
        return main;
    }

    public String getIcon() {
        return "https://openweathermap.org/img/w/"+icon+".png";
    }
}
