package com.kotlin.ttnpdev.theweather.controller.display;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.kotlin.ttnpdev.theweather.R;
import com.kotlin.ttnpdev.theweather.entity.Weather;
import com.kotlin.ttnpdev.theweather.service.ServiceWeatherApi;
import com.squareup.picasso.Picasso;
import org.json.JSONException;

import java.net.UnknownHostException;

import static android.content.ContentValues.TAG;

public class Display extends AppCompatActivity  implements View.OnClickListener  , Response.Listener , Response.ErrorListener{
    private StringRequest request; // StringRequest is  A canned request for retrieving the response body at a given URL as a String.
    private RequestQueue queue; // RequestQueue is where all the requests are queued up that has to be executed, it also manages the worker threads and maintain network call in the background also it handles reading from and writing to the cache and also parsing the response and delivering the parsed response to mainthread
    private FloatingActionButton fabCity;
    private TextInputLayout textLayout;
    private TextInputEditText editCity;
    private ColorStateList colorStateList; // for set color of TextInputLayout
    private Integer color; // for store color code xml
    private ServiceWeatherApi serviceWeatherApi;
    private TextView resultTemp,resultTempMinimum,resultTempMaximum,resultPredictDate,resultDescription,resultMain;
    private ImageView resultIconWeather;

    private void getWidgetFirstAction () {
        textLayout = findViewById(R.id.textLayout);
        editCity = findViewById(R.id.editCity);
        fabCity = findViewById(R.id.fabCity);
    }

    private void getWidgetSecondAction() {
        resultTemp = findViewById(R.id.resultTemp);
        resultTempMinimum = findViewById(R.id.resultTempMinimum);
        resultTempMaximum = findViewById(R.id.resultTempMaximum);
        resultPredictDate = findViewById(R.id.resultPredictDate);
        resultDescription = findViewById(R.id.resultDescription);
        resultMain = findViewById(R.id.resultMain);
        resultIconWeather = findViewById(R.id.resultIconWeather);
    }
    protected void display() {
        getWidgetFirstAction();
        fabCity.setOnClickListener(this::onClick);
    }

    private void afterClicked() {
        String city = editCity.getText().toString().trim().replaceAll(" ",""); // in java use getText() for widget edit
        if (!(city.isEmpty())) {
            Log.d(TAG, "afterClicked : "+city+" valid");
            color = getColor(R.color.green);
            colorStateList = ColorStateList.valueOf(color);
            textLayout.setHint("valid");
            textLayout.setHintTextColor(colorStateList);
            textLayout.setBoxStrokeColor(color);
            getApiWeather(city);
        }
        else {
            // Log.d(TAG, "afterClicked : "+city+" invalid");
            Toast.makeText(this , "city shouldn't be empty",Toast.LENGTH_SHORT).show();
            color = getColor(R.color.red);
            colorStateList = ColorStateList.valueOf(color);
            textLayout.setHint("invalid");
            textLayout.setHintTextColor(colorStateList);
            textLayout.setBoxStrokeColor(color); // set เส้นขอบ
        }
    }

    private void getApiWeather (String city) {
        serviceWeatherApi = new ServiceWeatherApi(city); /* Create Object ServiceWeatherApi class */
        /* In line below when this::onResponse work compiler will go to onResponse()
        *  and will work following the order
        *  when finishing onResponse() it will return StringRequest class and store to variable request */
        request = new StringRequest(Request.Method.POST , serviceWeatherApi.getURL() , this::onResponse , this::onErrorResponse );
        queue = Volley.newRequestQueue(this); // for controller any request in app
        queue.add(request); // Calling add(Request) will enqueue the given Request for dispatch, resolving from either cache or network on a worker thread, and then delivering a parsed response on the main thread.
    }
    @Override
    public void onClick(View v) {
        // Log.d(TAG, "logging after fab clicked");
        afterClicked();
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Log.d(TAG, "logging after request api failed");
        color = getColor(R.color.red);
        colorStateList = ColorStateList.valueOf(color);
        textLayout.setHint("invalid");
        textLayout.setHintTextColor(colorStateList);
        textLayout.setBoxStrokeColor(color); // set เส้นขอบ
        /* when doesn't connect internet
        error.toString() : com.android.volley.NoConnectionError: java.net.UnknownHostException: Unable to resolve host "api.openweathermap.org": No address associated with hostname
        error.getMessage() : java.net.UnknownHostException: Unable to resolve host "api.openweathermap.org": No address associated with hostname*/
        try {
            // Log.d(TAG, String.valueOf(error.getMessage().indexOf("java.net.UnknownHostException"))); when find than return zero
            if (error.getMessage().indexOf("java.net.UnknownHostException") == 0) {
                Toast.makeText(this,"check your internet before put on city name",Toast.LENGTH_SHORT).show();
            }
            Log.d(TAG, error.getMessage()); // when url wasn't correct it will throw exception
        } catch (java.lang.NullPointerException nullPointerException) {
        Toast.makeText(this,"city name wasn't correct api form",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onResponse(Object response) {
        Log.d(TAG, "logging after request api successes");
        // Log.d(TAG, "response stores : {"+response+"}"); // response stores : {{"coord":{"lon":100.5167,"lat":13.75},"weather":[{"id":804,"main":"Clouds","description":"overcast clouds","icon":"04d"}],"base":"stations","main":{"temp":305.88,"feels_like":312.88,"temp_min":303.1,"temp_max":309.21,"pressure":1008,"humidity":82,"sea_level":1008,"grnd_level":1006},"visibility":10000,"wind":{"speed":2.77,"deg":185,"gust":4.6},"clouds":{"all":100},"dt":1691232062,"sys":{"type":2,"id":2042694,"country":"TH","sunrise":1691190185,"sunset":1691235892},"timezone":25200,"id":1609350,"name":"Bangkok","cod":200}}
        // again ! serviceWeatherApi object got build in getApiWeather() method
        // So i don't need to create object again
        getWidgetSecondAction();
        try {
            serviceWeatherApi.jsonObjectWeatherApiResponse((String) response);
            Weather weather = serviceWeatherApi.getWeather();
            resultTemp.setText(weather.getTemp());
            resultTempMinimum.setText(weather.getTempMin());
            resultTempMaximum.setText(weather.getTempMax());
            resultPredictDate.setText(weather.getDate());
            resultDescription.setText(weather.getDescription());
            resultMain.setText(weather.getMain());
            Picasso.get().load(weather.getIcon()).into(resultIconWeather); // Picasso for getting image from url

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
}