package com.example.reciclemosdemo.Entities;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Usuario {

    @SerializedName("apellido")
    private String apellido;

    @SerializedName("codigo")
    private Integer codigo;

    @SerializedName("condominio")
    private Condominio condominio;

    @SerializedName("direccion")
    private String direccion;

    @SerializedName("dni")
    private String dni;

    @SerializedName("email")
    private String email;

    @SerializedName("nombre")
    private String nombre;

    @SerializedName("password")
    private String password;

    @SerializedName("salt")
    private String salt;

    @SerializedName("sexo")
    private Sexo sexo;

    @SerializedName("fecha_Nacimiento")
    private String fechanacimiento;

    @SerializedName("tipo_Usuario")
    private Tipo_Usuario tipo_Usuario;

    @SerializedName("telefono")
    private String telefono;

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getFechanacimiento() {
        return fechanacimiento;
    }

    public void setFechanacimiento(String fechanacimiento) { this.fechanacimiento = fechanacimiento; }

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public Condominio getCondominio() {
        return condominio;
    }

    public void setCondominio(Condominio condominio) {
        this.condominio = condominio;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public Sexo getSexo() {
        return sexo;
    }

    public void setSexo(Sexo sexo) {
        this.sexo = sexo;
    }

    public Tipo_Usuario getTipo_Usuario() {
        return tipo_Usuario;
    }

    public void setTipo_Usuario(Tipo_Usuario tipo_Usuario) {
        this.tipo_Usuario = tipo_Usuario;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

}