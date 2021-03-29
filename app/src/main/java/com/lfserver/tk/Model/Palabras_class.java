package com.lfserver.tk.Model;

import java.util.List;

public class Palabras_class {

    String letra;
    List<String> palabras;
    List<String> significados;

    public Palabras_class(String letra, List<String> palabra, List<String> significados){
        this.letra = letra;
        this.palabras = palabra;
        this.significados = significados;
    }

    public String getLetra() {
        return letra;
    }

    public List<String> getSignificados() {
        return significados;
    }

    public List<String> getPalabras() {
        return palabras;
    }


    public String getPal(){
        String cad = " ";
        for(int i = 0 ; i < palabras.size();i++){
            if(i == 0 ){
                cad += palabras.get(i);
            }else {
                cad += ","+palabras.get(i);
            }
        }
        return  cad;
    }
    public String getSig(){
        String cad = " ";
        for(int i = 0 ; i < significados.size();i++){
            if(i == 0 ){
                cad += significados.get(i);
            }else {
                cad += ","+significados.get(i);
            }
        }
        return  cad;
    }

}
