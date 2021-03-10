package com.lfserver.tk.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import com.lfserver.tk.ApiConnect;
import com.lfserver.tk.R;

public class MainFragment extends Fragment {

    Button enviar;
    EditText esp,map;
    ApiConnect api;
    Boolean existe_arch;
    public  MainFragment(Boolean existe_arc){
        this.existe_arch = existe_arc;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_fragment,container,false);

        //Obtiene el edit text
        esp = view.findViewById(R.id.txt_esp);
        map = view.findViewById(R.id.txt_map);

        //Carga el boton
        enviar = view.findViewById(R.id.enviar);

        //Si existe conexion a internet
        if (isOnlineNet()){
            api = new ApiConnect("http://lfserver.tk:5000",view.getContext());
            enviar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String palabra = esp.getText().toString();
                    String traduccion = api.getTrad(palabra);

                    map.setText(traduccion);
                }
            });
        }else{
            Toast.makeText(view.getContext(),"Error no hay conexion a internet",Toast.LENGTH_LONG).show();
        }



        return view;
    }

    public Boolean isOnlineNet() {

        try {
            Process p = java.lang.Runtime.getRuntime().exec("ping -c 1 www.google.es");

            int val           = p.waitFor();
            boolean reachable = (val == 0);
            return reachable;

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;
    }


}
