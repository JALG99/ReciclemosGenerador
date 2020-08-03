package com.example.reciclemosdemo.Entities;

import java.util.Date;

public class Bolsa {
    private int codigo;
    private Date creadoFecha;
    private Date recojoFecha;
    private int puntuacion;
    private boolean activa;
    private QrCode qrCode;
    private String observaciones;

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public Date getCreadoFecha() {
        return creadoFecha;
    }

    public void setCreadoFecha(Date creadoFecha) {
        this.creadoFecha = creadoFecha;
    }

    public boolean getActiva() {
        return activa;
    }

    public void setActiva(boolean activa) {
        this.activa = activa;
    }

    public Date getRecojoFecha() {
        return recojoFecha;
    }

    public void setRecojoFecha(Date recojoFecha) {
        this.recojoFecha = recojoFecha;
    }

    public int getPuntuacion() {
        return puntuacion;
    }

    public void setPuntuacion(int puntuacion) {
        this.puntuacion = puntuacion;
    }

    public QrCode getQrCode() {
        return qrCode;
    }

    public void setQrCode(QrCode qrCode) {
        this.qrCode = qrCode;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
}
