package com.example.appmensajesing;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class mensajePrincipal extends AppCompatActivity {
    ListView lsDatos;
    ArrayAdapter adaptador;
    ArrayList<String> listaBusqueda,listaMostrar,listaClaves,Semestre,Carrera,Grupo,Rol;
    String resultadoID="No hay datos";
    String opcion="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mensaje_principal);
        lsDatos = (ListView) findViewById(R.id.lvMensajes);
        //LLenar catalogos
        llenarSemestre();
        llenarCarrera();
        llenarGrupo();
        llenarRol();
        //Listar todos los mensajes del usuario
        listar(1);
    }
    public String obDatosLJSON(String response){
        try{
            JSONArray jsonDatos= new JSONArray(response);
            int length = jsonDatos.length();
            resultadoID= jsonDatos.getJSONObject(0).getString("ID");
            listaBusqueda = new ArrayList<>();
            listaClaves = new ArrayList<>();
            if (!resultadoID.equals("0")){
                for (int i=0;i<length;i++){
                    JSONObject json = jsonDatos.getJSONObject(i);
                  if(!json.getString("CARRERA").equals("-1")){
                      listaBusqueda.add( Carrera.get(Integer.parseInt(json.getString("CARRERA"))));
                      listaClaves.add("Carrera"+" - "+Integer.parseInt(json.getString("CARRERA")));
                  }else if(!json.getString("SEMESTRE").equals("-1")){
                      listaBusqueda.add( Semestre.get(Integer.parseInt(json.getString("SEMESTRE"))));
                      listaClaves.add("Semestre"+" - "+Integer.parseInt(json.getString("SEMESTRE")));
                  }else if(!json.getString("GRUPO").equals("-1")){
                      listaBusqueda.add( Grupo.get(Integer.parseInt(json.getString("GRUPO"))));
                      listaClaves.add("Grupo"+" - "+Integer.parseInt(json.getString("GRUPO")));
                  }else if(!json.getString("ROL").equals("-1")){
                      listaBusqueda.add( Rol.get(Integer.parseInt(json.getString("ROL"))));
                      listaClaves.add("Rol"+" - "+Integer.parseInt(json.getString("ROL")));
                  }else{ //Mensaje personal

                  }

                }

            }

        }catch (JSONException ex){
            ex.printStackTrace();
            Toast.makeText(this.getApplicationContext(),"Error"+ex.getMessage(),Toast.LENGTH_SHORT).show();
        }
        return  resultadoID;
    }
    public void listar(int id_usuario){
        try{
            //Genera una tarea asincrona que va a correr en segundo plano

            AsyncHttpClient cliente = new AsyncHttpClient();
            //Se indica la url de la pagina que consume el servicio web con json
            String url="https://agendaing.one-2-go.com/servicioWeb/mensaje.php?op=listar";
            RequestParams parametros = new RequestParams();
            parametros.put("id",id_usuario);
            cliente.post(url, parametros, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    mostrarDatosL(obDatosLJSON(new String(responseBody)));
                }
                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                }
            });



        }catch(Exception ex){

        }
    }
    public void mostrarDatosS(String rawData) {
        try {
            JSONArray jsonDatos= new JSONArray(rawData);
            int length = jsonDatos.length();
            resultadoID= jsonDatos.getJSONObject(0).getString("ID");
            Semestre= new ArrayList<>();
            if (!resultadoID.equals("0")){
                for (int i=0;i<length;i++){
                    JSONObject json = jsonDatos.getJSONObject(i);
                    Semestre.add(json.getString("NOMBRE"));
                }
            }
        }catch(Exception ex){
            ex.printStackTrace();
            Toast.makeText(getApplicationContext(),"Error:"+ex.getMessage().toString(),Toast.LENGTH_SHORT).show();
        }
    }
    public void llenarSemestre(){
        try {
            AsyncHttpClient cliente = new AsyncHttpClient();
            String url = "http://agendaing.one-2-go.com/servicioWeb/catalogos.php";
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
    public void mostrarDatosC(String rawData) {
        try {
            JSONArray jsonDatos= new JSONArray(rawData);
            int length = jsonDatos.length();
            resultadoID= jsonDatos.getJSONObject(0).getString("ID");
            Carrera= new ArrayList<>();
            if (!resultadoID.equals("0")){
                for (int i=0;i<length;i++){
                    JSONObject json = jsonDatos.getJSONObject(i);
                    Carrera.add(json.getString("NOMBRE"));
                }
            }

        }catch(Exception ex){
            ex.printStackTrace();
            Toast.makeText(getApplicationContext(),"Error:"+ex.getMessage().toString(),Toast.LENGTH_SHORT).show();
        }
    }
    public void llenarRol(){
        try {
            AsyncHttpClient cliente = new AsyncHttpClient();
            String url = "http://agendaing.one-2-go.com/servicioWeb/catalogos.php";
            RequestParams parametros = new RequestParams();
            parametros.put("op","rol");
            cliente.post(url, parametros, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    mostrarDatosR(new String(responseBody));
                }
                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                }
            });
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Error"+ e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    public void mostrarDatosR(String rawData) {
        try {
            JSONArray jsonDatos= new JSONArray(rawData);
            int length = jsonDatos.length();
            resultadoID= jsonDatos.getJSONObject(0).getString("ID");
            Rol= new ArrayList<>();
            if (!resultadoID.equals("0")){
                for (int i=0;i<length;i++){
                    JSONObject json = jsonDatos.getJSONObject(i);
                    Rol.add(json.getString("NOMBRE"));
                }
            }


        }catch(Exception ex){
            ex.printStackTrace();
            Toast.makeText(getApplicationContext(),"Error:"+ex.getMessage().toString(),Toast.LENGTH_SHORT).show();
        }
    }
    public void llenarGrupo(){
        try {
            AsyncHttpClient cliente = new AsyncHttpClient();
            String url = "http://agendaing.one-2-go.com/servicioWeb/catalogos.php";
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
            JSONArray jsonDatos= new JSONArray(rawData);
            int length = jsonDatos.length();
            resultadoID= jsonDatos.getJSONObject(0).getString("ID");
            Grupo= new ArrayList<>();
            if (!resultadoID.equals("0")){
                for (int i=0;i<length;i++){
                    JSONObject json = jsonDatos.getJSONObject(i);
                    Grupo.add(json.getString("NOMBRE"));
                }
            }


        }catch(Exception ex){
            ex.printStackTrace();
            Toast.makeText(getApplicationContext(),"Error:"+ex.getMessage().toString(),Toast.LENGTH_SHORT).show();
        }
    }

    public void llenarCarrera(){
        try {
            AsyncHttpClient cliente = new AsyncHttpClient();
            String url = "http://agendaing.one-2-go.com/servicioWeb/catalogos.php";
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
    public void mostrarDatosL(String dat){
        if (!resultadoID.equals("0")){
            adaptador = new ArrayAdapter(this, android.R.layout.simple_list_item_1, listaBusqueda);
            lsDatos.setAdapter(adaptador);
        }else{
            listaMostrar.add("No se encontro la busqueda");
            lsDatos.setAdapter(null);
        }
    }
    public void Destinatario(View v){
        Intent des=new Intent(this,seleccionDestinatario.class);
        startActivity(des);
    }
}