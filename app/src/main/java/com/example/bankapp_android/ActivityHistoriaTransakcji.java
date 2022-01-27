package com.example.bankapp_android;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ActivityHistoriaTransakcji extends AppCompatActivity {

    Intent previousWindowIntent;
    TextView textViewHistoriaTransakcji;
    ListView listViewHistoriaTransakcji;
    ArrayList<String> arrayListHistoriaOperacji;
    Button buttonWplacGotowke, buttonWyplacGotowke, buttonPrzelew;
    EditText editTextKwotaTransakcji;

    ArrayList<String> getArrayListHistoriaOperacji() {
        return this.arrayListHistoriaOperacji;
    }

    public void setArrayListHistoriaOperacji(ArrayList<String> inputList) {
        //this.arrayListHistoriaOperacji.clear();
        this.arrayListHistoriaOperacji = inputList;
    }

    ArrayAdapter arrayAdapterHistoriaOperacji;

    private int clientID;

    public void setClientID(int clientID) {
        this.clientID = clientID;
    }

    public void setRachunekID(int rachunekID) {
        this.rachunekID = rachunekID;
    }

    public int getClientID() {
        return clientID;
    }

    public int getRachunekID() {
        return rachunekID;
    }

    private int rachunekID;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historia_transakcji);

        initWidgets();

        previousWindowIntent = getIntent();

        setClientID(previousWindowIntent.getIntExtra("clientID", -1));
        setRachunekID(previousWindowIntent.getIntExtra("rachunekID", -1));
        String windowNameText = ("Historia transakcji " + MainActivity.listaKlientow.getListaWszystkichKlientow().get(clientID).getImie() + " " + MainActivity.listaKlientow.getListaWszystkichKlientow().get(clientID).getNazwisko() + " z rachunku " + MainActivity.listaKlientow.getListaWszystkichKlientow().get(clientID).getListaRachunkow().get(rachunekID).getNazwaRachunku());

        textViewHistoriaTransakcji.setText(windowNameText);

        updateHistoriaTranskacji();


        listViewHistoriaTransakcji = (ListView) findViewById(R.id.listViewHistoriaTranskacji);

        arrayAdapterHistoriaOperacji = new ArrayAdapter(this, android.R.layout.simple_list_item_1, getArrayListHistoriaOperacji());

        listViewHistoriaTransakcji.setAdapter(arrayAdapterHistoriaOperacji);

    }
    public void initWidgets() {
        textViewHistoriaTransakcji = findViewById(R.id.textViewHistoriaTransakcji);
        buttonWplacGotowke = findViewById(R.id.buttonWplacGotowke);
        buttonWyplacGotowke= findViewById(R.id.buttonWyplacGotowke);
        buttonPrzelew = findViewById(R.id.buttonPrzelew);
        editTextKwotaTransakcji = findViewById(R.id.editTextKwotaTrasakcji);
    }

    public void updateHistoriaTranskacji() {
        ArrayList<String> tempList= new ArrayList<>();

        int wielkoscHistorii = MainActivity.listaKlientow.getListaWszystkichKlientow().get(clientID).getListaRachunkow().get(rachunekID).getHistoriaTransakcji().size();

        if (wielkoscHistorii == 0) {
            tempList.add(" Brak historii transkacji tego rachunku");
        }
        else {
            for (int i = 0; i < wielkoscHistorii ; i++) {
                tempList.add(i + 1 + ". " + MainActivity.listaKlientow.getListaWszystkichKlientow().get(clientID).getListaRachunkow().get(rachunekID).getHistoriaTransakcji().get(i));
            }
        }


        setArrayListHistoriaOperacji(tempList);
    }

    public void wplacGotowke(View view) {
        double kwotaTransakcji = pobierzKwoteTransakcji();

        if (kwotaTransakcji > 0) {
            String rezultatPrzelewu = MainActivity.listaKlientow.getListaWszystkichKlientow().get(clientID).getListaRachunkow().get(rachunekID).zwiekszSaldo(kwotaTransakcji).getWiadomosc();
            MainActivity.listaKlientow.getListaWszystkichKlientow().get(clientID).getListaRachunkow().get(rachunekID).dodajWpisHistoriiTransakcji(rezultatPrzelewu);
            updateHistoriaTranskacji();
            arrayAdapterHistoriaOperacji = new ArrayAdapter(this, android.R.layout.simple_list_item_1, getArrayListHistoriaOperacji());
            listViewHistoriaTransakcji.setAdapter(arrayAdapterHistoriaOperacji);
            Toast.makeText(ActivityHistoriaTransakcji.this, "Pomyslnie wpłacono " + kwotaTransakcji + " złotych na rachunek", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(ActivityHistoriaTransakcji.this, "Podano niepoprawną kwotę", Toast.LENGTH_SHORT).show();
        }
    }

    public void wyplacGotowke(View view) {
        double kwotaTransakcji = pobierzKwoteTransakcji();

        if (kwotaTransakcji > 0) {
            String rezultatPrzelewu = MainActivity.listaKlientow.getListaWszystkichKlientow().get(clientID).getListaRachunkow().get(rachunekID).zmniejszSaldo(kwotaTransakcji).getWiadomosc();
            MainActivity.listaKlientow.getListaWszystkichKlientow().get(clientID).getListaRachunkow().get(rachunekID).dodajWpisHistoriiTransakcji(rezultatPrzelewu);
            updateHistoriaTranskacji();
            arrayAdapterHistoriaOperacji = new ArrayAdapter(this, android.R.layout.simple_list_item_1, getArrayListHistoriaOperacji());
            listViewHistoriaTransakcji.setAdapter(arrayAdapterHistoriaOperacji);
            Toast.makeText(ActivityHistoriaTransakcji.this, "Pomyslnie wypłacono " + kwotaTransakcji + " złotcyh z rachunku", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(ActivityHistoriaTransakcji.this, "Podano niepoprawną kwotę", Toast.LENGTH_SHORT).show();
        }
    }

    public void wykonajPrzelew(View view) {
        double kwotaTransakcji = pobierzKwoteTransakcji();

        if (kwotaTransakcji > 0 ) {
            if (kwotaTransakcji <= MainActivity.getListaKlientow().getListaWszystkichKlientow().get(clientID).getListaRachunkow().get(rachunekID).getSaldo() + MainActivity.getListaKlientow().getListaWszystkichKlientow().get(clientID).getListaRachunkow().get(rachunekID).getKosztPrzelewu(kwotaTransakcji)) {
                Intent intentPrzelew = new Intent(this, ActivityListaKlientow.class);

                intentPrzelew.putExtra("kwotaPrzelewu", kwotaTransakcji);
                intentPrzelew.putExtra("zlecajacyID", getClientID());
                intentPrzelew.putExtra("rachunekZlecajacegoID", getRachunekID());

                startActivity(intentPrzelew);
            }
            else {
                Toast.makeText(ActivityHistoriaTransakcji.this, "Brak wystarczającej ilości środków na koncie", Toast.LENGTH_SHORT).show();
            }

        }
        else {
            Toast.makeText(ActivityHistoriaTransakcji.this, "Kwota przelewu nie może wynosić 0.00 zł lub mniej", Toast.LENGTH_SHORT).show();
        }
    }



    public double pobierzKwoteTransakcji() {
        double tempDouble = 0.00;

        String tempString = editTextKwotaTransakcji.getText().toString();

        if (!(tempString.equals(""))) {
            tempDouble = Double.parseDouble(editTextKwotaTransakcji.getText().toString());
        }
        return tempDouble;
    }

}
