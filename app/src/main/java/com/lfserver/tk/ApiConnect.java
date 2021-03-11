package com.lfserver.tk;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.StrictMode;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;

/*
    @author : Luis Ferreira
    Clase que permite realizar la conexion a la api , donde utilizando el metodo requests se pide
    la conexion a la api , donde este devuelve un string que es transformado a json para ser
    procesado .
 */



public class ApiConnect {

    String url_base ;
    JSONObject jsonData;
    Context context;
    public ApiConnect(String url , Context context){
        this.url_base = url;
        this.context = context;
        if(this.isOnline()){
            this.jsonData = getDic();
        }
    }
    public JSONObject getData(){
        return this.jsonData;
    }

    /*
        Funcion que retorna el JsonObject con los datos obtenidos de /gDic de la api
     */
    public JSONObject getDic(){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        URL url = null;
        HttpURLConnection conn;

        try {
            //Realiza la conexion a /gDic
            url = new URL(this.url_base+"/gDic");
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String inputLine ;
            StringBuffer response = new StringBuffer();

            String json = " ";

            while ((inputLine = in.readLine()) != null){
                response.append(inputLine);
            }
            json = response.toString();
            JSONObject jsonobj = new JSONObject(json);
            return jsonobj;

        } catch (MalformedURLException e) {
            e.printStackTrace();
            Toast.makeText(this.context,"Error en conexion",Toast.LENGTH_SHORT).show();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this.context,"Error en response",Toast.LENGTH_SHORT).show();
            return null;
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this.context,"Error en json",Toast.LENGTH_SHORT).show();
            return null;
        }
    }

    /*
        Funcion que retorna una cadena  con la traduccion obtenida mediante la api
        teniendo como parametro la oracion a traduccir .
    */
    public String getTrad(String palabra){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        URL url = null;
        HttpURLConnection conn;

        try {
            url = new URL(this.url_base+"/gTrad/"+palabra);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            conn.connect();

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String inputLine ;
            StringBuffer response = new StringBuffer();

            String json = " ";

            while ((inputLine = in.readLine()) != null){
                response.append(inputLine);
            }

            json = response.toString();

            JSONObject jsonobj = new JSONObject(json);


            String trad = jsonobj.optString("traduccion").toString();
            return trad;

        } catch (MalformedURLException e) {
            e.printStackTrace();
            Toast.makeText(this.context,"Error en conexion",Toast.LENGTH_SHORT).show();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this.context,"Error en response",Toast.LENGTH_SHORT).show();
            return null;
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this.context,"Error en json",Toast.LENGTH_SHORT).show();
            return null;
        }
    }

    /*
     Funcion que retorna la cadena  con los datos obtenidos de /gDic de la api
  */
    public String dicString()  {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        URL url = null;
        HttpURLConnection conn;
        try {
            url = new URL(this.url_base+"/gDic");
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine ;
            StringBuffer response = new StringBuffer();
            String json = " ";
            while ((inputLine = in.readLine()) != null){
                response.append(inputLine);
            }
            json = response.toString();
            return json;
        } catch (MalformedURLException e) {
            //e.printStackTrace();
            Toast.makeText(this.context,"Error al cargar url",Toast.LENGTH_LONG).show();
            return " ";
        } catch (ProtocolException e) {
            //e.printStackTrace();
            Toast.makeText(this.context,"Error de conexion , intente nuevamente ",Toast.LENGTH_LONG).show();
            return " ";
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this.context,"Ha ocurrido un error",Toast.LENGTH_LONG).show();
            return " ";
        }
    }

 /*
    Funcion para establecer si existe conexion a internet en la aplicacion , donde verifica el
    estado del dispositivo respecto a la network y si existe acceso a internet
  */

    public  boolean isOnline() {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected();
    }

}