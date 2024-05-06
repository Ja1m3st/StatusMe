package es.sanchez.jaime.statusme;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Main_Seleccion extends AppCompatActivity implements View.OnClickListener{

    private TextView weatherTextView;
    FirebaseManager firebaseManager;
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_seleccion);
        String nombre = "";

        firebaseManager = new FirebaseManager();
        Date fechaActual = new Date();
        SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String fechaFormateada = formatoFecha.format(fechaActual);


        CheckBox checkBoxFeliz = findViewById(R.id.feliz);
        CheckBox checkBoxMedio = findViewById(R.id.medio);
        CheckBox checkBoxMal = findViewById(R.id.mal);
        //TextView fecha = findViewById(R.id.dia);
        Button guardar = findViewById(R.id.botonguardar);
        TextView saludo = findViewById(R.id.saludo);

        //fecha.setText(fechaFormateada);
        checkBoxFeliz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBoxFeliz.isChecked()) {
                    checkBoxMedio.setChecked(false);
                    checkBoxMal.setChecked(false);
                }
            }
        });

        checkBoxMedio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBoxMedio.isChecked()) {
                    checkBoxFeliz.setChecked(false);
                    checkBoxMal.setChecked(false);
                }
            }
        });

        checkBoxMal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBoxMal.isChecked()) {
                    checkBoxFeliz.setChecked(false);
                    checkBoxMedio.setChecked(false);
                }
            }
        });

        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser usuario = FirebaseAuth.getInstance().getCurrentUser();
                String nombreUsuario = usuario.getEmail();

                if (checkBoxFeliz.isChecked() || checkBoxMedio.isChecked() || checkBoxMal.isChecked()) {
                    firebaseManager.guardarArrayListEnFirebase(nombreUsuario, guardarRegistro());
                    Context context = v.getContext();
                    Toast.makeText(context, "Nuevo registro añadido", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Main_Seleccion.this, "Por favor, selecciona al menos una opción", Toast.LENGTH_SHORT).show();
                }
            }
        });

        weatherTextView = findViewById(R.id.dia);

        WeatherAPI weatherAPI = WeatherService.getWeatherAPI();
        // Introduce las coordenadas de tu ubicación
        double latitude = 40.416775 ;
        double longitude = -3.703790 ;
        Call<WeatherData> call = weatherAPI.getWeatherByCoords(latitude, longitude, WeatherService.getApiKey());

        call.enqueue(new Callback<WeatherData>() {
            @Override
            public void onResponse(Call<WeatherData> call, Response<WeatherData> response) {
                if (response.isSuccessful() && response.body() != null) {
                    WeatherData weatherData = response.body();
                    double tempCelsius = weatherData.getMain().getTemp() - 273.15; // Convertir de Kelvin a Celsius
                    weatherTextView.setText("Temperatura en tus coordenadas: " + tempCelsius + "°C");
                } else {
                    Toast.makeText(Main_Seleccion.this, "Error al obtener datos del clima", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<WeatherData> call, Throwable t) {
                Toast.makeText(Main_Seleccion.this, "Error de red", Toast.LENGTH_SHORT).show();
            }
        });

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);

        if (SesionGoogle() != null) {
            nombre = account.getGivenName();
        } else if (SesionAuth() != null) {
            nombre = "Usuario";
        }
        //fecha.setText(fechaFormateada);

        TextAnimator animator = new TextAnimator("A ver que tal", saludo);
        animator.setDuration(3000);
        saludo.startAnimation(animator);
    }

    private String SesionGoogle(){
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if (account != null) {
            return account.getEmail();
        } else {
            return null;
        }
    }
    private String SesionAuth(){
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null) {
            String email = currentUser.getEmail();
            return email;
        } else {
            return null;
        }
    }

    public ArrayList<Object> guardarRegistro() {
        ArrayList<String> valoresSeleccionados = new ArrayList<>();
        ArrayList<String> actividadesSeleccionadas = new ArrayList<>();
        ArrayList<Object> dia = new ArrayList<>();

        Date fechaActual = new Date();
        SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String fechaFormateada = formatoFecha.format(fechaActual);
        String clima = "Soleado";

        CheckBox checkBox_bien = findViewById(R.id.feliz);
        CheckBox checkBox_normal = findViewById(R.id.medio);
        CheckBox checkBox_mal = findViewById(R.id.mal);
        CheckBox checkBox_correr = findViewById(R.id.correr);
        CheckBox checkBox_jugar = findViewById(R.id.jugar);
        CheckBox checkBox_trabajar = findViewById(R.id.trabajar);
        CheckBox checkBox_familia = findViewById(R.id.familia);
        CheckBox checkBox_amigos = findViewById(R.id.amigos);
        CheckBox checkBox_cita = findViewById(R.id.amor);
        CheckBox checkBox_television = findViewById(R.id.television);
        CheckBox checkBox_compras = findViewById(R.id.compras);
        CheckBox checkBox_leer = findViewById(R.id.leer);

        if (checkBox_bien.isChecked()) {
            valoresSeleccionados.add("Bien");
        }
        if (checkBox_normal.isChecked()) {
            valoresSeleccionados.add("Normal");
        }
        if (checkBox_mal.isChecked()) {
            valoresSeleccionados.add("Mal");
        }
        if (checkBox_correr.isChecked()) {
            actividadesSeleccionadas.add("Correr");
        }
        if (checkBox_jugar.isChecked()) {
            actividadesSeleccionadas.add("Jugar");
        }
        if (checkBox_trabajar.isChecked()) {
            actividadesSeleccionadas.add("Trabjar");
        }
        if (checkBox_familia.isChecked()) {
            actividadesSeleccionadas.add("Familia");
        }
        if (checkBox_amigos.isChecked()) {
            actividadesSeleccionadas.add("Amigos");
        }
        if (checkBox_cita.isChecked()) {
            actividadesSeleccionadas.add("Cita");
        }
        if (checkBox_television.isChecked()) {
            actividadesSeleccionadas.add("TV");
        }
        if (checkBox_compras.isChecked()) {
            actividadesSeleccionadas.add("Compras");
        }
        if (checkBox_leer.isChecked()) {
            actividadesSeleccionadas.add("Leer");
        }


        dia.add(valoresSeleccionados);
        dia.add(actividadesSeleccionadas);
        dia.add(fechaFormateada);
        dia.add(clima);
        return dia;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.icono1) {
            Intent view = new Intent(Main_Seleccion.this, Main_Home.class);
            startActivity(view);
        }  else if (v.getId() == R.id.icono2){
                Intent view = new Intent(Main_Seleccion.this, Main_Estadisticas.class);
                startActivity(view);
        }  else if (v.getId() == R.id.icono3){
            Intent view = new Intent(Main_Seleccion.this, Main_Seleccion.class);
            startActivity(view);
        } else if (v.getId() == R.id.icono5){
            Intent view = new Intent(Main_Seleccion.this, Main_Usuario.class);
            startActivity(view);
        }
    }
}