package com.example.bankapp_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public boolean flagDataPopulated = false;

    public boolean isFlagDataPopulated() {
        return flagDataPopulated;
    }

    public void setFlagDataPopulated(boolean flagDataPopulated) {
        this.flagDataPopulated = flagDataPopulated;
    }

    public static ListaKlientow listaKlientow = new ListaKlientow();

    public static ListaKlientow getListaKlientow() {
        return MainActivity.listaKlientow;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            if (extras.containsKey("flagDataPopulated")) {
                setFlagDataPopulated(extras.getBoolean("flagDataPopulated"));
            }
        }

        if (isFlagDataPopulated() == false) {
            populateData(getListaKlientow());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.app_menu, menu);
        return true;
    }

    public void exitWelcomeScreen(View view) {
        Intent intent = new Intent(this, ActivityListaKlientow.class);
        startActivity(intent);
    }

    public void przejdzDoLokalizacji(View view) {
        Intent nextScreenIntent = new Intent(this, ActivityLokalizacja.class);
        startActivity(nextScreenIntent);
    }

    public void przejdzDoKursowWalut(View view) {
        Intent nextScreenintent = new Intent(this, ActivityKursyWalut.class);
        startActivity(nextScreenintent);
    }

    public void przejdzDoOdtwarzaczaWideo(View view) {
        Intent nextScreenIntent = new Intent(this, ActivityVideoPlayer.class);
        startActivity(nextScreenIntent);
    }



    public void populateData(ListaKlientow listaKlientow) {

        Klient klient1 = new Klient("Jan", "Kowalski");
        listaKlientow.dodajKlientaDoListy(klient1);

        klient1.getListaRachunkow().get(0).dodajWpisHistoriiTransakcji(klient1.getListaRachunkow().get(0).zwiekszSaldo(9000).getWiadomosc()) ;
        klient1.getListaRachunkow().get(0).dodajWpisHistoriiTransakcji(klient1.getListaRachunkow().get(0).zmniejszSaldo(8000).getWiadomosc());

        klient1.dodajRachunekOszczednosciowy();
        klient1.getListaRachunkow().get(1).dodajWpisHistoriiTransakcji(klient1.getListaRachunkow().get(1).zwiekszSaldo(10000).getWiadomosc());
        klient1.getListaRachunkow().get(1).dodajWpisHistoriiTransakcji(klient1.getListaRachunkow().get(1).zmniejszSaldo(5000).getWiadomosc());
        klient1.getListaRachunkow().get(1).dodajWpisHistoriiTransakcji(klient1.getListaRachunkow().get(1).zmniejszSaldo(3000).getWiadomosc());

        Klient klient2 = new Klient("Marek", "Nowak");
        listaKlientow.dodajKlientaDoListy(klient2);
        klient2.dodajRachunekOszczednosciowy("Pierwszy rachunek oszczędnościowy");
        klient2.dodajRachunekZwykly("Zwykły rachunek na różne wydatki");
        klient2.getListaRachunkow().get(0).dodajWpisHistoriiTransakcji(klient2.getListaRachunkow().get(0).zwiekszSaldo(100000).getWiadomosc());
        klient2.getListaRachunkow().get(0).dodajWpisHistoriiTransakcji(klient2.getListaRachunkow().get(0).zmniejszSaldo(1000).getWiadomosc());

        klient2.getListaRachunkow().get(1).dodajWpisHistoriiTransakcji(klient2.getListaRachunkow().get(1).zwiekszSaldo(70000).getWiadomosc());
        klient2.getListaRachunkow().get(1).dodajWpisHistoriiTransakcji(klient2.getListaRachunkow().get(1).zmniejszSaldo(700).getWiadomosc());

        listaKlientow.dodajKlientaDoListy(new Klient("zzzzz", "xxxx"));
        listaKlientow.dodajKlientaDoListy(new Klient("AAAa", "BBBB"));
        listaKlientow.dodajKlientaDoListy(new Klient("CCCC", "dddd"));
        listaKlientow.dodajKlientaDoListy(new Klient("EEe", "ffff"));
        listaKlientow.dodajKlientaDoListy(new Klient("gggg", "hhhh"));
        listaKlientow.dodajKlientaDoListy(new Klient("iiiii", "jjjj"));
        listaKlientow.dodajKlientaDoListy(new Klient("kkkk", "llll"));
        listaKlientow.dodajKlientaDoListy(new Klient("mmmm", "nnnn"));
        listaKlientow.dodajKlientaDoListy(new Klient("oooo", "pppp"));
        listaKlientow.dodajKlientaDoListy(new Klient("qqqqq", "rrrr"));



        listaKlientow.wykonajPrzelew(0, 1, 1, 0, 500);



        /**
         * Koniec ladowania danych
         */
    }

    public void populateData2(ListaKlientow listaKlientow) {


        Klient klient1 = new Klient("Jan", "Kowalski");
        listaKlientow.dodajKlientaDoListy(klient1);

        klient1.getListaRachunkow().get(0).dodajWpisHistoriiTransakcji(klient1.getListaRachunkow().get(0).zwiekszSaldo(9000).getWiadomosc()) ;
        klient1.getListaRachunkow().get(0).dodajWpisHistoriiTransakcji(klient1.getListaRachunkow().get(0).zmniejszSaldo(8000).getWiadomosc());

        klient1.dodajRachunekOszczednosciowy();
        klient1.getListaRachunkow().get(1).dodajWpisHistoriiTransakcji(klient1.getListaRachunkow().get(1).zwiekszSaldo(10000).getWiadomosc());
        klient1.getListaRachunkow().get(1).dodajWpisHistoriiTransakcji(klient1.getListaRachunkow().get(1).zmniejszSaldo(5000).getWiadomosc());
        klient1.getListaRachunkow().get(1).dodajWpisHistoriiTransakcji(klient1.getListaRachunkow().get(1).zmniejszSaldo(3000).getWiadomosc());

        Klient klient2 = new Klient("Marek", "Nowak");
        listaKlientow.dodajKlientaDoListy(klient2);
        klient2.dodajRachunekOszczednosciowy("Spory Rachunek oszczędnościowy");
        klient2.dodajRachunekZwykly("Zwykły rachunek na niezwykłe wydatki");
        klient2.getListaRachunkow().get(0).dodajWpisHistoriiTransakcji(klient2.getListaRachunkow().get(0).zwiekszSaldo(100000).getWiadomosc());
        klient2.getListaRachunkow().get(0).dodajWpisHistoriiTransakcji(klient2.getListaRachunkow().get(0).zmniejszSaldo(1000).getWiadomosc());

        klient2.getListaRachunkow().get(1).dodajWpisHistoriiTransakcji(klient2.getListaRachunkow().get(1).zwiekszSaldo(70000).getWiadomosc());
        klient2.getListaRachunkow().get(1).dodajWpisHistoriiTransakcji(klient2.getListaRachunkow().get(1).zmniejszSaldo(700).getWiadomosc());




    }
}