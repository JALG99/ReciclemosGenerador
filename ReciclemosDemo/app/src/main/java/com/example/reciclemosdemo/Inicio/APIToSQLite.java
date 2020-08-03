package com.example.reciclemosdemo.Inicio;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.example.reciclemosdemo.Adicionales.dbHelper;
import com.example.reciclemosdemo.Entities.Bolsa;
import com.example.reciclemosdemo.Entities.JsonPlaceHolderApi;
import com.example.reciclemosdemo.Entities.Probolsa;
import com.example.reciclemosdemo.Entities.Producto;
import com.example.reciclemosdemo.Entities.Reciclador;
import com.example.reciclemosdemo.Entities.Usuario;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static java.util.stream.Collectors.toMap;

public class APIToSQLite {

    dbHelper helper;
    SQLiteDatabase db;
    Retrofit retrofit;
    Context context;
    private double plasticoCount,pesoPlastico,puntosPlastico;
    private double vidrioCount,pesoVidrio,puntosVidrio;
    private double papelCartonCount,pesoPapelCarton,puntosPapelCarton;
    private double metalCount,pesoMetal,puntosMetal;
    private  double pesoCount,bolsasCount,puntosCount;
    private int residuosTotal;
    private int [] yAxisDataMonth= {0,0,0,0,0,0,0};
    private int [] cant_residuos_mensuales_plastico= {0,0,0,0,0,0,0,0,0,0,0,0};
    private int [] cant_residuos_mensuales_papelcarton= {0,0,0,0,0,0,0,0,0,0,0,0};
    private int [] yAxisDataYearBolsa= {0,0,0,0,0,0,0,0,0,0,0,0};
    private int [] cant_puntos_mensuales_plastico= {0,0,0,0,0,0,0,0,0,0,0,0};
    private int [] cant_puntos_mensuales_papelcarton= {0,0,0,0,0,0,0,0,0,0,0,0};
    private int [] xAxisDataMonthPuntos =  {0,0,0,0,0,0,0};
    private int [] yAxisDataMonthBoslsas = {0,0,0,0,0,0,0};
    private double [] pesosbyMes = {0,0,0,0,0,0,0,0,0,0,0,0};
    private int [] plasticobyMes = {0,0,0,0,0,0,0,0,0,0,0,0};
    private int [] papelbyMes = {0,0,0,0,0,0,0,0,0,0,0,0};
    private Map<Integer,Double> plasticoEnero = new HashMap<>();
    private Map<Integer,Double> plasticoFebrero = new HashMap<>();
    private Map<Integer,Double> plasticoMarzo = new HashMap<>();
    private Map<Integer,Double> plasticoAbril = new HashMap<>();
    private Map<Integer,Double> plasticoMayo = new HashMap<>();
    private Map<Integer,Double> plasticoJunio = new HashMap<>();
    private Map<Integer,Double> plasticoJulio = new HashMap<>();
    private Map<Integer,Double> plasticoAgosto = new HashMap<>();
    private Map<Integer,Double> plasticoSetiembre = new HashMap<>();
    private Map<Integer,Double> plasticoOctubre = new HashMap<>();
    private Map<Integer,Double> plasticoNoviembre = new HashMap<>();
    private Map<Integer,Double> plasticoDiciembre = new HashMap<>();
    private Map<Integer,Double> papelEnero = new HashMap<>();
    private Map<Integer,Double> papelFebrero = new HashMap<>();
    private Map<Integer,Double> papelMarzo = new HashMap<>();
    private Map<Integer,Double> papelAbril = new HashMap<>();
    private Map<Integer,Double> papelMayo = new HashMap<>();
    private Map<Integer,Double> papelJunio = new HashMap<>();
    private Map<Integer,Double> papelJulio = new HashMap<>();
    private Map<Integer,Double> papelAgosto = new HashMap<>();
    private Map<Integer,Double> papelSetiembre = new HashMap<>();
    private Map<Integer,Double> papelOctubre = new HashMap<>();
    private Map<Integer,Double> papelNoviembre = new HashMap<>();
    private Map<Integer,Double> papelDiciembre = new HashMap<>();
    private  Set<Integer> bolsasLast = new HashSet<>();
    private  Set<Integer> bolsasWeek = new HashSet<>();


