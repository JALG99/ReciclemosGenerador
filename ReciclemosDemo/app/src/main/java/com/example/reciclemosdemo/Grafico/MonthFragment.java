package com.example.reciclemosdemo.Grafico;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.reciclemosdemo.Adicionales.dbHelper;
import com.example.reciclemosdemo.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.LineChartView;


public class MonthFragment extends Fragment {
    View view;
    private com.example.reciclemosdemo.Grafico.RetrofitMain retrofit;
    private ArrayList<TextView> textList = new ArrayList<>();
    TextView txtPlasticoCount;
    TextView title;
    TextView txtVidrioCount;
    TextView txtMetalesCount;
    TextView txtPapelCartonCount,txtResiduosCount,txtPesoResiduos,txtPuntajeResiduos;
    TextView txtBolsasCount;
    private int totalResiduo;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_month,container,false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        retrofit = new com.example.reciclemosdemo.Grafico.RetrofitMain();
        title = view.findViewById(R.id.txtCuenta);
        txtPlasticoCount =view.findViewById(R.id.txtInputPlastico) ;
        txtPapelCartonCount =view.findViewById(R.id.txtInputPapelCarton) ;
        txtResiduosCount = getView().findViewById(R.id.txtInputResiduos);
        txtBolsasCount = getView().findViewById(R.id.txtInputBolsas);
        totalResiduo=0;
        getDataWeek();
        txtResiduosCount.setText(Integer.toString(totalResiduo));
        Date date = new Date();
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Europe/Paris"));
        cal.setTime(date);
        int year = cal.get(Calendar.YEAR);
        title.setText("Resumen "+Integer.toString(year));

    }

    public void getDataWeek() {
        dbHelper helper = new dbHelper(getActivity(),"Usuario.sqlite", null, 1);
        SQLiteDatabase db = helper.getReadableDatabase();

        Cursor f = db.rawQuery("select cantidad ,peso ,puntuacion,bolsa from Contador where tendenciaTipo = 'Year' and productoTipo = 'Plastico' ", null);

        if(f.moveToFirst()) {
            txtPlasticoCount.setText(Integer.toString(f.getInt(0)));
            totalResiduo+=f.getInt(0);
            txtBolsasCount.setText(Integer.toString(f.getInt(3)));

        }
   /*     Cursor f2 = db.rawQuery("select cantidad ,peso ,puntuacion,bolsa from Contador where tendenciaTipo = 'Year' and productoTipo = 'Vidrio' ", null);


        if(f2.moveToFirst()) {
            txtVidrioCount.setText(Integer.toString(f2.getInt(0)));
            totalResiduo+=f2.getInt(0);
            txtBolsasCount.setText(Integer.toString(f.getInt(3)));
        }*/

        Cursor f3 = db.rawQuery("select cantidad ,peso ,puntuacion,bolsa from Contador where tendenciaTipo = 'Year' and productoTipo = 'Papel' ", null);
        if(f3.moveToFirst()) {
            txtPapelCartonCount.setText(Integer.toString(f3.getInt(0)));
            totalResiduo+=f3.getInt(0);
            txtBolsasCount.setText(Integer.toString(f.getInt(3)));
        }

     /*   Cursor f4 = db.rawQuery("select cantidad ,peso ,puntuacion,bolsa from Contador where tendenciaTipo = 'Year' and productoTipo = 'Metal' ", null);

        if(f4.moveToFirst()) {
            txtMetalesCount.setText(Integer.toString(f4.getInt(0)));
            totalResiduo+=f4.getInt(0);
            txtBolsasCount.setText(Integer.toString(f.getInt(3)));
        }*/
    }



}