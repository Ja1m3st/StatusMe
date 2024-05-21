package es.sanchez.jaime.statusme;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class Main_Estadisticas extends AppCompatActivity implements View.OnClickListener {

    private ImageView imagenActividad, imagenEstado, imagen1Actividad, imagen1Estado;
    private TextView textActividad, textEstado, text1Actividad, text1Estado;
    private String nombre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_estadisticas);

        // Inicialización de vistas
        imagenActividad = findViewById(R.id.imagen2actividad);
        imagenEstado = findViewById(R.id.imagen2estado);
        textEstado = findViewById(R.id.estado2);
        textActividad = findViewById(R.id.actividad2);
        imagen1Actividad = findViewById(R.id.imagen1actividad);
        imagen1Estado = findViewById(R.id.imagen1estado);
        text1Estado = findViewById(R.id.estado1);
        text1Actividad = findViewById(R.id.actividad1);
        TextView saludo = findViewById(R.id.saludo);


        // Obtener datos de Firebase
        FirebaseManager firebaseManager = new FirebaseManager();
        firebaseManager.obtenerTotalDiasDeUsuario(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot != null && dataSnapshot.exists()) {
                    ArrayList<ArrayList> totalDias = (ArrayList<ArrayList>) dataSnapshot.getValue();
                    datos(totalDias);
                    datosGenerales(totalDias);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Manejo de error
            }
        });

        // Obtener la cuenta de Google y mostrar saludo
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser usuarioActual = mAuth.getCurrentUser();

        if (SesionGoogle() != null) {
            nombre = account.getGivenName();
            // Animación de saludo
            TextAnimator animator = new TextAnimator("Hola, " + nombre, saludo);
            animator.setDuration(3000);
            saludo.startAnimation(animator);
        } else if (SesionAuth() != null) {
            firebaseManager.obtenerInformacionUsuarioActual(usuarioActual, new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot != null && dataSnapshot.exists()) {
                        nombre = dataSnapshot.child("name").getValue(String.class);
                        TextAnimator animator = new TextAnimator("Hola, " + nombre, saludo);
                        animator.setDuration(3000);
                        saludo.startAnimation(animator);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Manejar el error de la base de datos si es necesario
                }
            });
        }
    }

    // Método para calcular el total de días
    private int totalDias(ArrayList<ArrayList> totalDias) {
        return totalDias == null ? 0 : totalDias.size() - 1;
    }

    // Método para procesar los datos y actualizar la UI
    private void datos(ArrayList<ArrayList> totalDias) {
        if (totalDias == null || totalDias.size() <= 7) {
            return;
        }

        int startIndex = totalDias.size() >= 16 ? totalDias.size() - 16 : 0;
        int endIndex = totalDias.size() >= 16 ? totalDias.size() - 8 : totalDias.size() - 1;

        ArrayList<String> infoAnimo = new ArrayList<>();
        ArrayList<String> infoActividad = new ArrayList<>();

        // Recopilación de información de estado de ánimo y actividad
        for (int i = startIndex; i <= endIndex; i++) {
            ArrayList<ArrayList<String>> dia = totalDias.get(i);
            if (dia != null) {
                if (dia.size() > 0 && dia.get(0) != null) {
                    infoAnimo.addAll(dia.get(0));
                }
                if (dia.size() > 1 && dia.get(1) != null) {
                    infoActividad.addAll(dia.get(1));
                }
            }
        }

        // Encontrar el estado de ánimo y la actividad más repetidos
        String animoMasRepetido = buscarMasRepetido(infoAnimo);
        String actividadMasRepetida = buscarMasRepetido(infoActividad);

        // Actualizar la UI con los datos obtenidos
        textEstado.setText(animoMasRepetido);
        cargarImagen(animoMasRepetido, R.id.imagen2estado);
        textActividad.setText(actividadMasRepetida);
        cargarImagen(actividadMasRepetida, R.id.imagen2actividad);
    }

    private void datosGenerales(ArrayList<ArrayList> totalDias) {
        if (totalDias == null || totalDias.size() <= 7) {
            return;
        }

        ArrayList<String> infoAnimo = new ArrayList<>();
        ArrayList<String> infoActividad = new ArrayList<>();

        // Recopilación de información de estado de ánimo y actividad
        for (int i = totalDias.size() - 1; i >= 0; i--) {
            ArrayList<ArrayList<String>> actividad = totalDias.get(i);
            if (actividad != null) {
                if (actividad.size() > 0 && actividad.get(0) != null) {
                    infoAnimo.addAll(actividad.get(0));
                }
                if (actividad.size() > 1 && actividad.get(1) != null) {
                    infoActividad.addAll(actividad.get(1));
                }
            }
        }

        // Encontrar el estado de ánimo y la actividad más repetidos
        String animoMasRepetido = buscarMasRepetido(infoAnimo);
        String actividadMasRepetida = buscarMasRepetido(infoActividad);

        // Actualizar la UI con los datos obtenidos
        text1Estado.setText(animoMasRepetido);
        cargarImagen(animoMasRepetido, R.id.imagen1estado);
        text1Actividad.setText(actividadMasRepetida);
        cargarImagen(actividadMasRepetida, R.id.imagen1actividad);
    }

    // Método para buscar el elemento más repetido en una lista
    private static String buscarMasRepetido(ArrayList<String> listaDeStrings) {
        HashMap<String, Integer> conteoDeStrings = new HashMap<>();
        String stringMasRepetido = null;
        int maxRepeticiones = 0;

        // Contar la frecuencia de cada string en el ArrayList
        for (String str : listaDeStrings) {
            conteoDeStrings.put(str, conteoDeStrings.getOrDefault(str, 0) + 1);
        }

        // Encontrar el string con el recuento más alto
        for (String str : conteoDeStrings.keySet()) {
            int repeticiones = conteoDeStrings.get(str);
            if (repeticiones > maxRepeticiones) {
                stringMasRepetido = str;
                maxRepeticiones = repeticiones;
            }
        }

        return stringMasRepetido;
    }

    // Método para cargar la imagen correspondiente según el tipo de estado o actividad
    private void cargarImagen(String stringMasRepetido, int imageViewId) {
        int imagenId = android.R.color.transparent;

        switch (stringMasRepetido) {
            case "Bien":
                imagenId = R.drawable.muyfeliz;
                break;
            case "Normal":
                imagenId = R.drawable.caraseria;
                break;
            case "Mal":
                imagenId = R.drawable.caratriste;
                break;
            case "Corriendo":
                imagenId = R.drawable.corriendo;
                break;
            case "Jugando":
                imagenId = R.drawable.controlador;
                break;
            case "Trabajando":
                imagenId = R.drawable.portafolio;
                break;
            case "Familia":
                imagenId = R.drawable.familia;
                break;
            case "Amigos":
                imagenId = R.drawable.abrazo;
                break;
            case "Cita":
                imagenId = R.drawable.amor;
                break;
            case "TV":
                imagenId = R.drawable.television;
                break;
            case "Compras":
                imagenId = R.drawable.cesta;
                break;
            case "Leyendo":
                imagenId = R.drawable.leer;
                break;
            case "Programando":
                imagenId = R.drawable.programar;
                break;
            case "Estudiando":
                imagenId = R.drawable.estudiar;
                break;
            case "Nadando":
                imagenId = R.drawable.nadar;
                break;
            default:
                imagenId = android.R.color.transparent;
                break;
        }

        // Asignar la imagen correspondiente al ImageView especificado
        ImageView imageView = findViewById(imageViewId);
        if (imageView != null) {
            imageView.setImageResource(imagenId);
        }
    }

    // Métodos para obtener la sesión de Google o Auth
    private String SesionGoogle() {
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        return account != null ? account.getEmail() : null;
    }

    private String SesionAuth() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        return user != null ? user.getEmail() : null;
    }

    // Método onClick para manejar los clics en las vistas
    public void onClick(View view) {
        if (view.getId() == R.id.icono1) {
            Intent signup = new Intent(Main_Estadisticas.this, Main_Home.class);
            startActivity(signup);
        } else if (view.getId() == R.id.icono5) {
            Intent remember2 = new Intent(Main_Estadisticas.this, Main_Usuario.class);
            startActivity(remember2);
        } else if (view.getId() == R.id.icono3) {
            Intent animo = new Intent(Main_Estadisticas.this, Main_Seleccion.class);
            startActivity(animo);
        } else if (view.getId() == R.id.icono2) {
            Intent animo = new Intent(Main_Estadisticas.this, Main_Estadisticas.class);
            startActivity(animo);
        }else if (view.getId() == R.id.icono4) {
            Intent calendario = new Intent(Main_Estadisticas.this, Main_Calendario.class);
            startActivity(calendario);
        }
    }
}


