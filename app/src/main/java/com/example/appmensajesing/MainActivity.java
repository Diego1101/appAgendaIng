package com.example.appmensajesing;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {
    EditText nom, contra;
    String resultados = "No hay datos";
    String resNom = "";
    Button acceso;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        nom = (EditText) findViewById(R.id.txtAccesoUsu);
        contra = (EditText) findViewById(R.id.txtAccesoPas);
        acceso= (Button)findViewById(R.id.btnAccesoAceptar);
       // acceso.setOnClickListener(new View.OnClickListener() {
         //   @Override
        //    public void onClick(View v) {
             //   if(TextUtils.isEmpty(nom.getText()) || TextUtils.isEmpty(contra.getText())){
           //         Toast.makeText(getApplicationContext(), "LLenar todos los campos", Toast.LENGTH_SHORT).show();
             //   }else {
            //        acceso();
            //    }
           // }
       // });
    }

    public void acceso() {
        try {
            AsyncHttpClient cliente = new AsyncHttpClient();
            String url = "https://agendaing.one-2-go.com/servicioWeb/contacto.php?op=acceso&";
            RequestParams parametros = new RequestParams();
            parametros.put("usuario",nom.getText().toString());
            parametros.put("contra",contra.getText().toString());

            cliente.post(url, parametros, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    //Toast.makeText(getApplicationContext(), "Entra", Toast.LENGTH_SHORT).show();
                    mostrarDatos(new String(responseBody));

                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                }
            });
        } catch (Exception e) {

        }
    }

    public void mostrarDatos(String rawData) {
        try {
            JSONObject data = new JSONObject(rawData);
            resultados=data.getString("ID");
            if(!resultados.equals("0")){
                resNom=data.getString("NOMBRE");
                Toast.makeText(getApplicationContext(),"Bienvenido "+ resNom, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, menuPrincipal.class);
                startActivity(intent);
            }else{
                Toast.makeText(getApplicationContext(), "Acceso denegado", Toast.LENGTH_SHORT).show();
            }

            //grid.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, gridData));
        }catch(Exception ex){
            Toast.makeText(getApplicationContext(),"Error:"+ex.getMessage().toString(),Toast.LENGTH_SHORT).show();
        }
    }
    public void Menu(View v){
        Intent chk=new Intent(this,menuPrincipal.class);
        startActivity(chk);
    }





}