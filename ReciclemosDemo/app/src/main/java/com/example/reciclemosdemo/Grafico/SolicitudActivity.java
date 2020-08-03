package com.example.reciclemosdemo.Grafico;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.reciclemosdemo.Adicionales.dbHelper;
import com.example.reciclemosdemo.Entities.JsonPlaceHolderApi;
import com.example.reciclemosdemo.Entities.Solicitud;
import com.example.reciclemosdemo.Entities.Usuario;
import com.example.reciclemosdemo.Inicio.BolsaActivity;
import com.example.reciclemosdemo.Inicio.CatalogoActivity;
import com.example.reciclemosdemo.Inicio.LoginActivity;
import com.example.reciclemosdemo.Inicio.MeInformoActivity;
import com.example.reciclemosdemo.Inicio.ScanBarCodeActivity;
import com.example.reciclemosdemo.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.text.ParseException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SolicitudActivity extends AppCompatActivity   implements NavigationView.OnNavigationItemSelectedListener {
    private Retrofit retrofit;
    private DrawerLayout mDrawerLayout;

    private EditText editTextSustento,editTextDatos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solicitud);
        retrofit = new Retrofit.Builder()
                .baseUrl("https://recyclerapiresttdp.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        editTextSustento = findViewById(R.id.editTextSolicitudSustento);
        editTextDatos = findViewById(R.id.editTextActualizaDatos);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mDrawerLayout = findViewById(R.id.drawer);
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle mToggle = new ActionBarDrawerToggle(this,mDrawerLayout,toolbar, R.string.open, R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();



        dbHelper helper = new dbHelper(this,"Usuario.sqlite", null, 1);
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor fila2 = db.rawQuery("select nombre from Usuario", null);

        fila2.moveToFirst();
        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = (TextView) headerView.findViewById(R.id.txtUser);
        navUsername.setText(fila2.getString(0));
        TextView navViewProfile = headerView.findViewById(R.id.txtMiPerfil);
        navViewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
            }
        });

        //INICIALIZA APP BAR
        BottomNavigationView bottomNavigationView = findViewById(R.id.botton_navigation);

        //SELECCIÓN
        bottomNavigationView.setSelectedItemId(R.id.miaporte);

        //CAMBIO DE SELECCIÓN
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.miaporte:
                        startActivity(new Intent(getApplicationContext(), ListBolsas.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.escaner:
                        startActivity(new Intent(getApplicationContext(), ScanBarCodeActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.mibolsa:
                        startActivity(new Intent(getApplicationContext(), BolsaActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.catalogo:
                        startActivity(new Intent(getApplicationContext(), CatalogoActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });



    }


    public void RegistrarSolicitud(View view) throws InvalidKeySpecException, NoSuchAlgorithmException, ParseException {
        if(editTextDatos.getText().toString().trim().isEmpty()){
            editTextDatos.setError("Por favor ingrese los datos que desea actualizar");
        }else if(editTextSustento.getText().toString().trim().isEmpty()){
            editTextSustento.setError("Por favor ingrese el sustento o razón por la cual cambiará los datos");
        }else{
            dbHelper helper = new dbHelper(this,"Usuario.sqlite", null, 1);
            SQLiteDatabase db = helper.getReadableDatabase();
            Cursor fila2 = db.rawQuery("select codigo from Usuario", null);
            fila2.moveToFirst();

            int codigoUsuario = fila2.getInt(0);
            Usuario userTemp = new Usuario();
            userTemp.setCodigo(codigoUsuario);

            String valor_sustento = editTextSustento.getText().toString();
            String valor_datosActualizar = editTextDatos.getText().toString();

            Solicitud solicitud = new Solicitud();
            solicitud.setActiva(true);
            solicitud.setSustento(valor_sustento);
            solicitud.setDatosActualizar(valor_datosActualizar);
            solicitud.setUsuario(userTemp);


            JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);
            Call<Solicitud> call = jsonPlaceHolderApi.createSolicitud(solicitud);
            System.out.println(solicitud.toString());
            call.enqueue(new Callback<Solicitud>() {
                @Override
                public void onResponse(Call<Solicitud> call, Response<Solicitud> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(getApplicationContext(), "Registro Exitoso", Toast.LENGTH_LONG).show();
                        Intent profileActi = new Intent(getApplicationContext(), ProfileActivity.class);
                        startActivity(profileActi);
                        Log.e("TAG", "post submitted to API.:" + response.toString());
                    } else {
                        Log.e("TAG", "onResponse:" + response.toString());
                        Intent profileActi = new Intent(getApplicationContext(), ProfileActivity.class);
                        startActivity(profileActi);
                    }
                }

                @Override
                public void onFailure(Call<Solicitud> call, Throwable t) {
                    Log.e("TAG", "onFailure:" + t.getMessage());
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        if(mDrawerLayout.isDrawerOpen(GravityCompat.START)){
            mDrawerLayout.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
        }
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_info:
                startActivity(new Intent(getApplicationContext(), MeInformoActivity.class));
                break;
            case R.id.nav_message:
                Toast.makeText(this, "Proximamente 2", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_logout:
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                break;

        }
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}

