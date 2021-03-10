package com.lfserver.tk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import com.google.android.material.navigation.NavigationView;
import com.lfserver.tk.Fragments.FragmentDiccionario;
import com.lfserver.tk.Fragments.MainFragment;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;



// <div>Iconos diseñados por <a href="https://www.freepik.com" title="Freepik">Freepik</a> from <a href="https://www.flaticon.es/" title="Flaticon">www.flaticon.es</a></div>
// NO olvidar atribuir //
public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    Toolbar toolbar;
    NavigationView navigationView;
    ApiConnect api ;

    //Variables para cargar el fragmento principal
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    private int featureId;
    private MenuItem item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        api = new ApiConnect("http://lfserver.tk:5000",getApplicationContext());

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer);

        navigationView = findViewById(R.id.navigationView);

        actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        actionBarDrawerToggle.syncState();


        //Establecer evento onclick a navigationview
        navigationView.setNavigationItemSelectedListener(this);


        //Cargar fragmento principal
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction  = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.container,new MainFragment(existe(fileList(),"data")));
        fragmentTransaction.commit();


    }


    //Se carga el menu con opciones
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_conf, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.creditos:
                Toast.makeText(getApplicationContext(),"Creditos",Toast.LENGTH_SHORT).show();

            case R.id.tema:
                Toast.makeText(getApplicationContext(),"Temas",Toast.LENGTH_SHORT).show();

            case R.id.bajar:
                //Toast.makeText(getApplicationContext(),"Descargar",Toast.LENGTH_SHORT).show();
                this.Savejson();
            default:
                return super.onOptionsItemSelected(item);
        }




    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        drawerLayout.closeDrawer(GravityCompat.START);
        if(item.getItemId() == R.id.home){
            //Cargar fragmento principal
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction  = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container,new MainFragment(existe(fileList(),"data")));
            fragmentTransaction.commit();
        }
        if(item.getItemId() == R.id.dic){
            Fragment dic = new FragmentDiccionario(existe(fileList(),"data"));
            //Cargar fragmento principal
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction  = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.container,dic);
            fragmentTransaction.commit();

        }
        return false;
    }


    //Funcion para guardar el json en el dispositivo
    private void Savejson() {
        String data  = api.dicString();
        int largo = data.length();

        if(largo > 1){

            //Guarda el archivo
            Toast.makeText(getApplicationContext(),"Descargando archivo .. ",Toast.LENGTH_SHORT).show();

            String filename = "data";
            File file = new File(getApplication().getFilesDir(), filename);

            FileOutputStream outputStream;

            try {
                outputStream = openFileOutput(filename,Context.MODE_PRIVATE);
                outputStream.write(data.getBytes());
                outputStream.close();


                Toast.makeText(getApplicationContext(),"Archivo guardado !  ",Toast.LENGTH_SHORT).show();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(),"Error al crear el archivo",Toast.LENGTH_LONG).show();

            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(),"Error al crear escribir el archivo",Toast.LENGTH_LONG).show();
            }

        }

    }

    private boolean existe(String[] archivos,String arch){
        for(int i = 0 ; i < archivos.length;i++){
            if(arch.equals(archivos[i])){
                return true;
            }
        }
        return false;
    }
}