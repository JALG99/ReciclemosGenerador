package com.example.reciclemosdemo.Grafico;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.reciclemosdemo.Adicionales.dbHelper;
import com.example.reciclemosdemo.Entities.Bolsa;
import com.example.reciclemosdemo.Entities.JsonPlaceHolderApi;
import com.example.reciclemosdemo.Entities.Probolsa;
import com.example.reciclemosdemo.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BolsasUserFragment extends Fragment  implements ListaBolsaAdapter.OnNoteListener{
    View view;
    private Retrofit retrofit;
    private RecyclerView recyclerView;
    private ListaBolsaAdapter listBolsasAdapter;
    private ArrayList<TextView> textList = new ArrayList<>();
    private TextView txtBolsasCount,txtPesoCount,txtPuntuacionCount;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.bolsas_fragment,container,false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler);
        listBolsasAdapter = new ListaBolsaAdapter(getContext());
        txtBolsasCount =view.findViewById(R.id.txtCantBolsasHoy) ;
        txtPesoCount =view.findViewById(R.id.txtPesoBolsasHoy) ;
        txtPuntuacionCount = view.findViewById(R.id.txtPtosBolsasHoy) ;
        textList.add(txtBolsasCount);
        textList.add(txtPesoCount);
        textList.add(txtPuntuacionCount);
        recyclerView.setAdapter(listBolsasAdapter);
        recyclerView.setHasFixedSize(true);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 1);
        recyclerView.setLayoutManager(layoutManager);
        retrofit = new Retrofit.Builder()
                .baseUrl("https://recyclerapiresttdp.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        String usuarioID = ((InitialValues)this.getActivity().getApplication()).getIdUsuario();

        try {
            obtenerDatos(usuarioID);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        // obtenerBolsasByDay("bolsasDay/",usuarioID,textList);
        return view;

    }

    private void obtenerDatos(String usuarioID) throws ParseException {

        dbHelper helper = new dbHelper(getActivity(),"Usuario.sqlite", null, 1);
        SQLiteDatabase db = helper.getReadableDatabase();
        ArrayList<Bolsa> listaprodbolsas = new ArrayList<>();
        Cursor f1 = db.rawQuery("select codigo from LastBolsas ",null);
        int codigoBolsa;
        if(f1.moveToFirst()){
            do{
                Bolsa bolsa = new Bolsa();
                codigoBolsa = f1.getInt(0);
                System.out.println(codigoBolsa);
                String query = "select codigo , creadoFecha , puntuacion  , recojoFecha ,observaciones  from Bolsa where codigo = "+codigoBolsa;
                System.out.println(query);
                Cursor f = db.rawQuery(query,null);
                f.moveToFirst();
                bolsa.setCodigo(f.getInt(0));
                System.out.println(bolsa.getCodigo());
                bolsa.setActiva(false);
                String sDate1 = f.getString(1);
                SimpleDateFormat  smpDate = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
                Date date1 = smpDate.parse(sDate1);
                bolsa.setCreadoFecha(date1);
                bolsa.setObservaciones(f.getString(4));
                bolsa.setPuntuacion(f.getInt(2));
                if(f.getString(3).equals("null") || f.getString(3).equals(null)) {
                    bolsa.setRecojoFecha(null);
                }else{
                    String sDate2 = f.getString(3);
                    Date date2 = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy",Locale.ENGLISH).parse(sDate2);
                    bolsa.setRecojoFecha(date2);

                }
                listaprodbolsas.add(0,bolsa);

            }while(f1.moveToNext());
        }
        listBolsasAdapter.adicionarListaCancion(listaprodbolsas);
    }

    /*public void obtenerBolsasByDay(String urlDate,String usuarioID,ArrayList<TextView> textViewsList){
        dbHelper helper = new dbHelper(getActivity(),"Usuario.sqlite", null, 1);
        SQLiteDatabase db = helper.getReadableDatabase();

        Cursor f = db.rawQuery("select cantidad ,peso ,puntuacion from Contador where tendenciaTipo = 'Dia' ", null);
        if(f.moveToFirst()) {
            textViewsList.get(0).setText(Integer.toString(f.getInt(0)));
            textViewsList.get(2).setText(Integer.toString(f.getInt(2)));
            textViewsList.get(1).setText(Integer.toString(f.getInt(1)/1000));
        }
    }*/

    @Override
    public void oneNoteClick(int position) {

    }
}