package com.lfserver.tk.Retrofit.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ModelDic {

    @SerializedName("palabras")
    private List<PalabrasItem> palabras;

    @SerializedName("letra")
    private String letra;


    public List<PalabrasItem> getPalabras() {
        return palabras;
    }

    public String getLetra() {
        return letra;
    }
}
