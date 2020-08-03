package com.example.reciclemosdemo.Inicio;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.reciclemosdemo.Grafico.ListBolsas;
import com.example.reciclemosdemo.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import java.io.IOException;

public class RegistrarBolsaActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private APIToSQLite sqlitedb;
    FragmentManager fragmentManager;
    FragmentTransaction transaction, transaction2;
    Fragment qrFragment = QrFragment.newInstance();
    Fragment exitosoFragment = new ExitosoFragment();
    private DrawerLayout mDrawerLayout;

    private ProgressDialog progressDialog;


    Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message message) {
            switch (message.what){
                case 2:
                    progressDialog.cancel();
                    Toast.makeText(getApplicationContext(), "Información cargada", Toast.LENGTH_LONG).show();
                    break;

            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_bolsa);
        fragmentManager = getSupportFragmentManager();
        transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment, qrFragment);
        transaction.commit();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mDrawerLayout = findViewById(R.id.drawer);
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle mToggle = new ActionBarDrawerToggle(this,mDrawerLayout,toolbar,R.string.open,R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();

        //INICIALIZA APP BAR
        BottomNavigationView bottomNavigationView = findViewById(R.id.botton_navigation);

        //SELECCIÓN
        bottomNavigationView.setSelectedItemId(R.id.mibolsa);

        //CAMBIO DE SELECCIÓN
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.miaporte:
                        ((QrFragment) qrFragment).pauseScan();
                        startActivity(new Intent(getApplicationContext(), ListBolsas.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.escaner:
                        ((QrFragment) qrFragment).pauseScan();
                        startActivity(new Intent(getApplicationContext(), ScanBarCodeActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.mibolsa:
                        ((QrFragment) qrFragment).pauseScan();
                        startActivity(new Intent(getApplicationContext(), BolsaActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.catalogo:
                        ((QrFragment) qrFragment).pauseScan();
                        startActivity(new Intent(getApplicationContext(), CatalogoActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });

    }

    public void creatingUpdate()
    {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Cargando información...");
        progressDialog.show();
        progressDialog.setCancelable(false);


        sqlitedb = new APIToSQLite(this,"actualizar");

        new BackgroundJob().execute();
    }

    private class BackgroundJob extends AsyncTask<Void,Void,Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                sqlitedb.InsertBolsas();
                sqlitedb.InsertProbolsa();
                sqlitedb.insertBolsasByMonthOrWeek("bolsasWeek/");
                sqlitedb.obtenerBolsasByYear("bolsasYear/");
                sqlitedb.obtenerBolsasByDay();
                sqlitedb.obtenerUltimasBolsas();
               // sqlitedb.obtenerDatosProductByBolsa();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void avoid){
            Message msg = new Message();
            msg.what = 2;
            mHandler.sendMessage(msg);
        }
    }

    public void registroExitoso(){
        fragmentManager = getSupportFragmentManager();
        transaction2 = fragmentManager.beginTransaction();
        transaction2.replace(R.id.fragment, exitosoFragment);
        transaction2.commit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_info:
                ((QrFragment) qrFragment).pauseScan();
                startActivity(new Intent(getApplicationContext(), MeInformoActivity.class));
                break;
            case R.id.nav_message:
                Toast.makeText(this, "Proximamente 2", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_logout:
                ((QrFragment) qrFragment).pauseScan();
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                break;
        }
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}
