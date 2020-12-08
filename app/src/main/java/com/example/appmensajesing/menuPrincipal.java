package com.example.appmensajesing;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class menuPrincipal extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);
    }
    public void Registro(View v){
        Intent reg=new Intent(this,registroContactos.class);
        startActivity(reg);
    }
    public void Consulta(View v){
        Intent con=new Intent(this,Consulta.class);
        startActivity(con);
    }
    public void Mensaje(View v){
        Intent men=new Intent(this,mensajePrincipal.class);
        startActivity(men);
    }
}