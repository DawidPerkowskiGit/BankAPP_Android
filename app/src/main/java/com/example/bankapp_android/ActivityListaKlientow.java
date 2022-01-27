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

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;

public class ActivityListaKlientow extends AppCompatActivity {

    ListView listViewListaKlientow;

    ArrayList<String> arrayListKlientowImieNazwisko = new ArrayList<>();

    public ArrayList<String> getArrayListKlientowImieNazwisko() {
    return arrayListKlientowImieNazwisko; }

    public void setArrayListKlientowImieNazwisko(ArrayList<String> inputList) {
        this.arrayListKlientowImieNazwisko.clear();
        this.arrayListKlientowImieNazwisko = inputList;
    }

    private int actionMode;

    private int getActionMode() {
        return actionMode;
    }

    private void setActionMode(int actionMode) {
        this.actionMode = actionMode;
    }

    ArrayAdapter arrayAdapterListaKlientow;

    int nextScreenID;

    public void setNextScreenID(int ID) {
        this.nextScreenID = ID;
    }

    public int getNextScreenID() {
        return this.nextScreenID;
    }

    int clientID = -1;

    public void setClientID(int ID) {
        this.clientID = ID;
    }

    public int getClientID() {
        return this.clientID;
    }

    TextView textViewListaKlientow;
    RadioButton radioButtonKlienciLista, radioButtonKlienciDodaj, radioButtonKlienciUsun;

    Intent intentPreviousWindow;

