package com.example.reciclemosdemo.Adicionales;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.reciclemosdemo.Inicio.APIToSQLite;

public class dbHelper extends SQLiteOpenHelper {
    public dbHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public void UpdateTable(SQLiteDatabase db){
        db.execSQL("drop table Bolsa");
        db.execSQL("drop table Probolsa");
        db.execSQL("drop table Contador");
        db.execSQL("drop table DatosDiarios");
        db.execSQL("drop table DatosAnuales");
        db.execSQL("drop table LastBolsas");
        db.execSQL("drop table LastProbolsas");
        db.execSQL("create table Bolsa(id_lite integer primary key autoincrement, codigo integer, activa string, creadoFecha string, puntuacion integer, qrcode string, recojoFecha string, observaciones string)");
        db.execSQL("create table Probolsa(id_lite integer primary key autoincrement, bolsa integer, cantidad integer, codigo integer, peso double, producto integer, puntuacion integer)");
        db.execSQL("create table Contador(codigo integer primary key autoincrement,tendenciaTipo string ,productoTipo string,cantidad integer,peso double,puntuacion double,bolsa integer)");
        db.execSQL("create table DatosDiarios(codigo integer primary key autoincrement,tipo string,lunes integer,martes integer,miercoles integer,jueves integer,viernes integer,sabado integer,domingo integer)  ");
        db.execSQL("create table DatosAnuales(codigo integer primary key autoincrement,tipo_residuo string,enero integer,febrero integer,marzo integer,abril integer,mayo integer,junio integer,julio integer,agosto integer,setiembre integer,octubre integer,noviembre integer,diciembre integer,tipo string)  ");
        db.execSQL("create table LastBolsas(codigo integer primary key,string tipo)");
        db.execSQL("create  table LastProbolsas(codigo integer primary key autoincrement,peso_prim double,mes string,codigo_producto_prim integer,tipo_producto string, peso_sec double, codigo_producto_sec integer, peso_ter double, codigo_producto_ter integer)");
    }

    public void DropCreate(SQLiteDatabase db){
        db.execSQL("drop table Bolsa");
        db.execSQL("drop table Categoria");
        db.execSQL("drop table Probolsa");
        db.execSQL("drop table Producto");
        db.execSQL("drop table Sexo");
        db.execSQL("drop table Usuario");
        db.execSQL("drop table Contador");
        db.execSQL("drop table DatosDiarios");
        db.execSQL("drop table DatosAnuales");
        db.execSQL("drop table LastBolsas");
        db.execSQL("drop table LastProbolsas");
        db.execSQL("drop table Reciclador");
        onCreate(db);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table Bolsa(id_lite integer primary key autoincrement, codigo integer, activa string, creadoFecha string, puntuacion integer, qrcode string, recojoFecha string, observaciones string)");
        db.execSQL("create table Categoria(codigo integer primary key, nombre string)");
        db.execSQL("insert into Categoria(codigo, nombre) values (1, 'Plástico'),(2, 'Vidrio'),(3, 'Papel/Carton'),(4, 'Metal')");
        db.execSQL("create table Probolsa(id_lite integer primary key autoincrement, bolsa integer, cantidad integer, codigo integer, peso double, producto integer, puntuacion integer)");
        db.execSQL("create table Producto(id_lite integer primary key autoincrement, barcode string, categoria integer, codigo integer, contenido double, descripcion string, nombre string, peso double, tipo_contenido string, urlimagen string)");
        db.execSQL("create table Sexo(codigo integer primary key autoincrement, nombre string)");
        db.execSQL("insert into Sexo(nombre) values ('Hombre'),('Mujer'),('Otros')");
        db.execSQL("create table Usuario(codigo integer primary key, nombre string, apellido string, condominio string, direccion string, dni string, email string, fecha_Nacimiento string, sexo string, telefono string,distrito_name string,condominio_direccion string,departamento_name string)");
        db.execSQL("create table Contador(codigo integer primary key autoincrement,tendenciaTipo string ,productoTipo string,cantidad integer,peso double,puntuacion double,bolsa integer)");
        db.execSQL("create table DatosDiarios(codigo integer primary key autoincrement,tipo string,lunes integer,martes integer,miercoles integer,jueves integer,viernes integer,sabado integer,domingo integer)  ");
        db.execSQL("create table DatosAnuales(codigo integer primary key autoincrement,tipo_residuo string,enero integer,febrero integer,marzo integer,abril integer,mayo integer,junio integer,julio integer,agosto integer,setiembre integer,octubre integer,noviembre integer,diciembre integer,tipo string)  ");
        db.execSQL("create table LastBolsas(codigo integer primary key,string tipo)");
        db.execSQL("create  table LastProbolsas(codigo integer primary key autoincrement,peso_prim double,mes string,codigo_producto_prim integer,tipo_producto string, peso_sec double, codigo_producto_sec integer, peso_ter double, codigo_producto_ter integer)");
        db.execSQL("create table Reciclador(codigo integer primary key,distrito integer,asociacion string,zona string, password string,salt string,apellido string,direccion string,dni string,email string,fecha_Nacimiento string,nombre string,codFormalizado string,celular string , imagen string)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
