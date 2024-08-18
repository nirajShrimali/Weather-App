package com.example.wheatherwithjava;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Method;
import java.security.Permission;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private ArrayList<forecastRVModal> forecastRVModalArrayList;
    private WeatherRVAdapter weatherRVAdapter;
    private LocationManager locationManager;
    private int PERMISSION_CODE = 1;
    private String city;
    private TextView cityNameTV;
    private ProgressBar pb;
    private RecyclerView forecast;
    private TextView temper;
    private ImageView tempImg;
    private TextView conditionTxt;
    private EditText cityNameEdt;
    private ImageView searchIV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        pb = findViewById(R.id.progressbar);
        cityNameTV = findViewById(R.id.city_txt);
        cityNameEdt = findViewById(R.id.city_edtxt);
        temper = findViewById(R.id.temp);
        tempImg = findViewById(R.id.tempimg);
        forecast = findViewById(R.id.forecast_rv);
        searchIV = findViewById(R.id.searchIV);
        forecastRVModalArrayList = new ArrayList<>();
        weatherRVAdapter = new WeatherRVAdapter(this, forecastRVModalArrayList);
        conditionTxt = findViewById(R.id.condition_txt);
        forecast.setAdapter(weatherRVAdapter);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_CODE);
        }

        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        city = getCity(location.getLongitude(),location.getLatitude());
        getWeather(city);

        searchIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String cityName = cityNameEdt.getText().toString();
                if(!cityName.equals("")) {
                    cityNameTV.setText(cityName);
                    getWeather(cityName);
                }else{
                    Toast.makeText(MainActivity.this, "Please enter city name!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==PERMISSION_CODE){
            if(grantResults.length > 0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(this, "Please provide the permissions", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private String getCity(double longitude, double latitude) {
        String cityName = "Not Found";
        Geocoder geocoder = new Geocoder(getBaseContext(),Locale.getDefault());

        try {
            List<Address> addresses = geocoder.getFromLocation(latitude,longitude,10);

            for(Address adr : addresses){
                if(adr!=null){
                    String city = adr.getLocality();
                    if(city!=null && !city.equals("")){
                        cityName = city;
                    }
                }
            }

            if(cityName.equals("Not found")){
                Toast.makeText(this, "User city not found", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return cityName;
    }

    private void getWeather(String city){
        String URL = "https://api.weatherapi.com/v1/forecast.json?key=ea36bfd1ba7f425c94d190152240408&q=" + city + "&days=1&aqi=no&alerts=no";
        cityNameTV.setText(city);

        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                pb.setVisibility(View.GONE);
                forecast.setVisibility(View.VISIBLE);
                forecastRVModalArrayList.clear();

                try {
                    String temp = response.getJSONObject("current").getString("temp_c");
                    temper.setText(temp+"°C");
                    int isDay = response.getJSONObject("current").getInt("is_day");
                    if(isDay==0){
                        Toast.makeText(MainActivity.this, "Night", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(MainActivity.this, "Day", Toast.LENGTH_SHORT).show();
                    }
                    String condition = response.getJSONObject("current").getJSONObject("condition").getString("text");
                    String conditionImg = response.getJSONObject("current").getJSONObject("condition").getString("icon");
                    Picasso.get().load("https:" + conditionImg).into(tempImg);
                    conditionTxt.setText(condition);
                    JSONObject forecast = response.getJSONObject("forecast");
                    JSONObject forecast0 = forecast.getJSONArray("forecastday").getJSONObject(0);
                    JSONArray hourArray = forecast0.getJSONArray("hour");

                    for(int i = 0;i<hourArray.length();i++){
                        JSONObject hourObj = hourArray.getJSONObject(i);

                        String time = hourObj.getString("time");
                        String tempe = hourObj.getString("temp_c");
                        String icon = hourObj.getJSONObject("condition").getString("icon");
                        String windSpeed = hourObj.getString("wind_kph");

                        forecastRVModalArrayList.add(new forecastRVModal(time,tempe,icon,windSpeed));
                    }
                    weatherRVAdapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Please enter valid city name", Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue.add(jsonObjectRequest);
    }

}
