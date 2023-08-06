package com.kotlin.ttnpdev.theweather.service;

import android.util.Log;
import com.kotlin.ttnpdev.theweather.entity.Weather;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ServiceWeatherApi {
    private final String APIKEY = "9b58796a91eb98062a4cd0ba4f90f048";
    private String url;
    private JSONObject jsonObjectResponse;
    public ServiceWeatherApi(String city) {
        url = "http://api.openweathermap.org/data/2.5/weather?q="+city+"&appid="+ APIKEY;
    }
    public String getURL() {
        return url;
    }
    // we set args like string json for setting to JSONObject jsonObjectResponse So u should use method in org.json for convert them to type java
    public void jsonObjectWeatherApiResponse(String response) throws JSONException {
        jsonObjectResponse = new JSONObject(response);
        // Log.d("DEBUG", "jsonObjectResponse {"+jsonObjectResponse+"}");
      /*
        {
            "coord": {
                "lon": -0.1257,
                "lat": 51.5085
            },
            "weather": [
                {
                    "id": 501,
                    "main": "Rain",
                    "description": "moderate rain",
                    "icon": "10d"
                }
            ],
            "base": "stations",
            "main": {
                "temp": 291.58,
                "feels_like": 291.49,
                "temp_min": 289.51,
                "temp_max": 293.02,
                "pressure": 986,
                "humidity": 77
            },
            "visibility": 10000,
            "wind": {
                "speed": 8.23,
                "deg": 240
            },
            "rain": {
                "1h": 3.65
            },
            "clouds": {
                "all": 75
            },
            "dt": 1690977928,
            "sys": {
                "type": 2,
                "id": 2075535,
                "country": "GB",
                "sunrise": 1690950304,
                "sunset": 1691005704
            },
            "timezone": 3600,
            "id": 2643743,
            "name": "London",
            "cod": 200
        }
        */
    }
    private JSONObject getTheWeatherApiObjectMain() throws JSONException {
        JSONObject main = jsonObjectResponse.getJSONObject("main"); // retrieve only object named "main"
        // Log.d("DEBUG", "getJSONObject(\"main\") {"+main+"}");
        return main;
    }
/*    private JSONObject getTheWeatherApiObjectSys() throws JSONException {
        JSONObject coord = jsonObjectResponse.getJSONObject("sys"); // retrieve only object named "sys"
        // Log.d("DEBUG", "getJSONObject(\"sys\") {"+coord+"}");
        return coord;
    }*/
    private JSONArray getTheWeatherApiArrayWeather() throws JSONException {
        JSONArray weather = jsonObjectResponse.getJSONArray("weather");  // retrieve only Array named "weather"
        // Log.d("DEBUG", "getJSONArray(\"weather\") {"+weather+"}");
        /* we use json array because it's array , focus [] */
        return weather;
    }
    private Float getTemp() throws JSONException {
        JSONObject temp = getTheWeatherApiObjectMain();
        float kalvin = Float.parseFloat(String.valueOf(temp.getDouble("temp")));
        return kalvin;
    }
    private Float getTempMin() throws JSONException {
        JSONObject temp = getTheWeatherApiObjectMain();
        float kalvin = Float.parseFloat(String.valueOf(temp.getDouble("temp_min")));
        return kalvin;
    }
    private Float getTempMax() throws JSONException {
        JSONObject temp = getTheWeatherApiObjectMain();
        float kalvin = Float.parseFloat(String.valueOf(temp.getDouble("temp_max")));
        return kalvin;
    }

    private String getDescription() throws JSONException {
        // retrieve json array element 0 and convert to json object
        JSONObject zero = getTheWeatherApiArrayWeather().optJSONObject(0);
        // result looks like : { "icon":"03n","description":"scattered clouds","main":"Clouds","id":802 }
        // so , you can get any key thru method getString() , getDouble() ,...
        return zero.getString("description");
    }
    private String getMain() throws JSONException {
        // retrieve json array element 0 and convert to json object
        JSONObject zero = getTheWeatherApiArrayWeather().optJSONObject(0);
        // result looks like : { "icon":"03n","description":"scattered clouds","main":"Clouds","id":802 }
        // so , you can get any key thru method getString() , getDouble() ,...
        return zero.getString("main");
    }
    private String getIcon() throws JSONException {
        // retrieve json array element 0 and convert to json object
        JSONObject zero = getTheWeatherApiArrayWeather().optJSONObject(0);
        return zero.getString("icon");
    }
    public Weather getWeather() throws JSONException {
        return new Weather(getTemp(),getTempMin(),getTempMax(),getDescription(),getMain(),getIcon());
    }

}
