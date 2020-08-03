package com.example.reciclemosdemo.Grafico;

import android.app.Application;
import android.content.Context;

import com.example.reciclemosdemo.R;

public class InitialValues extends Application {
    private static InitialValues initialValues;

    public static Context getContext() {
        return initialValues.getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initialValues = this;
    }

    private String idUsuario;
    private String observacion;

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

}
