package com.zerobug.unite.mybike;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.github.anastr.speedviewlib.SpeedView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


public class DashboardFragment extends Fragment implements LocationListener {

    private View mDashboardView;
    private SpeedView speedoMeter;

    private FirebaseDatabase mDatabase;
    private DatabaseReference mRef;
    private ValueEventListener mPostListner;

    private String baseURL = "http://api.openweathermap.org/data/2.5/weather?q=";
    private String API = "&APPID=ce0561bfb7f85db1240dbe34ead1561d";
    private LocationManager locationManager;
    private String provider;
    private Location location;

    double lat, lng;
    String city, myURL, engTemp, turbidity;

    private TextView temp, wType, wCity, eTemp, eOil;

    public DashboardFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mDashboardView = inflater.inflate(R.layout.fragment_dashboard, container, false);

        temp = mDashboardView.findViewById(R.id.temp);
        wType = mDashboardView.findViewById(R.id.wType);
        wCity = mDashboardView.findViewById(R.id.city);
        eTemp = mDashboardView.findViewById(R.id.eng_temp);
        eOil = mDashboardView.findViewById(R.id.engOil);

        mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference();
//        mDatabase.setValue("Rashmiranjan");


        speedoMeter = mDashboardView.findViewById(R.id.speedView);
        speedoMeter.speedTo(50, 4000);

        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, false);

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

        }
        location = locationManager.getLastKnownLocation(provider);

        if (location != null) {
            Log.d("Provider :", provider + " has been selected.");
            getLatLng(location);
            getLocation(location);
        } else {
            Log.d("Provider :", "No provider");
        }
        
        getWeather();

        return mDashboardView;
    }

    @Override
    public void onStart() {
        super.onStart();

        getEngineTemperature();
        getTurbidityData();
    }

    private void getTurbidityData() {

        mRef.child("turbidity").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                turbidity = dataSnapshot.getValue().toString();
                Log.d("Engine Oil", turbidity);
                eOil.setText(turbidity);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void getEngineTemperature() {

        mRef.child("temp").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                engTemp = dataSnapshot.getValue().toString();
                Log.d("Engine Temperature", engTemp);
                eTemp.setText(engTemp);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void getWeather() {

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, myURL, null,

                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("JSON", "JSON: " + response);

                        try {
                            String info = response.getString("weather");
                            Log.i("INFO", "INFO: " + info);
                            JSONArray ar = new JSONArray(info);

                            for (int i = 0; i < ar.length(); i++) {
                                JSONObject parObj = ar.getJSONObject(i);

                                String myWeather = parObj.getString("main");
//                                result.setText(myWeather);
                                Log.d("myweather", myWeather);
                                Log.d("ID", "ID: " + parObj.getString("id"));
                                Log.d("MAIN", "MAIN: " + parObj.getString("main"));
                                wType.setText(myWeather);

                            }

                            JSONObject main = response.getJSONObject("main");
                            String temperature =  String.format("%.2f", main.getDouble("temp"));
                            double tempCelcius = Double.parseDouble(temperature);
                            tempCelcius = tempCelcius - 273.15;
                            int r = (int) Math.round(tempCelcius*100);
                            double f = r / 100.0;
                            temp.setText(f + " ℃");


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("ERROR", "Something Went Wrong " + error);

                    }
                }

        );
        MySingleton.getInstance(getActivity()).addToRequestQue(jsonObjectRequest);

    }

    @Override
    public void onResume() {
        super.onResume();
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        locationManager.requestLocationUpdates(provider, 400, 1, this);
    }

    @Override
    public void onPause() {
        super.onPause();
        locationManager.removeUpdates(this);
    }

    public void getLatLng(Location location) {

        //You had this as int. It is advised to have Lat/Loing as double.
        lat = location.getLatitude();
        lng = location.getLongitude();

        Log.d("Latitude: ", String.valueOf(lat));
        Log.d("Longitude: ", String.valueOf(lng));

    }

    public void getLocation(Location location) {

        try {
            Geocoder geocoder = new Geocoder(getActivity());
            List<Address> addresses = null;
            addresses = geocoder.getFromLocation(lat, lng, 1);
            city = addresses.get(0).getLocality();
            Log.d("CITY: ", city);
            myURL = baseURL + city + API;
            wCity.setText(city);

        } catch (IOException e) {
            e.printStackTrace();

        }

    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {
        Toast.makeText(getContext(), "Enabled new provider " + provider,
                Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(getContext(), "Disabled provider " + provider,
                Toast.LENGTH_SHORT).show();
    }


}
