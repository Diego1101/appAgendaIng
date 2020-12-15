package com.example.appmensajesing;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.BaseAdapter;
import android.widget.EditText;
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

public class mensajeEnvio extends AppCompatActivity {
    TextView Usuario;
    String idUsuario="";
    ArrayList<String> listaFecha,listaMensaje,listaTipo;
    String resultadoID="No hay datos",Tipochat="",id_chat="";
    ListView lsDatos;
    EditText txtMensaje;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mensaje_envio);
        Usuario=(TextView) findViewById(R.id.lblUsuario);
        lsDatos = (ListView) findViewById(R.id.lsConversacion);
        txtMensaje = (EditText) findViewById(R.id.edtMensaje);
        Intent intent = getIntent();
        Bundle llenado = this.getIntent().getExtras();
        //LLenado de mensajes
        try {
            if (llenado!=null){
                String parametro = llenado.getString("parametro");
                String[] parts = parametro.split(" - ");
                Usuario.setText(parts[0]+parts[1]+parts[2]+parts[3]);//Nombre chat
                idUsuario =(parts[1]);//Id usuario
                Tipochat=parts[2];
                id_chat=parts[3];

                listar(Integer.parseInt(idUsuario),Tipochat,id_chat);
            }
        }catch(Exception ex){
            Toast.makeText(this.getApplicationContext(),"Error"+ex.getMessage(),Toast.LENGTH_SHORT).show();
        }


    }
    public String obDatosLJSON(String response){
        try{
            JSONArray jsonDatos= new JSONArray(response);
            int length = jsonDatos.length();
            resultadoID= jsonDatos.getJSONObject(0).getString("ID");
            listaTipo = new ArrayList<>();
            listaMensaje = new ArrayList<>();
            listaFecha = new ArrayList<>();
            if (length!=0){
                for (int i=0;i<length;i++){
                    JSONObject json = jsonDatos.getJSONObject(i);
                    listaFecha.add(json.getString("FECHA"));listaMensaje.add(json.getString("MENSAJE"));
                    String tipo=json.getString("TIPO");
                    if (tipo.equals("Enviado")){
                        listaTipo.add("Yo");
                    }else{
                        listaTipo.add(Usuario.getText().toString());
                    }


                }
            }
        }catch (JSONException ex){
            ex.printStackTrace();
            Toast.makeText(this.getApplicationContext(),"Error"+ex.getMessage(),Toast.LENGTH_SHORT).show();
        }
        return  resultadoID;
    }
    class CustomAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return listaMensaje.size();
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
            view = getLayoutInflater().inflate(R.layout.customchat,null);
            TextView textView_nombre= (TextView)view.findViewById(R.id.textView_nombre);
            TextView textView_fecha= (TextView)view.findViewById(R.id.textView2_fecha);
            TextView textView_mensaje= (TextView)view.findViewById(R.id.textView2_mensaje);
            TextView textView_nombreYo= (TextView)view.findViewById(R.id.textView_nombreyo);
            TextView textView_fechaYo= (TextView)view.findViewById(R.id.textView2_fechayo);
            TextView textView_mensajeYo= (TextView)view.findViewById(R.id.textView2_mensajeyo);
            textView_nombreYo.setVisibility(View.GONE);
            textView_nombre.setVisibility(View.GONE);
            textView_fecha.setVisibility(View.GONE);
            textView_mensaje.setVisibility(View.GONE);
            textView_fechaYo.setVisibility(View.GONE);
            textView_mensajeYo.setVisibility(View.GONE);
            if (listaTipo.get(i).equals("Yo")){
                textView_nombreYo.setVisibility(View.VISIBLE);
                textView_nombreYo.setText(listaTipo.get(i));
                textView_mensajeYo.setVisibility(View.VISIBLE);
                textView_mensajeYo.setText(listaMensaje.get(i));
                textView_fechaYo.setVisibility(View.VISIBLE);
                textView_fechaYo.setText(listaFecha.get(i));
            }else{
                textView_nombre.setVisibility(View.VISIBLE);
                textView_nombre.setText(listaTipo.get(i));
                textView_mensaje.setVisibility(View.VISIBLE);
                textView_mensaje.setText(listaMensaje.get(i));
                textView_fecha.setVisibility(View.VISIBLE);
                textView_fecha.setText(listaFecha.get(i));
            }
            return view;
        }
    }
    public void mostrarDatosL(String dat){
        if (!resultadoID.equals("0")){
            //adaptador = new ArrayAdapter(this, android.R.layout.simple_list_item_1, listaBusqueda);
            lsDatos.setAdapter(null);
            mensajeEnvio.CustomAdapter customAdapter= new mensajeEnvio.CustomAdapter();
            lsDatos.setAdapter(customAdapter);
        }else{
            listaMensaje.add("No se encontro la busqueda");
            lsDatos.setAdapter(null);
        }
    }
    public void mostrarDatosS(String dat){
        if (!resultadoID.equals("0")){
            //adaptador = new ArrayAdapter(this, android.R.layout.simple_list_item_1, listaBusqueda);
            Toast.makeText(getApplicationContext(), "Mensaje enviado!", Toast.LENGTH_SHORT).show();
            listar(Integer.parseInt(idUsuario),Tipochat,id_chat);
            txtMensaje.setText("");
        }else{
            Toast.makeText(getApplicationContext(), "No se pudo enviar el mensaje!", Toast.LENGTH_SHORT).show();
        }
    }
    public String obDatosSJSON(String response){
        try{
            JSONObject json = new JSONObject(response);
            resultadoID= json.getString("RES");
        }catch (JSONException ex){
            ex.printStackTrace();
            Toast.makeText(this.getApplicationContext(),"Error"+ex.getMessage(),Toast.LENGTH_SHORT).show();
        }
        return  resultadoID;
    }

    public void enviarMensaje(){
        try {
            AsyncHttpClient cliente = new AsyncHttpClient();
            String url = "https://agendaing.one-2-go.com/servicioWeb/mensaje.php";
            RequestParams parametros = new RequestParams();
            parametros.put("op","crear");
            parametros.put("id",idUsuario);
            parametros.put(Tipochat,id_chat);
            parametros.put("mensaje",txtMensaje.getText().toString());
            cliente.post(url, parametros, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    mostrarDatosS(obDatosSJSON(new String(responseBody)));
                }
                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                }
            });
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Error"+ e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    public void envio(View view){
        if (!txtMensaje.getText().toString().equals("")){
            enviarMensaje();
        }else{
            Toast.makeText(getApplicationContext(), "Asegurate de escribir tu mensaje!", Toast.LENGTH_SHORT).show();
    }
    }
    public void listar(int id_usuario,String Tipo,String Parametros){
        try{
            //Genera una tarea asincrona que va a correr en segundo plano
            AsyncHttpClient cliente = new AsyncHttpClient();
            //Se indica la url de la pagina que consume el servicio web con json
            String url="https://agendaing.one-2-go.com/servicioWeb/mensaje.php";
            RequestParams parametros = new RequestParams();
            parametros.put("op","listar");
            parametros.put("id",id_usuario);
            parametros.put(Tipo,Parametros);
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
             Toast.makeText(this.getApplicationContext(),"Error"+ex.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }
}