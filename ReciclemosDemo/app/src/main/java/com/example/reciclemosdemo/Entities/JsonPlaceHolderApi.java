package com.example.reciclemosdemo.Entities;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Url;

public interface JsonPlaceHolderApi {
    @GET
    Call<Producto> getProductoById(@Url String url);

    @GET
    Call<QrCode> getQrCode(@Url String url);

    @GET
    Call<List<Bolsa>> getBolsasByUsuario(@Url String url);

    @GET
    Call<Bolsa> getBolsaByQrCorde(@Url String url);

    @PUT
    Call<Bolsa> putQrBolsa(@Url String url);

    @PUT
    Call<Probolsa> putCantidadProbolsa(@Url String url);

    @GET
    Call<Bolsa> getBolsaActiva(@Url String url);

    @DELETE
    Call<Probolsa> deleteProbolsa(@Url String url);

    @GET
    Call<List<Probolsa>> getProductoByIdBolsa(@Url String url);

    @GET
    Call<List<Probolsa>> getProbolsaByUsuario(@Url String url);

    @GET
    Call<List<Probolsa>> getBolsasByDate(@Url String url);

    @GET
    Call<List<Producto>> getProductos(@Url String url);

    @GET
    Call<List<Departamento>> getAllDepartamento(@Url String url);

    @GET
    Call<List<Distrito>> getDistritoByDepartamento(@Url String url);

    @GET
    Call<List<Condominio>> getCondominioByDistrito(@Url String url);

    @GET
    Call<Sexo> getSexoById(@Url String url);

    @GET
    Call<Tipo_Usuario> getTipoUsuarioById(@Url String url);

    @GET
    Call<String[]> getUsuarioByEmail(@Url String url);

    @GET
    Call<Integer> getEmail(@Url String url);

    @GET
    Call<Integer> getDNI(@Url String url);

    @GET
    Call<Usuario> getUsuarioByEmailPassword(@Url String url);

    @POST("usuario")
    Call<Usuario> createUsuario(@Body Usuario usuario);

    @POST("probolsa")
    Call<Probolsa> addProBolsa(@Body Probolsa probolsa);

    @POST("solicitud")
    Call<Solicitud> createSolicitud(@Body Solicitud solicitud);

    @GET
    Call<Reciclador> getReciclador(@Url String url);
}
