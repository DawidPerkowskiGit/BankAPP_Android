package com.example.bankapp_android;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;

public class ActivityListaRachunkow extends AppCompatActivity {

    ListView listViewListaRachunkow;

    ArrayList<String> arrayListListaRachunkow = new ArrayList<>();

    public ArrayList<String> getArrayListListaRachunkow() {
        return this.arrayListListaRachunkow; }

    public void setListaRachunkow(ArrayList<String> inputList) {
        this.arrayListListaRachunkow.clear();
        this.arrayListListaRachunkow = inputList;
    }

    ArrayAdapter arrayAdapterListaRachunkow;


    int nextScreenID;

    public void setNextScreenID(int ID) {
        this.nextScreenID = ID;
    }

    public int getNextScreenID() {
        return this.nextScreenID;
    }

    int clientID = -1;
    public void setClientID(int id) {
        this.clientID = id;
    }

    int getClientID() {
        return this.clientID;
    }

    int rachunekID = -1;

    public void setRachunekID(int ID) {
        this.rachunekID = ID;
    }

    public int getRachunekID() {
        return this.rachunekID;
    }

    TextView textViewListaRachunkow;
    RadioButton radioButtonRachunkiLista, radioButtonRachunekDodaj, radioButtonRachunekUsun;


    Intent previousScreenintent;

    Bundle extras;

    private int actionMode;

    public int getActionMode() {
        return actionMode;
    }

    public void setActionMode(int actionMode) {
        this.actionMode = actionMode;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_rachunkow);

        previousScreenintent = getIntent();

        extras = getIntent().getExtras();

        initWidgets();
        setClientID(previousScreenintent.getIntExtra("clientID", 1));
        if (extras != null) {
            if (extras.containsKey("kwotaPrzelewu")) {
                setActionMode(1);
                textViewListaRachunkow.setText("Wybierz rachunek na który chcesz przelać pieniądze");
                radioButtonRachunekUsun.setText("");
                radioButtonRachunekDodaj.setText("");
            }
            else {


                Klient obecnyKlient = MainActivity.listaKlientow.getListaWszystkichKlientow().get(clientID);

                textViewListaRachunkow.setText(textViewListaRachunkow.getText() + " klienta - " + obecnyKlient.getImie() + " " + obecnyKlient.getNazwisko());

            }
        }





        updateRadioRachunki(radioButtonRachunkiLista);
        setNextScreenID(1);

        updateListaRachunkow();

        listViewListaRachunkow=(ListView) findViewById(R.id.listViewListaRachunkow);

        arrayAdapterListaRachunkow = new ArrayAdapter(this, android.R.layout.simple_list_item_1, getArrayListListaRachunkow());

        listViewListaRachunkow.setAdapter(arrayAdapterListaRachunkow);

