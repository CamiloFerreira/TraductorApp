package com.lfserver.tk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.lfserver.tk.Fragments.FragmentCreditos;
import com.lfserver.tk.Fragments.FragmentDiccionario;
import com.lfserver.tk.Fragments.MainFragment;
import com.lfserver.tk.Retrofit.ApiRetrofit;
import com.lfserver.tk.Retrofit.Model.PalabrasModel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;


/*
      @author : Luis Ferreira
      Clase principal para instanciar tanto menus , toolbars y conectar entre los distintos fragmentos


 */



// <div>Iconos diseñados por <a href="https://www.freepik.com" title="Freepik">Freepik</a> from <a href="https://www.flaticon.es/" title="Flaticon">www.flaticon.es</a></div>
// NO olvidar atribuir //
public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    Toolbar toolbar;
    NavigationView navigationView;
    ApiRetrofit apiRetrofit;
    //Variables para cargar el fragmento principal
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        apiRetrofit = new ApiRetrofit("http://lfserver.tk:5000",getApplicationContext());

        //Se carga el toolbar creado , cargando el archivo drawer_toolbar.xml
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        //Se carga el drawer que se encuentra en el Activity_main.xml
        drawerLayout = findViewById(R.id.drawer);

        //Se carga el navigationView que se encuentra en el Activity_main.xml
        navigationView = findViewById(R.id.navigationView);

        //Se crea el menu navegable , el drawerMenu
        actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        actionBarDrawerToggle.syncState();


        //Establecer evento onclick a navigationview
        navigationView.setNavigationItemSelectedListener(this);


        //Cargar fragmento principal
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction  = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.container,new MainFragment(existe(fileList(),"data"),this.apiRetrofit));
        fragmentTransaction.commit();


    }


    /*
        Detecta la pulsacion del drawermenu , donde dependiendo del id detecta que seccion fue
        presionada
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        drawerLayout.closeDrawer(GravityCompat.START);
        if(item.getItemId() == R.id.home){
            //Cargar fragmento principal
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction  = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container,new MainFragment(existe(fileList(),"data"),this.apiRetrofit));
            fragmentTransaction.commit();
        }
        if(item.getItemId() == R.id.dic){
            //Cargar fragmento Diccionario
            Fragment dic = new FragmentDiccionario(existe(fileList(),"data"),this.apiRetrofit);
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction  = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.container,dic);
            fragmentTransaction.commit();
        }
        if(item.getItemId() == R.id.creditos){
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction  = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container,new FragmentCreditos());
            fragmentTransaction.commit();
        }
        if(item.getItemId() == R.id.bajar){
            this.getJson();
        }
        return false;
    }


    /*
        Funcion que retorna true si existe el archivo que se le pasa por parametro , teniendo como parametro
        el arreglo de cadena , donde debe ir filelist() y el nombre del archivo
     */
    private boolean existe(String[] archivos,String arch){
        for(int i = 0 ; i < archivos.length;i++){
            if(arch.equals(archivos[i])){
                return true;
            }
        }
        return false;
    }

    private  void getJson(){

        ArrayList<PalabrasModel>  palabras = apiRetrofit.ListPalabras;


        if(apiRetrofit.isOnline()){
            Toast.makeText(getApplicationContext(),"Descargando datos ...",Toast.LENGTH_SHORT).show();
            String json = new Gson().toJson(palabras);
            String filename = "data";
            File file = new File(getApplicationContext().getFilesDir(), filename);
            FileOutputStream outputStream;

            try {
                outputStream = openFileOutput(filename,Context.MODE_PRIVATE);
                outputStream.write(json.getBytes());
                outputStream.close();
                Toast.makeText(getApplicationContext(),"Archivo guardado !  ",Toast.LENGTH_SHORT).show();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(),"Error al crear el archivo",Toast.LENGTH_LONG).show();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(),"Error al crear escribir el archivo",Toast.LENGTH_LONG).show();
            }
        }else {
            Toast.makeText(getApplicationContext(),"No hay conexion a internet :(",Toast.LENGTH_SHORT).show();
        }



    }
}