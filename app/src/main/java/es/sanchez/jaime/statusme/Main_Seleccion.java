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

public class Main_Seleccion extends AppCompatActivity implements View.OnClickListener {

    private TextView weatherTextView;
    private FirebaseManager firebaseManager;
    private FirebaseAuth firebaseAuth;
    private CheckBox checkBoxFeliz, checkBoxMedio, checkBoxMal;
    private Button guardar;
    private TextView saludo;
    private String nombre = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_seleccion);

        // Inicialización de Firebase Manager
        firebaseManager = new FirebaseManager();

        // Inicialización de vistas
        checkBoxFeliz = findViewById(R.id.feliz);
        checkBoxMedio = findViewById(R.id.medio);
        checkBoxMal = findViewById(R.id.mal);
        guardar = findViewById(R.id.botonguardar);
        saludo = findViewById(R.id.saludo);

        // Configuración de CheckBox para selección única
        checkBoxFeliz.setOnClickListener(v -> {
            if (checkBoxFeliz.isChecked()) {
                checkBoxMedio.setChecked(false);
                checkBoxMal.setChecked(false);
            }
        });

        checkBoxMedio.setOnClickListener(v -> {
            if (checkBoxMedio.isChecked()) {
                checkBoxFeliz.setChecked(false);
                checkBoxMal.setChecked(false);
            }
        });

        checkBoxMal.setOnClickListener(v -> {
            if (checkBoxMal.isChecked()) {
                checkBoxFeliz.setChecked(false);
                checkBoxMedio.setChecked(false);
            }
        });

        // Configuración del botón guardar
        guardar.setOnClickListener(v -> {
            if (checkBoxFeliz.isChecked() || checkBoxMedio.isChecked() || checkBoxMal.isChecked()) {
                firebaseManager.guardarArrayListEnFirebase(guardarRegistro());
                Context context = v.getContext();
                Toast.makeText(context, "Nuevo registro añadido", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(Main_Seleccion.this, "Por favor, selecciona al menos una opción", Toast.LENGTH_SHORT).show();
            }
        });

        // Obtener cuenta de Google
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);

        // Comprobar la sesión activa
        if (SesionGoogle() != null) {
            nombre = account.getGivenName();
        } else if (SesionAuth() != null) {
            nombre = "Usuario";
        }

        // Animación de saludo
        TextAnimator animator = new TextAnimator("A ver que tal", saludo);
        animator.setDuration(3000);
        saludo.startAnimation(animator);
    }

    // Método para verificar la sesión de Google
    private String SesionGoogle() {
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        return account != null ? account.getEmail() : null;
    }

    // Método para verificar la sesión de Firebase Auth
    private String SesionAuth() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        return currentUser != null ? currentUser.getEmail() : null;
    }

    // Método para guardar el registro en Firebase
    public ArrayList<Object> guardarRegistro() {
        ArrayList<String> valoresSeleccionados = new ArrayList<String>();
        ArrayList<String> actividadesSeleccionadas = new ArrayList<String>();
        ArrayList<Object> dia = new ArrayList<Object>();

        // Configuración de la fecha actual y clima
        Date fechaActual = new Date();
        SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String fechaFormateada = formatoFecha.format(fechaActual);

        // Inicialización de CheckBoxes para emociones y actividades
        CheckBox checkBoxCorrer = findViewById(R.id.correr);
        CheckBox checkBoxJugar = findViewById(R.id.jugar);
        CheckBox checkBoxTrabajar = findViewById(R.id.trabajar);
        CheckBox checkBoxFamilia = findViewById(R.id.familia);
        CheckBox checkBoxAmigos = findViewById(R.id.amigos);
        CheckBox checkBoxCita = findViewById(R.id.amor);
        CheckBox checkBoxTelevision = findViewById(R.id.television);
        CheckBox checkBoxCompras = findViewById(R.id.compras);
        CheckBox checkBoxLeer = findViewById(R.id.leer);
        CheckBox checkBoxProgramar = findViewById(R.id.programar);
        CheckBox checkBoxNadar = findViewById(R.id.nadar);
        CheckBox checkBoxEstudiar = findViewById(R.id.estudiar);

        // Agregar emociones seleccionadas
        if (checkBoxFeliz.isChecked()) {
            valoresSeleccionados.add("Bien");
        }
        if (checkBoxMedio.isChecked()) {
            valoresSeleccionados.add("Normal");
        }
        if (checkBoxMal.isChecked()) {
            valoresSeleccionados.add("Mal");
        }

        // Agregar actividades seleccionadas
        if (checkBoxCorrer.isChecked()) {
            actividadesSeleccionadas.add("Corriendo");
        }
        if (checkBoxJugar.isChecked()) {
            actividadesSeleccionadas.add("Jugando");
        }
        if (checkBoxTrabajar.isChecked()) {
            actividadesSeleccionadas.add("Trabajando");
        }
        if (checkBoxFamilia.isChecked()) {
            actividadesSeleccionadas.add("Familia");
        }
        if (checkBoxAmigos.isChecked()) {
            actividadesSeleccionadas.add("Amigos");
        }
        if (checkBoxCita.isChecked()) {
            actividadesSeleccionadas.add("Cita");
        }
        if (checkBoxTelevision.isChecked()) {
            actividadesSeleccionadas.add("TV");
        }
        if (checkBoxCompras.isChecked()) {
            actividadesSeleccionadas.add("Compras");
        }
        if (checkBoxLeer.isChecked()) {
            actividadesSeleccionadas.add("Leyendo");
        }
        if (checkBoxProgramar.isChecked()) {
            actividadesSeleccionadas.add("Programando");
        }
        if (checkBoxNadar.isChecked()) {
            actividadesSeleccionadas.add("Nadando");
        }
        if (checkBoxEstudiar.isChecked()) {
            actividadesSeleccionadas.add("Estudiando");
        }

        // Agregar datos al ArrayList
        dia.add(valoresSeleccionados);
        dia.add(actividadesSeleccionadas);
        dia.add(fechaFormateada);
        return dia;
    }

    // Método para manejar los clics en los íconos
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.icono1) {
            Intent view = new Intent(Main_Seleccion.this, Main_Home.class);
            startActivity(view);
        } else if (v.getId() == R.id.icono2) {
            Intent view = new Intent(Main_Seleccion.this, Main_Estadisticas.class);
            startActivity(view);
        } else if (v.getId() == R.id.icono3) {
            Intent view = new Intent(Main_Seleccion.this, Main_Seleccion.class);
            startActivity(view);
        }else if (v.getId() == R.id.icono4) {
            Intent calendario = new Intent(Main_Seleccion.this, Main_Calendario.class);
            startActivity(calendario);
        }else if (v.getId() == R.id.icono5) {
            Intent view = new Intent(Main_Seleccion.this, Main_Usuario.class);
            startActivity(view);
        }
    }
}
