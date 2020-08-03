package com.example.reciclemosdemo.Inicio;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.reciclemosdemo.Adicionales.dbHelper;
import com.example.reciclemosdemo.R;

public class DetalleFragment extends Fragment {

    TextView txtTotalPuntos, txtTotalCantidad;
    TextView btnTxtVincular;
    int TotalPuntos;
    int TotalCantidad;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_detalle, container, false);

        txtTotalPuntos = v.findViewById(R.id.txtTotalPuntos);
        txtTotalCantidad = v.findViewById(R.id.txtTotalCantidad);
        btnTxtVincular = v.findViewById(R.id.btnTxtVincular);

        btnTxtVincular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent regisrtarbolsaactivity = new Intent(getContext(), RegistrarBolsaActivity.class);
                startActivity(regisrtarbolsaactivity);
            }
        });
        calcular();

        return v;
    }

    public void calcular(){
        TotalPuntos = 0;
        TotalCantidad = 0;
        dbHelper helper = new dbHelper(getActivity(),"Usuario.sqlite", null, 1);
        SQLiteDatabase db = helper.getReadableDatabase();

        Cursor fila2 = db.rawQuery("select codigo from Bolsa where activa = 'true'", null);

        fila2.moveToFirst();

        Cursor fila = db.rawQuery("select cantidad, puntuacion, producto from Probolsa where bolsa = " + fila2.getInt(0), null);

        if(fila.moveToFirst()){
                do {
                    Cursor fila3 = db.rawQuery("select categoria from Producto where codigo = " + fila.getInt(2), null);
                    fila3.moveToFirst();
                    if(fila3.getInt(0) != 2 && fila3.getInt(0) != 4) {
                        TotalPuntos += fila.getInt(1);
                        TotalCantidad += fila.getInt(0);
                    }
                } while (fila.moveToNext());
                System.out.println(TotalPuntos);
                System.out.println(TotalCantidad);
                db.close();

                txtTotalPuntos.setText(TotalPuntos + "");
                txtTotalCantidad.setText(TotalCantidad + "");
        }else{
            ((BolsaActivity) getActivity()).setEscaneaAlgoFragment();
        }

    }
}
