package com.example.bankapp_android;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.Serializable;
import java.nio.charset.Charset;

/**
 *
 */
class ZwroconeWartosci implements Serializable{
    /**
     * Kod opowiadający wynikowi operacji
     */
    private int kod;

    /**
     * Opis zwróconego kodu
     */
    private String wiadomosc;

    ZwroconeWartosci(int kod, String wiadomosc) {
        this.kod = kod;
        this.wiadomosc = wiadomosc;
    }

    public int getKod() {
        return kod;
    }

    public String getWiadomosc() {
        return wiadomosc;
    }

    public void setKod(int kod) {
        this.kod = kod;
    }

    public void setWiadomosc(String wiadomosc) {
        this.wiadomosc = wiadomosc;
    }
}

/**
 * Rachunek
 */
abstract class Rachunek implements Serializable {

    private String nazwaRachunku;
    private double saldo;

    /**
     * Lista przechowująca historię transakcji tego rachunku.
     */
    protected ArrayList<String> historiaTransakcji = new ArrayList<>();


    Rachunek() {}

    /**
     * Pobranie wartości salda
     */
    public double getSaldo() {
        return this.saldo;
    }

    /**
     * Pobranie nazwy konta
     */
    public String getNazwaRachunku() {
        return this.nazwaRachunku;
    }

    public ArrayList<String> getHistoriaTransakcji(){
        return this.historiaTransakcji;
    }

    public void setNazwaRachunku(String nazwaRachunku) {
        this.nazwaRachunku = nazwaRachunku;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    public void dodajWpisHistoriiTransakcji(String wpis) {
        this.historiaTransakcji.add(wpis);
    }


    public ZwroconeWartosci czyMoznaZmniejszycSrodki(double kwota) {
        if (getSaldo() > kwota) {
            return new ZwroconeWartosci(1, "Wystarczająca ilość środków do przeprowadzenia operacji");
        }
        else {
            return new ZwroconeWartosci(0, "Niewystarczająca ilość środków do wykonania operacji");
        }
    }

    public ZwroconeWartosci czyMoznaDodacSrodki(double kwota) {
        if (kwota > 0) {
            return new ZwroconeWartosci(1, "Można zasilić rachunek kwotą: "+ kwota +" złotych");
        }
        else {
            return new ZwroconeWartosci(0, "Nie można zasilić rachunku kwotą :"+ kwota +" złotych");
        }
    }



    /**
     * Pobranie wartości prowizji przelewu
     */
    public double getKosztPrzelewu(double kwota) {
        return 0.00;
    }

    public double getKosztWyplaty() {
        return 0.00;
    }


    public ZwroconeWartosci uaktulanijStanRachunku (double kwota) {
        if (getSaldo() + kwota >= 0) {
            setSaldo(getSaldo() + kwota);
            return new ZwroconeWartosci(1, "Pomyślnie zaktualizowano saldo");
        }
        else {
            return new ZwroconeWartosci(0, "Nie można zaktualizować salda");
        }
    }

    /**
     * Wpłacenie gotówki na rachunek i wpisanie tej operacji do listy przechowującej historie transakcji.
     */
    public ZwroconeWartosci zwiekszSaldo(double iloscGotowki) {

        ZwroconeWartosci czyTransakcjaSiePowiedzie = czyMoznaDodacSrodki(iloscGotowki);

        if (czyTransakcjaSiePowiedzie.getKod() == 1) {
            uaktulanijStanRachunku(iloscGotowki);
            czyTransakcjaSiePowiedzie.setWiadomosc("Wpłata " + iloscGotowki + " | " + this.wypiszSaldo());

        }
        else {
            czyTransakcjaSiePowiedzie.setWiadomosc("Nie można wykonać operacji, podana kwota wpłaty gotówki wynosi " + iloscGotowki + " złotych");
        }

        return czyTransakcjaSiePowiedzie;
    }


    /**
     * Domyślanie wyplata pieniędzy z rachunku.
     */
    public ZwroconeWartosci zmniejszSaldo(double iloscGotowki) {

        double lacznaKwotaTransakcji = iloscGotowki + getKosztWyplaty();

        ZwroconeWartosci czyTransakcjaSiePowiedzie = czyMoznaZmniejszycSrodki(lacznaKwotaTransakcji);

        if (czyTransakcjaSiePowiedzie.getKod() == 1) {
            setSaldo(getSaldo()-iloscGotowki);
            czyTransakcjaSiePowiedzie.setWiadomosc("Wyplata " + iloscGotowki + " | " + this.wypiszSaldo());

        }
        else {
            czyTransakcjaSiePowiedzie.setWiadomosc("Nie można wypłacić gotówki, niewystarczająca ilość środków lub podana nieprawidłową wartość");
        }
        return czyTransakcjaSiePowiedzie;
    }



    public ZwroconeWartosci przelewPrzychodzacy(double kwota) {
        ZwroconeWartosci czyPrzelewSiePowiedzie = czyMoznaDodacSrodki(kwota);

        if (czyPrzelewSiePowiedzie.getKod() == 1) {
            czyPrzelewSiePowiedzie.setWiadomosc("przesłał Ci "+ kwota +" złotych.");
        }
        else {
            czyPrzelewSiePowiedzie.setWiadomosc("Nie można otrzymać przelewu");
        }
        return czyPrzelewSiePowiedzie;
    }

    public ZwroconeWartosci przelewWychodzacy(double kwota) {
        double calkowityKosztPrzelewu = kwota + getKosztPrzelewu(kwota);
        ZwroconeWartosci czyPrzelewSiePowiedzie = czyMoznaZmniejszycSrodki(calkowityKosztPrzelewu);

        if (czyPrzelewSiePowiedzie.getKod() == 1) {
            czyPrzelewSiePowiedzie.setWiadomosc("Przelano "+ kwota +" złotych.");
        }
        else {
            czyPrzelewSiePowiedzie.setWiadomosc("Nie można zlecić przelewu na "+ kwota +" złotych. Brak wystarczającej ilości środków na koncie");
        }
        return  czyPrzelewSiePowiedzie;
    }



    /**
     * Wyświetlenie stanu salda
     */
    public String wypiszSaldo() {
        return "Saldo : " + this.saldo;
    }
}


/**
 * Rachunek Zwykły
 */

class RachunekZwykly extends Rachunek implements Serializable {

