package com.example.appmensajesing;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
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
    ArrayList<String> listaBusqueda,listaMostrar,listaClaves,Semestre,Carrera,Grupo,Rol,listaMensaje;
    String resultadoID="No hay datos";
    String opcion="";
    String idUsuario="";
    Integer length=0;
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
         Intent intent = getIntent();
         Bundle llenado = this.getIntent().getExtras();
        //LLenado de clave usuario
         try {
           if (llenado!=null){
               idUsuario = llenado.getString("id_usuario");
            }
             }catch(Exception ex){
               Toast.makeText(this.getApplicationContext(),"Error"+ex.getMessage(),Toast.LENGTH_SHORT).show();
           }
        //Toast.makeText(this.getApplicationContext(),"ID "+idUsuario,Toast.LENGTH_SHORT).show();
       // idUsuario="14";
        try{
            listar(Integer.parseInt(idUsuario));
            lsDatos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    //Envio de ID de usuario seleccionado
                    // Toast.makeText(mensajePrincipal.this, "Usuario ID: "+listaClaves.get(i), Toast.LENGTH_SHORT).show();
                    Intent mensaje = new Intent(mensajePrincipal.this, mensajeEnvio.class);
                    Bundle datos= new Bundle();
                    datos.putString("parametro",listaClaves.get(i));
                    mensaje.putExtras(datos);
                    startActivity(mensaje);
                }
            });
        }catch(Exception ex){
            Toast.makeText(this, "Error: "+ex.getMessage().toString(), Toast.LENGTH_SHORT).show();
        }
    }
    public String obDatosLJSON(String response){
        try{
            JSONArray jsonDatos= new JSONArray(response);
            length=0;
            length = jsonDatos.length();
            resultadoID= jsonDatos.getJSONObject(0).getString("ID");
            listaBusqueda = new ArrayList<>();
            listaClaves = new ArrayList<>();
            listaMensaje = new ArrayList<>();
            if (length > 0){
                for (int i=0;i<length;i++){
                    JSONObject json = jsonDatos.getJSONObject(i);
                  if(!json.getString("CARRERA").equals("-1")){
                      String carrera=Carrera.get(Integer.parseInt(json.getString("CARRERA"))-1);
                      if(listaBusqueda.contains(carrera)){
                          listaMensaje.set(listaBusqueda.indexOf(carrera), json.getString("MENSAJE"));
                      }else{
                          listaBusqueda.add(carrera);
                          listaMensaje.add(json.getString("MENSAJE"));
                          listaClaves.add(carrera+" - "+idUsuario+" - "+"carrera"+" - "+Integer.parseInt(json.getString("CARRERA")));
                      }
                  }else if(!json.getString("SEMESTRE").equals("-1")){
                      String semestre=Semestre.get(Integer.parseInt(json.getString("SEMESTRE"))-1);
                      if(listaBusqueda.contains(semestre)){
                          listaMensaje.set(listaBusqueda.indexOf(semestre), json.getString("MENSAJE"));
                      }else{
                          listaBusqueda.add(semestre);
                          listaMensaje.add(json.getString("MENSAJE"));
                          listaClaves.add(semestre+" - "+idUsuario+" - "+"semestre"+" - "+Integer.parseInt(json.getString("SEMESTRE")));
                      }
                  }else if(!json.getString("GRUPO").equals("-1")){
                      String grupo=Grupo.get(Integer.parseInt(json.getString("GRUPO"))-1);
                      if(listaBusqueda.contains(grupo)){
                          listaMensaje.set(listaBusqueda.indexOf(grupo), json.getString("MENSAJE"));
                      }else{
                          listaBusqueda.add(grupo);
                          listaMensaje.add(json.getString("MENSAJE"));
                          listaClaves.add(grupo+" - "+idUsuario+" - "+"grupo"+" - "+Integer.parseInt(json.getString("GRUPO")));
                      }
                  }else if(!json.getString("ROL").equals("-1")){
                      String tipoRol=Rol.get(Integer.parseInt(json.getString("ROL"))-1);
                      if (!tipoRol.equals("Alumno")){
                          tipoRol="Personal "+tipoRol;
                      }else{
                          tipoRol="Alumnos";
                      }
                      if(listaBusqueda.contains(tipoRol)){
                          listaMensaje.set(listaBusqueda.indexOf(tipoRol), json.getString("MENSAJE"));
                      }else{
                          listaBusqueda.add(tipoRol);
                          listaMensaje.add(json.getString("MENSAJE"));
                          listaClaves.add(tipoRol+" - "+idUsuario+" - "+"rol"+" - "+Integer.parseInt(json.getString("ROL")));
                      }
                  }else{ //Mensaje personal
                      JSONArray destinatario = json.getJSONArray("DESTINATARIOS");
                      String nombre=destinatario.getJSONObject(0).getString("Nombre")+" "+destinatario.getJSONObject(0).getString("Apellido");
                      if(listaBusqueda.contains(nombre)){
                          listaMensaje.set( listaBusqueda.indexOf(nombre), json.getString("MENSAJE"));
                      }else{
                         if (Integer.parseInt(destinatario.getJSONObject(0).getString("Id"))!=Integer.parseInt(idUsuario)){
                              listaBusqueda.add(nombre);
                              listaMensaje.add(json.getString("MENSAJE"));
                              listaClaves.add(nombre+" - "+idUsuario+" - "+"idDes"+" - "+Integer.parseInt(destinatario.getJSONObject(0).getString("Id")));
                          }

                      }

                      String Remitente=json.getString("REMITENTE");
                      JSONObject jsonRemitente = new JSONObject(Remitente);
                      String nombreRemitente=jsonRemitente.getString("Nombre")+" "+jsonRemitente.getString("Apellido");
                      if(listaBusqueda.contains(nombreRemitente)){ //si no existe
                          listaMensaje.set( listaBusqueda.indexOf(nombreRemitente), json.getString("MENSAJE"));
                      }else{
                          if (Integer.parseInt(jsonRemitente.getString("Id"))!=Integer.parseInt(idUsuario)){
                              listaBusqueda.add(nombreRemitente);
                              listaMensaje.add(json.getString("MENSAJE"));
                              listaClaves.add(nombreRemitente+" - "+idUsuario+" - "+"idDes"+" - "+Integer.parseInt(jsonRemitente.getString("Id")));
                          }
                      }
                  }

                }

            }else{
                Toast.makeText(this.getApplicationContext(),"Aún no tienes mensajes recientes",Toast.LENGTH_SHORT).show();
            }

        }catch (JSONException ex){
            ex.printStackTrace();
            Toast.makeText(this.getApplicationContext(),"Aún no tienes mensajes recientes",Toast.LENGTH_SHORT).show();
            Toast.makeText(this.getApplicationContext(),"Error"+ex.getMessage(),Toast.LENGTH_SHORT).show();
        }
        return  resultadoID;
    }
    public void listar(int id_usuario){
        try{
            //Genera una tarea asincrona que va a correr en segundo plano

            AsyncHttpClient cliente = new AsyncHttpClient();
            //Se indica la url de la pagina que consume el servicio web con json
            String url="https://agendaing.one-2-go.com/servicioWeb/mensaje.php";
            RequestParams parametros = new RequestParams();
            parametros.put("op","listar");
            parametros.put("id",id_usuario);
            cliente.post(url, parametros, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    mostrarDatosL(obDatosLJSON(new String(responseBody)));
                }
                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    Toast.makeText(mensajePrincipal.this.getApplicationContext(),"Aún no tienes mensajes recientes",Toast.LENGTH_SHORT).show();
                }
            });



        }catch(Exception ex){
            Toast.makeText(this.getApplicationContext(),"Error"+ex.getMessage(),Toast.LENGTH_SHORT).show();
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
        if (!resultadoID.equals("0")&&length>0){
            adaptador = new ArrayAdapter(this, android.R.layout.simple_list_item_1, listaBusqueda);
            CustomAdapter customAdapter= new CustomAdapter();
            lsDatos.setAdapter(customAdapter);
        }else{
            Toast.makeText(this.getApplicationContext(),"Aún no tienes mensajes recientes",Toast.LENGTH_SHORT).show();
            lsDatos.setAdapter(null);
        }
    }
    class CustomAdapter extends BaseAdapter{
        @Override
        public int getCount() {
            return listaBusqueda.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            if (listaBusqueda.size()>0){
                view = getLayoutInflater().inflate(R.layout.customlayout,null);
                TextView textView_nombre= (TextView)view.findViewById(R.id.textView_nombre);
                TextView textView_mensaje= (TextView)view.findViewById(R.id.textView2_mensaje);
                textView_nombre.setText(listaBusqueda.get(i));
                textView_mensaje.setText(listaMensaje.get(i));
            }
            return view;
        }
    }
    public void Destinatario(View v){
        Intent reg=new Intent(this,seleccionDestinatario.class);
        Bundle datos= new Bundle();
        datos.putString("id_usuario",idUsuario);
        reg.putExtras(datos);
        startActivity(reg);
    }
}