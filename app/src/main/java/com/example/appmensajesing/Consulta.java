package com.example.appmensajesing;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Consulta extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consulta);
    }
    public void Menu(View v){
        Intent chk=new Intent(this,menuPrincipal.class);
        startActivity(chk);
    }
}