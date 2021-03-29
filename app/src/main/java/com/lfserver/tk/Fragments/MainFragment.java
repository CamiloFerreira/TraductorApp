package com.lfserver.tk.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lfserver.tk.R;
import com.lfserver.tk.Retrofit.ApiRetrofit;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import com.lfserver.tk.Model.*;
import com.lfserver.tk.Traductor;


/*
      @author : Luis Ferreira
      Clase principal del fragmento principal , este siendo el fragmento donde se presentan
      los edit text con el ingreso de texto para realizar la traduccion

 */


public class MainFragment extends Fragment {

    Button enviar;
    Boolean existe = false;
    EditText esp,map;
    ApiRetrofit api;
    Boolean existe_arch;
    ArrayList<Palabras_class> ListPalabras;

    public  MainFragment(Boolean existe_arc, ApiRetrofit api){
        this.existe_arch = existe_arc;
        this.api         = api;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_fragment,container,false);

        //Obtiene el edit text
        esp = view.findViewById(R.id.txt_esp);
        map = view.findViewById(R.id.txt_map);

        //Carga el boton
        enviar = view.findViewById(R.id.enviar);


       //if(existe_arch){
       //     CargarArchivo(view.getContext());
       //     enviar.setOnClickListener(this::click);
       //     Toast.makeText(view.getContext(),"Estoy trabajando con el archivo",Toast.LENGTH_SHORT).show();
       // }else
      //{
           //Si existe conexion a internet
           if (api.isOnline()){
               enviar.setOnClickListener(this::Presionado);

           }else{

               Toast.makeText(view.getContext(),"No hay conexion a internet !!",Toast.LENGTH_LONG).show();
           }
      //}


        return view;
    }

    public void Presionado(View view){
        String palabra = esp.getText().toString();
        api.getTrad(palabra,map);
    }

    public void click(View view){
       Traductor traductor = new Traductor(ListPalabras,esp.getText().toString(),map,view.getContext());
       // traductor.start();
        String traduccion = traductor.Traducir();
        map.setText(traduccion);

    }
    public void CargarArchivo(Context context){
        String file = "data";
        InputStreamReader archivo = null;

        try {
            archivo = new InputStreamReader(context.openFileInput(file));
            BufferedReader br = new BufferedReader(archivo);
            String linea = br.readLine();

            Gson gson = new Gson();

            Type modelList = new TypeToken<ArrayList<Palabras_class>>(){}.getType();
            // Carga los datos en el ArrayList
            this.ListPalabras = gson.fromJson(linea, modelList);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(context,"Error al cargar el archivo ", Toast.LENGTH_SHORT).show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
