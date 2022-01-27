package com.example.bankapp_android;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;

public class ActivityListaWyborow extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_wyborow);
    }

    public void generujListeKlientow(View view) {
        Intent intent = new Intent(this, ActivityListaKlientow.class);
        startActivity(intent);
    }
}
