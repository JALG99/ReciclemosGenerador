package com.example.reciclemosdemo.Grafico;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.reciclemosdemo.Adicionales.dbHelper;
import com.example.reciclemosdemo.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
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


public class WeekFragment extends Fragment {
    private com.example.reciclemosdemo.Grafico.RetrofitMain retrofit;
    private ArrayList<TextView> textList = new ArrayList<>();
    TextView txtPlasticoCount,txtPlasticoPuntos,txtPlasticoPeso;
    //  TextView txtVidrioCount,txtVidrioPuntos,txtVidrioPeso;
    //TextView txtMetalesCount,txtMetalesPuntos,txtMetalesPeso;
    TextView txtPapelCartonCount,txtPapelCartonPuntos,txtPapelCartonPeso,txtResiduosCount,txtPesoResiduos,txtPuntajeResiduos;
    LineChartView lineChartView,lineChartViewBolsa,lineChartViewPunto;
    LineChart chartBolsas,chartResiduos,chartPuntos;
    private int totalResiduo,totalPeso,totalPuntos;
    private int [] yAxisDataYear= {0,0,0,0,0,0,0};
    private int [] yAxisDataYearBolsas= {0,0,0,0,0,0,0};
    private int [] xAxisDataMonthPuntos =  {0,0,0,0,0,0,0};
    private int maximo = 0;
    String[] axisDataMonth = {"Dom","Lun", "Mar", "Mie", "Jue", "Vie", "Sab"};

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_week,container,false);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        retrofit = new com.example.reciclemosdemo.Grafico.RetrofitMain();
        txtPlasticoCount =getView().findViewById(R.id.txtPlasticoCantidad) ;
        txtPlasticoPuntos =getView().findViewById(R.id.txtPlasticoPuntos) ;
        txtPlasticoPeso =getView().findViewById(R.id.txtPlasticoPeso) ;
        txtPapelCartonCount =getView().findViewById(R.id.txtPapelCartonCantidad) ;
        txtPapelCartonPuntos =getView().findViewById(R.id.txtPapelCartonPuntos) ;
        txtPapelCartonPeso =getView().findViewById(R.id.txtPapelCartonPeso) ;
        chartBolsas = getView().findViewById(R.id.chart);
        chartResiduos = getView().findViewById(R.id.chart2);
        chartPuntos = getView().findViewById(R.id.chart3);
        txtResiduosCount = getView().findViewById(R.id.txtCantBolsasHoy);
        txtPesoResiduos = getView().findViewById(R.id.txtPesoBolsasHoy);
        txtPuntajeResiduos = getView().findViewById(R.id.txtPtosBolsasHoy);
        totalPeso=0;
        totalPuntos=0;
        totalResiduo=0;
        getDaysOfWeek();
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

        Cursor f = db.rawQuery("select cantidad ,peso ,puntuacion from Contador where tendenciaTipo = 'Semana' and productoTipo = 'Plastico' ", null);

        if(f.moveToFirst()) {
            txtPlasticoCount.setText(Integer.toString(f.getInt(0)));
            txtPlasticoPeso.setText(Integer.toString((int)f.getDouble(1))+ " g");
            txtPlasticoPuntos.setText(Integer.toString((int)f.getDouble(2))+ " ptos");
            totalResiduo+=f.getInt(0);
            totalPuntos+=f.getDouble(2);
            totalPeso+=f.getDouble(1);

        }
     /*   Cursor f2 = db.rawQuery("select cantidad ,peso ,puntuacion from Contador where tendenciaTipo = 'Semana' and productoTipo = 'Vidrio' ", null);


       if(f2.moveToFirst()) {
            txtVidrioCount.setText(Integer.toString(f2.getInt(0)));
            txtVidrioPeso.setText(Integer.toString((int)f2.getDouble(1))+ " g");
            txtVidrioPuntos.setText(Integer.toString((int)f2.getDouble(2))+ " ptos");
            totalResiduo+=f2.getInt(0);
            totalPuntos+=f2.getDouble(2);
            totalPeso+=f2.getDouble(1);
        }*/

        Cursor f3 = db.rawQuery("select cantidad ,peso ,puntuacion from Contador where tendenciaTipo = 'Semana' and productoTipo = 'Papel' ", null);
        if(f3.moveToFirst()) {
            txtPapelCartonCount.setText(Integer.toString(f3.getInt(0)));
            txtPapelCartonPeso.setText(Integer.toString((int)f3.getDouble(1)) + " g");
            txtPapelCartonPuntos.setText(Integer.toString((int)f3.getDouble(2))+ " ptos");
            totalResiduo+=f3.getInt(0);
            totalPuntos+=f3.getDouble(2);
            totalPeso+=f3.getDouble(1);
        }

      /*  Cursor f4 = db.rawQuery("select cantidad ,peso ,puntuacion from Contador where tendenciaTipo = 'Semana' and productoTipo = 'Metal' ", null);

        if(f4.moveToFirst()) {
            txtMetalesCount.setText(Integer.toString(f4.getInt(0)));
            txtMetalesPeso.setText(Integer.toString((int)f4.getDouble(1))+ " g");
            txtMetalesPuntos.setText(Integer.toString((int)f4.getDouble(2))+ " ptos");
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
        leftAxis.setLabelCount(5, true);

        if(maximo < 5)
            leftAxis.setAxisMaximum(5);
        else
            leftAxis.setAxisMaximum(maximo);

        leftAxis.setAxisMinimum(0);


        xAxis.setSpaceMax(0.5f);
        xAxis.setSpaceMin(0.5f);

        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setTextSize(8);


        chartResiduos.setDrawGridBackground(false);
        chartResiduos.setData(data);
        chartResiduos.invalidate();
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

        leftAxis.setDrawGridLines(false);
        leftAxis.setLabelCount(5, true);
        if(maximo < 5)
            leftAxis.setAxisMaximum(5);
        else
            leftAxis.setAxisMaximum(maximo);
        leftAxis.setAxisMinimum(0);

        xAxis.setSpaceMax(0.5f);
        xAxis.setSpaceMin(0.5f);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setTextSize(8);


        chartBolsas.setDrawGridBackground(false);
        chartBolsas.setData(data);
        chartBolsas.invalidate();
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
        leftAxis.setLabelCount(5, true);
        leftAxis.setAxisMaximum(1000);
        leftAxis.setAxisMinimum(0f);

        xAxis.setSpaceMax(0.5f);
        xAxis.setSpaceMin(0.5f);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setTextSize(8);

        chartPuntos.setDrawGridBackground(false);
        chartPuntos.setData(data);
        chartPuntos.invalidate();
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
        chartPuntos.getDescription().setEnabled(false);
        chartPuntos.getLegend().setEnabled(false);


    }

    private ArrayList<Entry> dataBolsasValue()
    {
        ArrayList<Entry> dataValues = new ArrayList<>();
        dbHelper helper = new dbHelper(getActivity(),"Usuario.sqlite", null, 1);
        SQLiteDatabase db = helper.getReadableDatabase();

        Cursor f = db.rawQuery("select lunes,martes,miercoles,jueves,viernes,sabado,domingo from DatosDiarios where tipo = 'bolsas' ", null);
        if(f.moveToFirst()) {
            dataValues.add(new Entry(0,f.getInt(6)));
            for (int i = 0; i < 6; i++) {
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

        Cursor f = db.rawQuery("select lunes,martes,miercoles,jueves,viernes,sabado,domingo from DatosDiarios where tipo = 'Semana' ", null);
        if(f.moveToFirst()) {
            dataValues.add(new Entry(0,f.getInt(6)));
            for (int i = 0; i < 6; i++) {
                if(maximo< f.getInt(i))
                    maximo = f.getInt(i);
                dataValues.add(new Entry(i+1,f.getInt(i)));
            }
            if(maximo< f.getInt(6))
                maximo = f.getInt(6);

        }
        return dataValues;
    }

    private ArrayList<Entry> dataPuntosValue()
    {
        ArrayList<Entry> dataValues = new ArrayList<>();
        dbHelper helper = new dbHelper(getActivity(),"Usuario.sqlite", null, 1);
        SQLiteDatabase db = helper.getReadableDatabase();

        Cursor f = db.rawQuery("select lunes,martes,miercoles,jueves,viernes,sabado,domingo from DatosDiarios where tipo = 'puntos' ", null);
        if(f.moveToFirst()) {
            dataValues.add(new Entry(0,f.getInt(6)));
            for (int i = 0; i < 6; i++) {
                dataValues.add(new Entry(i+1,f.getInt(i)));
            }

        }
        return dataValues;
    }

    private void getDaysOfWeek()
    {
        Calendar c = Calendar.getInstance();
        int dia =  c.get(Calendar.DAY_OF_WEEK);
        if(dia==Calendar.SUNDAY){
            setDays(0,c.get(Calendar.DAY_OF_MONTH),c.get(Calendar.MONTH));
        }if(dia==Calendar.MONDAY){
        setDays(1,c.get(Calendar.DAY_OF_MONTH),c.get(Calendar.MONTH));
    }
        if(dia==Calendar.TUESDAY){
            setDays(2,c.get(Calendar.DAY_OF_MONTH),c.get(Calendar.MONTH));
        }
        if(dia==Calendar.WEDNESDAY){
            setDays(3,c.get(Calendar.DAY_OF_MONTH),c.get(Calendar.MONTH));
        }
        if(dia==Calendar.THURSDAY){
            setDays(4,c.get(Calendar.DAY_OF_MONTH),c.get(Calendar.MONTH));
        }
        if(dia==Calendar.FRIDAY){
            setDays(5,c.get(Calendar.DAY_OF_MONTH),c.get(Calendar.MONTH));
        }
        if(dia==Calendar.SATURDAY){
            setDays(6,c.get(Calendar.DAY_OF_MONTH),c.get(Calendar.MONTH));
        }
    }

    private void setDays(int index,int dia,int mes)
    {
        int x = 0;
        int y = index;
        for(int i= 0 ;i<index;i++){
            axisDataMonth[i] = axisDataMonth[i]+"\n" + Integer.toString(dia-y) + "/"+Integer.toString(mes);
            y--;

        }
        for(int i=index;i<axisDataMonth.length;i++){
            axisDataMonth[i] = axisDataMonth[i]+"\n" + Integer.toString(dia+x) + "/"+Integer.toString(mes);
            x++;
        }

    }




}