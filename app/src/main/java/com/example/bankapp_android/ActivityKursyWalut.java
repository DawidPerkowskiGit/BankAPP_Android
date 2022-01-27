package com.example.bankapp_android;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.DecimalFormat;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

public class ActivityKursyWalut extends AppCompatActivity {
    private TextView textViewKursyWalut, textViewKursyWalutNazwa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kursy_walut);

        textViewKursyWalut = findViewById(R.id.textViewKursyWalut);
        textViewKursyWalutNazwa = findViewById(R.id.textViewKutsyWalutNazwa);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://data.fixer.io/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        CurrencyExchangeService currencyInterface = retrofit.create(CurrencyExchangeService.class);
        Call<fixerIoResponseData> call = currencyInterface.getCurrency();
        call.enqueue(new Callback<fixerIoResponseData>() {
            @Override
            public void onResponse(Call<fixerIoResponseData> call, Response<fixerIoResponseData> response) {
                fixerIoResponseData responseBody = response.body();
                responseBody.getRates().oneForeignCurrToPln();

                textViewKursyWalutNazwa.setText(textViewKursyWalutNazwa.getText() + " z dnia " + responseBody.getDate());
                textViewKursyWalut.setText(responseBody.getRates().toString());
            }

            @Override
            public void onFailure(Call<fixerIoResponseData> call, Throwable t) {
                textViewKursyWalut.setText(t.getMessage());
            }
        });
    }
}

interface CurrencyExchangeService {
    @GET("") // tu mój klucz do dostępu do API pobierania kursów walut
    Call<fixerIoResponseData> getCurrency();
}

class fixerIoResponseData {
    private String success;

    private String date;

    private Rates rates;

    private String base;

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getDate ()
    {
        return date;
    }

    public void setDate (String date)
    {
        this.date = date;
    }

    public Rates getRates ()
    {
        return rates;
    }

    public void setRates (Rates rates)
    {
        this.rates = rates;
    }

    public String getBase ()
    {
        return base;
    }

    public void setBase (String base)
    {
        this.base = base;
    }

    public class Rates
    {

        private String PLN, USD, EUR, CHF, CAD, GBP;
        private Double dPLN, dUSD, dEUR, dCHF, dCAD, dGBP;

        public void setPLN(String PLN) {
            this.PLN = PLN;
        }

        public void setUSD(String USD) {
            this.USD = USD;
        }

        public void setEUR(String EUR) {
            this.EUR = EUR;
        }

        public void setCHF(String CHF) {
            this.CHF = CHF;
        }

        public void setCAD(String CAD) {
            this.CAD = CAD;
        }

        public void setGBP(String GBP) {
            this.GBP = GBP;
        }

        public void setdPLN(Double dPLN) {
            this.dPLN = dPLN;
        }

        public void setdUSD(Double dUSD) {
            this.dUSD = dUSD;
        }

        public void setdEUR(Double dEUR) {
            this.dEUR = dEUR;
        }

        public void setdCHF(Double dCHF) {
            this.dCHF = dCHF;
        }

        public void setdCAD(Double dCAD) {
            this.dCAD = dCAD;
        }

        public void setdGBP(Double dGBP) {
            this.dGBP = dGBP;
        }

        public String getPLN() {
            return PLN;
        }

        public String getUSD() {
            return USD;
        }

        public String getEUR() {
            return EUR;
        }

        public String getCHF() {
            return CHF;
        }

        public String getCAD() {
            return CAD;
        }

        public String getGBP() {
            return GBP;
        }

        public Double getdPLN() {
            return dPLN;
        }

        public Double getdUSD() {
            return dUSD;
        }

        public Double getdEUR() {
            return dEUR;
        }

        public Double getdCHF() {
            return dCHF;
        }

        public Double getdCAD() {
            return dCAD;
        }

        public Double getdGBP() {
            return dGBP;
        }

        public void getStrings() {
            //setPLN();
        }


        public void convertStringToDouble() {
            setdPLN(Double.parseDouble(getPLN()));
            setdUSD(Double.parseDouble(getUSD()));
            setdEUR(Double.parseDouble(getEUR()));
            setdCHF(Double.parseDouble(getCHF()));
            setdCAD(Double.parseDouble(getCAD()));
            setdGBP(Double.parseDouble(getGBP()));

        }

        public void fromEurBaseToPlnBase() {
            convertStringToDouble();
            double tempRatio = getdEUR()/getdPLN();
            setdPLN(tempRatio * getdPLN());
            setdUSD(tempRatio * getdUSD());
            setdEUR(tempRatio * getdEUR());
            setdCHF(tempRatio * getdCHF());
            setdCAD(tempRatio * getdCAD());
            setdGBP(tempRatio * getdGBP());
        }

        public void oneForeignCurrToPln() {
            convertStringToDouble();

            setdUSD(getdPLN()/getdUSD());
            setdCHF(getdPLN()/getdCHF());
            setdCAD(getdPLN()/getdCAD());
            setdGBP(getdPLN()/getdGBP());


        }

        @Override
        public String toString()
        {
            DecimalFormat df = new DecimalFormat("#.####");
            return "1 EUR = " + df.format(getdPLN()) + " PLN\n1 USD = " + df.format(getdUSD()) + " PLN\n1 CHF = " + df.format(getdCHF()) + " PLN\n1 CAD = " + df.format(getdCAD()) + " PLN\n1 GBP = " + df.format(getdGBP()) + " PLN";
        }
    }
}


