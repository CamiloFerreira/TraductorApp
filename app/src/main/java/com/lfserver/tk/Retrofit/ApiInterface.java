package com.lfserver.tk.Retrofit;



import com.lfserver.tk.Model.ModelDic;
import com.lfserver.tk.Model.TradModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiInterface {

    @GET("gDic2")
    Call<List<ModelDic>> getDic();


    @POST("gTrad")
    Call<TradModel> getTrad(@Body String send);

}