        listViewListaRachunkow.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                setRachunekID(i);
            }
        });
    }

    private void updateRadioRachunki(RadioButton selected) {
        radioButtonRachunkiLista.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.radio_off));
        radioButtonRachunekDodaj.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.radio_off));
        radioButtonRachunekUsun.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.radio_off));

        selected.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.radio_on));
    }

    private void initWidgets() {
        textViewListaRachunkow = findViewById(R.id.textViewListaRachunkow);
        radioButtonRachunkiLista = findViewById(R.id.radioButtonRachunkiLista);
        radioButtonRachunekDodaj = findViewById(R.id.radioButtonRachunekiDodaj);
        radioButtonRachunekUsun = findViewById(R.id.radioButtonRachunkiUsun);
    }

    public void updateListaRachunkow() {
        ArrayList<String> tempList= new ArrayList<>();

        int iloscRachunkow = MainActivity.listaKlientow.getListaWszystkichKlientow().get(clientID).getListaRachunkow().size();

        for (int i = 0; i < iloscRachunkow ; i++) {
            tempList.add(i + 1 + ". " + MainActivity.listaKlientow.getListaWszystkichKlientow().get(clientID).getListaRachunkow().get(i).getNazwaRachunku() + " | Saldo : " + MainActivity.listaKlientow.getListaWszystkichKlientow().get(clientID).getListaRachunkow().get(i).getSaldo() + " złotych");
        }
        setListaRachunkow(tempList);
    }


    public void radioTappedRachunki(View view) {
        if (getActionMode() == 1) {
            setNextScreenID(1);
        }
        else {
            int operationId = view.getId();

            switch (operationId) {
                case R.id.radioButtonRachunkiLista: {
                    setNextScreenID(1);
                    updateRadioRachunki(radioButtonRachunkiLista);
                    break;
                }
                case R.id.radioButtonRachunekiDodaj: {
                    setNextScreenID(2);
                    updateRadioRachunki(radioButtonRachunekDodaj);
                    break;
                }
                case R.id.radioButtonRachunkiUsun: {
                    setNextScreenID(3);
                    updateRadioRachunki(radioButtonRachunekUsun);
                    break;
                }
                default: {
                    setNextScreenID(1);
                }
            }
        }
    }

    public void renderNextScreenRachunki(View view) {

        Intent nextScreenIntent;

        if (getActionMode() == 1) {
            int odbiorcaID = extras.getInt("odbiorcaID", -1);
            int zlecajacyID = extras.getInt("zlecajacyID", -1);
            int rachunekZlecajacego = extras.getInt("rachunekZlecajacegoID", -1);
            double kwotaPrzelewu = extras.getDouble("kwotaPrzelewu");
            String wynikPrzelewu = MainActivity.getListaKlientow().wykonajPrzelew(zlecajacyID, rachunekZlecajacego, odbiorcaID, getRachunekID(), kwotaPrzelewu);
            Toast.makeText(ActivityListaRachunkow.this, wynikPrzelewu, Toast.LENGTH_SHORT).show();
            nextScreenIntent = new Intent(this, MainActivity.class);
            nextScreenIntent.putExtra("flagDataPopulated", true);
            startActivity(nextScreenIntent);
        }
        else {
            int iloscRachunkow =  MainActivity.listaKlientow.getListaWszystkichKlientow().get(clientID).getListaRachunkow().size();



            switch (getNextScreenID()) {
                case 1:{
                    if (rachunekID < 0) {
                        Toast.makeText(ActivityListaRachunkow.this, "Wybierz rachunek, którego historię transakcji chcesz wyświetlić", Toast.LENGTH_SHORT).show();
                    }
                    else if (iloscRachunkow > 0){
                        nextScreenIntent = new Intent(this, ActivityHistoriaTransakcji.class);
                        nextScreenIntent.putExtra("rachunekID", getRachunekID());
                        nextScreenIntent.putExtra("clientID", getClientID());
                        startActivity(nextScreenIntent);
                    }
                    else {
                        Toast.makeText(ActivityListaRachunkow.this, "Brak rachunków do wyświetlenia", Toast.LENGTH_SHORT).show();
                    }
                    break;
                }
                case 2: {
                    nextScreenIntent = new Intent(this, ActivityDodajRachunek.class);
                    nextScreenIntent.putExtra("clientID", getClientID());
                    startActivity(nextScreenIntent);
                    break;
                }
                case 3: {
                    if (rachunekID < 0) {
                        Toast.makeText(ActivityListaRachunkow.this, "Wybierz rachunek, który chcesz usunąć", Toast.LENGTH_SHORT).show();
                    }
                    else if (rachunekID < MainActivity.listaKlientow.getListaWszystkichKlientow().get(clientID).getListaRachunkow().size()){
                        Toast.makeText(ActivityListaRachunkow.this, "Usunięto rachunek : " + MainActivity.listaKlientow.getListaWszystkichKlientow().get(clientID).getListaRachunkow().get(getRachunekID()).getNazwaRachunku(), Toast.LENGTH_SHORT).show();
                        MainActivity.listaKlientow.getListaWszystkichKlientow().get(clientID).usunRachunek(rachunekID);
                        getArrayListListaRachunkow().remove(rachunekID);
                        updateListaRachunkow();
                        arrayAdapterListaRachunkow = new ArrayAdapter(this, android.R.layout.simple_list_item_1, getArrayListListaRachunkow());

                        listViewListaRachunkow.setAdapter(arrayAdapterListaRachunkow);
                    }
                    else if (iloscRachunkow <= 0) {
                        Toast.makeText(ActivityListaRachunkow.this, "Brak rachunków do usunięcia", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(ActivityListaRachunkow.this, "Wybierz rachunek, który chcesz usunąć", Toast.LENGTH_SHORT).show();
                    }
                }
                default: {
                    nextScreenIntent = new Intent(this, ActivityListaRachunkow.class);
                }
        }


        }
    }
}
