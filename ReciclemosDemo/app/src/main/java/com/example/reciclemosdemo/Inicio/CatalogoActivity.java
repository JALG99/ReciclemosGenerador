package com.example.reciclemosdemo.Inicio;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.reciclemosdemo.Adicionales.dbHelper;
import com.example.reciclemosdemo.Entities.Bolsalist;
import com.example.reciclemosdemo.Entities.Producto;
import com.example.reciclemosdemo.Entities.Productolist;
import com.example.reciclemosdemo.Grafico.ListBolsas;
import com.example.reciclemosdemo.Grafico.ProfileActivity;
import com.example.reciclemosdemo.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

public class CatalogoActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private DrawerLayout mDrawerLayout;
    private RecyclerView recyclerView;
    private ArrayList<Productolist> productolists;
    private FragmentManager fragmentManager;
    private FrameLayout fragment;
    private View view;
    private CardView cvPlastico, cvPapelCarton;
    private ArrayList<Productolist> productolistssecondary;
    private AdapterProducto mAdapter;
    private ImageButton imgbtnPlastico, imgbtnPapelCarton;
    private EditText etxtBuscar;
    private TextView txtvPlastico, txtvPapelCarton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalogo);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mDrawerLayout = findViewById(R.id.drawer);
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle mToggle = new ActionBarDrawerToggle(this,mDrawerLayout,toolbar,R.string.open,R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();

        cvPlastico = findViewById(R.id.cvPlastico);
        cvPapelCarton = findViewById(R.id.cvPapelCarton);
        txtvPlastico = findViewById(R.id.txtvPlastico);
        txtvPapelCarton = findViewById(R.id.txtvPapelCarton);
        cvPlastico.setCardElevation(10);
        cvPapelCarton.setCardElevation(10);

        imgbtnPlastico = findViewById(R.id.imgbtnPlastico);
        imgbtnPapelCarton = findViewById(R.id.imgbtnPapelCarton);

        imgbtnPlastico.setOnClickListener(onClickListener);
        imgbtnPapelCarton.setOnClickListener(onClickListener);

        recyclerView = findViewById(R.id.recycler_view);
        productolists = new ArrayList<>();

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);

        etxtBuscar = findViewById(R.id.etxtBuscar);
        etxtBuscar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filterS(s.toString());
            }
        });

        dbHelper helper = new dbHelper(this, "Usuario.sqlite", null, 1);
        SQLiteDatabase db = helper.getReadableDatabase();

        Cursor filaUsuario = db.rawQuery("select codigo, nombre from Usuario", null);

        filaUsuario.moveToFirst();

        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = (TextView) headerView.findViewById(R.id.txtUser);
        navUsername.setText(filaUsuario.getString(1));
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
        bottomNavigationView.setSelectedItemId(R.id.catalogo);

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

        fragment = findViewById(R.id.fragment);
        fragment.setVisibility(View.INVISIBLE);
        view = findViewById(R.id.include);
        view.setVisibility(View.VISIBLE);

        asignarDatos();

        mAdapter.setOnItemClickListener(new AdapterProducto.OnItemClickListener(){
            @Override
            public void onAdd(int codigo) {

                Cursor f = db.rawQuery("select barcode from Producto where codigo = " + codigo, null);

                f.moveToFirst();

                FragmentTransaction transaction2;
                Fragment productoFragment = new ProductoFragment(f.getString(0));
                fragmentManager = getSupportFragmentManager();
                transaction2 = fragmentManager.beginTransaction();
                transaction2.replace(R.id.fragment, productoFragment);
                transaction2.commit();
                fragment.setVisibility(View.VISIBLE);
                view.setVisibility(View.INVISIBLE);
            }
        });

        productolistssecondary = productolists;
    }

    @Override
    public void onBackPressed() {
        if(mDrawerLayout.isDrawerOpen(GravityCompat.START)){
            mDrawerLayout.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
        }
    }

    public View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.imgbtnPlastico:
                    filterP("Plástico");
                    imgbtnPlastico.setOnClickListener(offClickListener);
                    imgbtnPapelCarton.setOnClickListener(onClickListener);
                    imgbtnPlastico.setBackground(getResources().getDrawable(R.color.colorPlastico));
                    txtvPlastico.setBackground(getResources().getDrawable(R.color.colorPlastico));
                    imgbtnPapelCarton.setBackground(getResources().getDrawable(R.color.colorPrimary));
                    txtvPapelCarton.setBackground(getResources().getDrawable(R.color.colorPrimary));
                    break;
                case R.id.imgbtnPapelCarton:
                    filterP("Papel/Carton");
                    imgbtnPapelCarton.setOnClickListener(offClickListener);
                    imgbtnPlastico.setOnClickListener(onClickListener);
                    imgbtnPapelCarton.setBackground(getResources().getDrawable(R.color.colorPapelCarton));
                    txtvPapelCarton.setBackground(getResources().getDrawable(R.color.colorPapelCarton));
                    imgbtnPlastico.setBackground(getResources().getDrawable(R.color.colorPrimary));
                    txtvPlastico.setBackground(getResources().getDrawable(R.color.colorPrimary));
                    break;
            }
        }
    };

    public View.OnClickListener offClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            filterP("");
            imgbtnPlastico.setOnClickListener(onClickListener);
            imgbtnPapelCarton.setOnClickListener(onClickListener);
            imgbtnPapelCarton.setBackground(getResources().getDrawable(R.color.colorPapelCarton));
            imgbtnPlastico.setBackground(getResources().getDrawable(R.color.colorPlastico));
            txtvPapelCarton.setBackground(getResources().getDrawable(R.color.colorPapelCarton));
            txtvPlastico.setBackground(getResources().getDrawable(R.color.colorPlastico));
        }
    };

    private void filterP(String text){
        productolistssecondary = new ArrayList<>();

        for(Productolist item : productolists){
            if(item.getCategoria().toLowerCase().contains(text.toLowerCase())){
                productolistssecondary.add(item);
            }
        }

        mAdapter.filterList(productolistssecondary);
        filterS(etxtBuscar.getText().toString());
    }

    private void filterS(String text){
        ArrayList<Productolist> filteredList = new ArrayList<>();

        for(Productolist item : productolistssecondary){
            if(item.getNombre().toLowerCase().contains(text.toLowerCase())){
                filteredList.add(item);
            }
        }

        mAdapter.filterList(filteredList);
    }

    void asignarDatos(){
        dbHelper helper = new dbHelper(this,"Usuario.sqlite", null, 1);
        SQLiteDatabase db = helper.getReadableDatabase();

        Cursor fila2 = db.rawQuery("select nombre, tipo_contenido, contenido, categoria, urlimagen, peso, codigo from Producto", null);
        fila2.moveToFirst();
        do {
            if(fila2.getInt(3) != 2 && fila2.getInt(3) != 4) {
                Cursor fila3 = db.rawQuery("select codigo, nombre from Categoria where codigo = " + fila2.getInt(3), null);
                fila3.moveToFirst();

                Productolist ayuda = new Productolist();

                ayuda.setNombre(fila2.getString(0));
                ayuda.setCodigo(fila2.getInt(6));
                ayuda.setPeso(fila2.getDouble(5));
                ayuda.setContenido(fila2.getDouble(2));
                ayuda.setUrlImage(fila2.getString(4));
                ayuda.setAbreviatura(fila2.getString(1));
                ayuda.setCategoria(fila3.getString(1));
                ayuda.setCodcontenido(fila3.getInt(0));

                productolists.add(ayuda);
            }
        } while (fila2.moveToNext());


        mAdapter = new AdapterProducto(productolists);
        recyclerView.setAdapter(mAdapter);

        db.close();
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
