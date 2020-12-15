package com.example.appmensajesing;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class registroContactos extends AppCompatActivity {
    public Spinner Semestre,Carrera,Grupo,Rol;
    String resultados = "No hay datos";
    ArrayList<String> sem = new ArrayList<>();
    ArrayList<String> carr = new ArrayList<>();
    ArrayList<String> gru = new ArrayList<>();
    ArrayList<String> rl = new ArrayList<>();
    ArrayAdapter<String> AdaptadorS, adapC, adapG, adapRol;
    private ArrayList<String> clavesS, clavesC, clavesG, clavesR;
    EditText nom, ape, us,con,tel, mail;
    Integer car,sm,gr,rol;
    String id_usuario="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_contactos);
        Semestre=(Spinner)findViewById(R.id.spRegistroSemestre);
        Carrera=(Spinner)findViewById(R.id.spRegistroCarrera);
        Grupo=(Spinner)findViewById(R.id.spRegistroGrupo);
        Rol=(Spinner)findViewById(R.id.spRol);
        nom =(EditText) findViewById(R.id.txtRegistroNombre);
        ape=(EditText) findViewById(R.id.txtRegisrtroApellido);
        us=(EditText) findViewById(R.id.txtRegistroUsuario);
        con=(EditText) findViewById(R.id.txtRegistroContra);
        tel=(EditText) findViewById(R.id.txtRegistroTel);
        mail=(EditText) findViewById(R.id.txtRegistroCorreo);

        llenarSemestre();
        llenarCarrera();
        llenarGrupo();
        llenarRol();
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
    }
    public void llenarRol(){
        try {
            AsyncHttpClient cliente = new AsyncHttpClient();
            String url = "http://agendaing.one-2-go.com/servicioWeb/catalogos.php?";
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
            JSONArray data = new JSONArray(rawData);
            clavesR= new ArrayList<>();
            for (int i=0; i < data.length(); i++) {
                clavesR.add(data.getJSONObject(i).getString("ID"));
                rl.add(data.getJSONObject(i).getString("NOMBRE"));
            }
            adapRol= new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,rl);
            Rol.setAdapter(adapRol);
            Rol.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    //Toast.makeText(getApplicationContext(),"datos:"+clavesS.get(position),Toast.LENGTH_SHORT).show();
                    rol=Integer.parseInt(clavesR.get(position));
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
                    sm=Integer.parseInt(clavesS.get(position));
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
                    car=Integer.parseInt(clavesC.get(position));
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
                    gr=Integer.parseInt(clavesG.get(position));
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

    public void registrar(View v){
        if(TextUtils.isEmpty(nom.getText()) ||TextUtils.isEmpty(ape.getText()) ||TextUtils.isEmpty(us.getText())  ||TextUtils.isEmpty(con.getText())||TextUtils.isEmpty(mail.getText())  ||TextUtils.isEmpty(tel.getText())){
            Toast.makeText(getApplicationContext(), "Debe llenar todos los campos" , Toast.LENGTH_SHORT).show();
        }else{
            try {
                AsyncHttpClient cliente = new AsyncHttpClient();
                String url = "https://agendaing.one-2-go.com/servicioWeb/contacto.php?op=registrar&";
                RequestParams parametros = new RequestParams();
                parametros.put("nombre", nom.getText().toString());
                parametros.put("ap",ape.getText().toString());
                parametros.put("usuario",us.getText().toString());
                parametros.put("contra",con.getText().toString());
                parametros.put("rol",rol);
                parametros.put("tel",tel.getText().toString());
                parametros.put("email",mail.getText().toString());
                parametros.put("carrera",car);
                parametros.put("grupo",gr);
                parametros.put("semestre",sm);
                cliente.post(url, parametros, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        mostrarDatos(obDatosJSON(new String(responseBody)));
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                    }
                });
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Error"+ e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }

    }

    public void mostrarDatos(String dat) {
        if (!resultados.equals("{\"ID\":0}")) {
            Toast.makeText(getApplicationContext(), "Registrado", Toast.LENGTH_SHORT).show();
            limpiar();
        } else {
            Toast.makeText(getApplicationContext(), "El usuario ya existe", Toast.LENGTH_SHORT).show();
        }
    }

    public String obDatosJSON(String response) {
        try {
            JSONObject jsonDatos = new JSONObject(response);
            resultados=response;
           // Toast.makeText(getApplicationContext(), "Registrado" + response , Toast.LENGTH_SHORT).show();

        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
        }
        return resultados;
    }

    public void limpiar(){
        nom.setText("");
        ape.setText("");
        us.setText("");
        con.setText("");
        tel.setText("");
        mail.setText("");
    }
}