    /**
     *Domyslnie kazde nowy zwykly rachunek bedzie mial nazwe "Zwykly rachunek"
     */
    RachunekZwykly() {
        this.setNazwaRachunku("Rachunek zwykły");
    }

    /**
     * Tworzenie nowego rachunku ale z podaniem wlasnej nazwy
     */
    RachunekZwykly(String nazwa) {
        this.setNazwaRachunku(nazwa);
    }

    @Override
    public double getKosztWyplaty() {
        return 1.50;
    }

    @Override
    public String toString() {
        return getNazwaRachunku() + " | " + this.wypiszSaldo();
    }
}

/**
 * Rachunek oszczędnościowy
 */


class RachunekOszczednosciowy extends Rachunek implements Serializable {

    /**
     * Koszt wykonania przelewu dla oszczędnościowego typu rachunku.
     */
    private final double kosztPrzelewu = 1.50;

    /**
     * Domyślnie stworzony rachunek oszczędnościowy ma nazwa "Rachunek oszczędnościowy"
     */
    RachunekOszczednosciowy() {
        setNazwaRachunku("Rachunek oszczędnościowy");
    }

    /**
     * Stworzenie rachunku o wskazanej nazwie
     */
    RachunekOszczednosciowy(String nazwa) {
        setNazwaRachunku(nazwa);
    }

    /**
     * Prowizja od wyplaty gotowki jest zalezna od wyplacanej kwoty
     */
    @Override
    public double getKosztPrzelewu(double wartoscTransakcji) {
        if (wartoscTransakcji < 20 && wartoscTransakcji > 0)
            return 1.50;
        else if (wartoscTransakcji < 100)
            return 5.00;
        else if (wartoscTransakcji < 1000)
            return 20.00;
        else if (wartoscTransakcji > 1000) {
            return wartoscTransakcji * 0.20;
        } else
            return -1;
    }

    @Override
    public double getKosztWyplaty() {
        return 5.00;
    }
}


/**
 * Klient
 */

class Klient implements Serializable {
    private String Imie, Nazwisko;

    private ArrayList<Rachunek> listaRachunkow = new ArrayList<>();

    Klient() {}

    /**
     * Stworzenie klienta o podanym imieniu i nazwisku, domyslnie kazdy klient otrzymuje zwykly rachunek
     */
    Klient(String Imie, String Nazwisko) {
        this.Imie = Imie;
        this.Nazwisko = Nazwisko;
        this.listaRachunkow.add(new RachunekZwykly("Zwykły rachunek #1"));
    }

    /**
     * Pobranie Imienia
     */
    public String getImie() {
        return Imie;
    }

    /**
     * Pobranie nazwiska
     */
    public String getNazwisko() {
        return Nazwisko;
    }

