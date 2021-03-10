package com.lfserver.tk.Fragments;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lfserver.tk.ApiConnect;
import com.lfserver.tk.Modelo;
import com.lfserver.tk.R;
import com.lfserver.tk.AdapterModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class FragmentDiccionario extends Fragment implements AdapterView.OnItemSelectedListener, TextWatcher {
    Spinner trad_comb,letra_comb;
    EditText palabra;
    ArrayList <Modelo> listModel;
    RecyclerView recycler;
    ApiConnect api;
    JSONObject datos;
    AdapterModel adaptador;
    String letra = "a";
    boolean existe_arch;

    public FragmentDiccionario(boolean existe_arch){
        this.existe_arch = existe_arch;
    }

    Button btn1 ;
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.diccionario_fragment,container,false);
        //Primero pregunta si el archivo fue creado
        if(existe_arch){
            datos = CargarFile(view.getContext());
        }else{
            //Si hay conexion a internet y no existe el archivo
            if(isOnlineNet()){
                //Se crea la clase que conecta con la api
                ApiConnect api = new ApiConnect("http://lfserver.tk:5000",getContext());
                datos = api.getDic();
            }else {
                try {
                    datos = new JSONObject("{'a':[{'palabra':'Internet','significado':'No existe conexion a internet'}]}");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }
        //Carga el array list y busca el recycler view
        recycler = view.findViewById(R.id.recicler);
        recycler.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        listModel = llenarDatos(datos,letra); //llena con los datos
        adaptador = new AdapterModel(listModel);
        recycler.setAdapter(adaptador);

        //Obtiene la palabra ingresada
        palabra = (EditText) view.findViewById(R.id.palabra);
        palabra.addTextChangedListener(this);


        /* Crea el combox con la lista */
        trad_comb = (Spinner) view.findViewById(R.id.trad_combo);
        ArrayAdapter adapter = ArrayAdapter.createFromResource(getActivity(),R.array.lista_combox, android.R.layout.simple_spinner_dropdown_item);
        trad_comb.setAdapter(adapter);
        trad_comb.setOnItemSelectedListener(this);

        // Crea el combox con letras
        letra_comb = (Spinner) view.findViewById(R.id.letra_comb);
        ArrayAdapter adapter2 = ArrayAdapter.createFromResource(getActivity(),R.array.lista_letra, android.R.layout.simple_spinner_dropdown_item);
        letra_comb.setAdapter(adapter2);
        letra_comb.setOnItemSelectedListener(this);

        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private ArrayList<Modelo> llenarDatos(JSONObject d, String letra) {
        ArrayList<Modelo> data = new ArrayList<Modelo>();

        try {
            JSONArray json = d.getJSONArray(letra);
            for(int i = 0 ; i < json.length();i++){
                JSONObject obj = json.getJSONObject(i);

                String sig = obj.opt("significado").toString();

                //Se quitan los caracteres que sobran
                sig = sig.replace("[","");
                sig = sig.replace("]","");
                sig = sig.replace("\"","");




                Modelo mdl1 = new Modelo(obj.optString("palabra").toString(),sig);
                data.add(mdl1);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return data;
    }

    public JSONObject CargarFile(Context context){


        //Funcion para cargar el archivo que fue guardado
        
        String filename = "data";
        JSONObject jsonObject;
        //Carga el archivo
        InputStreamReader archivo = null;
        try {
            archivo = new InputStreamReader(context.openFileInput(filename));
            BufferedReader br = new BufferedReader(archivo);
            String linea = br.readLine();
            jsonObject = new JSONObject(linea);

            return  jsonObject;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(context,"Error al cargar  ",Toast.LENGTH_SHORT).show();
            return  null;
        } catch (IOException e) {
            e.printStackTrace();
            return  null;
        } catch (JSONException e) {
            e.printStackTrace();
            return  null;
        }


    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId())
        {
            case R.id.letra_comb:
                //Vuelve a recargar el recicycler view con los datos seleccionados
                letra = letra_comb.getSelectedItem().toString();
                this.listModel = llenarDatos(datos,letra);
                this.adaptador = new AdapterModel(this.listModel);
                this.recycler.setAdapter(this.adaptador);

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    public Boolean isOnlineNet() {

        try {
            Process p = java.lang.Runtime.getRuntime().exec("ping -c 1 www.google.es");

            int val           = p.waitFor();
            boolean reachable = (val == 0);
            return reachable;

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        String text = palabra.getText().toString().toLowerCase(); // Obtiene la palabra
        ArrayList<Modelo> modelList = new ArrayList<Modelo>();

        if(text.length() > 0 ){

            //Se cargaa
            Log.d("Edit",palabra.getText().toString());
            for(int i = 0 ; i< listModel.size();i++){
                Modelo mdl = listModel.get(i);
                String palabra = mdl.getPalabra().toLowerCase();
                String significado=  mdl.getSignificado().toLowerCase();
                String selection = trad_comb.getSelectedItem().toString().toLowerCase();
                if(selection.equals("esp")){

                    //Buscan el significado que contengan el texto
                    if(significado.contains(text)){
                        modelList.add(mdl);
                    }
                }else {
                    //Buscan la palabra que contengan el texto
                    if(palabra.contains(text)){
                        modelList.add(mdl);
                    }
                }
            }
        }
        //Actualiza los datos
        if(modelList.size() == 0 ){
            if(text.length() > 0 ){
                Toast.makeText(getContext(),"No se ha encontrado un resultado :(",Toast.LENGTH_SHORT).show();
            }
        }else{
            this.adaptador = new AdapterModel(modelList);
            this.recycler.setAdapter(this.adaptador);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}

