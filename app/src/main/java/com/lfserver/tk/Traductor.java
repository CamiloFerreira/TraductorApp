package com.lfserver.tk;

import android.content.Context;
import android.util.Log;
import android.widget.EditText;

import com.lfserver.tk.Model.PalabrasModel;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class Traductor extends Thread {

    ArrayList <PalabrasModel> ListPalabras;
    String palabra ;
    EditText map;
    Context context;
    public Traductor(ArrayList<PalabrasModel> listPalabras, String palabra, EditText map, Context context) {
        this.ListPalabras = listPalabras;
        this.palabra = palabra;
        this.map = map;
        this.context = context;
    }

    public void run(){


        String traduccion = "";
        String p = "";
        //Se realiza la tokenizacion
        StringTokenizer st = new StringTokenizer(palabra);
        Boolean existe = false;
        while(st.hasMoreTokens()){
            String cad  = "";
            for(PalabrasModel model:ListPalabras){
                if(model.getSignificados() != null){
                    List<String> listPalabras = model.getSignificados();
                    for(String pal:listPalabras){
                        if(pal.equals(st.nextToken().toString())){
                            existe = true;
                            cad = st.nextToken().toString();
                        }
                    }
                }

            }

            if(existe){
                traduccion += cad + " ";
                existe = false;
            }else{
                traduccion += st.nextToken() + " ";
            }

        }
        map.setText(traduccion);
    }
}
