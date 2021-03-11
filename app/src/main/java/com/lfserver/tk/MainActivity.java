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
    ApiConnect api ;

    //Variables para cargar el fragmento principal
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    private MenuItem item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        api = new ApiConnect("http://lfserver.tk:5000",getApplicationContext());

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
        fragmentTransaction.add(R.id.container,new MainFragment(existe(fileList(),"data"),this.api));
        fragmentTransaction.commit();


    }


    //Se carga el menu con opciones
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //Se carga el menu creado con el id
        getMenuInflater().inflate(R.menu.menu_conf, menu);
        return true;
    }

    /*
        Detecta el presionado del menu que esta en la parte superior derecha , detectando que
        boton fue presionado dependiendo del id que este tenga , los id se encuentran en
        res/menu/menu_conf.xml
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == R.id.creditos){
            getApplicationContext().deleteFile("data");
            Toast.makeText(getApplicationContext(),"Creditos",Toast.LENGTH_SHORT).show();
        }
        if(item.getItemId() == R.id.bajar){
            this.Savejson();
        }
        return false;
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
            fragmentTransaction.replace(R.id.container,new MainFragment(existe(fileList(),"data"),this.api));
            fragmentTransaction.commit();
        }
        if(item.getItemId() == R.id.dic){
            //Cargar fragmento Diccionario
            Fragment dic = new FragmentDiccionario(existe(fileList(),"data"),this.api);
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction  = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.container,dic);
            fragmentTransaction.commit();

        }
        return false;
    }
/*
    Funcion que sirve para guardar los datos que retorna la api en un arhivo llamado "data",
    para posteriormente ser leido en los otros fragmentos
 */
    private void Savejson() {
        String data  = api.dicString();
        int largo = data.length();

        //Si el largo de los datos recibidos es mayor a uno se escribe
        //ya que si es menor significa que los datos fueron recibidos de manera erronea
        if(largo > 1){
            //Guarda el archivo
            Toast.makeText(getApplicationContext(),"Descargando archivo .. ",Toast.LENGTH_SHORT).show();
            String filename = "data"; //Nombre del archivo donde se guardaran los datos de la api
            File file = new File(getApplication().getFilesDir(), filename); //Crea el archivo
            FileOutputStream outputStream;
            try {

                //Escribe el archivo y lo cierra
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
}