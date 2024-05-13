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

    ImageView imagenActividad, imagenEstado;
    TextView textActividad, textEstado;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_estadisticas);

        FirebaseManager firebaseManager = new FirebaseManager();
        firebaseManager.obtenerTotalDiasDeUsuario(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot != null && dataSnapshot.exists()) {
                    ArrayList<ArrayList> totalDias = (ArrayList<ArrayList>) dataSnapshot.getValue();
                    datos(totalDias);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        TextView saludo = findViewById(R.id.saludo);
        String nombre = "";

        if (SesionGoogle() != null) {
            nombre = account.getGivenName();
        } else if (SesionAuth() != null) {
            nombre = "Usuario";
        }

        TextAnimator animator = new TextAnimator("Veamos que tal vas, " + nombre, saludo);
        animator.setDuration(3000);
        saludo.startAnimation(animator);
    }


    public int totalDias(ArrayList<ArrayList> totalDias){
        int dias = 0;
        if (totalDias != null) {
            for (ArrayList<ArrayList<String>> dia : totalDias) {
                if (dia != null) {
                    dias++;
                }
            }
        }
        dias--;
        return dias;
    }



    public void datos(ArrayList<ArrayList> totalDias){
        String animoMasRepetido = null;
        String actividadMasRepetida = null;
        ArrayList<String> infoAnimo = new ArrayList<String>();
        ArrayList<String> infoActividad = new ArrayList<String>();
        textEstado = findViewById(R.id.estado2);
        textActividad = findViewById(R.id.actividad2);

        if (totalDias != null && totalDias.size() > 7){
            if (totalDias.size() >= 16) {
                for (int i = totalDias.size() - 16; i < totalDias.size() - 8; i++) {
                    ArrayList<ArrayList<String>> dia = totalDias.get(i);
                    if (dia != null) {
                        ArrayList<String> estadosAnimo = dia.get(0);
                        ArrayList<String> actividades = dia.get(1);
                        if (estadosAnimo != null) {
                            for (String estado : estadosAnimo) {
                                infoAnimo.add(estado);
                            }
                        }
                        if (actividades != null){
                            for (String actividad : actividades) {
                                infoActividad.add(actividad);
                            }
                        }
                    }
                }
            } else if (totalDias.size() > 7) {
                for (int i = totalDias.size() - 1; i >= 0; i--) {
                    ArrayList<ArrayList<String>> dia = totalDias.get(i);
                    if (dia != null) {
                        ArrayList<String> estadosAnimo = dia.get(0);
                        ArrayList<String> actividades = dia.get(1);
                        if (estadosAnimo != null) {
                            for (String estado : estadosAnimo) {
                                infoAnimo.add(estado);
                            }
                        }
                        if (actividades != null) {
                            for (String actividad : actividades) {
                                infoActividad.add(actividad);
                            }
                        }
                    }
                }
            }
            animoMasRepetido = buscarMasRepetido(infoAnimo);
            textEstado.setText(animoMasRepetido);
            cargarImagen(animoMasRepetido);
            actividadMasRepetida = buscarMasRepetido(infoActividad);
            textActividad.setText(actividadMasRepetida);
            cargarImagen(actividadMasRepetida);
        }
    }

    public static String buscarMasRepetido(ArrayList<String> listaDeStrings) {
        HashMap<String, Integer> conteoDeStrings = new HashMap<>();
        String stringMasRepetido = null;
        int maxRepeticiones = 0;

        // Contar la frecuencia de cada string en el ArrayList
        for (String str : listaDeStrings) {
            if (conteoDeStrings.containsKey(str)) {
                // Si encuentra otro str igual le añade uno mas a la clave
                conteoDeStrings.put(str, conteoDeStrings.get(str) + 1);
            } else {
                // Si no encuentra otro str crea otra entrada n ueva
                conteoDeStrings.put(str, 1);
            }
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

    public void cargarImagen(String stringMasRepetido){
        imagenActividad = findViewById(R.id.imagen2actividad);
        imagenEstado = findViewById(R.id.imagen2estado);

        String tipoImagen = stringMasRepetido;

        switch (tipoImagen) {
            case "Bien":
                imagenEstado.setImageResource(R.drawable.muyfeliz);
                break;
            case "Normal":
                imagenEstado.setImageResource(R.drawable.caraseria);
                break;
            case "Mal":
                imagenEstado.setImageResource(R.drawable.caratriste);
                break;
            case "Corriendo":
                imagenActividad.setImageResource(R.drawable.corriendo);
                break;
            case "Jugando":
                imagenActividad.setImageResource(R.drawable.controlador);
                break;
            case "Trabajando":
                imagenActividad.setImageResource(R.drawable.portafolio);
                break;
            case "Familia":
                imagenActividad.setImageResource(R.drawable.familia);
                break;
            case "Amigos":
                imagenActividad.setImageResource(R.drawable.abrazo);
                break;
            case "Cita":
                imagenActividad.setImageResource(R.drawable.amor);
                break;
            case "TV":
                imagenActividad.setImageResource(R.drawable.television);
                break;
            case "Compras":
                imagenActividad.setImageResource(R.drawable.cesta);
                break;
            case "Leyendo":
                imagenActividad.setImageResource(R.drawable.leer);
                break;
            case "Programando":
                imagenActividad.setImageResource(R.drawable.programar);
                break;
            case "Estudiando":
                imagenActividad.setImageResource(R.drawable.estudiar);
                break;
            case "Nadando":
                imagenActividad.setImageResource(R.drawable.nadar);
                break;
            default:
                imagenActividad.setImageResource(android.R.color.transparent);
                imagenEstado.setImageResource(android.R.color.transparent);
                break;
        }
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
        }
    }
}
