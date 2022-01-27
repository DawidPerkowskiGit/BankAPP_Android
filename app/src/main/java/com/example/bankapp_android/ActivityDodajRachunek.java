package com.example.bankapp_android;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class ActivityDodajRachunek extends AppCompatActivity {

    Intent intentPrevWindow;

    private int clientID;

    public int getClientID() {
        return clientID;
    }

    public void setClientID(int clientID) {
        this.clientID = clientID;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dodaj_rachunek);

        intentPrevWindow = getIntent();
        setClientID(intentPrevWindow.getIntExtra("clientID", -1));
    }

    public void dodajRachunekApp(View view) {
        Intent intentListaRachunkow = new Intent(this, ActivityListaRachunkow.class);
        intentListaRachunkow.putExtra("clientID", getClientID());

        EditText inputNazwaRachunku = findViewById(R.id.editTextDodajRachunek);

        String nazwaNowegoRachunku = inputNazwaRachunku.getText().toString();

        Switch switchWyborRachunku = findViewById(R.id.switchWyborRachunku);

        if (getClientID() > -1) {
            String infoDodanoRachunek = "Pomyślnie dodano nowy rachunek";

            if (!(switchWyborRachunku.isChecked())) {
                infoDodanoRachunek += " zwykły : " + nazwaNowegoRachunku;
                MainActivity.listaKlientow.getListaWszystkichKlientow().get(clientID).dodajRachunekZwykly(nazwaNowegoRachunku);
            }
            else {
                infoDodanoRachunek += " oszczędnościowy : " + nazwaNowegoRachunku;
                MainActivity.listaKlientow.getListaWszystkichKlientow().get(clientID).dodajRachunekOszczednosciowy(nazwaNowegoRachunku);
            }


            Toast.makeText(ActivityDodajRachunek.this, infoDodanoRachunek, Toast.LENGTH_SHORT).show();
            intentListaRachunkow.putExtra("clientID", getClientID());
            startActivity(intentListaRachunkow);
        }
        else {
            Toast.makeText(ActivityDodajRachunek.this, "Błędne dane klienta, nie dodano rachunku", Toast.LENGTH_SHORT).show();
        }


    }
}