    public ArrayList<Rachunek> getListaRachunkow() {
        return this.listaRachunkow;
    }

    /**
     * Stworzenia zwyklego rachunku z domyslna nazwa oraz dodanie go do listy rachunkow tego klienta
     */
    public void dodajRachunekZwykly() {
        listaRachunkow.add(new RachunekZwykly());
    }

    /**
     * Stworzenia zwylego rachunku z wskazana nazwa oraz dodanie go do listy rachunkow tego klienta
     */
    public void dodajRachunekZwykly(String nazwaRachunku) {
        listaRachunkow.add(new RachunekZwykly(nazwaRachunku));
    }

    /**
     * Stworzenia rachunku oszczednosciowego z domyslna nazwa oraz dodanie go do listy rachunkow tego klienta
     */
    public void dodajRachunekOszczednosciowy() {
        listaRachunkow.add(new RachunekOszczednosciowy());
    }

    /**
     * Stworzenia rachunku oszczednosciowego z wskazana nazwa oraz dodanie go do listy rachunkow tego klienta
     */
    public void dodajRachunekOszczednosciowy(String nazwa) {
        listaRachunkow.add(new RachunekOszczednosciowy(nazwa));
    }

    /**
     * Usuniecie wskazanego rachunku, parametrem jest indeks na liscie
     */
    public void usunRachunek(int ktoryNaLiscie) {
        getListaRachunkow().remove(ktoryNaLiscie);
    }

    public ArrayList<ZwroconeWartosci> ustalPoprawnoscPrzelewu(double kwotaPrzelewu, int idRachunkuNadawcy, Klient klientOdbiorca, int idRachunkuOdbiorcy) {
        double kwotaCalkowita = kwotaPrzelewu + getListaRachunkow().get(idRachunkuNadawcy).getKosztPrzelewu(kwotaPrzelewu);

        ZwroconeWartosci czyPrzelewZlecajacegoSiePowiedzie = getListaRachunkow().get(idRachunkuNadawcy).przelewWychodzacy(kwotaCalkowita );
        ZwroconeWartosci czyPrzelewOdbiorcySiePowiedzie = klientOdbiorca.getListaRachunkow().get(idRachunkuOdbiorcy).przelewPrzychodzacy(kwotaPrzelewu);
        if (czyPrzelewZlecajacegoSiePowiedzie.getKod() == 1 && czyPrzelewOdbiorcySiePowiedzie.getKod() == 1) {
            czyPrzelewZlecajacegoSiePowiedzie.setWiadomosc( czyPrzelewZlecajacegoSiePowiedzie.getWiadomosc() + " Przelew otrzymał "+klientOdbiorca.getImie() +" "+getNazwisko() + ". ");
            czyPrzelewOdbiorcySiePowiedzie.setWiadomosc(getImie()+ " "+getNazwisko()+ " "+ czyPrzelewOdbiorcySiePowiedzie.getWiadomosc());
        }
        else {
            czyPrzelewOdbiorcySiePowiedzie.setKod(0);
            czyPrzelewZlecajacegoSiePowiedzie.setKod(0);
        }
        ArrayList<ZwroconeWartosci> zwroconaLista = new ArrayList<>();
        zwroconaLista.add(czyPrzelewZlecajacegoSiePowiedzie);
        zwroconaLista.add(czyPrzelewOdbiorcySiePowiedzie);
        return zwroconaLista;
    }
}



class ListaKlientow implements Serializable{

    private ArrayList<Klient> listaWszystkichKlientow = new ArrayList<>();

    public ArrayList<Klient> getListaWszystkichKlientow() {
        return this.listaWszystkichKlientow;
    }

    public void setListaWszystkichKlientow(ArrayList<Klient> lista) {
        this.listaWszystkichKlientow = lista;
    }

    public Klient pobierzKlienta(int idKlienta) {
        return getListaWszystkichKlientow().get(idKlienta);
    }

    ListaKlientow() {}

    public String dodajKlientaDoListy(Klient klient) {
        getListaWszystkichKlientow().add(klient);
        return "Pomyślnie dodano klienta " + klient.toString();
    }

    // Usuniecie klienta z listy
    public String usunKlienta(Klient klient) {
        getListaWszystkichKlientow().remove(klient);
        return "Pomyślnie usunięto klienta " + klient.toString();
    }

    public String usunKlienta(int id) {
        Klient tempKlient = getListaWszystkichKlientow().get(id);
        getListaWszystkichKlientow().remove(id);
        return "Pomyślnie usunięto klienta " + tempKlient.toString();
    }

