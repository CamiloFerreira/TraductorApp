package com.lfserver.tk;

import android.content.Context;
import android.util.Log;
import android.widget.EditText;

import com.lfserver.tk.Model.Palabras_class;

import java.util.ArrayList;
import java.util.List;

public class Traductor{

    ArrayList <Palabras_class> ListPalabras;
    String oracion ;
    EditText map;
    Context context;
    public Traductor(ArrayList<Palabras_class> listPalabras, String palabra, EditText map, Context context) {
        this.ListPalabras = listPalabras;
        this.oracion = palabra;
        this.map = map;
        this.context = context;
    }

    /*
        Funcion que busca la traduccion por tokens
    */
    public void BuscarxToken(){

    }
    /*
        Funcion que sirve para determinar si es pregunta
     */
    public String[] IsAnswer(String oracion){
        oracion = oracion.toLowerCase(); // transforma a minuscula

        //String[] listPal = oracion.split(String.valueOf(R.string.pregunta));
        String[] listPal = oracion.split("¿");

        String palabra = " ";
        Log.d("largo",Integer.toString(listPal.length));

        //Pregunta si existen datos en la lista
        if(listPal.length  > 1  ){

            for (String pal : listPal){
                if(pal != ""){
                    palabra = pal;
                }
            }

            System.out.println(palabra);
            int largo_pal = palabra.length()-1;

            System.out.println("cadena"+" "+palabra.charAt(largo_pal));


            return new String[]{"true", palabra};
        }else{
            return new String[]{"false", oracion};
        }


    }

    /*
        Funcion que busca palabra completa
     */
    public String BuscarPalabra(String oracion){

        oracion = oracion.toLowerCase(); // transforma a minuscula
        String palabra = " ";

        //Realiza comprobacion para ver si es pregunta

        String[] isAnswer= this.IsAnswer(oracion);

        if(isAnswer[0].equals("true")){
            Log.d("Palabra",isAnswer[1]);
        }


        for(Palabras_class model:ListPalabras){
            List<String> listPal = model.getSignificados();

            //Recorre la lista palabra y busca la oracion completa si existe
            for(String pal :listPal){
                if(pal.toLowerCase().equals(oracion)){
                    palabra = pal;
                }
            }
        }

        return palabra;
    }

    /*
        Funcion principal que realiza la traduccion
     */
    public  String Traducir(){
        //Primero busca la traduccion completa



        String oracion_completa = this.BuscarPalabra(oracion);
        if(oracion_completa != " ")
        {
            return oracion_completa;
        }else{
            //Si no existe como palabra completa
            return "no existe";
        }

    }
}