    Bundle extras;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_klientow);

        initWidgets();
        updateRadioGroup(radioButtonKlienciLista);
        setNextScreenID(1);

        intentPreviousWindow = getIntent();

        extras = getIntent().getExtras();

        if (extras != null)
            if (extras.containsKey("kwotaPrzelewu")) {
                setActionMode(1);
                radioButtonKlienciDodaj.setText("");
                radioButtonKlienciUsun.setText("");
                textViewListaKlientow.setText("Wybierz klienta, który ma otrzymać przelew");
        }

        updateListaKlientowImieNazwisko();

        listViewListaKlientow=(ListView) findViewById(R.id.listViewKlienci);

        arrayAdapterListaKlientow = new ArrayAdapter(this, android.R.layout.simple_list_item_1, getArrayListKlientowImieNazwisko());

        listViewListaKlientow.setAdapter(arrayAdapterListaKlientow);

        listViewListaKlientow.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
               setClientID(i);
            }
        });
    }

    private void initWidgets() {
        textViewListaKlientow = findViewById(R.id.textViewListaKlientow);
        radioButtonKlienciLista = findViewById(R.id.radioButtonKlienciLista);
        radioButtonKlienciDodaj = findViewById(R.id.radioButtonKlienciDodaj);
        radioButtonKlienciUsun = findViewById(R.id.radioButtonKlienciUsun);

    }

    public void exitToMainMenu(View view) {
        Intent intentExitToMainMenu = new Intent(this, MainActivity.class);
        intentExitToMainMenu.putExtra("flagDataPopulated", true);
        startActivity(intentExitToMainMenu);
    }

    public void radioTappedKlienci(View view) {

        if (getActionMode() == 1) {
            setNextScreenID(1);
        }
        else {
            int operationId = view.getId();

            switch (operationId) {
                case R.id.radioButtonKlienciLista: {
                    setNextScreenID(1);
                    updateRadioGroup(radioButtonKlienciLista);
                    break;
                }
                case R.id.radioButtonKlienciDodaj: {
                    setNextScreenID(2);
                    updateRadioGroup(radioButtonKlienciDodaj);
                    break;
                }
                case R.id.radioButtonKlienciUsun: {
                    setNextScreenID(3);
                    updateRadioGroup(radioButtonKlienciUsun);
                    break;
                }
                default: {
                    setNextScreenID(1);
                }
            }
        }


        //Toast.makeText(ActivityListaKlientow.this, "Kliknięto opcję : " + current.getAccessibilityDelegate(), Toast.LENGTH_SHORT).show();
    }

    private void updateRadioGroup(RadioButton selected) {
        radioButtonKlienciLista.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.radio_off));
        radioButtonKlienciDodaj.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.radio_off));
        radioButtonKlienciUsun.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.radio_off));

        selected.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.radio_on));
    }

    public void updateListaKlientowImieNazwisko() {
        ArrayList<String> tempList= new ArrayList<>();

        int iloscKlientow = MainActivity.listaKlientow.getListaWszystkichKlientow().size();

        for (int i = 0; i < iloscKlientow ; i++) {
            tempList.add(i + 1 + ". " + MainActivity.listaKlientow.getListaWszystkichKlientow().get(i).getImie() + " " + MainActivity.listaKlientow.getListaWszystkichKlientow().get(i).getNazwisko());
        }

        setArrayListKlientowImieNazwisko(tempList);
    }

    public void renderNextScreenKlienci(View view) {
        Intent intentNextScreen;
        if (getActionMode() == 1) {
            if (clientID > -1) {
                intentNextScreen = new Intent(this, ActivityListaRachunkow.class);
                intentNextScreen.putExtra("kwotaPrzelewu", intentPreviousWindow.getDoubleExtra("kwotaPrzelewu", 0.00));
                intentNextScreen.putExtra("zlecajacyID", intentPreviousWindow.getIntExtra("zlecajacyID", -1));
                intentNextScreen.putExtra("rachunekZlecajacegoID", intentPreviousWindow.getIntExtra("rachunekZlecajacegoID", -1));
                intentNextScreen.putExtra("odbiorcaID", clientID);
                intentNextScreen.putExtra("clientID", getClientID());
                startActivity(intentNextScreen);
            }
        }
        else {
            int iloscKlientow = MainActivity.listaKlientow.getListaWszystkichKlientow().size();



            switch (getNextScreenID()) {
                case 1:{
                    if (clientID < 0) {
                        Toast.makeText(ActivityListaKlientow.this, "Wybierz klienta, którego listę rachunków chcesz zobaczyc", Toast.LENGTH_SHORT).show();
                    }
                    else if (iloscKlientow > 0){
                        intentNextScreen = new Intent(this, ActivityListaRachunkow.class);
                        intentNextScreen.putExtra("clientID", getClientID());
                        startActivity(intentNextScreen);
                    }
                    else {
                        Toast.makeText(ActivityListaKlientow.this, "Brak klientów - nie można wyświetlić rachunków", Toast.LENGTH_SHORT).show();
                    }
                    break;
                }
                case 2: {
                    intentNextScreen = new Intent(this, ActivityDodajKlienta.class);
                    startActivity(intentNextScreen);
                    break;
                }
                case 3: {
                    if (clientID < 0) {
                        Toast.makeText(ActivityListaKlientow.this, "Wybierz klienta, którego chcesz usunąć", Toast.LENGTH_SHORT).show();
                    }
                    else if (clientID < MainActivity.listaKlientow.getListaWszystkichKlientow().size()){
                        Toast.makeText(ActivityListaKlientow.this, "Usunięto klienta : " + MainActivity.listaKlientow.getListaWszystkichKlientow().get(clientID).getImie() + " " + MainActivity.getListaKlientow().getListaWszystkichKlientow().get(clientID).getNazwisko(), Toast.LENGTH_SHORT).show();
                        MainActivity.listaKlientow.usunKlienta(clientID);
                        getArrayListKlientowImieNazwisko().remove(clientID);
                        updateListaKlientowImieNazwisko();
                        arrayAdapterListaKlientow = new ArrayAdapter(this, android.R.layout.simple_list_item_1, getArrayListKlientowImieNazwisko());
                        listViewListaKlientow.setAdapter(arrayAdapterListaKlientow);
                    }
                    else if (iloscKlientow <= 0) {
                        Toast.makeText(ActivityListaKlientow.this, "Brak klientów - nie można usunąć", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(ActivityListaKlientow.this, "Wybierz klienta, którego chcesz usunąć", Toast.LENGTH_SHORT).show();
                    }
                }
                default: {
                    intentNextScreen = new Intent(this, ActivityListaRachunkow.class);
                }
        }


        }
    }
}