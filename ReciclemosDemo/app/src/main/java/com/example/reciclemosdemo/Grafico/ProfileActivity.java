package com.example.reciclemosdemo.Grafico;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.reciclemosdemo.Adicionales.dbHelper;
import com.example.reciclemosdemo.Inicio.BolsaActivity;
import com.example.reciclemosdemo.Inicio.CatalogoActivity;
import com.example.reciclemosdemo.Inicio.LoginActivity;
import com.example.reciclemosdemo.Inicio.MeInformoActivity;
import com.example.reciclemosdemo.Inicio.ScanBarCodeActivity;
import com.example.reciclemosdemo.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class ProfileActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private  TextView txtNombre,txtApellido,txtCorreo,txtPassword,txtDni;
    private TextView txtDepartamento,txtDistrito,txtCondominio,txtDireccion,txtFechaNacimiento,txtSexo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        txtNombre = findViewById(R.id.txtInputNombre);
        txtApellido = findViewById(R.id.txtInputApellidos);
        txtCorreo = findViewById(R.id.txtInputCorreo);
        txtPassword = findViewById(R.id.txtInputPassword);
        txtDepartamento = findViewById(R.id.txtInputDepartamento);
        txtDistrito = findViewById(R.id.txtInputDistrito);
        txtCondominio = findViewById(R.id.txtInputCondominio);
        txtDireccion = findViewById(R.id.txtInputDireccion);
        txtFechaNacimiento = findViewById(R.id.txtInputFechaNacimiento);
        txtSexo = findViewById(R.id.txtInputSexo);
        txtDni = findViewById(R.id.txtInputDNI);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mDrawerLayout = findViewById(R.id.drawer);
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle mToggle = new ActionBarDrawerToggle(this,mDrawerLayout,toolbar,R.string.open,R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();

        dbHelper helper = new dbHelper(this,"Usuario.sqlite", null, 1);
        SQLiteDatabase db = helper.getReadableDatabase();

        Cursor fila2 = db.rawQuery("select codigo, nombre, apellido ,email, condominio , direccion  , email , fecha_Nacimiento , sexo ,distrito_name ,condominio_direccion ,departamento_name ,dni from Usuario", null);

        fila2.moveToFirst();
        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = (TextView) headerView.findViewById(R.id.txtUser);
        navUsername.setText(fila2.getString(1));
        TextView navViewProfile = headerView.findViewById(R.id.txtMiPerfil);
        navViewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
            }
        });

        txtNombre.setText(fila2.getString(1));
        txtApellido.setText(fila2.getString(2));
        txtCorreo.setText(fila2.getString(3));
        txtDepartamento.setText(fila2.getString(11));
        txtDistrito.setText(fila2.getString(9));
        txtCondominio.setText(fila2.getString(4));
        txtDireccion.setText( fila2.getString(10) + " N° "+fila2.getString(5));
        txtDni.setText(fila2.getString(12));
        String[] parts = fila2.getString(7).split("-");
        String year = parts[0];
        String month = parts[1];
        String dateAux = parts[2];
        String[] partsDate = dateAux.split("T");
        String date = partsDate[0];
        txtFechaNacimiento.setText(date+"/"+month+"/"+year);
        txtSexo.setText(fila2.getString(8));



        navigationView.setNavigationItemSelectedListener(this);
        //INICIALIZA APP BAR
        BottomNavigationView bottomNavigationView = findViewById(R.id.botton_navigation);

        //SELECCIÓN
        bottomNavigationView.setSelectedItemId(R.id.escaner);

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

    public void SolicitudSol(View v){
        startActivity(new Intent(getApplicationContext(), SolicitudActivity.class));
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



