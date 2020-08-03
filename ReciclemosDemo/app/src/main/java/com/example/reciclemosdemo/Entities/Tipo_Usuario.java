package com.example.reciclemosdemo.Entities;

import com.google.gson.annotations.SerializedName;

public class Tipo_Usuario {

    @SerializedName("codigo")
    private Integer codigo;
    @SerializedName("nombre")
    private String nombre;

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

}