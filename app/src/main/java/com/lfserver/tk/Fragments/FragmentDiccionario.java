package com.lfserver.tk.Fragments;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lfserver.tk.AdapterModel;
import com.lfserver.tk.R;
import com.lfserver.tk.Retrofit.ApiRetrofit;
import com.lfserver.tk.Model.PalabrasModel;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;

/*
      @author : Luis Ferreira
      Clase principal del Fragmento de diccioanario , donde este debe enlazar tanto el spinner ,
      como el editText que se encuentra en el , para poder realizar el filtrado de palabras.

 */

public class FragmentDiccionario extends Fragment implements AdapterView.OnItemSelectedListener, TextWatcher {
    Spinner trad_comb,letra_comb;
    EditText palabra;
    ArrayList <PalabrasModel> listPalabras;
    RecyclerView recycler;
    ApiRetrofit apiRetrofit;
    AdapterModel adaptador;
    boolean existe_arch;

    public FragmentDiccionario(boolean existe_arch, ApiRetrofit apiRetrofit){
        this.existe_arch = existe_arch;
        this.apiRetrofit = apiRetrofit;
    }

    Button btn1 ;
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.diccionario_fragment,container,false);


        //Primero pregunta si el archivo fue creado
        if(existe_arch){
            //Si el archivo existe
            this.CargarArchivo(view.getContext());
        }else{
            if(apiRetrofit.isOnline()){
                //Si el archivo no fue creado
                listPalabras =  apiRetrofit.ListPalabras;
            }else {
                Toast.makeText(view.getContext(),"No hay conexion a internet !!",Toast.LENGTH_LONG).show();
            }
        }


        //Carga el array list y busca el recycler view
        recycler = view.findViewById(R.id.recicler);
        recycler.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        adaptador = new AdapterModel(listPalabras);
        recycler.setAdapter(adaptador);

        //Obtiene la palabra ingresada
        palabra = (EditText) view.findViewById(R.id.palabra);
        palabra.addTextChangedListener(this);


        // Crea el combox con la lista
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


    /*
         Funcion que carga el archivo , utilizano gson para cargar el arraylist y ser devuelto
         al listPalabras
     */

    public void CargarArchivo(Context context){
        String file = "data";
        InputStreamReader archivo = null;

        try {
            archivo = new InputStreamReader(context.openFileInput(file));
            BufferedReader br = new BufferedReader(archivo);
            String linea = br.readLine();
            Log.d("linea",linea);

            Gson gson = new Gson();

            Type modelList = new TypeToken<ArrayList<PalabrasModel>>(){}.getType();
            // Carga los datos en el ArrayList
            this.listPalabras = gson.fromJson(linea, modelList);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(context,"Error al cargar el archivo ", Toast.LENGTH_SHORT).show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }




    /*
        Funciones de implements
     */

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId())
        {
            case R.id.letra_comb:
                //Vuelve a recargar el recicycler view con los datos seleccionados
               String letra = letra_comb.getSelectedItem().toString();

               ArrayList<PalabrasModel> ListFiltrada = new ArrayList<PalabrasModel>();

               //Se busca las que contienen solamente la letra
               for(PalabrasModel model : this.listPalabras){
                   if(letra.equals(model.getLetra())){
                       ListFiltrada.add(model);
                   }
               }


               //Se carga la nueva lista con solamente la letra seleccionada
                adaptador = new AdapterModel(ListFiltrada);
                recycler.setAdapter(adaptador);




        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        String text = palabra.getText().toString().toLowerCase(); // Obtiene la palabra
        String selection = trad_comb.getSelectedItem().toString().toLowerCase();

        ArrayList<PalabrasModel> listSelection = new ArrayList<PalabrasModel>();
        if(text.length() > 0){

            for(PalabrasModel model:listPalabras){
                if(selection.equals("esp")){
                    if(model.getSig().toLowerCase().contains(text)){
                        listSelection.add(model);
                    }
                }else {
                    if(model.getPalabra().toLowerCase().contains(text)){
                        listSelection.add(model);
                    }
                }
            }

            //Se carga la nueva lista con solamente la letra seleccionada
            adaptador = new AdapterModel(listSelection);
            recycler.setAdapter(adaptador);

            if(listSelection.size() == 0){
                Toast.makeText(getContext(),"No existe resultado :( ",Toast.LENGTH_SHORT).show();
            }

        }else {
            //Se carga la nueva lista con solamente la letra seleccionada
            adaptador = new AdapterModel(listPalabras);
            recycler.setAdapter(adaptador);

        }

    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}

