package com.example.reciclemosdemo.Inicio;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.example.reciclemosdemo.Adicionales.CondicionesUso;
import com.example.reciclemosdemo.Adicionales.dbHelper;
import com.example.reciclemosdemo.Entities.Condominio;
import com.example.reciclemosdemo.Entities.Departamento;
import com.example.reciclemosdemo.Entities.Distrito;
import com.example.reciclemosdemo.Entities.JsonPlaceHolderApi;
import com.example.reciclemosdemo.Entities.Sexo;
import com.example.reciclemosdemo.Entities.Tipo_Usuario;
import com.example.reciclemosdemo.Entities.Usuario;
import com.example.reciclemosdemo.R;

import java.io.IOException;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegisterActivity extends AppCompatActivity {
    private static Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    "(?=.*[0-9])" +         //AL MENOS UN DIGITO
                    "(?=.*[a-z])" +         //AL MENOS UNA MINUSCULA
                    "(?=.*[A-Z])" +         //AL MENOS UNA MAYUSCULA
                    "(?=\\S+$)" +           //SIN ESPACIOS EN BLANCO
                    ".{6,}" +               //MINIMO 6 CARACTERES
                    "$");

    private static Pattern NOMAPE_PATTERN = Pattern.compile("^[a-zA-Z\\s]*$");

    private EditText txtNombres, txtApellidos, txtDNI, txtNacimiento, txtDireccion, txtEmail, txtPassword, txtCelular, txtRepetirPassword;
    private TextView btntxtCondominio, btntxtCondiciones;
    private Button btnRegistrar, btnLogin;
    private ImageButton btnCalendar;
    private CheckBox checkBox;
    private Spinner spnDepartamento, spnDistrito, spnCondominio;

    public Calendar c = Calendar.getInstance();

    private int dia = c.get(Calendar.DAY_OF_MONTH);
    private int mes = c.get(Calendar.MONTH);
    private int anio = c.get(Calendar.YEAR);

    private Retrofit retrofit;

    public List<Condominio> condominios = new ArrayList<>();
    public Sexo sexo = new Sexo();
    public Tipo_Usuario tipousuario = new Tipo_Usuario();


    private ProgressDialog progressDialog, progressDialog2;
    Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message message) {
            switch (message.what){
                case 1:
                    progressDialog.show();
                    progressDialog.setCancelable(false);
                    break;
                case 2:
                    txtDNI.setError("El DNI ya existe");
                    break;
                case 3:
                    txtEmail.setError("El email ya existe");
                    break;
                case 4:
                    progressDialog.cancel();
                    break;
                case 5:
                    progressDialog2.show();
                    progressDialog2.setCancelable(false);
                    break;
                case 6:
                    progressDialog2.cancel();
                    Toast.makeText(getApplicationContext(), "Registro exitoso", Toast.LENGTH_LONG).show();
                    Intent loginactivity = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(loginactivity);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        retrofit = new Retrofit.Builder()
                .baseUrl("https://recyclerapiresttdp.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        btntxtCondominio = findViewById(R.id.btntxtCondominio);
        btntxtCondiciones = findViewById(R.id.btntxtCondiciones);

        btnRegistrar = findViewById(R.id.btnRegistrar);
        btnLogin = findViewById(R.id.btnLogin);
        btnCalendar = findViewById(R.id.btnCalendar);

        txtNacimiento = findViewById(R.id.txtNacimiento);
        txtNombres = findViewById(R.id.txtNombres);
        txtApellidos = findViewById(R.id.txtApellidos);
        txtDNI = findViewById(R.id.txtDNI);
        txtDireccion = findViewById(R.id.txtDireccion);
        txtEmail = findViewById(R.id.txtEmail);
        txtPassword = findViewById(R.id.txtPassword);
        txtCelular = findViewById(R.id.txtCelular);
        txtRepetirPassword = findViewById(R.id.txtRepetirPassword);

        checkBox = findViewById(R.id.checkbox);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    txtPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    txtRepetirPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }else {
                    txtPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    txtRepetirPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

        spnDepartamento = findViewById(R.id.spnDepartamento);
        spnDistrito = findViewById(R.id.spnDistrito);
        spnCondominio = findViewById(R.id.spnCondominio);

        ListaDepartamentos();

        spnDepartamento.setOnItemSelectedListener(nOnItemSelectedListener);
        spnDistrito.setOnItemSelectedListener(mOnItemSelectedListener);

        btnLogin.setOnClickListener(mOnClickListener);
        btntxtCondiciones.setOnClickListener(nOnClickListener);
        btnRegistrar.setOnClickListener(oOnClickListener);

        btntxtCondiciones.setPaintFlags(btntxtCondiciones.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        btntxtCondominio.setPaintFlags(btntxtCondominio.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Validando, por favor espere...");

        progressDialog2 = new ProgressDialog(this);
        progressDialog2.setMessage("Registrando...");


        obtenerSexo(new EntityCallBack<Sexo>() {
            @Override
            public void onSuccess(@NonNull Sexo value) {
                sexo = value;
            }

            @Override
            public void onError(@NonNull Throwable throwable) {

            }
        });
        obtenerTipoUsuario(new EntityCallBack<Tipo_Usuario>() {
            @Override
            public void onSuccess(@NonNull Tipo_Usuario value) {
                tipousuario = value;
            }

            @Override
            public void onError(@NonNull Throwable throwable) {

            }
        });
    }

    //LISTAR DISTRITOS
    private Spinner.OnItemSelectedListener nOnItemSelectedListener = new Spinner.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            ListaDistritos();
        }
        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };

    //INTERFACE LISTA
    public interface ListCallBack<T> {

        void onSuccess(@NonNull List<T> value);

        void onError(@NonNull Throwable throwable);
    }

    //INTERFACE ENTITY
    public interface EntityCallBack<T> {

        void onSuccess(@NonNull T value);

        void onError(@NonNull Throwable throwable);
    }

    //LISTAR CONDOMINIOS
    private Spinner.OnItemSelectedListener mOnItemSelectedListener = new Spinner.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            ListaCondominios(new ListCallBack<Condominio>() {
                @Override
                public void onSuccess(@NonNull List<Condominio> value) {
                    condominios = value;
                }

                @Override
                public void onError(@NonNull Throwable throwable) {

                }
            });
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent loginactivity = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(loginactivity);
        }
    };

    private View.OnClickListener nOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent condicionesuso = new Intent(getApplicationContext(), CondicionesUso.class);
            startActivity(condicionesuso);
        }
    };

    private View.OnClickListener oOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            try {
                validacionSimple();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    };

    //SELECCIONAR FECHA
    public void obtenerFecha(View view){
        DatePickerDialog recogerFecha = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                int mesActual = month + 1;
                String diaFormateado = (dayOfMonth < 10)? "0" + String.valueOf(dayOfMonth):String.valueOf(dayOfMonth);
                String mesFormateado = (mesActual < 10)? "0" + String.valueOf(mesActual):String.valueOf(mesActual);
                txtNacimiento.setText(year + "-" + mesFormateado + "-" + diaFormateado);
            }
        },anio,mes,dia);
        recogerFecha.show();
    }

    //LISTA DEPARTAMENTOS
    public void ListaDepartamentos(){
        JsonPlaceHolderApi jsonPlaceHolderApi=retrofit.create(JsonPlaceHolderApi.class);
        Call<List<Departamento>> call=jsonPlaceHolderApi.getAllDepartamento("departamento");
        call.enqueue(new Callback<List<Departamento>>() {
            @Override
            public void onResponse(Call<List<Departamento>> call, Response<List<Departamento>> response) {
                if(response.isSuccessful()) {

                    List<Departamento> departamentos = response.body();
                    List<String> lista = new ArrayList<>();
                    lista.add("Departamento");
                    for(Departamento str : departamentos)
                    {
                        lista.add(str.getNombre());
                    }
                    ArrayAdapter<String> arreglo = new ArrayAdapter(RegisterActivity.this,android.R.layout.simple_spinner_item,lista);

                    spnDepartamento.setAdapter(arreglo);
                }else{
                    Log.e("TAG","onResponse:" + response.toString());
                }
            }

            @Override
            public void onFailure(Call<List<Departamento>> call, Throwable t) {
                Log.e("TAG","onFailure:" + t.getMessage());
            }
        });
    }

    //FILTRAR DISTRITOS
    public void ListaDistritos(){
        JsonPlaceHolderApi jsonPlaceHolderApi=retrofit.create(JsonPlaceHolderApi.class);
        Call<List<Distrito>> call=jsonPlaceHolderApi.getDistritoByDepartamento("distrito/" + spnDepartamento.getSelectedItem().toString());
        call.enqueue(new Callback<List<Distrito>>() {
            @Override
            public void onResponse(Call<List<Distrito>> call, Response<List<Distrito>> response) {
                if(response.isSuccessful()) {

                    List<Distrito> distritos = response.body();
                    List<String> lista = new ArrayList<>();
                    lista.add("Distrito");
                    for(Distrito str : distritos)
                    {
                        lista.add(str.getNombre());
                    }
                    ArrayAdapter<String> arreglo = new ArrayAdapter(RegisterActivity.this,android.R.layout.simple_spinner_item,lista);

                    spnDistrito.setAdapter(arreglo);
                }else{
                    Log.e("TAG","onResponse:" + response.toString());
                }
            }

            @Override
            public void onFailure(Call<List<Distrito>> call, Throwable t) {
                Log.e("TAG","onFailure:" + t.getMessage());
            }
        });
    }

    //FILTRAR CONDOMINIOS
    public void ListaCondominios(@Nullable final ListCallBack<Condominio> listCallBack){

        JsonPlaceHolderApi jsonPlaceHolderApi=retrofit.create(JsonPlaceHolderApi.class);
        //Call<List<Condominio>> call=jsonPlaceHolderApi.getCondominioByDistrito("condominio/" + spnDistrito.getSelectedItem().toString());
        Call<List<Condominio>> call=jsonPlaceHolderApi.getCondominioByDistrito("condominio");
        call.enqueue(new Callback<List<Condominio>>() {
            @Override
            public void onResponse(Call<List<Condominio>> call, Response<List<Condominio>> response) {
                if(response.isSuccessful()) {

                    condominios = response.body();
                    List<String> lista = new ArrayList<>();
                    lista.add("Condominios");
                    for(Condominio str : condominios)
                    {
                        lista.add(str.getNombre());
                    }
                    ArrayAdapter<String> arreglo = new ArrayAdapter(RegisterActivity.this,android.R.layout.simple_spinner_item,lista);

                    spnCondominio.setAdapter(arreglo);
                    listCallBack.onSuccess(condominios);
                }else{
                    Log.e("TAG","onResponse:" + response.toString());
                }
            }

            @Override
            public void onFailure(Call<List<Condominio>> call, Throwable t) {
                Log.e("TAG","onFailure:" + t.getMessage());
            }
        });
    }

    //INICIO ALGORTIMO PARA HASH DE CONTRASEÑA
    private static String[] generateStorngPasswordHash(String password) throws NoSuchAlgorithmException, InvalidKeySpecException {
        int iterations = 1000;
        char[] chars = password.toCharArray();
        byte[] salt = getSalt();

        PBEKeySpec spec = new PBEKeySpec(chars, salt, iterations, 64 * 8);
        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] hash = skf.generateSecret(spec).getEncoded();
        String[] respuesta = new String[2];
        String saltext = Arrays.toString(salt);
        respuesta[0] = iterations + ":" + toHex(salt) + ":" + toHex(hash);
        respuesta[1] = saltext;

        return respuesta;
    }

    private static byte[] getSalt() throws NoSuchAlgorithmException {
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        byte[] salt = new byte[16];
        sr.nextBytes(salt);
        return salt;
    }

    private static String toHex(byte[] array) throws NoSuchAlgorithmException {
        BigInteger bi = new BigInteger(1, array);
        String hex = bi.toString(16);
        int paddingLength = (array.length * 2) - hex.length();
        if(paddingLength > 0)
        {
            return String.format("%0"  +paddingLength + "d", 0) + hex;
        }else{
            return hex;
        }
    }
    //FIN DE ALGORITMO PARA HASH DE CONTRASEÑA

    //VALIDACION DE EMAIL
    private boolean validarEmail(){
        String emailInput = txtEmail.getText().toString().trim();

        if(emailInput.isEmpty()){
            txtEmail.setError("Debe llenar el campo");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()){
            txtEmail.setError("Por favor ingrese un email válido");
            return false;
        } else{
            txtEmail.setError(null);
            return true;
        }
    }

    //VALIDACION DE PASSWORD
    private boolean validarPassword(){
        String passwordInput = txtPassword.getText().toString().trim();
        String passwordRepetido = txtRepetirPassword.getText().toString().trim();

        if(passwordInput.isEmpty()){
            txtPassword.setError("Debe llenar el campo");
            return false;
        } else if (!PASSWORD_PATTERN.matcher(passwordInput).matches()){
            txtPassword.setError("Contraseña débil");
            return false;
        } else if(passwordInput.compareTo(passwordRepetido) != 0) {
            txtPassword.setError("Las contraseñas no coinciden");
            return false;
        }else {
            txtPassword.setError(null);
            return true;
        }
    }

    //VALIDACION DE DNI
    private boolean validarDNI(){
        String dniInput = txtDNI.getText().toString().trim();

        if(dniInput.isEmpty()){
            txtDNI.setError("Debe llenar el campo");
            return false;
        } else if (dniInput.length() != 8){
            txtDNI.setError("DNI inválido");
            return false;
        } else{
            txtDNI.setError(null);
            return true;
        }
    }

    //VALIDACION DE CELULAR
    private boolean validarCelular(){
        String celularInput = txtCelular.getText().toString().trim();

        if(celularInput.isEmpty()){
            txtCelular.setError("Debe llenar el campo");
            return false;
        } else if (celularInput.length() != 9){
            txtCelular.setError("Celular inválido");
            return false;
        } else{
            txtCelular.setError(null);
            return true;
        }
    }

    //VALIDACION DE EDAD
    private boolean validarEdad() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date Limite = sdf.parse("1900-12-31");
        Date nacimiento = new Date();

        Date utilDate = new Date();
        long lnMilisegundos = utilDate.getTime();
        Date Hoy = new java.sql.Date(lnMilisegundos);
        String nacimientoInput = txtNacimiento.getText().toString().trim();


        if(!nacimientoInput.isEmpty()){
            nacimiento = sdf.parse(nacimientoInput);
        }

        if(nacimientoInput.isEmpty()){
            txtNacimiento.setError("Seleccione una fecha de nacimiento");
            return false;
        } else if (nacimiento.compareTo(Hoy) >= 0 || nacimiento.compareTo(Limite) <= 0){
            txtNacimiento.setError("Seleccione una fecha válida");
            return false;
        } else{
            txtNacimiento.setError(null);
            return true;
        }
    }

    //VALIDACION DE NOMBRE
    private boolean validarNombres(){
        String nombresInput = txtNombres.getText().toString().trim();

        if(nombresInput.isEmpty()){
            txtNombres.setError("Debe llenar el campo");
            return false;
        } else if (!NOMAPE_PATTERN.matcher(nombresInput).matches()){
            txtNombres.setError("Nombre inválido");
            return false;
        } else{
            txtNombres.setError(null);
            return true;
        }
    }

    //VALIDACION DE APELLIDO
    private boolean validarApellidos(){
        String apellidosInput = txtApellidos.getText().toString().trim();

        if(apellidosInput.isEmpty()){
            txtApellidos.setError("Debe llenar el campo");
            return false;
        } else if (!NOMAPE_PATTERN.matcher(apellidosInput).matches()){
            txtApellidos.setError("Apellido inválido");
            return false;
        } else{
            txtApellidos.setError(null);
            return true;
        }
    }

    //VALIDACION DE DIRECCION
    private boolean validarDireccion(){
        String direccionInput = txtDireccion.getText().toString().trim();

        if(direccionInput.isEmpty()){
            txtDireccion.setError("Debe llenar el campo");
            return false;
        } else {
            txtDireccion.setError(null);
            return true;
        }
    }

    //VALIDACION DE CONDOMINIO
    private boolean validarCondominio(Integer condominio){
        try {
            if ((spnDepartamento.getSelectedItem().toString()).compareTo("Departamento") == 0) {
                Toast.makeText(getApplicationContext(), "Selecciona un Departamento", Toast.LENGTH_LONG).show();
                return false;
            } else {
                if((spnDistrito.getSelectedItem().toString()).compareTo("Distrito") == 0){
                    Toast.makeText(getApplicationContext(), "Selecciona un Distrito", Toast.LENGTH_LONG).show();
                    return false;
                } else if(condominio >= 1) {
                    return true;
                } else {
                    Toast.makeText(getApplicationContext(), "Selecciona un Condominio", Toast.LENGTH_LONG).show();
                    return false;
                }
            }
        }catch (Exception e){
            Toast.makeText(getApplicationContext(), "Espera que carguen los datos", Toast.LENGTH_LONG).show();
            return false;
        }
    }

    //VALIDAR EMAIL REPETIDO
    public boolean BuscarEmail(String email) throws IOException {
        JsonPlaceHolderApi jsonPlaceHolderApi=retrofit.create(JsonPlaceHolderApi.class);
        Call<Integer> call=jsonPlaceHolderApi.getEmail("usuario/correo/activo/" + email);
        Response<Integer> response = call.execute();
        Log.e("TAG","onResponse:" + response.toString());
        if(response.body() == 1){
            Message msg = new Message();
            msg.what = 3;
            mHandler.sendMessage(msg);
            return false;
        }else
            return true;
    }

    //VALIDAR EMAIL DNI
    public boolean BuscarDNI(String dni) throws IOException {
        JsonPlaceHolderApi jsonPlaceHolderApi=retrofit.create(JsonPlaceHolderApi.class);
        Call<Integer> call=jsonPlaceHolderApi.getDNI("usuario/dni/activo/" + dni);
        Response<Integer> response = call.execute();
        Log.e("TAG","onResponse:" + response.toString());
        if(response.body() == 1){
            Message msg = new Message();
            msg.what = 2;
            mHandler.sendMessage(msg);
            return false;
        }else
            return true;
    }

    //OBETENER SEXO
    public void obtenerSexo(@Nullable final EntityCallBack entityCallBack){
        JsonPlaceHolderApi jsonPlaceHolderApi=retrofit.create(JsonPlaceHolderApi.class);
        Call<Sexo> call=jsonPlaceHolderApi.getSexoById("sexo/1");
        call.enqueue(new Callback<Sexo>() {
            @Override
            public void onResponse(Call<Sexo> call, Response<Sexo> response) {
                if(response.isSuccessful()) {
                    sexo = response.body();
                    entityCallBack.onSuccess(sexo);
                }else{
                    Log.e("TAG","onResponse:" + response.toString());
                }
            }

            @Override
            public void onFailure(Call<Sexo> call, Throwable t) {
                Log.e("TAG","onFailure:" + t.getMessage());
            }
        });
    }

    //OBTENER TIPOUSUARIO
    public void obtenerTipoUsuario(@Nullable final EntityCallBack entityCallBack){
        JsonPlaceHolderApi jsonPlaceHolderApi=retrofit.create(JsonPlaceHolderApi.class);
        Call<Tipo_Usuario> call=jsonPlaceHolderApi.getTipoUsuarioById("tipousuario/1");
        call.enqueue(new Callback<Tipo_Usuario>() {
            @Override
            public void onResponse(Call<Tipo_Usuario> call, Response<Tipo_Usuario> response) {
                if(response.isSuccessful()) {
                    tipousuario = response.body();
                    entityCallBack.onSuccess(tipousuario);
                }else{
                    Log.e("TAG","onResponse:" + response.toString());
                }
            }

            @Override
            public void onFailure(Call<Tipo_Usuario> call, Throwable t) {
                Log.e("TAG","onFailure:" + t.getMessage());
            }
        });
    }

    public void validacionSimple() throws ParseException {
        String valor_nombres = txtNombres.getText().toString();
        String valor_apellidos = txtApellidos.getText().toString();
        String valor_dni = txtDNI.getText().toString();
        String valor_nacimiento = txtNacimiento.getText().toString();
        Integer valor_condominio = spnCondominio.getSelectedItemPosition();
        String valor_direccion = txtDireccion.getText().toString();
        String valor_email = txtEmail.getText().toString().toLowerCase();
        String valor_celular = txtCelular.getText().toString();
        String password = txtPassword.getText().toString();

        if(validarNombres() & validarApellidos() & validarDNI() & validarEdad()
                & validarCondominio(valor_condominio) & validarDireccion()
                & validarEmail() & validarPassword() & validarCelular()) {
            new BackgroundJob(valor_nombres, valor_apellidos, valor_dni, valor_nacimiento, valor_condominio,
                    valor_direccion, valor_email, valor_celular, password).execute();
        }
    }

    public boolean Registrar(String valor_nombres, String valor_apellidos, String valor_dni, String valor_nacimiento, Integer valor_condominio,
                             String valor_direccion, String valor_email, String valor_celular, String password) throws InvalidKeySpecException, NoSuchAlgorithmException, ParseException, IOException {

        if(BuscarDNI(valor_dni) & BuscarEmail(valor_email)) {
            Message msg2 = new Message();
            msg2.what = 5;
            mHandler.sendMessage(msg2);
            String[] valor_password = generateStorngPasswordHash(password);
            Usuario user = new Usuario();
            user.setNombre(valor_nombres);
            user.setApellido(valor_apellidos);
            user.setCondominio(condominios.get(spnCondominio.getSelectedItemPosition() - 1));
            user.setDireccion(valor_direccion);
            user.setDni(valor_dni);
            user.setFechanacimiento(valor_nacimiento);
            user.setEmail(valor_email);
            user.setTelefono(valor_celular);
            user.setSalt(valor_password[1]);
            user.setPassword(valor_password[0]);
            user.setTipo_Usuario(tipousuario);
            user.setSexo(sexo);
            JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);
            Call<Usuario> call = jsonPlaceHolderApi.createUsuario(user);
            call.execute();
            return true;
        }else{
            return false;
        }
    }

    private class BackgroundJob extends AsyncTask<Void,Void,Void> {

        boolean resultado = false;
        String valor_nombres, valor_apellidos, valor_dni, valor_nacimiento, valor_direccion, valor_email, valor_celular, password;
        Integer valor_condominio;

        public BackgroundJob(String valor_nombres, String valor_apellidos, String valor_dni, String valor_nacimiento, Integer valor_condominio,
                      String valor_direccion, String valor_email, String valor_celular, String password){
            this.valor_nombres = valor_nombres;
            this.valor_apellidos = valor_apellidos;
            this.valor_dni = valor_dni;
            this.valor_nacimiento = valor_nacimiento;
            this.valor_direccion = valor_direccion;
            this.valor_condominio = valor_condominio;
            this.valor_direccion = valor_direccion;
            this.valor_email = valor_email;
            this.valor_celular = valor_celular;
            this.password = password;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                Message msg = new Message();
                msg.what = 1;
                mHandler.sendMessage(msg);
                resultado = Registrar(valor_nombres, valor_apellidos, valor_dni, valor_nacimiento, valor_condominio,
                        valor_direccion, valor_email, valor_celular, password);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InvalidKeySpecException | NoSuchAlgorithmException | ParseException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void avoid){
            if(resultado){
                Message msg2 = new Message();
                msg2.what = 4;
                mHandler.sendMessage(msg2);
                Message msg = new Message();
                msg.what = 6;
                mHandler.sendMessage(msg);
            } else{
                Message msg = new Message();
                msg.what = 4;
                mHandler.sendMessage(msg);
            }
        }
    }

}
