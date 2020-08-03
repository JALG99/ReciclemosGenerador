package com.example.reciclemosdemo.Grafico;

import android.graphics.Color;
import android.util.Log;
import android.widget.TextView;

import com.example.reciclemosdemo.Entities.Bolsa;
import com.example.reciclemosdemo.Entities.JsonPlaceHolderApi;
import com.example.reciclemosdemo.Entities.Probolsa;
import com.example.reciclemosdemo.Entities.Producto;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.LineChartView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitMain {

    private retrofit2.Retrofit retrofit;
    private static final String TAG ="APP";
    private ArrayList<Bolsa> dataset = new ArrayList<>();
    private int plasticoCount,pesoPlastico,puntosPlastico;
    private int vidrioCount,pesoVidrio,puntosVidrio;
    private int papelCartonCount,pesoPapelCarton,puntosPapelCarton;
    private int metalCount,pesoMetal,puntosMetal;
    private int [] yAxisDataMonth= {0,0,0,0,0,0,0};
    private int residuosTotal;
    com.example.reciclemosdemo.Entities.CategoriaDivided plasticoDivided = new com.example.reciclemosdemo.Entities.CategoriaDivided();
    com.example.reciclemosdemo.Entities.CategoriaDivided vidrioDivided = new com.example.reciclemosdemo.Entities.CategoriaDivided();
    com.example.reciclemosdemo.Entities.CategoriaDivided metalDivided = new com.example.reciclemosdemo.Entities.CategoriaDivided();
    com.example.reciclemosdemo.Entities.CategoriaDivided papelDivided = new com.example.reciclemosdemo.Entities.CategoriaDivided();
    ArrayList<com.example.reciclemosdemo.Entities.CategoriaDivided> categoriaDivideds = new ArrayList<com.example.reciclemosdemo.Entities.CategoriaDivided>();
    private int [] yAxisDataYear= {0,0,0,0,0,0,0,0,0,0,0,0};
    LineChartView lineChartView;
    String[] axisDataMonth = {"Lun", "Mar", "Mie", "Jue", "Vie", "Sab", "Dom"};
    String[] axisDataYear ={"Jan", "Feb", "Mar", "Apr", "May", "June", "July", "Aug", "Sept",
            "Oct", "Nov", "Dec"};

    public RetrofitMain(){
        retrofit = new retrofit2.Retrofit.Builder()
                .baseUrl("https://recyclerapiresttdp.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public void obtenerProductoByBarcode(String barcode, TextView txtNombre,TextView txtPeso,TextView txtAbreviatura){
        JsonPlaceHolderApi jsonPlaceHolderApi=retrofit.create(JsonPlaceHolderApi.class);
        Call<Producto> call=jsonPlaceHolderApi.getProductoById("producto/barcode/"+barcode);
        call.enqueue(new Callback<Producto>() {
            @Override
            public void onResponse(Call<Producto> call, Response<Producto> response) {
                if(response.isSuccessful()) {

                    Producto productos = response.body();
                    if(productos!=null) {
                        String nombre = productos.getNombre();
                        String barcode = productos.getBarcode();
                        Double peso = productos.getPeso();
                        Double contenido = productos.getContenido();
                        String categoria_nombre = productos.getCategoria().getNombre();
                        String abreviatura = productos.getTipo_Contenido().getAbreviatura();
                        txtNombre.setText(nombre);
                        txtPeso.setText(peso.toString());
                        txtAbreviatura.setText(abreviatura);
                    }

                }else{
                    Log.e(TAG,"onResponse:" + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<Producto> call, Throwable t) {
                Log.e(TAG,"onFailure:" + t.getMessage());
            }
        });
    }

    public void obtenerDatosProductByBolsa(String usuarioID, com.example.reciclemosdemo.Grafico.ListaProductoByBolsaAdapter listProdBolsasAdapter, com.example.reciclemosdemo.Grafico.ListaProductoByBolsaAdapter.OnNoteListener prodbyBolsa, ArrayList<TextView> textViewsList){
        initialCounter();
        JsonPlaceHolderApi jsonPlaceHolderApi=retrofit.create(JsonPlaceHolderApi.class);
        Call<List<Probolsa>> call=jsonPlaceHolderApi.getProductoByIdBolsa("probolsa/bolsa/"+usuarioID);
        call.enqueue(new Callback<List<Probolsa>>() {
            @Override
            public void onResponse(Call<List<Probolsa>> call, Response<List<Probolsa>> response) {
                if(response.isSuccessful()) {

                    List<Probolsa> probolsas = response.body();
                    if(probolsas.size()>0) {
                        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                        Date recojo = probolsas.get(0).getBolsa().getRecojoFecha();
                        String recojoFecha;
                        if(recojo != null) {
                            recojoFecha = formatter.format(recojo);
                            textViewsList.get(1).setText("Recojo : "+recojoFecha);
                        }
                        else{
                            textViewsList.get(1).setText("Recojo : Pendiente");
                        }
                        String creadoFecha = formatter.format(probolsas.get(0).getBolsa().getCreadoFecha());
                        textViewsList.get(0).setText("Creada : "+creadoFecha);


                        textViewsList.get(4).setText("Juan Torres");

                        String observaciones = probolsas.get(0).getBolsa().getObservaciones();
                        if(observaciones != null ) {
                            textViewsList.get(5).setText("SI");
                            textViewsList.get(6).setText(observaciones);
                        }
                        else {
                            textViewsList.get(5).setText("NO");
                            textViewsList.get(6).setText("No hay observaciones");
                        }



                    }
                    for(int i=0;i<probolsas.size();i++)
                    {
                        if(probolsas.get(i).getProducto().getCategoria().getNombre().equals("Plastico")){
                            plasticoCount+=probolsas.get(i).getCantidad();;
                            puntosPlastico+=probolsas.get(i).getPuntuacion();
                            pesoPlastico+=probolsas.get(i).getPeso();
                        }else if (probolsas.get(i).getProducto().getCategoria().getNombre().equals("Vidrio"))
                        {
                            vidrioCount+=probolsas.get(i).getCantidad();;
                            puntosVidrio+=probolsas.get(i).getPuntuacion();
                            pesoVidrio+=probolsas.get(i).getPeso();
                        }else if (probolsas.get(i).getProducto().getCategoria().getNombre().equals("Metal")){
                            metalCount+=probolsas.get(i).getCantidad();;
                            puntosMetal+=probolsas.get(i).getPuntuacion();
                            pesoMetal+=probolsas.get(i).getPeso();
                        }else{
                            papelCartonCount+=probolsas.get(i).getCantidad();
                            puntosPapelCarton+=probolsas.get(i).getPuntuacion();
                            pesoPapelCarton+=probolsas.get(i).getPeso();
                        }
                    }
                    if(plasticoCount != 0){
                        plasticoDivided.setCantidad(plasticoCount);
                        plasticoDivided.setPeso(pesoPlastico);
                        plasticoDivided.setPuntos(puntosPlastico);
                        plasticoDivided.setTipo("Plastico");
                        categoriaDivideds.add(plasticoDivided);
                    }
                    if(vidrioCount != 0){
                        vidrioDivided.setCantidad(vidrioCount);
                        vidrioDivided.setPeso(pesoVidrio);
                        vidrioDivided.setPuntos(puntosVidrio);
                        vidrioDivided.setTipo("Vidrio");
                        categoriaDivideds.add(vidrioDivided);
                    }
                    if(metalCount != 0){
                        metalDivided.setCantidad(metalCount);
                        metalDivided.setPeso(pesoMetal);
                        metalDivided.setPuntos(puntosMetal);
                        metalDivided.setTipo("Metal");
                        categoriaDivideds.add(metalDivided);
                    }
                    if (papelCartonCount != 0){
                        papelDivided.setCantidad(papelCartonCount);
                        papelDivided.setPeso(pesoPapelCarton);
                        papelDivided.setPuntos(puntosPapelCarton);
                        papelDivided.setTipo("Papel/Carton");
                        categoriaDivideds.add(papelDivided);
                    }
                    listProdBolsasAdapter.adicionarProductoBolsas(categoriaDivideds,prodbyBolsa);

                    textViewsList.get(2).setText(Integer.toString(pesoPapelCarton+pesoMetal+pesoVidrio+pesoPlastico));
                    textViewsList.get(3).setText(Integer.toString(puntosMetal+puntosPapelCarton+puntosVidrio+puntosPlastico));

                }else{
                    Log.e(TAG,"onResponse:" + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<List<Probolsa>> call, Throwable t) {
                Log.e(TAG,"onFailure:" + t.getMessage());
            }
        });
    }

    public void obtenerBolsasByYear(String urlDate,String usuarioID,ArrayList<TextView> textViewsList,LineChartView lineChartView){
        initialCounter();
        JsonPlaceHolderApi jsonPlaceHolderApi=retrofit.create(JsonPlaceHolderApi.class);
        Call<List<Probolsa>> call=jsonPlaceHolderApi.getBolsasByDate("probolsa/"+urlDate+usuarioID);
        call.enqueue(new Callback<List<Probolsa>>() {
            @Override
            public void onResponse(Call<List<Probolsa>> call, Response<List<Probolsa>> response) {
                if(response.isSuccessful()) {

                    List<Probolsa> bolsasbydate = response.body();
                    for(int i=0;i< bolsasbydate.size();i++){
                        if (bolsasbydate.get(i).getBolsa().getRecojoFecha() != null) {

                            Date dia = bolsasbydate.get(i).getBolsa().getRecojoFecha();
                            Calendar cal = Calendar.getInstance();
                            cal.setTime(dia);
                            if (cal.get(Calendar.MONTH) == 1)
                                yAxisDataYear[0] += 1;
                            else if (cal.get(Calendar.MONTH) == 2)
                                yAxisDataYear[1] += 1;
                            else if (cal.get(Calendar.MONTH) == 3)
                                yAxisDataYear[2] += 1;
                            else if (cal.get(Calendar.MONTH) == 4)
                                yAxisDataYear[3] += 1;
                            else if (cal.get(Calendar.MONTH) == 5)
                                yAxisDataYear[4] += 1;
                            else if (cal.get(Calendar.MONTH) == 6)
                                yAxisDataYear[5] += 1;
                            else if (cal.get(Calendar.MONTH) == 7)
                                yAxisDataYear[6] += 1;
                            else if (cal.get(Calendar.MONTH) == 8)
                                yAxisDataYear[7] += 1;
                            else if (cal.get(Calendar.MONTH) == 9)
                                yAxisDataYear[8] += 1;
                            else if (cal.get(Calendar.MONTH) == 10)
                                yAxisDataYear[9] += 1;
                            else if (cal.get(Calendar.MONTH) == 11)
                                yAxisDataYear[10] += 1;
                            else if (cal.get(Calendar.MONTH) == 12)
                                yAxisDataYear[11] += 1;

                            addingValuestoText(bolsasbydate,i,textViewsList);
                        }
                        trendingData(textViewsList);

                    }
                    List yAxisValues = new ArrayList();
                    List axisValues = new ArrayList();

                    Line line = new Line(yAxisValues).setColor(Color.parseColor("#252525"));
                    for (int i = 0; i < axisDataYear.length; i++) {
                        axisValues.add(i, new AxisValue(i).setLabel(axisDataYear[i]));
                    }

                    for (int i = 0; i < yAxisDataYear.length; i++) {
                        yAxisValues.add(new PointValue(i, yAxisDataYear[i]));
                    }
                    List lines = new ArrayList();
                    lines.add(line);

                    LineChartData data = new LineChartData();
                    data.setLines(lines);

                    Axis axis = new Axis();
                    axis.setValues(axisValues);
                    axis.setTextSize(13);
                    axis.setTextColor(Color.parseColor("#03A9F4"));
                    data.setAxisXBottom(axis);

                    Axis yAxis = new Axis();
                    yAxis.setTextColor(Color.parseColor("#03A9F4"));
                    yAxis.setTextSize(13);
                    data.setAxisYLeft(yAxis);

                    lineChartView.setLineChartData(data);
                    Viewport viewport = new Viewport(lineChartView.getMaximumViewport());
                    viewport.top = 100;
                    lineChartView.animate().alpha(1f).setDuration(250);
                    lineChartView.setMaximumViewport(viewport);
                    lineChartView.setCurrentViewport(viewport);
                }else{
                    Log.e(TAG,"onResponse:" + response.errorBody());
                }
            }



            @Override
            public void onFailure(Call<List<Probolsa>> call, Throwable t) {
                Log.e(TAG,"onFailure:" + t.getMessage());
            }



        });
    }

    public void obtenerBolsasByMonthorWeek(String urlDate,String usuarioID,ArrayList<TextView> textViewsList,LineChartView lineChartView){
        initialCounter();
        JsonPlaceHolderApi jsonPlaceHolderApi=retrofit.create(JsonPlaceHolderApi.class);
        Call<List<Probolsa>> call=jsonPlaceHolderApi.getBolsasByDate("probolsa/"+urlDate+usuarioID);
        call.enqueue(new Callback<List<Probolsa>>() {
            @Override
            public void onResponse(Call<List<Probolsa>> call, Response<List<Probolsa>> response) {
                if(response.isSuccessful()) {

                    List<Probolsa> bolsasbydate = response.body();
                    for(int i=0;i< bolsasbydate.size();i++){
                        if (bolsasbydate.get(i).getBolsa().getRecojoFecha() != null) {

                            if(urlDate.equals("bolsasWeek/") || urlDate.equals("bolsasMonth/")) {
                                Date dia = bolsasbydate.get(i).getBolsa().getRecojoFecha();
                                SimpleDateFormat simpleDateformat = new SimpleDateFormat("EEEE");


                                if (simpleDateformat.format(dia).equals("Monday")) {
                                    yAxisDataMonth[0] += 1;
                                }
                                else if (simpleDateformat.format(dia).equals("Tuesday"))
                                    yAxisDataMonth[1] += 1;
                                else if (simpleDateformat.format(dia).equals("Wednesday"))
                                    yAxisDataMonth[2] += 1;
                                else if (simpleDateformat.format(dia).equals("Thursday")) {
                                    yAxisDataMonth[3] += 1;
                                }
                                else if (simpleDateformat.format(dia).equals("Friday"))
                                    yAxisDataMonth[4] += 1;
                                else if (simpleDateformat.format(dia).equals("Saturday"))
                                    yAxisDataMonth[5] += 1;
                                else
                                    yAxisDataMonth[6] += 1;

                                addingValuestoText(bolsasbydate,i,textViewsList);
                            }
                            trendingData(textViewsList);

                        }
                    }
                    List yAxisValues = new ArrayList();
                    List axisValues = new ArrayList();
                    Line line = new Line(yAxisValues).setColor(Color.parseColor("#252525"));
                    for (int i = 0; i < axisDataMonth.length; i++) {
                        axisValues.add(i, new AxisValue(i).setLabel(axisDataMonth[i]));
                    }

                    for (int i = 0; i < yAxisDataMonth.length; i++) {
                        yAxisValues.add(new PointValue(i, yAxisDataMonth[i]));
                    }
                    List lines = new ArrayList();
                    lines.add(line);

                    LineChartData data = new LineChartData();
                    data.setLines(lines);

                    Axis axis = new Axis();
                    axis.setValues(axisValues);
                    axis.setTextSize(13);
                    axis.setTextColor(Color.parseColor("#03A9F4"));
                    data.setAxisXBottom(axis);

                    Axis yAxis = new Axis();
                    yAxis.setTextColor(Color.parseColor("#03A9F4"));
                    yAxis.setTextSize(13);
                    data.setAxisYLeft(yAxis);

                    lineChartView.setLineChartData(data);
                    Viewport viewport = new Viewport(lineChartView.getMaximumViewport());
                    viewport.top = 100;
                    lineChartView.animate().alpha(1f).setDuration(250);
                    lineChartView.setMaximumViewport(viewport);
                    lineChartView.setCurrentViewport(viewport);
                }else{
                    Log.e(TAG,"onResponse:" + response.errorBody());
                }
            }



            @Override
            public void onFailure(Call<List<Probolsa>> call, Throwable t) {
                Log.e(TAG,"onFailure:" + t.getMessage());
            }



        });
    }

    public void addingValuestoText(List<Probolsa> bolsasbydate ,int i,ArrayList<TextView> textViewsList){
        if (bolsasbydate.get(i).getProducto().getCategoria().getNombre().equals("Plastico")) {
            pesoPlastico += bolsasbydate.get(i).getProducto().getPeso();
            puntosPlastico += bolsasbydate.get(i).getPuntuacion();
            plasticoCount++;
            textViewsList.get(0).setText(Integer.toString(plasticoCount));
            textViewsList.get(4).setText(Integer.toString(pesoPlastico) + "  g");
            textViewsList.get(8).setText(Integer.toString(puntosPlastico)+ "  ptos");
        } else if (bolsasbydate.get(i).getProducto().getCategoria().getNombre().equals("Vidrio")) {
            pesoVidrio += bolsasbydate.get(i).getProducto().getPeso();
            puntosVidrio += bolsasbydate.get(i).getPuntuacion();
            vidrioCount++;
            textViewsList.get(1).setText(Integer.toString(vidrioCount));
            textViewsList.get(5).setText(Integer.toString(pesoVidrio)+ "  g");
            textViewsList.get(9).setText(Integer.toString(puntosVidrio)+ "  ptos");
        } else if (bolsasbydate.get(i).getProducto().getCategoria().getNombre().equals("Papel/Carton")) {
            pesoPapelCarton += bolsasbydate.get(i).getProducto().getPeso();
            puntosPapelCarton += bolsasbydate.get(i).getPuntuacion();
            papelCartonCount++;
            textViewsList.get(2).setText(Integer.toString(papelCartonCount));
            textViewsList.get(6).setText(Integer.toString(pesoPapelCarton)+ "  g");
            textViewsList.get(10).setText(Integer.toString(puntosPapelCarton)+ "  ptos");
        } else if (bolsasbydate.get(i).getProducto().getCategoria().getNombre().equals("Metal")) {
            pesoMetal += bolsasbydate.get(i).getProducto().getPeso();
            puntosMetal += bolsasbydate.get(i).getPuntuacion();
            metalCount++;
            textViewsList.get(3).setText(Integer.toString(metalCount));
            textViewsList.get(7).setText(Integer.toString(pesoMetal)+ "  g");
            textViewsList.get(11).setText(Integer.toString(puntosMetal)+ "  ptos");
        }
    }

    public void initialCounter()
    {
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

    public void trendingData(ArrayList<TextView> textViewsList){
        textViewsList.get(12).setText(Integer.toString(plasticoCount+vidrioCount+metalCount+papelCartonCount));
        textViewsList.get(13).setText(Integer.toString((pesoPapelCarton+pesoVidrio+pesoPlastico+pesoMetal)/1000));
        textViewsList.get(14).setText(Integer.toString(puntosMetal+puntosPlastico+puntosVidrio+puntosPapelCarton));
    }

}
