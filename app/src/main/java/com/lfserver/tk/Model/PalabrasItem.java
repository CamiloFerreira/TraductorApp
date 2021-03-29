package com.lfserver.tk.Model;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class PalabrasItem{

	@SerializedName("significado")
	private List<String> significado;

	@SerializedName("palabra")
	private List<String> palabra;

	public List<String> getSignificado(){
		return significado;
	}

	public List<String> getPalabra(){
		return palabra;
	}
}