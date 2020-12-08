package com.example.appmensajesing;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class registroContactos extends AppCompatActivity {
    public Spinner Semestre,Carrera,Grupo;
    ArrayList<String> sem = new ArrayList<>();
    ArrayList<String> carr = new ArrayList<>();
    ArrayList<String> gru = new ArrayList<>();
    ArrayAdapter<String> AdaptadorS, adapC, adapG;
    private ArrayList<String> clavesS, clavesC, clavesG;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_contactos);
        Semestre=(Spinner)findViewById(R.id.spRegistroSemestre);
        Carrera=(Spinner)findViewById(R.id.spRegistroCarrera);
        Grupo=(Spinner)findViewById(R.id.spRegistroGrupo);
        llenarSemestre();
        llenarCarrera();
        llenarGrupo();
    }
    public void consulta(View v){
        Intent chk=new Intent(this, Consulta.class);
        startActivity(chk);
    }

    public void llenarSemestre(){
        try {
            AsyncHttpClient cliente = new AsyncHttpClient();
            String url = "http://agendaing.one-2-go.com/servicioWeb/catalogos.php?";
            RequestParams parametros = new RequestParams();
            parametros.put("op","semestre");
            cliente.post(url, parametros, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    mostrarDatosS(new String(responseBody));
                }
                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                }
            });
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Error"+ e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    public void Menu(View v){
        Intent chk=new Intent(this,menuPrincipal.class);
        startActivity(chk);
    }
    public void mostrarDatosS(String rawData) {
        try {
            JSONArray data = new JSONArray(rawData);
            clavesS= new ArrayList<>();
            for (int i=0; i < data.length(); i++) {
                clavesS.add(data.getJSONObject(i).getString("ID"));
                sem.add(data.getJSONObject(i).getString("NOMBRE"));
            }
            AdaptadorS= new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,sem);
            Semestre.setAdapter(AdaptadorS);
            Semestre.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    //Toast.makeText(getApplicationContext(),"datos:"+clavesS.get(position),Toast.LENGTH_SHORT).show();

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });


        }catch(Exception ex){
            ex.printStackTrace();
            Toast.makeText(getApplicationContext(),"Error:"+ex.getMessage().toString(),Toast.LENGTH_SHORT).show();
        }
    }

    public void llenarCarrera(){
        try {
            AsyncHttpClient cliente = new AsyncHttpClient();
            String url = "http://agendaing.one-2-go.com/servicioWeb/catalogos.php?";
            RequestParams parametros = new RequestParams();
            parametros.put("op","carrera");
            cliente.post(url, parametros, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    mostrarDatosC(new String(responseBody));
                }
                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                }
            });
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Error"+ e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    public void mostrarDatosC(String rawData) {
        try {
            JSONArray data = new JSONArray(rawData);
            clavesC= new ArrayList<>();
            for (int i=0; i < data.length(); i++) {
                clavesC.add(data.getJSONObject(i).getString("ID"));
                carr.add(data.getJSONObject(i).getString("NOMBRE"));
            }
            adapC= new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,carr);
            Carrera.setAdapter(adapC);
            Carrera.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    //Toast.makeText(getApplicationContext(),"datos:"+clavesS.get(position),Toast.LENGTH_SHORT).show();

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });


        }catch(Exception ex){
            ex.printStackTrace();
            Toast.makeText(getApplicationContext(),"Error:"+ex.getMessage().toString(),Toast.LENGTH_SHORT).show();
        }
    }

    public void llenarGrupo(){
        try {
            AsyncHttpClient cliente = new AsyncHttpClient();
            String url = "http://agendaing.one-2-go.com/servicioWeb/catalogos.php?";
            RequestParams parametros = new RequestParams();
            parametros.put("op","grupo");
            cliente.post(url, parametros, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    mostrarDatosG(new String(responseBody));
                }
                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                }
            });
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Error"+ e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    public void mostrarDatosG(String rawData) {
        try {
            JSONArray data = new JSONArray(rawData);
            clavesG= new ArrayList<>();
            for (int i=0; i < data.length(); i++) {
                clavesG.add(data.getJSONObject(i).getString("ID"));
                gru.add(data.getJSONObject(i).getString("NOMBRE"));
            }
            adapG= new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,gru);
            Grupo.setAdapter(adapG);
            Grupo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    //Toast.makeText(getApplicationContext(),"datos:"+clavesS.get(position),Toast.LENGTH_SHORT).show();

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });


        }catch(Exception ex){
            ex.printStackTrace();
            Toast.makeText(getApplicationContext(),"Error:"+ex.getMessage().toString(),Toast.LENGTH_SHORT).show();
        }
    }

}