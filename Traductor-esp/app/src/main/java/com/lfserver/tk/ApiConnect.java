package com.lfserver.tk;

import android.content.Context;
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

public class ApiConnect {

    String url_base ;
    ArrayList <Modelo> listModelo;
    Context context;
    public ApiConnect(String url , Context context){
        this.url_base = url;
        this.context = context;
        this.listModelo = new ArrayList<Modelo>();
    }

    public JSONObject getDic(){
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
}