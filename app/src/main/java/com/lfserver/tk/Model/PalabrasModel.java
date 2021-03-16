package com.lfserver.tk.Model;

import java.util.List;

public class PalabrasModel {

    String letra;
    String palabra;
    List<String> significados;

    public PalabrasModel(String letra,String palabra,List<String> significados){
        this.letra = letra;
        this.palabra = palabra;
        this.significados = significados;
    }

    public String getLetra() {
        return letra;
    }

    public List<String> getSignificados() {
        return significados;
    }

    public String getPalabra() {
        return palabra;
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
