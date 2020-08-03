package com.example.reciclemosdemo.Grafico;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.reciclemosdemo.Adicionales.dbHelper;
import com.example.reciclemosdemo.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.LineChartView;


public class YearFragment extends Fragment {
    View view;
    private com.example.reciclemosdemo.Grafico.RetrofitMain retrofit;
    private ArrayList<TextView> textList = new ArrayList<>();
    TextView txtPlasticoCount,txtPlasticoPuntos,txtPlasticoPeso;
    /* TextView txtVidrioCount,txtVidrioPuntos,txtVidrioPeso;
     TextView txtMetalesCount,txtMetalesPuntos,txtMetalesPeso;*/
    TextView txtPapelCartonCount,txtPapelCartonPuntos,txtPapelCartonPeso,txtResiduosCount,txtPesoResiduos,txtPuntajeResiduos;
    LineChart chartBolsas,chartResiduos,chartPuntos;
    String[] axisDataMonth = {"","En", "Febr", "Mar", "Abr", "May", "Jun", "Jul", "Ag", "Set", "Oct", "Nov", "Dic"};
    int [] yAxisDataYear = {0,0,0,0,0,0,0,0,0,0,0,0};
    int [] yAxisDataYearBolsa = {0,0,0,0,0,0,0,0,0,0,0,0};
    private int [] yAxisDataYearPunto= {0,0,0,0,0,0,0,0,0,0,0,0};
    private int maximo = 0;
    private int totalResiduo,totalPeso,totalPuntos;
    private int datasetSize = 0;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_year,container,false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        retrofit = new com.example.reciclemosdemo.Grafico.RetrofitMain();
        txtPlasticoCount =view.findViewById(R.id.txtPlasticoCantidad) ;
        txtPlasticoPuntos =view.findViewById(R.id.txtPlasticoPuntos) ;
        txtPlasticoPeso =view.findViewById(R.id.txtPlasticoPeso) ;
    /*    txtVidrioCount =view.findViewById(R.id.txtVidrioCantidad) ;
        txtVidrioPuntos =view.findViewById(R.id.txtVidrioPuntos) ;
        txtVidrioPeso =view.findViewById(R.id.txtVidrioPeso) ;
        txtMetalesCount =view.findViewById(R.id.txtMetalesCantidad) ;
        txtMetalesPuntos =view.findViewById(R.id.txtMetalesPuntos) ;
        txtMetalesPeso =view.findViewById(R.id.txtMetalesPeso);*/
        txtPapelCartonCount =view.findViewById(R.id.txtPapelCartonCantidad) ;
        txtPapelCartonPuntos =view.findViewById(R.id.txtPapelCartonPuntos) ;
        txtPapelCartonPeso =view.findViewById(R.id.txtPapelCartonPeso) ;
        chartBolsas = getView().findViewById(R.id.chart);
        chartResiduos = getView().findViewById(R.id.chart2);
        chartPuntos = getView().findViewById(R.id.chart3);
        txtResiduosCount = getView().findViewById(R.id.txtCantBolsasHoy);
        txtPesoResiduos = getView().findViewById(R.id.txtPesoBolsasHoy);
        txtPuntajeResiduos = getView().findViewById(R.id.txtPtosBolsasHoy);
        totalPeso=0;
        totalPuntos=0;
        totalResiduo=0;
        getDataWeek();
        graphicData();
        graphicData2();
        graphicData3();
        txtResiduosCount.setText(Integer.toString(totalResiduo));
        txtPesoResiduos.setText(Integer.toString(totalPeso/1000));
        txtPuntajeResiduos.setText(Integer.toString(totalPuntos));

    }

    public void getDataWeek() {
        dbHelper helper = new dbHelper(getActivity(),"Usuario.sqlite", null, 1);
        SQLiteDatabase db = helper.getReadableDatabase();

        Cursor f = db.rawQuery("select cantidad ,peso ,puntuacion from Contador where tendenciaTipo = 'Year' and productoTipo = 'Plastico' ", null);

        if(f.moveToFirst()) {
            txtPlasticoCount.setText(Integer.toString(f.getInt(0)));
            txtPlasticoPeso.setText(Integer.toString((int)f.getDouble(1)/1000)+ " kg");
            txtPlasticoPuntos.setText(Integer.toString((int)f.getDouble(2))+ " ptos");
            totalResiduo+=f.getInt(0);
            totalPuntos+=f.getDouble(2);
            totalPeso+=f.getDouble(1);

        }
  /*      Cursor f2 = db.rawQuery("select cantidad ,peso ,puntuacion from Contador where tendenciaTipo = 'Year' and productoTipo = 'Vidrio' ", null);


        if(f2.moveToFirst()) {
            txtVidrioCount.setText(Integer.toString(f2.getInt(0)));
            txtVidrioPeso.setText(Integer.toString((int)f2.getDouble(1)/1000)+ " kg");
            txtVidrioPuntos.setText(Integer.toString((int)f2.getDouble(2))+ " ptos");
            totalResiduo+=f2.getInt(0);
            totalPuntos+=f2.getDouble(2);
            totalPeso+=f2.getDouble(1);
        }*/

        Cursor f3 = db.rawQuery("select cantidad ,peso ,puntuacion from Contador where tendenciaTipo = 'Year' and productoTipo = 'Papel' ", null);
        if(f3.moveToFirst()) {
            txtPapelCartonCount.setText(Integer.toString(f3.getInt(0)));
            txtPapelCartonPeso.setText(Integer.toString((int)f3.getDouble(1)/1000)+ " kg");
            txtPapelCartonPuntos.setText(Integer.toString((int)f3.getDouble(2)) + " ptos");
            totalResiduo+=f3.getInt(0);
            totalPuntos+=f3.getDouble(2);
            totalPeso+=f3.getDouble(1);
        }

   /*     Cursor f4 = db.rawQuery("select cantidad ,peso ,puntuacion from Contador where tendenciaTipo = 'Year' and productoTipo = 'Metal' ", null);

        if(f4.moveToFirst()) {
            txtMetalesCount.setText(Integer.toString(f4.getInt(0)));
            txtMetalesPeso.setText(Integer.toString((int)f4.getDouble(1)/1000)+ " kg");
            txtMetalesPuntos.setText(Integer.toString((int)f4.getDouble(2)) + " ptos");
            totalResiduo+=f4.getInt(0);
            totalPuntos+=f4.getDouble(2);
            totalPeso+=f4.getDouble(1);
        }*/
    }

    public void graphicData(){
        LineDataSet lineDataSetBolsas = new LineDataSet(dataResiduosValue(),"Data Set 1");
        lineDataSetBolsas.setColors(ColorTemplate.rgb("2e2a29"));
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(lineDataSetBolsas);
        LineData data = new LineData(dataSets);

        YAxis rightAxis = chartResiduos.getAxisRight();
        YAxis leftAxis = chartResiduos.getAxisLeft();
        XAxis xAxis = chartResiduos.getXAxis();


        rightAxis.setEnabled(false);
        rightAxis.disableAxisLineDashedLine();

        leftAxis.setDrawGridLines(false);

        if(maximo < 5)
            leftAxis.setAxisMaximum(5);
        else {
            if( maximo % 5 != 0)
                leftAxis.setAxisMaximum(maximo + (5 - (maximo % 5)));
            else
                leftAxis.setAxisMaximum(maximo);
        }
        leftAxis.setGranularity(10);
        leftAxis.setGranularityEnabled(true);
        leftAxis.setAxisMinimum(0);


        xAxis.setSpaceMax(0.5f);
        xAxis.setSpaceMin(0.5f);
        xAxis.setTextSize(8);
        xAxis.setLabelCount(12);

        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);

        chartResiduos.setDrawGridBackground(false);
        chartResiduos.setData(data);
        chartResiduos.invalidate();
        chartResiduos.setScaleEnabled(false);
        chartResiduos.getXAxis().setValueFormatter(new com.github.mikephil.charting.formatter.IndexAxisValueFormatter(axisDataMonth));
        chartResiduos.getAxisLeft().setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return String.valueOf((int) Math.floor(value));
            }
        });
        chartResiduos.getData().setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return String.valueOf((int) Math.floor(value));
            }
        });
        chartResiduos.getDescription().setEnabled(false);
        chartResiduos.getLegend().setEnabled(false);
        chartResiduos.getData().setValueTextSize(10);




    }

    public void graphicData2(){
        LineDataSet lineDataSetBolsas = new LineDataSet(dataBolsasValue(),"Data Set 1");
        lineDataSetBolsas.setColors(ColorTemplate.rgb("2e2a29"));
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(lineDataSetBolsas);
        LineData data = new LineData(dataSets);

        YAxis rightAxis = chartBolsas.getAxisRight();
        YAxis leftAxis = chartBolsas.getAxisLeft();
        XAxis xAxis = chartBolsas.getXAxis();


        rightAxis.setEnabled(false);
        rightAxis.disableAxisLineDashedLine();
        leftAxis.setGranularity(10);
        leftAxis.setGranularityEnabled(true);
        leftAxis.setDrawGridLines(false);
        if(maximo < 5)
            leftAxis.setAxisMaximum(5);
        else {
            if( maximo % 5 != 0)
                leftAxis.setAxisMaximum(maximo + (5 - (maximo % 5)));
            else
                leftAxis.setAxisMaximum(maximo);
        }

        leftAxis.setAxisMinimum(0);

        xAxis.setSpaceMax(0.5f);
        xAxis.setSpaceMin(0.5f);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setTextSize(8);
        xAxis.setLabelCount(12);

        chartBolsas.setDrawGridBackground(false);
        chartBolsas.setData(data);
        chartBolsas.invalidate();
        chartBolsas.setScaleEnabled(false);
        chartBolsas.getXAxis().setValueFormatter(new com.github.mikephil.charting.formatter.IndexAxisValueFormatter(axisDataMonth));
        chartBolsas.getAxisLeft().setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return String.valueOf((int) Math.floor(value));
            }
        });
        chartBolsas.getData().setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return String.valueOf((int) Math.floor(value));
            }
        });
        chartBolsas.getDescription().setEnabled(false);
        chartBolsas.getLegend().setEnabled(false);
        chartBolsas.getData().setValueTextSize(10);

    }

    public void graphicData3(){
        LineDataSet lineDataSetBolsas = new LineDataSet(dataPuntosValue(),"Data Set 1");
        lineDataSetBolsas.setColors(ColorTemplate.rgb("2e2a29"));
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(lineDataSetBolsas);
        LineData data = new LineData(dataSets);

        YAxis rightAxis = chartPuntos.getAxisRight();
        YAxis leftAxis = chartPuntos.getAxisLeft();
        XAxis xAxis = chartPuntos.getXAxis();


        rightAxis.setEnabled(false);
        rightAxis.disableAxisLineDashedLine();

        leftAxis.setDrawGridLines(false);
        leftAxis.setAxisMaximum(3000);
        leftAxis.setGranularity(1000);
        leftAxis.setGranularityEnabled(true);
        leftAxis.setAxisMinimum(0f);

        xAxis.setSpaceMax(0.5f);
        xAxis.setSpaceMin(0.5f);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setTextSize(8);
        xAxis.setLabelCount(12);

        chartPuntos.setDrawGridBackground(false);
        chartPuntos.setData(data);
        chartPuntos.invalidate();
        chartPuntos.setScaleEnabled(false);
        chartPuntos.getXAxis().setValueFormatter(new com.github.mikephil.charting.formatter.IndexAxisValueFormatter(axisDataMonth));
        chartPuntos.getAxisLeft().setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return String.valueOf((int) Math.floor(value));
            }
        });
        chartPuntos.getData().setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return String.valueOf((int) Math.floor(value));
            }
        });
        chartPuntos.getData().setValueTextSize(10);
        chartPuntos.getDescription().setEnabled(false);
        chartPuntos.getLegend().setEnabled(false);


    }


    private ArrayList<Entry> dataBolsasValue()
    {
        ArrayList<Entry> dataValues = new ArrayList<>();
        dbHelper helper = new dbHelper(getActivity(),"Usuario.sqlite", null, 1);
        SQLiteDatabase db = helper.getReadableDatabase();

        Cursor f = db.rawQuery("select enero,febrero,marzo,abril,mayo,junio,julio,agosto,setiembre,octubre,noviembre,diciembre from DatosAnuales where tipo = 'bolsa'", null);
        if(f.moveToFirst()) {

            for (int i = 0; i <= 11; i++) {
                dataValues.add(new Entry(i+1,f.getInt(i)));
            }
        }
        return dataValues;
    }

    private ArrayList<Entry> dataResiduosValue()
    {
        ArrayList<Entry> dataValues = new ArrayList<>();
        dbHelper helper = new dbHelper(getActivity(),"Usuario.sqlite", null, 1);
        SQLiteDatabase db = helper.getReadableDatabase();

        Cursor f = db.rawQuery("select enero,febrero,marzo,abril,mayo,junio,julio,agosto,setiembre,octubre,noviembre,diciembre from DatosAnuales where tipo = 'probolsa'", null);
        if(f.moveToFirst()) {

            for (int i = 0; i <= 11; i++) {
                if(maximo< f.getInt(i))
                    maximo = f.getInt(i);
                dataValues.add(new Entry(i+1,f.getInt(i)));
            }
        }
        return dataValues;
    }

    private ArrayList<Entry> dataPuntosValue()
    {
        ArrayList<Entry> dataValues = new ArrayList<>();
        dbHelper helper = new dbHelper(getActivity(),"Usuario.sqlite", null, 1);
        SQLiteDatabase db = helper.getReadableDatabase();

        Cursor f = db.rawQuery("select enero,febrero,marzo,abril,mayo,junio,julio,agosto,setiembre,octubre,noviembre,diciembre from DatosAnuales where tipo = 'puntos'", null);
        if(f.moveToFirst()) {

            for (int i = 0; i <= 11; i++) {
                dataValues.add(new Entry(i+1,f.getInt(i)));
            }
        }
        return dataValues;
    }






}

