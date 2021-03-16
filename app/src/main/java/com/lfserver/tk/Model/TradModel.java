package com.lfserver.tk.Model;

import com.google.gson.annotations.SerializedName;

public class TradModel {

	@SerializedName("traduccion")
	private String traduccion;

	public String getTraduccion(){
		return traduccion;
	}
}