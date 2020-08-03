package com.example.reciclemosdemo.Grafico;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.reciclemosdemo.Adicionales.dbHelper;
import com.example.reciclemosdemo.Entities.Bolsalist;
import com.example.reciclemosdemo.Entities.Probolsa;
import com.example.reciclemosdemo.Inicio.AdapterDetalle;
import com.example.reciclemosdemo.Inicio.BolsaActivity;
import com.example.reciclemosdemo.Inicio.CatalogoActivity;
import com.example.reciclemosdemo.Inicio.LoginActivity;
import com.example.reciclemosdemo.Inicio.MeInformoActivity;
import com.example.reciclemosdemo.Inicio.ScanBarCodeActivity;
import com.example.reciclemosdemo.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ProductByBolsa extends AppCompatActivity implements com.example.reciclemosdemo.Grafico.ListaProductoByBolsaAdapter.OnNoteListener , NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG ="PRODUCTOS";
    RecyclerView rclDetalle;
    private DrawerLayout mDrawerLayout;
    private ArrayList<Probolsa> dataset = new ArrayList<>();
    private com.example.reciclemosdemo.Grafico.RetrofitMain retrofit;
    private RecyclerView recyclerView;
    private com.example.reciclemosdemo.Grafico.ListaProductoByBolsaAdapter listBolsasAdapter;
    private ArrayList<TextView> textList = new ArrayList<>();
    private TextView txt_RecojoFecha;
    AdapterDetalle adapter;
    private TextView txt_CreadoFecha;
    private TextView txt_PesoTotal;
    private TextView txt_PuntosTotal;
    private RelativeLayout item_product;
    ArrayList<Bolsalist> listDetalle = new ArrayList<>();
    private TextView txt_Reciclador;
    private TextView txt_Observaciones;
    private Dialog myObservacionDialog;
    private SpannableString content = new SpannableString("SI");
    com.example.reciclemosdemo.Entities.CategoriaDivided plasticoDivided = new com.example.reciclemosdemo.Entities.CategoriaDivided();
    com.example.reciclemosdemo.Entities.CategoriaDivided vidrioDivided = new com.example.reciclemosdemo.Entities.CategoriaDivided();
    com.example.reciclemosdemo.Entities.CategoriaDivided metalDivided = new com.example.reciclemosdemo.Entities.CategoriaDivided();
    com.example.reciclemosdemo.Entities.CategoriaDivided papelDivided = new com.example.reciclemosdemo.Entities.CategoriaDivided();
    ArrayList<com.example.reciclemosdemo.Entities.CategoriaDivided> categoriaDivideds = new ArrayList<com.example.reciclemosdemo.Entities.CategoriaDivided>();

    private int plasticoCount, puntosPlastico,pesoPlastico ;
    private int papelCartonCount,puntosPapelCarton ,pesoPapelCarton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_by_bolsa);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mDrawerLayout = findViewById(R.id.drawer);
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle mToggle = new ActionBarDrawerToggle(this,mDrawerLayout,toolbar,R.string.open,R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();




        recyclerView = (RecyclerView) findViewById(R.id.recyclerProductos);
        listBolsasAdapter = new com.example.reciclemosdemo.Grafico.ListaProductoByBolsaAdapter(this);
        recyclerView.setAdapter(listBolsasAdapter);
        recyclerView.setHasFixedSize(true);
        item_product= findViewById(R.id.relativeProductByBolsa);
        txt_CreadoFecha = findViewById(R.id.txtCreadoFecha);
        txt_RecojoFecha = findViewById(R.id.txtRecoojoFecha);
        txt_PesoTotal = findViewById(R.id.txtPesoTotales);
        txt_PuntosTotal = findViewById(R.id.txtPuntosTotales);
        txt_Reciclador = findViewById(R.id.txtReciclador);
        txt_Observaciones = findViewById(R.id.txtObservaciones);
        textList.add(txt_CreadoFecha);
        textList.add(txt_RecojoFecha);
        textList.add(txt_PesoTotal);
        textList.add(txt_PuntosTotal);
        textList.add(txt_Reciclador);
        textList.add(txt_Observaciones);

        myObservacionDialog = new Dialog(this);
        myObservacionDialog.setContentView(R.layout.editable_profile);
        myObservacionDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        TextView txtObservacionesDialog = myObservacionDialog.findViewById(R.id.txtObservaciones);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(layoutManager);
        Bundle datos= getIntent().getExtras();
        int position= datos.getInt("posicion");
        retrofit = new com.example.reciclemosdemo.Grafico.RetrofitMain();
        textList.add(txtObservacionesDialog);


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


        try {
            getInfoOfProducts(String.valueOf(position),textList);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        getProductsByBolsa(String.valueOf(position));

        txt_Reciclador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), RecicladorActivity.class));
            }
        });

        if(txt_Observaciones.getText().equals(content))
            txt_Observaciones.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    myObservacionDialog.show();
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
                        startActivity(new Intent(getApplicationContext(), com.example.reciclemosdemo.Grafico.ListBolsas.class));
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

  /*  public void obtenerDatosProductByBolsa(String posicion, ArrayList<TextView> textViewsList) throws ParseException {
        dbHelper helper = new dbHelper(this,"Usuario.sqlite", null, 1);
        SQLiteDatabase db = helper.getReadableDatabase();
        String query = "select codigo , creadoFecha , puntuacion  , recojoFecha ,observaciones  from Bolsa where codigo = "+posicion;
        System.out.println(query);
        Cursor f = db.rawQuery(query,null);
        f.moveToFirst();
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.ENGLISH);
        if(f.getString(3).equals("null") || f.getString(3).equals(null)) {
            textViewsList.get(1).setText("Recojo : Pendiente");
        }else {
            Date date1 = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy",Locale.ENGLISH).parse(f.getString(3));
            Date recojo = date1;
            String recojoFecha;
            recojoFecha = formatter.format(recojo);
            textViewsList.get(1).setText("Recojo : "+recojoFecha);
        }
        Date creado = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH).parse(f.getString(1));
        Date creadoDate = creado;
        String creadoFecha;
        creadoFecha = formatter.format(creadoDate);
        textViewsList.get(0).setText("Creada : "+creadoFecha);
        String recicladorQuery = "select nombre , apellido  from Reciclador";
        System.out.println(query);
        Cursor reciclerF = db.rawQuery(recicladorQuery,null);
        reciclerF.moveToFirst();
        SpannableString reciclador = new SpannableString(reciclerF.getString(0) +" "+ reciclerF.getString(1));
        reciclador.setSpan(new UnderlineSpan(), 0, reciclador.length(), 0);
        textViewsList.get(4).setText(reciclador);
        if(f.getString(4).equals("null") || f.getString(4).equals(null)) {
            textViewsList.get(5).setText("NO");
            textViewsList.get(6).setText("No hay observaciones");
        }

        else {
            content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
            textViewsList.get(5).setText(content);
            textViewsList.get(6).setText(f.getString(4));
        }

        String query2 = "select tendenciaTipo  ,productoTipo ,cantidad ,peso ,puntuacion ,bolsa   from Contador where bolsa = "+ posicion + " and tendenciaTipo = 'LastBolsas'";
        Cursor f1 = db.rawQuery(query2,null);
        f1.moveToFirst();
        do {
            if ( f1.getString(1).equals("Plastico")  && f1.getInt(2) > 0) {
                plasticoDivided.setCantidad( f1.getInt(2));
                plasticoDivided.setPeso( f1.getInt(3));
                plasticoDivided.setPuntos( f1.getInt(4));
                plasticoDivided.setTipo("Plastico");
                categoriaDivideds.add(plasticoDivided);
            }
           if ( f1.getString(1).equals("Vidrio")  && f1.getInt(2) > 0) {
                vidrioDivided.setCantidad( f1.getInt(2));
                vidrioDivided.setPeso( f1.getInt(3));
                vidrioDivided.setPuntos( f1.getInt(4));
                vidrioDivided.setTipo("Vidrio");
                categoriaDivideds.add(vidrioDivided);
            }
            if ( f1.getString(1).equals("Metal")  && f1.getInt(2) > 0) {
                metalDivided.setCantidad( f1.getInt(2));
                metalDivided.setPeso( f1.getInt(3));
                metalDivided.setPuntos( f1.getInt(4));
                metalDivided.setTipo("Metal");
                categoriaDivideds.add(metalDivided);
            }
            if ( f1.getString(1).equals("Papel")  && f1.getInt(2) > 0) {
                papelDivided.setCantidad( f1.getInt(2));
                papelDivided.setPeso( f1.getInt(3));
                papelDivided.setPuntos( f1.getInt(4));
                papelDivided.setTipo("Papel/Carton");
                categoriaDivideds.add(papelDivided);
            }
        }while(f1.moveToNext());
        listBolsasAdapter.adicionarProductoBolsas(categoriaDivideds, ProductByBolsa.this);

        textViewsList.get(2).setText(Integer.toString((papelDivided.getPeso()+metalDivided.getPeso()+plasticoDivided.getPeso()+vidrioDivided.getPeso())) +" g");
        textViewsList.get(3).setText(Integer.toString(papelDivided.getPuntos()+metalDivided.getPuntos()+plasticoDivided.getPuntos()+vidrioDivided.getPuntos()));
    }
*/

    public void getInfoOfProducts(String posicion, ArrayList<TextView> textViewsList) throws ParseException
    {
        initialCounter();
        dbHelper helper = new dbHelper(this,"Usuario.sqlite", null, 1);
        SQLiteDatabase db = helper.getReadableDatabase();
        String query = "select codigo , creadoFecha , puntuacion  , recojoFecha ,observaciones  from Bolsa where codigo = "+posicion;
        System.out.println(query);
        Cursor f = db.rawQuery(query,null);
        f.moveToFirst();
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.ENGLISH);
        if(f.getString(3).equals("null") || f.getString(3).equals(null)) {
            textViewsList.get(1).setText("Recojo : Pendiente");
        }else {
            Date date1 = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy",Locale.ENGLISH).parse(f.getString(3));
            Date recojo = date1;
            String recojoFecha;
            recojoFecha = formatter.format(recojo);
            textViewsList.get(1).setText("Recojo : "+recojoFecha);
        }
        Date creado = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH).parse(f.getString(1));
        Date creadoDate = creado;
        String creadoFecha;
        creadoFecha = formatter.format(creadoDate);
        textViewsList.get(0).setText("Creada : "+creadoFecha);
        String recicladorQuery = "select nombre , apellido  from Reciclador";
        System.out.println(query);
        Cursor reciclerF = db.rawQuery(recicladorQuery,null);
        reciclerF.moveToFirst();
        SpannableString reciclador = new SpannableString(reciclerF.getString(0) +" "+ reciclerF.getString(1));
        reciclador.setSpan(new UnderlineSpan(), 0, reciclador.length(), 0);
        textViewsList.get(4).setText(reciclador);
        if(f.getString(4).equals("null") || f.getString(4).equals(null)) {
            textViewsList.get(5).setText("NO");
            textViewsList.get(6).setText("No hay observaciones");
        }

        else {
            content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
            textViewsList.get(5).setText(content);
            textViewsList.get(6).setText(f.getString(4));
        }
        Cursor fila = db.rawQuery("select bolsa, cantidad, codigo, peso, producto, puntuacion from Probolsa where bolsa = " + f.getInt(0), null);
        fila.moveToFirst();

        System.out.println(fila.getString(1));


        do {
            Cursor fila2 = db.rawQuery("select nombre, tipo_contenido, contenido, categoria, urlimagen from Producto where codigo = " + fila.getInt(4), null);
            fila2.moveToFirst();

            Cursor fila3 = db.rawQuery("select codigo, nombre from Categoria where codigo = " + fila2.getInt(3), null);
            fila3.moveToFirst();

            if(fila3.getString(1).equals("Plástico") ) {
                plasticoCount += fila.getInt(1);
                puntosPlastico += fila.getInt(5);
                pesoPlastico += fila.getDouble(3);

            }else if(fila3.getString(1).equals("Papel/Carton"))
            {
                papelCartonCount += fila.getInt(1);
                puntosPapelCarton += fila.getInt(5);
                pesoPapelCarton += fila.getDouble(3);
            }
            System.out.println("PapelCarton" + papelCartonCount);
        } while (fila.moveToNext());

        if(plasticoCount>0){
            plasticoDivided.setCantidad( plasticoCount);
            plasticoDivided.setPeso( pesoPlastico);
            plasticoDivided.setPuntos( puntosPlastico);
            plasticoDivided.setTipo("Plastico");
            categoriaDivideds.add(plasticoDivided);
        }
        if(papelCartonCount>0)
        {
            papelDivided.setCantidad(papelCartonCount);
            papelDivided.setPeso( pesoPapelCarton);
            papelDivided.setPuntos( puntosPapelCarton);
            papelDivided.setTipo("Papel/Carton");
            categoriaDivideds.add(papelDivided);
        }


        listBolsasAdapter.adicionarProductoBolsas(categoriaDivideds, ProductByBolsa.this);

        textViewsList.get(2).setText(Integer.toString((papelDivided.getPeso()+metalDivided.getPeso()+plasticoDivided.getPeso()+vidrioDivided.getPeso())) +" g");
        textViewsList.get(3).setText(Integer.toString(papelDivided.getPuntos()+metalDivided.getPuntos()+plasticoDivided.getPuntos()+vidrioDivided.getPuntos()));



    }

    public void getProductsByBolsa(String posicion){
        rclDetalle = findViewById(R.id.rclDetalle);
        rclDetalle.setLayoutManager(new LinearLayoutManager(this));
        dbHelper helper = new dbHelper(this,"Usuario.sqlite", null, 1);
        SQLiteDatabase db = helper.getReadableDatabase();

        String query = "select codigo from Bolsa where codigo = " + posicion;
        Cursor f = db.rawQuery(query, null);

        f.moveToFirst();

        Cursor fila = db.rawQuery("select bolsa, cantidad, codigo, peso, producto, puntuacion from Probolsa where bolsa = " + f.getInt(0), null);
        fila.moveToFirst();

        System.out.println(fila.getString(1));


        do {
            Cursor fila2 = db.rawQuery("select nombre, tipo_contenido, contenido, categoria, urlimagen from Producto where codigo = " + fila.getInt(4), null);
            fila2.moveToFirst();

            Cursor fila3 = db.rawQuery("select codigo, nombre from Categoria where codigo = " + fila2.getInt(3), null);
            fila3.moveToFirst();

            if(fila3.getString(1).equals("Plástico") || fila3.getString(1).equals("Papel/Carton") ) {
                Bolsalist ayuda = new Bolsalist();

                ayuda.setNombre(fila2.getString(0));
                ayuda.setCodigo(fila.getInt(2));
                ayuda.setPeso(fila.getDouble(3));
                ayuda.setCantidad(fila.getInt(1));
                ayuda.setPuntos(fila.getInt(5));
                ayuda.setContenido(fila2.getDouble(2));
                ayuda.setUrlImage(fila2.getString(4));
                ayuda.setAbreviatura(fila2.getString(1));
                ayuda.setCategoria(fila3.getString(1));
                ayuda.setCodcontenido(fila3.getInt(0));

                listDetalle.add(ayuda);
            }
        } while (fila.moveToNext());

        adapter = new AdapterDetalle(listDetalle,"view");
        rclDetalle.setAdapter(adapter);

        db.close();

    }

    public void gettingCount(Probolsa probolsas)
    {
        if (probolsas.getProducto().getCategoria().getNombre().equals("Plastico")) {
            plasticoCount += probolsas.getCantidad();
            puntosPlastico += probolsas.getPuntuacion();
            pesoPlastico += probolsas.getPeso();
        }
        else {
            papelCartonCount += probolsas.getCantidad();
            puntosPapelCarton += probolsas.getPuntuacion();
            pesoPapelCarton += probolsas.getPeso();
        }
    }



    public void initialCounter(){
        plasticoCount=0;
        pesoPlastico=0;
        puntosPlastico=0;
    /*    vidrioCount=0;
        pesoVidrio=0;
        puntosVidrio=0;*/
        papelCartonCount=0;
        pesoPapelCarton=0;
        puntosPapelCarton=0;
      /*  metalCount=0;
        pesoMetal=0;
        puntosMetal=0;
        residuosTotal=0;*/
    }

    @Override
    public void oneNoteClick(int position) {
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