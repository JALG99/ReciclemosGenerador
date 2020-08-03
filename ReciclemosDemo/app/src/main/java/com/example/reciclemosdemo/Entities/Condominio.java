package com.example.reciclemosdemo.Entities;

import com.google.gson.annotations.SerializedName;

public class Condominio {

    @SerializedName("codigo")
    private Integer codigo;
    @SerializedName("direccion")
    private String direccion;
    @SerializedName("distrito")
    private Distrito distrito;
    @SerializedName("nombre")
    private String nombre;

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public Distrito getDistrito() {
        return distrito;
    }

    public void setDistrito(Distrito distrito) {
        this.distrito = distrito;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

}