    public APIToSQLite(Context context,String tipo){
        helper = new dbHelper(context,"Usuario.sqlite", null, 1);
        db = helper.getWritableDatabase();
        if(!tipo.equals("actualizar")) {
            helper.DropCreate(db);
        }
        else{
            helper.UpdateTable(db);
        }
        this.context = context;
        retrofit = new Retrofit.Builder()
                .baseUrl("https://recyclerapiresttdp.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public void InsertUsuario(String email, String password) throws IOException, InterruptedException {
        JsonPlaceHolderApi jsonPlaceHolderApi=retrofit.create(JsonPlaceHolderApi.class);
        Call<Usuario> call=jsonPlaceHolderApi.getUsuarioByEmailPassword("/usuario/correo/" + email + "/" + password);
        Response<Usuario> response = call.execute();
        String[] ayuda = new String[13];
        ayuda[0] = response.body().getNombre();
        ayuda[1] = response.body().getApellido();
        ayuda[2] = response.body().getCondominio().getNombre();
        ayuda[3] = response.body().getDireccion();
        ayuda[4] = response.body().getDni();
        ayuda[5] = response.body().getEmail();
        ayuda[6] = response.body().getFechanacimiento();
        ayuda[7] = response.body().getSexo().getNombre();
        ayuda[8] = response.body().getTelefono();
        ayuda[9] = response.body().getCodigo().toString();
        ayuda[10] = response.body().getCondominio().getDistrito().getNombre();
        ayuda[11] = response.body().getCondominio().getDireccion();
        ayuda[12] = response.body().getCondominio().getDistrito().getDepartamento().getNombre();
        String query = "insert into Usuario (nombre, apellido, condominio, direccion, dni, email, fecha_Nacimiento, sexo, telefono, codigo,distrito_name,condominio_direccion,departamento_name) " +
                "values ('" + ayuda[0] + "', '" + ayuda[1] + "', '" + ayuda[2] + "', '" + ayuda[3] + "', '" + ayuda[4] + "', '" + ayuda[5] + "', '"
                + ayuda[6] + "', '" + ayuda[7] + "', '" + ayuda[8] + "', '" + ayuda[9] + "', '" + ayuda[10] + "', '" + ayuda[11] + "', '" + ayuda[12] + "')";
        db.execSQL(query);
        System.out.println(query);


        Log.e("TAG","onResponse:" + response.toString());
        /*call.enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                if(response.isSuccessful()) {
                    String[] ayuda = new String[10];
                    ayuda[0] = response.body().getNombre();
                    ayuda[1] = response.body().getApellido();
                    ayuda[2] = response.body().getCondominio().getNombre();
                    ayuda[3] = response.body().getDireccion();
                    ayuda[4] = response.body().getDni();
                    ayuda[5] = response.body().getEmail();
                    ayuda[6] = response.body().getFechanacimiento();
                    ayuda[7] = response.body().getSexo().getNombre();
                    ayuda[8] = response.body().getTelefono();
                    ayuda[9] = response.body().getCodigo().toString();
                    String query = "insert into Usuario (nombre, apellido, condominio, direccion, dni, email, fecha_Nacimiento, sexo, telefono, codigo) " +
                            "values ('" + ayuda[0] + "', '" + ayuda[1] + "', '" + ayuda[2] + "', '" + ayuda[3] + "', '" + ayuda[4] + "', '" + ayuda[5] + "', '"
                            + ayuda[6] + "', '" + ayuda[7] + "', '" + ayuda[8] + "', " + ayuda[9] + ")";
                    db.execSQL(query);
                    System.out.println(query);
                    Log.e("TAG","onResponse:" + response.toString());
                }else{
                    Log.e("TAG","onResponse:" + response.toString());
                }
            }

            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {
                Log.e("TAG","onFailure:" + t.getMessage());
            }
        });*/
    }

    public void InsertReciclador()throws  IOException, InterruptedException{
        dbHelper helper = new dbHelper(context,"Usuario.sqlite", null, 1);
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor f1 = db.rawQuery("select codigo from Usuario ",null);
        f1.moveToFirst();
        String codigoUsuario = f1.getString(0);
        JsonPlaceHolderApi jsonPlaceHolderApi=retrofit.create(JsonPlaceHolderApi.class);
        Call<Reciclador> call=jsonPlaceHolderApi.getReciclador("/reciclador/usuario/"+codigoUsuario);
        Response<Reciclador> response = call.execute();
        Reciclador p = response.body();
        String query = "insert into Reciclador (codigo,distrito ,asociacion ,zona , password ,salt ,apellido ,direccion ,dni ,email ,fecha_Nacimiento ,nombre ,codFormalizado ,celular ,imagen) " +
                "values ("+p.getCodigo()+",'"+p.getDistrito()+"','"+p.getAsociacion().getNombre()+"','"+p.getZona()+"','"+p.getPassword()+"','"+p.getSalt()+"','"+p.getApellido()+"','"+p.getDireccion()+"',+'"+
                p.getDni()+"','"+p.getEmail()+"','"+p.getFecha_Nacimiento()+"','"+p.getNombre()+"','"+p.getCodFormalizado()+"','"+p.getCelular()+"','"+p.getRecilador_Imagen()+"')";
        db.execSQL(query);
        System.out.println(query);
        db.close();
        Log.e("TAG","onResponse:" + response.toString());
    }

    public void InsertProductos() throws IOException, InterruptedException {
        JsonPlaceHolderApi jsonPlaceHolderApi=retrofit.create(JsonPlaceHolderApi.class);
        Call<List<Producto>> call=jsonPlaceHolderApi.getProductos("/producto");
        Response<List<Producto>> response = call.execute();
        for (Producto p: response.body()) {
            if(p.getCategoria().getNombre().equals("Plástico")) {
                plasticoEnero.put(p.getCodigo(), 0.0);
                plasticoFebrero.put(p.getCodigo(), 0.0);
                plasticoMarzo.put(p.getCodigo(), 0.0);
                plasticoAbril.put(p.getCodigo(), 0.0);
                plasticoMayo.put(p.getCodigo(), 0.0);
                plasticoJunio.put(p.getCodigo(), 0.0);
                plasticoJulio.put(p.getCodigo(), 0.0);
                plasticoAgosto.put(p.getCodigo(), 0.0);
                plasticoSetiembre.put(p.getCodigo(), 0.0);
                plasticoOctubre.put(p.getCodigo(), 0.0);
                plasticoNoviembre.put(p.getCodigo(), 0.0);
                plasticoDiciembre.put(p.getCodigo(), 0.0);
            }
            else
                if(p.getCategoria().getNombre().equals("Papel/Cartón")) {
                    papelEnero.put(p.getCodigo(), 0.0);
                    papelFebrero.put(p.getCodigo(), 0.0);
                    papelMarzo.put(p.getCodigo(), 0.0);
                    papelAbril.put(p.getCodigo(), 0.0);
                    papelMayo.put(p.getCodigo(), 0.0);
                    papelJunio.put(p.getCodigo(), 0.0);
                    papelJulio.put(p.getCodigo(), 0.0);
                    papelAgosto.put(p.getCodigo(), 0.0);
                    papelSetiembre.put(p.getCodigo(), 0.0);
                    papelOctubre.put(p.getCodigo(), 0.0);
                    papelNoviembre.put(p.getCodigo(), 0.0);
                    papelDiciembre.put(p.getCodigo(), 0.0);
                }
            String query = "insert into Producto (barcode , categoria , codigo , contenido , descripcion , nombre , peso , tipo_contenido , urlimagen) " +
                    "values ('" + p.getBarcode() + "', " + p.getCategoria().getCodigo() + ", " + p.getCodigo() + ", " + p.getContenido() + ", " + p.getDescripcion()
                    + ", '" + p.getNombre() + "', " + p.getPeso() + ", '" + p.getTipo_Contenido().getAbreviatura() + "', '" + p.getUrlImage() + "')";
            db.execSQL(query);
            System.out.println(query);
        }
        Log.e("TAG","onResponse:" + response.toString());
        /*call.enqueue(new Callback<List<Producto>>() {
            @Override
            public void onResponse(Call<List<Producto>> call, Response<List<Producto>> response) {
                if(response.isSuccessful()) {
                    for (Producto p: response.body()) {
                        String query = "insert into Producto (barcode , categoria , codigo , contenido , descripcion , nombre , peso , tipo_contenido , urlimagen) " +
                                "values ('" + p.getBarcode() + "', " + p.getCategoria().getCodigo() + ", " + p.getCodigo() + ", " + p.getContenido() + ", " + p.getDescripcion()
                                + ", '" + p.getNombre() + "', " + p.getPeso() + ", '" + p.getTipo_Contenido().getAbreviatura() + "', " + p.getUrlimagen() + ")";
                        db.execSQL(query);
                        System.out.println(query);
                    }
                    Log.e("TAG","onResponse:" + response.toString());
                }else{
                    Log.e("TAG","onResponse:" + response.toString());
                }
            }

            @Override
            public void onFailure(Call<List<Producto>> call, Throwable t) {
                Log.e("TAG","onFailure:" + t.getMessage());
            }
        });*/
    }

  /*  public void obtenerDatosProductByBolsa() throws IOException, InterruptedException{
        initialCounter();
        dbHelper helper = new dbHelper(context,"Usuario.sqlite", null, 1);
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor f1 = db.rawQuery("select codigo from LastBolsas ",null);
        int codigoBolsa=0;
        if(f1.moveToFirst()){
            do {
                codigoBolsa = f1.getInt(0);
                JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);
                Call<List<Probolsa>> call = jsonPlaceHolderApi.getProductoByIdBolsa("probolsa/bolsa/" + codigoBolsa);
                Response<List<Probolsa>> response = call.execute();
                for (Probolsa probolsas : response.body()) {
                    if (probolsas.getProducto().getCategoria().getNombre().equals("Plastico")) {
                        plasticoCount += probolsas.getCantidad();
                        puntosPlastico += probolsas.getPuntuacion();
                        pesoPlastico += probolsas.getPeso();
                    } /*else if (probolsas.getProducto().getCategoria().getNombre().equals("Vidrio")) {
                        vidrioCount += probolsas.getCantidad();
                        puntosVidrio += probolsas.getPuntuacion();
                        pesoVidrio += probolsas.getPeso();
                    } else if (probolsas.getProducto().getCategoria().getNombre().equals("Metal")) {
                        metalCount += probolsas.getCantidad();
                        puntosMetal += probolsas.getPuntuacion();
                        pesoMetal += probolsas.getPeso();
                    } else {
                        papelCartonCount += probolsas.getCantidad();
                        puntosPapelCarton += probolsas.getPuntuacion();
                        pesoPapelCarton += probolsas.getPeso();
                    }
                    String query = "insert into LastProbolsas (codigo,bolsa) " +
                            "values (" + probolsas.getCodigo() + ","+probolsas.getBolsa().getCodigo()+")";
                    db.execSQL(query);
                    System.out.println(query);
                }
                generateQuery("LastBolsas", "Plastico", plasticoCount, pesoPlastico, puntosPlastico, codigoBolsa);
                generateQuery("LastBolsas", "Vidrio", vidrioCount, pesoVidrio, puntosVidrio, codigoBolsa);
                generateQuery("LastBolsas", "Papel", papelCartonCount, pesoPapelCarton, puntosPapelCarton,codigoBolsa);
                generateQuery("LastBolsas", "Metal", metalCount, pesoMetal, puntosMetal, codigoBolsa);
                initialCounter();

            }while(f1.moveToNext()); }
    }*/

    public void InsertBolsas() throws IOException, InterruptedException {
        dbHelper helper = new dbHelper(context, "Usuario.sqlite", null, 1);
        SQLiteDatabase db = helper.getWritableDatabase();

        Cursor fila = db.rawQuery("select codigo from Usuario", null);

        System.out.println(fila.getColumnNames().toString());

        fila.moveToFirst();

        String codigo = fila.getString(0);

        JsonPlaceHolderApi jsonPlaceHolderApi=retrofit.create(JsonPlaceHolderApi.class);
        Call<List<Bolsa>> call=jsonPlaceHolderApi.getBolsasByUsuario("/bolsa/usuario/" + codigo);
        Response<List<Bolsa>> response = call.execute();
        for (Bolsa p: response.body()) {
            if(p.getCreadoFecha() == null){
                String query = "insert into Bolsa (codigo, activa, creadoFecha, puntuacion, qrcode, recojoFecha) " +
                        "values (" + p.getCodigo() + ", 'true', " + null + ", " + p.getPuntuacion() + ", " + null
                        + ", " + null + ")";
                db.execSQL(query);
                System.out.println(query);
            } else{
                String query = "insert into Bolsa (codigo, activa, creadoFecha, puntuacion, qrcode, recojoFecha,observaciones) " +
                        "values (" + p.getCodigo() + ", '" + p.getActiva() + "', '" + p.getCreadoFecha() + "', " + p.getPuntuacion() + ", " + p.getQrCode().getCodigo()
                        + ", '" + p.getRecojoFecha() + "','"+p.getObservaciones()+"')";
                db.execSQL(query);
                System.out.println(query);
            }
        }
        Log.e("TAG","onResponse:" + response.toString());
    }

    public void InsertProbolsa() throws IOException, InterruptedException {
        dbHelper helper = new dbHelper(context, "Usuario.sqlite", null, 1);
        SQLiteDatabase db = helper.getWritableDatabase();

        Cursor fila = db.rawQuery("select codigo from Usuario", null);

        fila.moveToFirst();

        String codigo = fila.getString(fila.getColumnIndex("codigo"));

        JsonPlaceHolderApi jsonPlaceHolderApi=retrofit.create(JsonPlaceHolderApi.class);
        Call<List<Probolsa>> call=jsonPlaceHolderApi.getProbolsaByUsuario("/probolsa/usuario/" + codigo);
        Response<List<Probolsa>> response = call.execute();
        /*call.enqueue(new Callback<List<Probolsa>>() {
            @Override
            public void onResponse(Call<List<Probolsa>> call, Response<List<Probolsa>> response) {
                if(response.isSuccessful()) {*/
        for (Probolsa p: response.body()) {
            String query = "insert into Probolsa (bolsa, cantidad, codigo, peso, producto, puntuacion) " +
                    "values (" + p.getBolsa().getCodigo() + ", " + p.getCantidad() + ", " + p.getCodigo() + ", "
                    + p.getPeso() + ", " + p.getProducto().getCodigo() + ", " + p.getPuntuacion() + ")";
            db.execSQL(query);
            System.out.println(query);
        }
        Log.e("TAG","onResponse:" + response.toString());
                /*}else{
                    Log.e("TAG","onResponse:" + response.toString());
                }
            }

            @Override
            public void onFailure(Call<List<Probolsa>> call, Throwable t) {
                Log.e("TAG","onFailure:" + t.getMessage());
            }
        });*/
    }

    public void insertBolsasByMonthOrWeek(String urlDate) throws IOException, InterruptedException{
        initialCounter();
        dbHelper helper = new dbHelper(context, "Usuario.sqlite", null, 1);
        SQLiteDatabase db = helper.getWritableDatabase();

        Cursor fila = db.rawQuery("select codigo from Usuario", null);

        fila.moveToFirst();
        int valor =0;
        String codigo = fila.getString(fila.getColumnIndex("codigo"));
        JsonPlaceHolderApi jsonPlaceHolderApi=retrofit.create(JsonPlaceHolderApi.class);
        Call<List<Probolsa>> call=jsonPlaceHolderApi.getBolsasByDate("probolsa/"+urlDate+codigo);
        Response<List<Probolsa>> response = call.execute();
        for(Probolsa bolsasbydate: response.body()){
            valor++;
            if (bolsasbydate.getBolsa().getRecojoFecha() != null) {
                if (bolsasbydate.getProducto().getCategoria().getNombre().equals("Plastico") || bolsasbydate.getProducto().getCategoria().getNombre().equals("Papel/Carton")) {
                    bolsasWeek.add(bolsasbydate.getBolsa().getCodigo());
                    if (urlDate.equals("bolsasWeek/") || urlDate.equals("bolsasMonth/")) {
                        Date dia = bolsasbydate.getBolsa().getRecojoFecha();
                        SimpleDateFormat simpleDateformat = new SimpleDateFormat("EEEE", Locale.ENGLISH);
                        if (simpleDateformat.format(dia).equals("Monday")) {
                            yAxisDataMonth[0] += 1;
                            xAxisDataMonthPuntos[0] += bolsasbydate.getPuntuacion();
                        } else if (simpleDateformat.format(dia).equals("Tuesday")) {
                            yAxisDataMonth[1] += 1;
                            xAxisDataMonthPuntos[1] += bolsasbydate.getPuntuacion();
                        } else if (simpleDateformat.format(dia).equals("Wednesday")) {
                            yAxisDataMonth[2] += 1;
                            xAxisDataMonthPuntos[2] += bolsasbydate.getPuntuacion();
                        } else if (simpleDateformat.format(dia).equals("Thursday")) {
                            yAxisDataMonth[3] += 1;
                            xAxisDataMonthPuntos[3] += bolsasbydate.getPuntuacion();
                        } else if (simpleDateformat.format(dia).equals("Friday")) {
                            yAxisDataMonth[4] += 1;
                            xAxisDataMonthPuntos[4] += bolsasbydate.getPuntuacion();
                        } else if (simpleDateformat.format(dia).equals("Saturday")) {
                            yAxisDataMonth[5] += 1;
                            xAxisDataMonthPuntos[5] += bolsasbydate.getPuntuacion();
                        } else {
                            yAxisDataMonth[6] += 1;
                            xAxisDataMonthPuntos[6] += bolsasbydate.getPuntuacion();
                        }
                        addingValuestoText(bolsasbydate);
                    }
                }
            }
        }
        if(valor>0) {
            generateQuery("Semana", "Plastico", plasticoCount, pesoPlastico, puntosPlastico,0);
            generateQuery("Semana", "Vidrio", vidrioCount, pesoVidrio, puntosVidrio,0);
            generateQuery("Semana", "Papel", papelCartonCount, pesoPapelCarton, puntosPapelCarton,0);
            generateQuery("Semana", "Metal", metalCount, pesoMetal, puntosMetal,0);
            String query = "insert into DatosDiarios(tipo,lunes,martes,miercoles,jueves,viernes,sabado,domingo) " +
                    "values ('Semana', " + yAxisDataMonth[0] + ", " + yAxisDataMonth[1] + ", "
                    + yAxisDataMonth[2] + ", " + yAxisDataMonth[3] + ", " + yAxisDataMonth[4] + "," + yAxisDataMonth[5] + "," + yAxisDataMonth[6] + ")";
            db.execSQL(query);
            System.out.println(query);

            String query2 = "insert into DatosDiarios(tipo,lunes,martes,miercoles,jueves,viernes,sabado,domingo) " +
                    "values ('puntos', " + xAxisDataMonthPuntos[0] + ", " + xAxisDataMonthPuntos[1] + ", "
                    + xAxisDataMonthPuntos[2] + ", " + xAxisDataMonthPuntos[3] + ", " + xAxisDataMonthPuntos[4] + "," + xAxisDataMonthPuntos[5] + "," + xAxisDataMonthPuntos[6] + ")";
            db.execSQL(query2);
            System.out.println(query2);

        }

        Log.e("TAG","onResponse:" + response.toString());

        try {
            obtenerNumberBolsasByWeek();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void obtenerNumberBolsasByWeek() throws IOException, InterruptedException, ParseException {
        initialCounter();
        dbHelper helper = new dbHelper(context, "Usuario.sqlite", null, 1);
        SQLiteDatabase db = helper.getWritableDatabase();
        int valor = 0;
        for (Integer bolsasWeek : bolsasWeek) {
            Cursor f1 = db.rawQuery("select recojoFecha from Bolsa where codigo = "+ bolsasWeek,null);
            System.out.println(bolsasWeek);
            if(f1.moveToFirst()) {
                do {
                    valor++;
                    System.out.println("Entro");
                    if(!f1.getString(0).equals("null") || !f1.getString(0).equals(null)) {
                        String sDate1 = f1.getString(0);
                        Date dia =new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH).parse(sDate1);
                        System.out.println(dia.toString());
                        SimpleDateFormat simpleDateformat = new SimpleDateFormat("EEEE", Locale.ENGLISH);

                        if (simpleDateformat.format(dia).equals("Monday")) {
                            yAxisDataMonthBoslsas[0] += 1;
                        } else if (simpleDateformat.format(dia).equals("Tuesday"))
                            yAxisDataMonthBoslsas[1] += 1;
                        else if (simpleDateformat.format(dia).equals("Wednesday"))
                            yAxisDataMonthBoslsas[2] += 1;
                        else if (simpleDateformat.format(dia).equals("Thursday")) {
                            yAxisDataMonthBoslsas[3] += 1;
                        } else if (simpleDateformat.format(dia).equals("Friday"))
                            yAxisDataMonthBoslsas[4] += 1;
                        else if (simpleDateformat.format(dia).equals("Saturday"))
                            yAxisDataMonthBoslsas[5] += 1;
                        else
                            yAxisDataMonthBoslsas[6] += 1;
                    }

                } while (f1.moveToNext()) ;
            }
        }

        if(valor>0) {
            String query = "insert into DatosDiarios(tipo,lunes,martes,miercoles,jueves,viernes,sabado,domingo) " +
                    "values ('bolsas', " + yAxisDataMonthBoslsas[0] + ", " + yAxisDataMonthBoslsas[1] + ", "
                    + yAxisDataMonthBoslsas[2] + ", " + yAxisDataMonthBoslsas[3] + ", " + yAxisDataMonthBoslsas[4] + "," + yAxisDataMonthBoslsas[5] + "," + yAxisDataMonthBoslsas[6] + ")";
            db.execSQL(query);
            System.out.println(query);

        }

    }

    public void obtenerBolsasByDay()throws IOException, InterruptedException{
        pesoCount=0;
        bolsasCount=0;
        puntosCount=0;
        dbHelper helper = new dbHelper(context, "Usuario.sqlite", null, 1);
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor fila = db.rawQuery("select codigo from Usuario", null);
        fila.moveToFirst();
        String codigo = fila.getString(fila.getColumnIndex("codigo"));
        Set<Integer> bolsas = new HashSet<>();
        JsonPlaceHolderApi jsonPlaceHolderApi=retrofit.create(JsonPlaceHolderApi.class);
        Call<List<Probolsa>> call=jsonPlaceHolderApi.getBolsasByDate("probolsa/bolsasDay/"+codigo);
        Response<List<Probolsa>> response = call.execute();
        for(Probolsa probolsas : response.body()){
            bolsas.add(probolsas.getBolsa().getCodigo());
            pesoCount+=probolsas.getPeso();
            puntosCount+=probolsas.getPuntuacion();
        }
        String query = "insert into Contador (tendenciaTipo,cantidad ,peso ,puntuacion ) " +
                "values ('Dia', " +  bolsas.size()+ ", " + pesoCount + ", "
                + puntosCount + ")";
        db.execSQL(query);
        System.out.println(query);


        Log.e("TAG","onResponse:" + response.toString());


    }

    public void obtenerUltimasBolsas()throws IOException, InterruptedException{
        dbHelper helper = new dbHelper(context, "Usuario.sqlite", null, 1);
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor fila = db.rawQuery("select codigo from Usuario", null);
        fila.moveToFirst();
        int valor =0;
        String codigo = fila.getString(fila.getColumnIndex("codigo"));
        JsonPlaceHolderApi jsonPlaceHolderApi=retrofit.create(JsonPlaceHolderApi.class);
        Call<List<Bolsa>> call=jsonPlaceHolderApi.getBolsasByUsuario("bolsa/last/"+codigo);
        Response<List<Bolsa>> response = call.execute();
        for(Bolsa lastBolsas : response.body()){
            String query = "insert into LastBolsas (codigo) " +
                    "values (" + lastBolsas.getCodigo() + ")";
            db.execSQL(query);
            System.out.println(query);
        }

        Log.e("TAG","onResponse:" + response.toString());
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void obtenerBolsasByYear(String urlDate)throws IOException, InterruptedException{
        initialCounter();
        dbHelper helper = new dbHelper(context, "Usuario.sqlite", null, 1);
        SQLiteDatabase db = helper.getWritableDatabase();
        Set<Integer> bolsas = new HashSet<>();
        Cursor fila = db.rawQuery("select codigo from Usuario", null);

        fila.moveToFirst();
        int valor =0;
        String codigo = fila.getString(fila.getColumnIndex("codigo"));
        JsonPlaceHolderApi jsonPlaceHolderApi=retrofit.create(JsonPlaceHolderApi.class);
        Call<List<Probolsa>> call=jsonPlaceHolderApi.getBolsasByDate("probolsa/"+urlDate+codigo);
        Response<List<Probolsa>> response = call.execute();
        for (Probolsa bolsasbydate : response.body()) {

            valor++;
            if (bolsasbydate.getBolsa().getRecojoFecha() != null) {
                if (bolsasbydate.getProducto().getCategoria().getNombre().equals("Plástico") || bolsasbydate.getProducto().getCategoria().getNombre().equals("Papel/Cartón")) {
                    System.out.println(bolsasbydate.getProducto().getCategoria().getNombre());
                    bolsasLast.add(bolsasbydate.getBolsa().getCodigo());
                    Date dia = bolsasbydate.getBolsa().getRecojoFecha();
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(dia);
                    if (cal.get(Calendar.MONTH) == 0) {
                        pesosbyMes[0] += bolsasbydate.getPeso();
                        if(bolsasbydate.getProducto().getCategoria().getNombre().equals("Plástico")) {
                            cant_residuos_mensuales_plastico[0] += bolsasbydate.getCantidad();
                            cant_puntos_mensuales_plastico[0] += bolsasbydate.getPuntuacion();
                            plasticobyMes[0] += bolsasbydate.getPeso();
                            plasticoEnero.put(bolsasbydate.getProducto().getCodigo(), plasticoEnero.get(bolsasbydate.getProducto().getCodigo()) + bolsasbydate.getPeso() );

                        }
                        else {
                            cant_residuos_mensuales_papelcarton[0] += bolsasbydate.getCantidad();
                            cant_puntos_mensuales_papelcarton[0] += bolsasbydate.getPuntuacion();
                            papelbyMes[0] += bolsasbydate.getPeso();
                            papelEnero.put(bolsasbydate.getProducto().getCodigo(), papelEnero.get(bolsasbydate.getProducto().getCodigo()) + bolsasbydate.getPeso() );

                        }
                    } else if (cal.get(Calendar.MONTH) == 1) {
                        pesosbyMes[1] += bolsasbydate.getPeso();
                        if(bolsasbydate.getProducto().getCategoria().getNombre().equals("Plástico")) {
                            cant_residuos_mensuales_plastico[1] += bolsasbydate.getCantidad();
                            cant_puntos_mensuales_plastico[1] += bolsasbydate.getPuntuacion();
                            plasticobyMes[1] += bolsasbydate.getPeso();
                            plasticoFebrero.put(bolsasbydate.getProducto().getCodigo(), plasticoFebrero.get(bolsasbydate.getProducto().getCodigo()) + bolsasbydate.getPeso() );

                        }
                        else {
                            cant_residuos_mensuales_papelcarton[1] += bolsasbydate.getCantidad();
                            cant_puntos_mensuales_papelcarton[1] += bolsasbydate.getPuntuacion();
                            papelbyMes[1] += bolsasbydate.getPeso();
                            papelFebrero.put(bolsasbydate.getProducto().getCodigo(), papelFebrero.get(bolsasbydate.getProducto().getCodigo()) + bolsasbydate.getPeso() );


                        }
                    } else if (cal.get(Calendar.MONTH) == 2) {
                        pesosbyMes[2] += bolsasbydate.getPeso();
                        if(bolsasbydate.getProducto().getCategoria().getNombre().equals("Plástico")) {
                            cant_residuos_mensuales_plastico[2] += bolsasbydate.getCantidad();
                            cant_puntos_mensuales_plastico[2] += bolsasbydate.getPuntuacion();
                            plasticobyMes[2] += bolsasbydate.getPeso();
                            plasticoMarzo.put(bolsasbydate.getProducto().getCodigo(), plasticoMarzo.get(bolsasbydate.getProducto().getCodigo()) + bolsasbydate.getPeso() );

                        }
                        else {
                            cant_residuos_mensuales_papelcarton[2] += bolsasbydate.getCantidad();
                            cant_puntos_mensuales_papelcarton[2] += bolsasbydate.getPuntuacion();
                            papelbyMes[2] += bolsasbydate.getPeso();
                            papelMarzo.put(bolsasbydate.getProducto().getCodigo(), papelMarzo.get(bolsasbydate.getProducto().getCodigo()) + bolsasbydate.getPeso() );

                        }
                    } else if (cal.get(Calendar.MONTH) == 3) {
                        pesosbyMes[3] += bolsasbydate.getPeso();
                        if(bolsasbydate.getProducto().getCategoria().getNombre().equals("Plástico")) {
                            cant_residuos_mensuales_plastico[3] += bolsasbydate.getCantidad();
                            cant_puntos_mensuales_plastico[3] += bolsasbydate.getPuntuacion();
                            plasticobyMes[3] += bolsasbydate.getPeso();
                            plasticoAbril.put(bolsasbydate.getProducto().getCodigo(), plasticoAbril.get(bolsasbydate.getProducto().getCodigo()) + bolsasbydate.getPeso() );

                        }
                        else {
                            cant_residuos_mensuales_papelcarton[3] += bolsasbydate.getCantidad();
                            cant_puntos_mensuales_papelcarton[3] += bolsasbydate.getPuntuacion();
                            papelbyMes[3] += bolsasbydate.getPeso();
                            papelAbril.put(bolsasbydate.getProducto().getCodigo(), papelAbril.get(bolsasbydate.getProducto().getCodigo()) + bolsasbydate.getPeso());

                        }
                    } else if (cal.get(Calendar.MONTH) == 4) {
                        pesosbyMes[4] += bolsasbydate.getPeso();
                        if(bolsasbydate.getProducto().getCategoria().getNombre().equals("Plástico")) {
                            cant_residuos_mensuales_plastico[4] += bolsasbydate.getCantidad();
                            cant_puntos_mensuales_plastico[4] += bolsasbydate.getPuntuacion();
                            plasticobyMes[4] += bolsasbydate.getPeso();
                            plasticoMayo.put(bolsasbydate.getProducto().getCodigo(), plasticoMayo.get(bolsasbydate.getProducto().getCodigo()) + bolsasbydate.getPeso() );

                        }
                        else {
                            cant_residuos_mensuales_papelcarton[4] += bolsasbydate.getCantidad();
                            cant_puntos_mensuales_papelcarton[4] += bolsasbydate.getPuntuacion();
                            papelbyMes[4] += bolsasbydate.getPeso();
                            papelMayo.put(bolsasbydate.getProducto().getCodigo(), papelMayo.get(bolsasbydate.getProducto().getCodigo()) + bolsasbydate.getPeso() );

                        }
                    } else if (cal.get(Calendar.MONTH) == 5) {
                        pesosbyMes[5] += bolsasbydate.getPeso();
                        if(bolsasbydate.getProducto().getCategoria().getNombre().equals("Plástico")) {
                            cant_residuos_mensuales_plastico[5] += bolsasbydate.getCantidad();
                            cant_puntos_mensuales_plastico[5] += bolsasbydate.getPuntuacion();
                            plasticobyMes[5] += bolsasbydate.getPeso();
                            plasticoJunio.put(bolsasbydate.getProducto().getCodigo(), plasticoJunio.get(bolsasbydate.getProducto().getCodigo()) + bolsasbydate.getPeso() );

                        }
                        else {
                            cant_residuos_mensuales_papelcarton[5] += bolsasbydate.getCantidad();
                            cant_puntos_mensuales_papelcarton[5] += bolsasbydate.getPuntuacion();
                            papelbyMes[5] += bolsasbydate.getPeso();
                            papelJunio.put(bolsasbydate.getProducto().getCodigo(), papelJunio.get(bolsasbydate.getProducto().getCodigo()) + bolsasbydate.getPeso() );

                        }
                    } else if (cal.get(Calendar.MONTH) == 6) {
                        pesosbyMes[6] += bolsasbydate.getPeso();
                        if(bolsasbydate.getProducto().getCategoria().getNombre().equals("Plástico")) {
                            cant_residuos_mensuales_plastico[6] += bolsasbydate.getCantidad();
                            cant_puntos_mensuales_plastico[6] += bolsasbydate.getPuntuacion();
                            plasticobyMes[6] += bolsasbydate.getPeso();
                            plasticoJulio.put(bolsasbydate.getProducto().getCodigo(), plasticoJulio.get(bolsasbydate.getProducto().getCodigo()) + bolsasbydate.getPeso() );

                        }
                        else {
                            cant_residuos_mensuales_papelcarton[6] += bolsasbydate.getCantidad();
                            cant_puntos_mensuales_papelcarton[6] += bolsasbydate.getPuntuacion();
                            papelbyMes[6] += bolsasbydate.getPeso();
                            papelJulio.put(bolsasbydate.getProducto().getCodigo(), papelJulio.get(bolsasbydate.getProducto().getCodigo()) + bolsasbydate.getPeso() );

                        }
                    } else if (cal.get(Calendar.MONTH) == 7) {
                        pesosbyMes[7] += bolsasbydate.getPeso();
                        if(bolsasbydate.getProducto().getCategoria().getNombre().equals("Plástico")) {
                            cant_residuos_mensuales_plastico[7] += bolsasbydate.getCantidad();
                            cant_puntos_mensuales_plastico[7] += bolsasbydate.getPuntuacion();
                            plasticobyMes[7] += bolsasbydate.getPeso();
                            plasticoAgosto.put(bolsasbydate.getProducto().getCodigo(), plasticoAgosto.get(bolsasbydate.getProducto().getCodigo()) + bolsasbydate.getPeso() );

                        }
                        else {
                            cant_residuos_mensuales_papelcarton[7] += bolsasbydate.getCantidad();
                            cant_puntos_mensuales_papelcarton[7] += bolsasbydate.getPuntuacion();
                            papelbyMes[7] += bolsasbydate.getPeso();
                            papelAgosto.put(bolsasbydate.getProducto().getCodigo(), papelAgosto.get(bolsasbydate.getProducto().getCodigo()) + bolsasbydate.getPeso() );

                        }
                    } else if (cal.get(Calendar.MONTH) == 8) {
                        pesosbyMes[8] += bolsasbydate.getPeso();
                        if(bolsasbydate.getProducto().getCategoria().getNombre().equals("Plástico")) {
                            cant_residuos_mensuales_plastico[8] += bolsasbydate.getCantidad();
                            cant_puntos_mensuales_plastico[8] += bolsasbydate.getPuntuacion();
                            plasticobyMes[8] += bolsasbydate.getPeso();
                            plasticoSetiembre.put(bolsasbydate.getProducto().getCodigo(), plasticoSetiembre.get(bolsasbydate.getProducto().getCodigo()) + bolsasbydate.getPeso() );

                        }
                        else {
                            cant_residuos_mensuales_papelcarton[8] += bolsasbydate.getCantidad();
                            cant_puntos_mensuales_papelcarton[8] += bolsasbydate.getPuntuacion();
                            papelbyMes[8] += bolsasbydate.getPeso();
                            papelSetiembre.put(bolsasbydate.getProducto().getCodigo(), papelSetiembre.get(bolsasbydate.getProducto().getCodigo()) + bolsasbydate.getPeso() );

                        }
                    } else if (cal.get(Calendar.MONTH) == 9) {
                        pesosbyMes[9] += bolsasbydate.getPeso();
                        if(bolsasbydate.getProducto().getCategoria().getNombre().equals("Plástico")) {
                            cant_residuos_mensuales_plastico[9] += bolsasbydate.getCantidad();
                            cant_puntos_mensuales_plastico[9] += bolsasbydate.getPuntuacion();
                            plasticobyMes[9] += bolsasbydate.getPeso();
                            plasticoOctubre.put(bolsasbydate.getProducto().getCodigo(), plasticoOctubre.get(bolsasbydate.getProducto().getCodigo()) + bolsasbydate.getPeso() );

                        }
                        else {
                            cant_residuos_mensuales_papelcarton[9] += bolsasbydate.getCantidad();
                            cant_puntos_mensuales_papelcarton[9] += bolsasbydate.getPuntuacion();
                            papelbyMes[9] += bolsasbydate.getPeso();
                            papelOctubre.put(bolsasbydate.getProducto().getCodigo(), papelOctubre.get(bolsasbydate.getProducto().getCodigo()) + bolsasbydate.getPeso() );

                        }
                    } else if (cal.get(Calendar.MONTH) == 10) {
                        pesosbyMes[10] += bolsasbydate.getPeso();
                        if(bolsasbydate.getProducto().getCategoria().getNombre().equals("Plástico")) {
                            cant_residuos_mensuales_plastico[10] += bolsasbydate.getCantidad();
                            cant_puntos_mensuales_plastico[10] += bolsasbydate.getPuntuacion();
                            plasticobyMes[10] += bolsasbydate.getPeso();
                            plasticoNoviembre.put(bolsasbydate.getProducto().getCodigo(), plasticoNoviembre.get(bolsasbydate.getProducto().getCodigo()) + bolsasbydate.getPeso() );

                        }
                        else {
                            cant_residuos_mensuales_papelcarton[10] += bolsasbydate.getCantidad();
                            cant_puntos_mensuales_papelcarton[10] += bolsasbydate.getPuntuacion();
                            papelbyMes[10] += bolsasbydate.getPeso();
                            papelNoviembre.put(bolsasbydate.getProducto().getCodigo(), papelNoviembre.get(bolsasbydate.getProducto().getCodigo()) + bolsasbydate.getPeso() );

                        }
                    } else if (cal.get(Calendar.MONTH) == 11) {
                        pesosbyMes[11] += bolsasbydate.getPeso();
                        if(bolsasbydate.getProducto().getCategoria().getNombre().equals("Plástico")) {
                            cant_residuos_mensuales_plastico[11] += bolsasbydate.getCantidad();
                            cant_puntos_mensuales_plastico[11] += bolsasbydate.getPuntuacion();
                            plasticobyMes[11] += bolsasbydate.getPeso();
                            plasticoDiciembre.put(bolsasbydate.getProducto().getCodigo(), plasticoDiciembre.get(bolsasbydate.getProducto().getCodigo()) + bolsasbydate.getPeso() );

                        }
                        else {
                            cant_residuos_mensuales_papelcarton[11] += bolsasbydate.getCantidad();
                            cant_puntos_mensuales_papelcarton[11] += bolsasbydate.getPuntuacion();
                            papelbyMes[11] += bolsasbydate.getPeso();
                            papelDiciembre.put(bolsasbydate.getProducto().getCodigo(), papelDiciembre.get(bolsasbydate.getProducto().getCodigo()) + bolsasbydate.getPeso() );

                        }
                    }
                    bolsas.add(bolsasbydate.getBolsa().getCodigo());
                    addingValuestoText(bolsasbydate);
                }
            }

        }
        if(valor>0) {
               ordenar();

            generateQuery("Year", "Plastico", plasticoCount, pesoPlastico, puntosPlastico,bolsas.size());
            generateQuery("Year", "Vidrio", vidrioCount, pesoVidrio, puntosVidrio,bolsas.size());
            generateQuery("Year", "Papel", papelCartonCount, pesoPapelCarton, puntosPapelCarton,bolsas.size());
            generateQuery("Year", "Metal", metalCount, pesoMetal, puntosMetal,bolsas.size());
            String queryplastico = "insert into DatosAnuales(enero,febrero,marzo,abril,mayo,junio,julio,agosto,setiembre,octubre,noviembre,diciembre,tipo,tipo_residuo) " +
                    "values ( " + cant_residuos_mensuales_plastico[0] + ", " + cant_residuos_mensuales_plastico[1] + ", "
                    + cant_residuos_mensuales_plastico[2] + ", " + cant_residuos_mensuales_plastico[3] + ", " + cant_residuos_mensuales_plastico[4] + "," + cant_residuos_mensuales_plastico[5]  + ", "
                    + cant_residuos_mensuales_plastico[6] + ", " + cant_residuos_mensuales_plastico[7] + ","+ + cant_residuos_mensuales_plastico[8] + ","+cant_residuos_mensuales_plastico[9] + ", " + cant_residuos_mensuales_plastico[10] +","+ cant_residuos_mensuales_plastico[11] +  ",'probolsa','Plástico')";
            db.execSQL(queryplastico);
            System.out.println(queryplastico);

            String querypapel = "insert into DatosAnuales(enero,febrero,marzo,abril,mayo,junio,julio,agosto,setiembre,octubre,noviembre,diciembre,tipo,tipo_residuo) " +
                    "values ( " + cant_residuos_mensuales_papelcarton[0] + ", " + cant_residuos_mensuales_papelcarton[1] + ", "
                    + cant_residuos_mensuales_papelcarton[2] + ", " + cant_residuos_mensuales_papelcarton[3] + ", " + cant_residuos_mensuales_papelcarton[4] + "," + cant_residuos_mensuales_papelcarton[5]  + ", "
                    + cant_residuos_mensuales_papelcarton[6] + ", " + cant_residuos_mensuales_papelcarton[7] + ","+ + cant_residuos_mensuales_papelcarton[8] + ","+cant_residuos_mensuales_papelcarton[9] + ", " + cant_residuos_mensuales_papelcarton[10] +","+ cant_residuos_mensuales_papelcarton[11] +  ",'probolsa','Papel/Cartón')";
            db.execSQL(querypapel);
            System.out.println(querypapel);

            String queryplastico_puntos = "insert into DatosAnuales(enero,febrero,marzo,abril,mayo,junio,julio,agosto,setiembre,octubre,noviembre,diciembre,tipo,tipo_residuo) " +
                    "values ( " + cant_puntos_mensuales_plastico[0] + ", " + cant_puntos_mensuales_plastico[1] + ", "
                    + cant_puntos_mensuales_plastico[2] + ", " + cant_puntos_mensuales_plastico[3] + ", " + cant_puntos_mensuales_plastico[4] + "," + cant_puntos_mensuales_plastico[5]  + ", "
                    + cant_puntos_mensuales_plastico[6] + ", " + cant_puntos_mensuales_plastico[7] + ","+ + cant_puntos_mensuales_plastico[8] + ","+cant_puntos_mensuales_plastico[9] + ", " + cant_puntos_mensuales_plastico[10] +","+ cant_puntos_mensuales_plastico[11] +  ",'puntos','Plástico')";
            db.execSQL(queryplastico_puntos);
            System.out.println(queryplastico_puntos);

            String querypapelpuntos = "insert into DatosAnuales(enero,febrero,marzo,abril,mayo,junio,julio,agosto,setiembre,octubre,noviembre,diciembre,tipo,tipo_residuo) " +
                    "values ( " + cant_puntos_mensuales_papelcarton[0] + ", " + cant_puntos_mensuales_papelcarton[1] + ", "
                    + cant_puntos_mensuales_papelcarton[2] + ", " + cant_puntos_mensuales_papelcarton[3] + ", " + cant_puntos_mensuales_papelcarton[4] + "," + cant_puntos_mensuales_papelcarton[5]  + ", "
                    + cant_puntos_mensuales_papelcarton[6] + ", " + cant_puntos_mensuales_papelcarton[7] + ","+ + cant_puntos_mensuales_papelcarton[8] + ","+cant_puntos_mensuales_papelcarton[9] + ", " + cant_puntos_mensuales_papelcarton[10] +","+ cant_puntos_mensuales_papelcarton[11] +  ",'puntos','Papel/Cartón')";
            db.execSQL(querypapelpuntos);
            System.out.println(querypapelpuntos);

            String pesosQuery = "insert into DatosAnuales(enero,febrero,marzo,abril,mayo,junio,julio,agosto,setiembre,octubre,noviembre,diciembre,tipo) " +
                    "values ( " + pesosbyMes[0] + ", " + pesosbyMes[1] + ", "
                    + pesosbyMes[2] + ", " + pesosbyMes[3] + ", " + pesosbyMes[4] + "," + pesosbyMes[5]  + ", "
                    + pesosbyMes[6] + ", " + pesosbyMes[7] + ","+ + pesosbyMes[8] + ","+pesosbyMes[9] + ", " + pesosbyMes[10] +","+ pesosbyMes[11] +  ",'pesos')";
            db.execSQL(pesosQuery);
            System.out.println(pesosQuery);

            String plasticoQuery = "insert into DatosAnuales(enero,febrero,marzo,abril,mayo,junio,julio,agosto,setiembre,octubre,noviembre,diciembre,tipo) " +
                    "values ( " + plasticobyMes[0] + ", " + plasticobyMes[1] + ", "
                    + plasticobyMes[2] + ", " + plasticobyMes[3] + ", " + plasticobyMes[4] + "," + plasticobyMes[5]  + ", "
                    + plasticobyMes[6] + ", " + plasticobyMes[7] + ","+ + plasticobyMes[8] + ","+plasticobyMes[9] + ", " + plasticobyMes[10] +","+ plasticobyMes[11] +  ",'Plástico')";
            db.execSQL(plasticoQuery);
            System.out.println(plasticoQuery);

            String papelQuery = "insert into DatosAnuales(enero,febrero,marzo,abril,mayo,junio,julio,agosto,setiembre,octubre,noviembre,diciembre,tipo) " +
                    "values ( " + papelbyMes[0] + ", " + papelbyMes[1] + ", "
                    + papelbyMes[2] + ", " + papelbyMes[3] + ", " + papelbyMes[4] + "," + papelbyMes[5]  + ", "
                    + papelbyMes[6] + ", " + papelbyMes[7] + ","+ + papelbyMes[8] + ","+papelbyMes[9] + ", " + papelbyMes[10] +","+ papelbyMes[11] +  ",'Papel/Cartón')";
            db.execSQL(papelQuery);
            System.out.println(papelQuery);

        }

        Log.e("TAG","onResponse:" + response.toString());
        System.out.println(bolsasLast.size());
        try {
            obtenerLasBolsasMonth("bolsasYear/");
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void obtenerLasBolsasMonth(String urlDate) throws ParseException {
        initialCounter();
        dbHelper helper = new dbHelper(context, "Usuario.sqlite", null, 1);
        SQLiteDatabase db = helper.getWritableDatabase();
        int valor = 0;
        for (Integer bolsasLast : bolsasLast) {
            Cursor f1 = db.rawQuery("select recojoFecha from Bolsa where codigo = "+ bolsasLast,null);
            if(f1.moveToFirst()){
                do {
                    valor++;
                    if(!f1.getString(0).equals("null") || !f1.getString(0).equals(null)) {
                        String sDate1 = f1.getString(0);
                        Date dia=new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH).parse(sDate1);
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(dia);
                        if (cal.get(Calendar.MONTH) == 0)
                            yAxisDataYearBolsa[0] += 1;
                        else if (cal.get(Calendar.MONTH) == 1)
                            yAxisDataYearBolsa[1] += 1;
                        else if (cal.get(Calendar.MONTH) == 2)
                            yAxisDataYearBolsa[2] += 1;
                        else if (cal.get(Calendar.MONTH) == 3)
                            yAxisDataYearBolsa[3] += 1;
                        else if (cal.get(Calendar.MONTH) == 4)
                            yAxisDataYearBolsa[4] += 1;
                        else if (cal.get(Calendar.MONTH) == 5)
                            yAxisDataYearBolsa[5] += 1;
                        else if (cal.get(Calendar.MONTH) == 6)
                            yAxisDataYearBolsa[6] += 1;
                        else if (cal.get(Calendar.MONTH) == 7)
                            yAxisDataYearBolsa[7] += 1;
                        else if (cal.get(Calendar.MONTH) == 8)
                            yAxisDataYearBolsa[8] += 1;
                        else if (cal.get(Calendar.MONTH) == 9)
                            yAxisDataYearBolsa[9] += 1;
                        else if (cal.get(Calendar.MONTH) == 10)
                            yAxisDataYearBolsa[10] += 1;
                        else if (cal.get(Calendar.MONTH) == 11)
                            yAxisDataYearBolsa[11] += 1;
                    }

                }while(f1.moveToNext());
            }

        }
        if(valor>0) {
            String query = "insert into DatosAnuales(enero,febrero,marzo,abril,mayo,junio,julio,agosto,setiembre,octubre,noviembre,diciembre,tipo) " +
                    "values ( " + yAxisDataYearBolsa[0] + ", " + yAxisDataYearBolsa[1] + ", "
                    + yAxisDataYearBolsa[2] + ", " + yAxisDataYearBolsa[3] + ", " + yAxisDataYearBolsa[4] + "," + yAxisDataYearBolsa[5]  + ", "
                    + yAxisDataYearBolsa[6] + ", " + yAxisDataYearBolsa[7] + ","+ + yAxisDataYearBolsa[8] + ","+yAxisDataYearBolsa[9] + ", " + yAxisDataYearBolsa[10] +","+ yAxisDataYearBolsa[11] +  ",'bolsa')";
            db.execSQL(query);
            System.out.println(query);

        }
    }

    public void initialCounter(){
        plasticoCount=0;
        pesoPlastico=0;
        puntosPlastico=0;
        vidrioCount=0;
        pesoVidrio=0;
        puntosVidrio=0;
        papelCartonCount=0;
        pesoPapelCarton=0;
        puntosPapelCarton=0;
        metalCount=0;
        pesoMetal=0;
        puntosMetal=0;
        residuosTotal=0;
    }

    public void addingValuestoText(Probolsa bolsasbydate ){
        if (bolsasbydate.getProducto().getCategoria().getNombre().equals("Plastico")) {
            pesoPlastico += bolsasbydate.getProducto().getPeso();
            puntosPlastico += bolsasbydate.getPuntuacion();
            plasticoCount += bolsasbydate.getCantidad();
        }else if (bolsasbydate.getProducto().getCategoria().getNombre().equals("Vidrio")) {
            pesoVidrio += bolsasbydate.getProducto().getPeso();
            puntosVidrio += bolsasbydate.getPuntuacion();
            vidrioCount+= bolsasbydate.getCantidad();
        } else if (bolsasbydate.getProducto().getCategoria().getNombre().equals("Papel/Carton")) {
            pesoPapelCarton += bolsasbydate.getProducto().getPeso();
            puntosPapelCarton += bolsasbydate.getPuntuacion();
            papelCartonCount+=bolsasbydate.getCantidad();
        } else if (bolsasbydate.getProducto().getCategoria().getNombre().equals("Metal")) {
            pesoMetal += bolsasbydate.getProducto().getPeso();
            puntosMetal += bolsasbydate.getPuntuacion();
            metalCount+=bolsasbydate.getCantidad();
        }
    }

    public void generateQuery(String tipo,String producto,double cantidad,double peso,double puntuacion,int bolsaID){
        String query = "insert into Contador (tendenciaTipo,productoTipo,cantidad,peso,puntuacion,bolsa) " +
                "values ('" + tipo + "', '" + producto + "', " + cantidad + ", "
                + peso + ", " + puntuacion + ","+bolsaID+")";
        db.execSQL(query);
        System.out.println(query);
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    public void ordenar(){
        plasticoEnero = sortByValue(plasticoEnero);
        insertar_tops(plasticoEnero,"enero", "Plástico");
        plasticoFebrero = sortByValue(plasticoFebrero);
        insertar_tops(plasticoFebrero,"febrero", "Plástico");
        plasticoMarzo = sortByValue(plasticoMarzo);
        insertar_tops(plasticoMarzo,"marzo", "Plástico");
        plasticoAbril = sortByValue(plasticoAbril);
        insertar_tops(plasticoAbril,"abril", "Plástico");
        plasticoMayo = sortByValue(plasticoMayo);
        insertar_tops(plasticoMayo,"mayo", "Plástico");
        System.out.println("PLASTICOOOOOOOOOOOOOOOO MAPP MAYOOOOOOOOOOOOOOOOOOOOOO");
        System.out.println(plasticoMayo);
        plasticoJunio = sortByValue(plasticoJunio);
        insertar_tops(plasticoJunio,"junio", "Plástico");
        plasticoJulio = sortByValue(plasticoJulio);
        insertar_tops(plasticoJulio,"julio", "Plástico");
        plasticoAgosto = sortByValue(plasticoAgosto);
        insertar_tops(plasticoAgosto,"agosto", "Plástico");
        plasticoSetiembre = sortByValue(plasticoSetiembre);
        insertar_tops(plasticoSetiembre,"setiembre", "Plástico");
        plasticoOctubre = sortByValue(plasticoOctubre);
        insertar_tops(plasticoOctubre,"octubre", "Plástico");
        plasticoNoviembre = sortByValue(plasticoNoviembre);
        insertar_tops(plasticoNoviembre,"noviembre", "Plástico");
        plasticoDiciembre = sortByValue(plasticoDiciembre);
        insertar_tops(plasticoDiciembre,"diciembre", "Plástico");
        papelEnero = sortByValue(papelEnero);
        insertar_tops(papelEnero,"enero", "Papel/Cartón");
        papelFebrero = sortByValue(papelFebrero);
        insertar_tops(papelFebrero,"febrero", "Papel/Cartón");
        papelMarzo = sortByValue(papelMarzo);
        insertar_tops(papelMarzo,"marzo", "Papel/Cartón");
        papelAbril = sortByValue(papelAbril);
        insertar_tops(papelAbril,"abril", "Papel/Cartón");
        papelMayo = sortByValue(papelMayo);
        insertar_tops(papelMayo,"mayo", "Papel/Cartón");
        papelJunio = sortByValue(papelJunio);
        insertar_tops(papelJunio,"junio", "Papel/Cartón");
        papelJulio = sortByValue(papelJulio);
        insertar_tops(papelJulio,"julio", "Papel/Cartón");
        papelAgosto = sortByValue(papelAgosto);
        insertar_tops(papelAgosto,"agosto", "Papel/Cartón");
        papelSetiembre = sortByValue(papelSetiembre);
        insertar_tops(papelSetiembre,"setiembre", "Papel/Cartón");
        papelOctubre = sortByValue(papelOctubre);
        insertar_tops(papelOctubre,"octubre", "Papel/Cartón");
        papelNoviembre = sortByValue(papelNoviembre);
        insertar_tops(papelNoviembre,"noviembre", "Papel/Cartón");
        papelDiciembre = sortByValue(papelDiciembre);
        insertar_tops(papelDiciembre,"diciembre", "Papel/Cartón");
    }

    public void insertar_tops(Map<Integer,Double> hm, String mes,String tipo_producto){
        double peso1 = 0, peso2 = 0, peso3 = 0;
        int codigo1 = 0,codigo2 = 0,codigo3 = 0;
        Iterator<Map.Entry<Integer, Double>> it = hm.entrySet().iterator();
        for(int i=0;i<3;i++){
            Map.Entry<Integer, Double> entry = it.next();
            if(i == 0) {
                codigo1 = entry.getKey();
                peso1 = entry.getValue();
            }else{
                if( i == 1){
                    codigo2 = entry.getKey();
                    peso2 = entry.getValue();
                }
                else{
                    codigo3 = entry.getKey();
                    peso3 = entry.getValue();
                }
            }
        }
        generateQueryTopProductos(peso1, mes, codigo1, tipo_producto,peso2,codigo2,peso3,codigo3);
    }



    public void generateQueryTopProductos(double Peso,String mes,int codigo_producto,String tipo_producto, double Peso2, int codigo_2, double Peso3, int codigo3){
        String query = "insert into LastProbolsas (peso_prim,mes,codigo_producto_prim,tipo_producto, peso_sec, codigo_producto_sec, peso_ter, codigo_producto_ter) " +
                "values (" + Peso + ", '" + mes + "', " + codigo_producto + ", '"
                + tipo_producto + "', " + Peso2+", " + codigo_2+" , "+Peso3+", "+codigo3+")";
        db.execSQL(query);
        System.out.println(query);
    }



    @RequiresApi(api = Build.VERSION_CODES.N)
    public Map<Integer,Double> sortByValue(Map<Integer, Double> hm)
    {

        hm = hm.entrySet()
                .stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2,
                        LinkedHashMap::new));
        return hm;
    }

}
