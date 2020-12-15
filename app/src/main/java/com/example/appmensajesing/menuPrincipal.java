package com.example.appmensajesing;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class menuPrincipal extends AppCompatActivity {
    String id_usuario="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);
        Intent intent = getIntent();
        Bundle llenado = this.getIntent().getExtras();
        //LLenado de clave usuario
        try {
            if (llenado!=null){
               id_usuario = llenado.getString("id_usuario");
            }
        }catch(Exception ex){
            Toast.makeText(this.getApplicationContext(),"Error"+ex.getMessage(),Toast.LENGTH_SHORT).show();
        }
     //   Toast.makeText(this.getApplicationContext(),"ID "+id_usuario,Toast.LENGTH_SHORT).show();
    }
    public void Registro(View v){
        Intent reg=new Intent(this,registroContactos.class);
        Bundle datos= new Bundle();
        datos.putString("id_usuario",id_usuario);
        reg.putExtras(datos);
        startActivity(reg);
    }
    public void Consulta(View v){
        Intent con=new Intent(this,Consulta.class);
        Bundle datos= new Bundle();
        datos.putString("id_usuario",id_usuario);
        con.putExtras(datos);
        startActivity(con);
    }
    public void Acceso(View v){
        Intent acc=new Intent(this,MainActivity.class);
        startActivity(acc);
    }
    public void Mensaje(View v){
        Intent men=new Intent(this,mensajePrincipal.class);
        Bundle datos= new Bundle();
        datos.putString("id_usuario",id_usuario);
        men.putExtras(datos);
        startActivity(men);
    }
}