package com.example.appmensajesing;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
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

public class Consulta extends AppCompatActivity {
    public Spinner spTipoBus,spParametros;
    String[]opciones={"Selecciona un tipo de búsqueda...","Carrera","Grupo","Semestre","Rol"};
    String[]parametros;
    int seleccion=0;
    ArrayAdapter<String> spinnerAdaptor;
    String resultadoID="No hay datos";
    ArrayList<String> listaMostrar,listaBusqueda,listaParametros;
    ListView lsDatos;
    ArrayAdapter adaptador;
    EditText Busqueda;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consulta);
        //Lista busqueda
        spTipoBus=(Spinner) findViewById(R.id.spTipoBus);
        spParametros=(Spinner)findViewById(R.id.spParametros);
        spinnerAdaptor = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,opciones);
        spTipoBus.setAdapter(spinnerAdaptor);
        Busqueda=(EditText) findViewById(R.id.txtBusqueda);
        lsDatos = (ListView) findViewById(R.id.lvBusqueda);
        listar();
        //LLenado de parametros
        try{
            spTipoBus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    seleccion=0;
                    seleccion=i;
                    switch(i) {
                        case 1: //Carrera
                            llenarParametros(i);
                            break;
                        case 2: //Grupo
                            llenarParametros(i);
                            break;
                        case 3: //Semestre
                            llenarParametros(i);
                            break;
                        case 4: //Rol
                            llenarParametros(i);
                            break;
                        default:
                            parametros = new String[]{"Filtros.."};
                            spinnerAdaptor = new ArrayAdapter<String>(Consulta.this,android.R.layout.simple_spinner_item,parametros);
                            spParametros.setAdapter(spinnerAdaptor);
                            break;
                    }

                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

        }catch (Exception ex){
            Toast.makeText(this, "Error: "+ex.getMessage().toString(), Toast.LENGTH_SHORT).show();
        }
        //LLenado de busqueda a partir de los parametros
        try{
            spParametros.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        llenarBusqueda(seleccion);
                }
                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
        }catch(Exception ex){
            Toast.makeText(this, "Error: "+ex.getMessage().toString(), Toast.LENGTH_SHORT).show();
        }
        //Busqueda de contacto a partir que escibe
        Busqueda.addTextChangedListener(new TextWatcher() {


            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    llenarBusqueda(5);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
    public void Menu(View v){
        Intent chk=new Intent(this,menuPrincipal.class);
        startActivity(chk);
    }
    public void mostrarDatosS(String rawData) {
        try {
            JSONArray jsonDatos= new JSONArray(rawData);
            int length = jsonDatos.length();
            resultadoID= jsonDatos.getJSONObject(0).getString("ID");
            listaParametros= new ArrayList<>();
            if (!resultadoID.equals("0")){

                for (int i=0;i<length;i++){
                    JSONObject json = jsonDatos.getJSONObject(i);
                    listaParametros.add(json.getString("NOMBRE"));
                }
                spinnerAdaptor = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,listaParametros);
                spParametros.setAdapter(spinnerAdaptor);
            }

        }catch(Exception ex){
            ex.printStackTrace();
            Toast.makeText(getApplicationContext(),"Error:"+ex.getMessage().toString(),Toast.LENGTH_SHORT).show();
        }
    }
    public void mostrarDatosB(String rawData) {
        try {
            JSONArray jsonDatos= new JSONArray(rawData);
            int length = jsonDatos.length();
            lsDatos.setAdapter(null);
            if (length!=0){
                resultadoID= jsonDatos.getJSONObject(0).getString("ID");
                listaBusqueda= new ArrayList<>();
                for (int i=0;i<length;i++){
                    JSONObject json = jsonDatos.getJSONObject(i);
                    listaBusqueda.add(json.getString("NOMBRE")+" - "+json.getString("EMAIL"));
                }
                adaptador = new ArrayAdapter(this, android.R.layout.simple_list_item_1, listaBusqueda);
                lsDatos.setAdapter(adaptador);
            }

        }catch(Exception ex){
            ex.printStackTrace();
            Toast.makeText(getApplicationContext(),"Error aqui:"+ex.getMessage().toString(),Toast.LENGTH_SHORT).show();
        }
    }
    public void llenarBusqueda(int i){
        try {
            AsyncHttpClient cliente = new AsyncHttpClient();
            String url = "https://agendaing.one-2-go.com/servicioWeb/contacto.php";
            RequestParams parametros = new RequestParams();
            parametros.put("op","buscar");
            switch(i) {
                case 0:
                    listar();
                    break;
                case 1: //Carrera
                    String cadena=spParametros.getSelectedItem().toString();
                    String[] parts = cadena.split(" ");
                    if(parts[1].equals("Cibernética")){
                        parametros.put("carrera","Cibernetica");
                    }else if(parts[1].equals("Mecatrónica")){
                        parametros.put("carrera","Meca");
                    }else{
                        parametros.put("carrera",parts[1]);
                    }

                    break;
                case 2: //Grupo
                    parametros.put("grupo",spParametros.getSelectedItem());
                    break;
                case 3: //Semestre
                    parametros.put("semestre",spParametros.getSelectedItem());
                    break;
                case 4: //Rol
                    parametros.put("rol",spParametros.getSelectedItem());
                    break;
                    case 5: //Nombre
                    parametros.put("nombre",Busqueda.getText());
                    break;
                default:
                    break;
            }
            cliente.post(url, parametros, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    mostrarDatosB(new String(responseBody));
                }
                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                }
            });
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Error"+ e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    public void llenarParametros(int i){
        try {
            AsyncHttpClient cliente = new AsyncHttpClient();
            String url = "http://agendaing.one-2-go.com/servicioWeb/catalogos.php";
            RequestParams parametros = new RequestParams();
            switch(i) {
                case 1: //Carrera
                    parametros.put("op","carrera");
                    break;
                case 2: //Grupo
                    parametros.put("op","grupo");
                    break;
                case 3: //Semestre
                    parametros.put("op","semestre");
                    break;
                case 4: //Rol
                    parametros.put("op","rol");
                    break;
                default:
                    break;
            }
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
    public String obDatosLJSON(String response){
        try{
            JSONArray jsonDatos= new JSONArray(response);
            int length = jsonDatos.length();
            resultadoID= jsonDatos.getJSONObject(0).getString("ID");
            listaBusqueda = new ArrayList<>();
            if (!resultadoID.equals("0")){

                for (int i=0;i<length;i++){
                    JSONObject json = jsonDatos.getJSONObject(i);
                    listaBusqueda.add(json.getString("NOMBRE")+" - "+json.getString("EMAIL"));
                }

            }

        }catch (JSONException ex){
            ex.printStackTrace();
            Toast.makeText(this.getApplicationContext(),"Error"+ex.getMessage(),Toast.LENGTH_SHORT).show();
        }
        return  resultadoID;
    }
    public void listar(){
        try{
            //Genera una tarea asincrona que va a correr en segundo plano

            AsyncHttpClient cliente = new AsyncHttpClient();
            //Se indica la url de la pagina que consume el servicio web con json
            String url="https://agendaing.one-2-go.com/servicioWeb/contacto.php?op=listar";

            cliente.post(url, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    //Ejecuta el metodo  de acceso del servicio web y el resultado que obtiene
                    //Se envia a mostrar datos que da el mensaje de bienvenida
                    mostrarDatosL(obDatosLJSON(new String(responseBody)));
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                }
            });



        }catch(Exception ex){

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
}