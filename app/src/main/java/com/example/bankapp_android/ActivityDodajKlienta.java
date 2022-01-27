package com.example.bankapp_android;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class ActivityDodajKlienta extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dodaj_klienta);
    }

    public void dodajKlientaApp(View view) {
        EditText inputImie = findViewById(R.id.editTextDodajImie);
        EditText inputNazwisko = findViewById(R.id.editTextDodajNazwisko);

        String imieNowegoKlienta = inputImie.getText().toString();
        String nazwiskoNowegoKlienta = inputNazwisko.getText().toString();

        MainActivity.listaKlientow.dodajKlientaDoListy(new Klient(imieNowegoKlienta, nazwiskoNowegoKlienta));

        Intent intentListaKlientow = new Intent(this, ActivityListaKlientow.class);

        String infoDodanoKlienta = "Pomyślnie dodano nowego klienta : " + imieNowegoKlienta + " " + nazwiskoNowegoKlienta;

        Toast.makeText(ActivityDodajKlienta.this, infoDodanoKlienta, Toast.LENGTH_SHORT).show();

        startActivity(intentListaKlientow);
    }
}
