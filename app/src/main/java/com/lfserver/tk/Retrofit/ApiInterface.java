package com.lfserver.tk.Retrofit;

import com.lfserver.tk.Retrofit.Model.ModelDic;
import com.lfserver.tk.Retrofit.Model.TradModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApiInterface {

    @GET("gDic2")
    Call<List<ModelDic>> getDic();


    @GET("gTrad/{text}")
    Call<TradModel> getTrad(@Path("text") String text);

}
