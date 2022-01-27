package com.example.bankapp_android;

import android.Manifest;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by Arvin on 4/5/2018.
 * https://www.sourcecodester.com/android/12188/android-simple-gps-location.html
 */

public class ActivityLokalizacja extends AppCompatActivity {

    Button buttonGenerujKoordynaty;
    TextView textViewSzerokosc, textViewDlugosc, textViewAdres, textViewKraj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lokalizacja);

        initWidgets();

        ActivityCompat.requestPermissions(ActivityLokalizacja.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 123);
    }

    public void checkLoc(View view) throws IOException, InterruptedException {
        GPSLocator gpsLocator = new GPSLocator(getApplicationContext());
        Location location = gpsLocator.GetLocation();
        if (location != null) {
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            textViewSzerokosc.setText(String.valueOf(latitude));
            textViewDlugosc.setText(String.valueOf(longitude));
/**
 *
 * Autor : https://stackoverflow.com/questions/57863500/how-can-i-get-my-current-location-in-android-using-gps
 */
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());

            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 10);
            if (addresses != null && addresses.size() > 0) {
                textViewKraj.setText(addresses.get(0).getCountryName());
                textViewAdres.setText(addresses.get(0).getAddressLine(0));
            }


        }
    }

    public void initWidgets() {
        buttonGenerujKoordynaty = findViewById(R.id.buttonGenerujKoordynaty);
        textViewSzerokosc = findViewById(R.id.textViewSzerokosc);
        textViewDlugosc = findViewById(R.id.textViewDlugosc);
        textViewAdres = findViewById(R.id.textViewAdres);
        textViewKraj = findViewById(R.id.textViewKraj);
    }
}
