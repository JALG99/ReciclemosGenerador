package com.example.reciclemosdemo.Inicio;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.reciclemosdemo.Adicionales.dbHelper;
import com.example.reciclemosdemo.Entities.Bolsalist;
import com.example.reciclemosdemo.Entities.JsonPlaceHolderApi;
import com.example.reciclemosdemo.Entities.Probolsa;
import com.example.reciclemosdemo.R;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ProductoDetalleFragment extends Fragment {

    ArrayList<Bolsalist> listDetalle = new ArrayList<>();
    RecyclerView rclDetalle;
    AdapterDetalle adapter;
    ProgressDialog progressDialog;
    int position, codigo;
    private Retrofit retrofit;

    Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message message) {
            switch (message.what){
                case 1:
                    progressDialog.show();
                    progressDialog.setCancelable(false);
                    listDetalle.remove(position);
                    adapter.notifyItemRemoved(position);
                    break;
                case 2:
                    ((DetalleFragment) getParentFragment()).calcular();
                    progressDialog.cancel();
                    break;

            }
        }
    };


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    public void removeItem(int position, int codigo) throws IOException {
        this.position = position;
        this.codigo = codigo;
        dbHelper helper = new dbHelper(getActivity(),"Usuario.sqlite", null, 1);
        SQLiteDatabase db = helper.getReadableDatabase();

        db.execSQL("delete from Probolsa where codigo = " + codigo);

        JsonPlaceHolderApi jsonPlaceHolderApi=retrofit.create(JsonPlaceHolderApi.class);
        Call<Probolsa> call=jsonPlaceHolderApi.deleteProbolsa("probolsa/" + codigo);
        call.execute();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_producto_detalle, container, false);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Eliminando...");

        rclDetalle = v.findViewById(R.id.rclDetalle);
        rclDetalle.setLayoutManager(new LinearLayoutManager(getActivity()));

        retrofit = new Retrofit.Builder()
                .baseUrl("https://recyclerapiresttdp.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        dbHelper helper = new dbHelper(getActivity(),"Usuario.sqlite", null, 1);
        SQLiteDatabase db = helper.getReadableDatabase();

        Cursor f = db.rawQuery("select codigo from Bolsa where activa = 'true'", null);

        f.moveToFirst();

        Cursor fila = db.rawQuery("select bolsa, cantidad, codigo, peso, producto, puntuacion from Probolsa where bolsa = " + f.getInt(0), null);
        fila.moveToFirst();

        System.out.println(fila.getString(1));


        do {
            Cursor fila2 = db.rawQuery("select nombre, tipo_contenido, contenido, categoria, urlimagen from Producto where codigo = " + fila.getInt(4), null);
            fila2.moveToFirst();

            Cursor fila3 = db.rawQuery("select codigo, nombre from Categoria where codigo = " + fila2.getInt(3), null);
            fila3.moveToFirst();

            if(fila3.getInt(0) != 2 && fila3.getInt(0) != 4) {
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

        adapter = new AdapterDetalle(listDetalle, "");
        rclDetalle.setAdapter(adapter);

        adapter.setOnItemClickListener(new AdapterDetalle.OnItemClickListener(){
            @Override
            public void onDelete(int position, int codigo) {
                new BackgroundJob(position,codigo).execute();
            }
        });
        db.close();
        return v;
    }

    private class BackgroundJob extends AsyncTask<Void,Void,Void> {

        int position, codigo;

        public BackgroundJob(int position, int codigo){
            this.position = position;
            this.codigo = codigo;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                Message msg = new Message();
                msg.what = 1;
                mHandler.sendMessage(msg);
                removeItem(position, codigo);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void avoid){
            Message msg = new Message();
            msg.what = 2;
            mHandler.sendMessage(msg);
        }
    }
}
