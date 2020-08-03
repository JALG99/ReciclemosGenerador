package com.example.reciclemosdemo.Grafico;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class RecicladorActivity extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener{
    private DrawerLayout mDrawerLayout;
    private TextView txtNombre,txtApellido,txtAsociacion,txtDNI,txtCodigoReciclador;
    private EditText editTextMensaje;
    private Dialog myObservacionDialog;
    private ImageView imagenReciclador;
    private Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reciclador);
        txtNombre = findViewById(R.id.txtInputNombre);
        txtApellido = findViewById(R.id.txtInputApellidos);
        txtAsociacion = findViewById(R.id.txtInputCelular);
        txtDNI = findViewById(R.id.txtInputCorreo);
        txtCodigoReciclador = findViewById(R.id.txtInputFechaNacimiento);
        editTextMensaje = findViewById(R.id.editTextActualizaDatos);
        imagenReciclador = findViewById(R.id.imageRecycler);
        btn = findViewById(R.id.imgCerrarSesion);

        myObservacionDialog = new Dialog(this);
        myObservacionDialog.setContentView(R.layout.editable_profile);
        myObservacionDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        TextView txtObservacionesDialog = myObservacionDialog.findViewById(R.id.txtObservaciones);
        TextView txtValor = myObservacionDialog.findViewById(R.id.txtValor);
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

        Cursor recicladorF = db.rawQuery("select nombre, apellido,dni ,asociacion ,fecha_Nacimiento,imagen from Reciclador", null);
        recicladorF.moveToFirst();
        txtNombre.setText(recicladorF.getString(0));
        txtApellido.setText(recicladorF.getString(1));
        txtAsociacion.setText(recicladorF.getString(3));
        txtDNI.setText(recicladorF.getString(2));
        String sDate1 = recicladorF.getString(4);
        System.out.println("MILIBROOOOOOOOOOOOOOOOO");

        try {
            Date date1=new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy").parse(sDate1);
            Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Europe/Paris"));
            cal.setTime(date1);
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH);
            int day = cal.get(Calendar.DAY_OF_MONTH);
            System.out.println(Integer.toString(year));
            System.out.println(Integer.toString(day));

            System.out.println(Integer.toString(month));


            System.out.println(calculateDate(year,month,day));
            txtCodigoReciclador.setText(calculateDate(year,month,day));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Picasso.get()
                .load(recicladorF.getString(4))
                .error(R.drawable.login)
                .into(imagenReciclador);


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

        btn.setOnClickListener(v -> {
            if(editTextMensaje.getText().toString().trim().isEmpty()){
                editTextMensaje.setError("Por favor ingrese un mensaje");
            }else{
                txtValor.setText("");
                String mensaje = "Acabas de mandarle un mensaje a: "+txtNombre.getText() +" " + txtApellido.getText() ;
                txtObservacionesDialog.setText(mensaje + "\r\n \r\nContinuemos apoyando su trabajo");
                myObservacionDialog.show();
            }
        });
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


    public String calculateDate(int year,int month,int day){
        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();

        dob.set(year, month, day);

        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)){
            age--;
        }

        Integer ageInt = new Integer(age);
        String ageS = ageInt.toString();
        return ageS;
    }
}
