package com.lfserver.tk.Retrofit;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.lfserver.tk.Model.ModelDic;
import com.lfserver.tk.Model.PalabrasItem;
import com.lfserver.tk.Model.PalabrasModel;
import com.lfserver.tk.Model.TradModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiRetrofit {

    private String url_base;
    Context context;
    private Retrofit retrofit;
    private ApiInterface apiInterface;
    public  ArrayList<PalabrasModel> ListPalabras;
    public ApiRetrofit(String url_base,Context context){
        this.url_base = url_base;
        this.context = context;
        this.ListPalabras = new ArrayList<PalabrasModel>();

        this.retrofit = new Retrofit.Builder()
                .baseUrl(this.url_base)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        this.apiInterface = retrofit.create(ApiInterface.class);
        this.getDic();



    }


    public void getDic(){
        Call<List<ModelDic>> call = apiInterface.getDic();


        call.enqueue(new Callback<List<ModelDic>>() {
            @Override
            public void onResponse(Call<List<ModelDic>> call, Response<List<ModelDic>> response) {

                //Si sucede un error en la respuesta ( Codigos de error http)
                if(!response.isSuccessful()){
                    Toast.makeText(context,"Codigo:" +response.code(),Toast.LENGTH_SHORT);
                }else {
                    List<ModelDic>  modelList = response.body();

                    ArrayList<PalabrasModel> list2 = new ArrayList<>();
                    for(ModelDic modelDic:modelList){
                        String letra = modelDic.getLetra();
                        List<PalabrasItem> palabrasItemList = modelDic.getPalabras();
                        for(PalabrasItem palabrasItem:palabrasItemList){
                            String palabra = palabrasItem.getPalabra();
                            List<String> significados = palabrasItem.getSignificado();
                            PalabrasModel model= new PalabrasModel(letra,palabra,significados);
                            list2.add(model);
                        }
                    }

                    ListPalabras = list2;
                }
            }


            @Override
            public void onFailure(Call<List<ModelDic>> call, Throwable t) {
                Toast.makeText(context,t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("OnFailure",t.getMessage());
            }
        });


    }

    public void getTrad(String text, EditText map){


        String json = "{'cadena':'"+text+"'}";
        Call<TradModel> call = null;
        try {
            call = apiInterface.getTrad(new JSONObject(json).toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        call.enqueue(new Callback<TradModel>() {
            @Override
            public void onResponse(Call<TradModel> call, Response<TradModel> response) {
                if(!response.isSuccessful()){
                    Toast.makeText(context,"Codigo:" +response.code(),Toast.LENGTH_SHORT).show();
                }else {
                    TradModel trad = response.body();
                    map.setText(trad.getTraduccion());
                }
            }

            @Override
            public void onFailure(Call<TradModel> call, Throwable t) {
                Toast.makeText(context,t.getLocalizedMessage(),Toast.LENGTH_SHORT).show();
            }
        });

    }

    public ArrayList<PalabrasModel> getListPalabras() {
        return ListPalabras;
    }

    public  boolean isOnline() {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected();
    }




}
