package com.example.reciclemosdemo.Inicio;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.reciclemosdemo.Adicionales.PermissionUtil;
import com.example.reciclemosdemo.Adicionales.dbHelper;
import com.example.reciclemosdemo.Entities.Bolsa;
import com.example.reciclemosdemo.Entities.JsonPlaceHolderApi;
import com.example.reciclemosdemo.Entities.QrCode;
import com.example.reciclemosdemo.R;
import com.google.zxing.ResultPoint;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class QrFragment extends Fragment {

    private Button btnQr;
    private Retrofit retrofit;
    private ProgressDialog progressDialog;
    DecoratedBarcodeView decoratedBarcodeView;
    Context context;

    public static QrFragment newInstance() {
        return new QrFragment();
    }

    public void pauseScan(){
        decoratedBarcodeView.pause();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private Handler mHandler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message message) {
            switch (message.what){
                case 1:
                    progressDialog.show();
                    progressDialog.setCancelable(false);
                    break;
                case 2:
                    progressDialog.cancel();
                    Toast.makeText(getActivity(), "No existe código QR", Toast.LENGTH_LONG).show();
                    scan();
                    break;
                case 3:
                    progressDialog.cancel();
                    ((RegistrarBolsaActivity) getActivity()).registroExitoso();
                    break;
                case 4:
                    progressDialog.cancel();
                    Toast.makeText(getActivity(), "El código QR ya fue utilizado", Toast.LENGTH_LONG).show();
                    scan();
                    break;
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_qr, container, false);

        retrofit = new Retrofit.Builder()
                .baseUrl("https://recyclerapiresttdp.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Enlazando QR a la bolsa...");

        decoratedBarcodeView = v.findViewById(R.id.zxing_barcode_scanner);
        decoratedBarcodeView.setStatusText("");

        if(PermissionUtil.on().requestPermission(this, Manifest.permission.CAMERA))
            scan();

        return v;
    }

    void scan(){
        decoratedBarcodeView.resume();
        decoratedBarcodeView.decodeSingle(new BarcodeCallback() {
            @Override
            public void barcodeResult(BarcodeResult result) {
                decoratedBarcodeView.pause();

                if (result != null) {
                    decoratedBarcodeView.pause();
                    new Background(result.getText()).execute();
                } else {
                    decoratedBarcodeView.resume();
                }
            }

            @Override
            public void possibleResultPoints(List<ResultPoint> resultPoints) {

            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null)
            if (result.getContents() != null) {

            } else {
                Toast.makeText(getActivity().getApplicationContext(), "Error al escanear", Toast.LENGTH_LONG).show();
            }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PermissionUtil.REQUEST_CODE_PERMISSION_DEFAULT) {
            boolean isAllowed = true;

            for (int i = 0; i < permissions.length; i++) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    isAllowed = false;
                }
            }

            if (isAllowed) {
                scan();
            }
        }
    }

    public void GetQr(String qr) throws IOException {
        JsonPlaceHolderApi jsonPlaceHolderApi=retrofit.create(JsonPlaceHolderApi.class);
        Call<QrCode> call=jsonPlaceHolderApi.getQrCode("qrcode/qrCode/" + qr);
        Response<QrCode> response = call.execute();
        Log.e("TAG","onResponse:" + response.toString());

        Call<Bolsa> call4 =jsonPlaceHolderApi.getBolsaByQrCorde("bolsa/qrCode/" + qr);
        Response<Bolsa> response4 = call4.execute();
        Log.e("TAG","onResponse:" + response4.toString());

        try{
            if(response.body().getQrCode().compareTo(qr) == 0 && response4.code() != 200){

                dbHelper helper = new dbHelper(getActivity(),"Usuario.sqlite", null, 1);
                SQLiteDatabase db = helper.getReadableDatabase();

                Cursor fila = db.rawQuery("select codigo from Bolsa where activa = 'true'", null);

                fila.moveToFirst();

                db.execSQL("update Bolsa set activa = 'false' where codigo = " + fila.getInt(0));

                Call<Bolsa> call2=jsonPlaceHolderApi.putQrBolsa("bolsa/" + fila.getInt(0) + "/qrCode/" + qr);
                Response<Bolsa> response2 = call2.execute();
                Log.e("TAG","onResponse:" + response2.toString());

                Cursor fila2 = db.rawQuery("select codigo from Usuario", null);

                fila2.moveToFirst();

                Call<Bolsa> call3=jsonPlaceHolderApi.getBolsaActiva("bolsa/activa/" + fila2.getInt(0));
                Response<Bolsa> response3 = call3.execute();
                Log.e("TAG","onResponse:" + response3.toString());

                Bolsa nueva = response3.body();

                String query = "insert into Bolsa (codigo, activa, creadoFecha, puntuacion, qrcode, recojoFecha, observaciones) " +
                        "values (" + nueva.getCodigo() + ", 'true', " + null + ", " + nueva.getPuntuacion() + ", " + null
                        + ", " + null + "," + null + ")";
                db.execSQL(query);
                System.out.println(query);

                Message msg = new Message();
                msg.what = 3;
                mHandler.sendMessage(msg);
            } else{
                Message msg = new Message();
                msg.what = 4;
                mHandler.sendMessage(msg);
            }
        }catch (Exception e){
            Message msg = new Message();
            msg.what = 2;
            mHandler.sendMessage(msg);
        }

    }

    public class Background extends AsyncTask<Void,Void,Void> {

        String qr;

        public Background(String qr){
            this.qr = qr;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                Message msg = new Message();
                msg.what = 1;
                mHandler.sendMessage(msg);
                GetQr(qr);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void avoid){
        }
    }
}
