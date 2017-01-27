package com.example.lavanya.hikerswatch;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    LocationManager locationManager;
    LocationListener locationListener;


    public void updatelocation(Location location){
        Log.i("location",location.toString());
        TextView latextview= (TextView) findViewById(R.id.latitudetext);
        TextView longtextview= (TextView) findViewById(R.id.longitudetext);
        TextView actextview= (TextView) findViewById(R.id.accuracytext);
        TextView altextview= (TextView) findViewById(R.id.altitudetext);
        TextView addrestext= (TextView) findViewById(R.id.addresstext);
        latextview.setText("Latitude: " +location.getLatitude());
        longtextview.setText("Longitude: " + location.getLongitude());
        actextview.setText("Accuracy: " + location.getAccuracy());
        altextview.setText("Altitude: " + location.getAltitude());
        String address="Address:";
        Geocoder geocoder=new Geocoder(getApplicationContext(), Locale.US);
        try {
            List<Address> addressList=geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
            if(addressList.size()>0 && addressList.get(0)!=null) {
                if(addressList.get(0).getSubThoroughfare() !=null) {
                    address = address + addressList.get(0).getSubThoroughfare()+" ";

                }
                if(addressList.get(0).getThoroughfare() !=null) {
                    address = address + addressList.get(0).getThoroughfare()+"\n";

                }
                if(addressList.get(0).getLocality() !=null) {
                    address = address + addressList.get(0).getLocality()+"\n";

                }
                if(addressList.get(0).getPostalCode() !=null) {
                    address = address + addressList.get(0).getPostalCode()+"\n";

                }
                if(addressList.get(0).getCountryName() !=null) {
                    address = address + addressList.get(0).getCountryName()+"\n";

                }
                addrestext.setText(address);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults.length>0 && grantResults[0]== PackageManager.PERMISSION_GRANTED){
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        locationManager= (LocationManager) getSystemService(LOCATION_SERVICE);
        Location location=locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if(location !=null)
            updatelocation(location);
        locationListener=new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Log.i("location",location.toString());
                updatelocation(location);
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };

        if(Build.VERSION.SDK_INT<23){
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);

        }
        else{
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},1);
            }
            else{
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);
                 location=locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if(location !=null)
                updatelocation(location);
            }

        }
    }
}
