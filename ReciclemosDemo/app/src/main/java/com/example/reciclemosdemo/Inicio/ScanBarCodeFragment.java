package com.example.reciclemosdemo.Inicio;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.reciclemosdemo.Adicionales.PermissionUtil;
import com.example.reciclemosdemo.R;
import com.google.zxing.ResultPoint;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ScanBarCodeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ScanBarCodeFragment extends Fragment {

    DecoratedBarcodeView decoratedBarcodeView;
    Context context;

    public static ScanBarCodeFragment newInstance() {
        return new ScanBarCodeFragment();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_scan_bar_code, container, false);;

        decoratedBarcodeView = v.findViewById(R.id.zxing_barcode_scanner);

        if(PermissionUtil.on().requestPermission(this, Manifest.permission.CAMERA))
            scan();
        return v;
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

    void scan(){
        decoratedBarcodeView.setStatusText("");
        decoratedBarcodeView.resume();
        decoratedBarcodeView.decodeSingle(new BarcodeCallback() {
            @Override
            public void barcodeResult(BarcodeResult result) {
                decoratedBarcodeView.pause();

                if (result != null) {
                    ((ScanBarCodeActivity) getActivity()).cambiarProducto(result.getText());
                } else {
                    decoratedBarcodeView.resume();
                }
            }

            @Override
            public void possibleResultPoints(List<ResultPoint> resultPoints) {

            }
        });
    }
}