    public String wykonajPrzelew(int idNadawcy, int idRachunkuNadawcy, int idOdiorcy, int idRachunkuOdbiorcy, double kwotaPrzelewu) {
        ArrayList<ZwroconeWartosci> listaWartosci= new ArrayList<ZwroconeWartosci>();

        listaWartosci = getListaWszystkichKlientow().get(idNadawcy).ustalPoprawnoscPrzelewu(kwotaPrzelewu, idRachunkuNadawcy, getListaWszystkichKlientow().get(idOdiorcy), idRachunkuOdbiorcy);

        if (listaWartosci.get(0).getKod() == 1 && listaWartosci.get(1).getKod() == 1) {
            Klient odbiorca = pobierzKlienta(idOdiorcy);
            odbiorca.getListaRachunkow().get(idRachunkuOdbiorcy).zwiekszSaldo(kwotaPrzelewu);
            odbiorca.getListaRachunkow().get(idRachunkuOdbiorcy).dodajWpisHistoriiTransakcji(listaWartosci.get(1).getWiadomosc() + "| Saldo : " + odbiorca.getListaRachunkow().get(idRachunkuOdbiorcy).getSaldo());

            Klient nadawca = pobierzKlienta(idNadawcy);
            nadawca.getListaRachunkow().get(idRachunkuNadawcy).zmniejszSaldo(kwotaPrzelewu + nadawca.getListaRachunkow().get(idRachunkuNadawcy).getKosztPrzelewu(kwotaPrzelewu));
            nadawca.getListaRachunkow().get(idRachunkuNadawcy).dodajWpisHistoriiTransakcji(listaWartosci.get(0).getWiadomosc() + "| Saldo : " + nadawca.getListaRachunkow().get(idRachunkuNadawcy).getSaldo());
            return "Pomyślnie wykonano przelew";
        }
        else {
            return "Nie można było wykonać przelewu";
        }
    }
    /**
     * Dodanie klienta o wskazanym imieniu i nazwisku
     */
    public void dodajKlienta(String Imie, String Nazwisko) {
        Klient tempKlient = new Klient(Imie, Nazwisko);
        dodajKlientaDoListy(tempKlient);
    }

    /**
     * Metoda ta pobiera od uzytkownika kwote pieniedzy
     */
    public double podajKwote() {
        Scanner tempScanner = new Scanner(System.in);
        double tempKwota;
        do {
            tempKwota = tempScanner.nextDouble();
            if (tempKwota < 0)
                System.out.println("Wprowadz kwote wieksza od 0");
        } while (tempKwota < 0);
        return tempKwota;
    }

    /**
     * Metoda pobiera z listy indeks klienta, na ktorym uzytkownik chce pracowac
     */
    public int podajIndeksKlienta() {
        Scanner tempScanner = new Scanner(System.in);
        boolean error = false;
        int tempNumerKlienta;
        do {
            tempNumerKlienta = tempScanner.nextInt();
            if (tempNumerKlienta < 0 || getListaWszystkichKlientow().size() < tempNumerKlienta) {
                error = true;
                System.out.println("\nKlient nie istnieje, podaj wartosc ponownie\n");
                drukujKlientow();
            } else
                error = false;
        } while (error == true);
        return tempNumerKlienta - 1;
    }

    /**
     * Metoda wypisuje liste klientow, wraz z ich indeksami
     */
    public void drukujKlientow() {
        System.out.println("Lista wszystkich klientow : ");
        for (int i = 0; i < getListaWszystkichKlientow().size(); i++)
            System.out.println(i + 1 + ". " + getListaWszystkichKlientow().get(i).getImie() + " " + getListaWszystkichKlientow().get(i).getNazwisko());
    }

    /**
     * Metoda zapisuje do pliku klientow ich rachunki oraz historie operacji
     */
    public void zapiszDaneDoPliku() throws IOException {
        FileOutputStream f = new FileOutputStream("myObjects.txt");
        ObjectOutputStream o = new ObjectOutputStream(f);

        o.writeObject(this.getListaWszystkichKlientow());
        o.close();
        f.close();
    }

    /**
     * metoda wczytuje klientow, rachunki oraz historie operacji
     */
    public ArrayList<Klient> wczytajDaneZPliku() throws IOException, ClassNotFoundException {
        FileInputStream fi = new FileInputStream("myObjects.txt");
        ObjectInputStream oi = new ObjectInputStream(fi);

        ArrayList<Klient> lista = (ArrayList<Klient>) (oi.readObject());
        oi.close();

        return lista;

    }
}
public class BankAPP {

}
