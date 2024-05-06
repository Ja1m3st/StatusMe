package es.sanchez.jaime.statusme;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class Main_Home extends AppCompatActivity {

    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_home);
        // Obtener la fecha actual
        Date fechaActual = new Date();

        // Formatear la fecha en el formato deseado
        SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String fechaFormateada = formatoFecha.format(fechaActual);

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        TextView saludo = findViewById(R.id.saludo);
        String nombre = "";

        if (SesionGoogle() != null) {
            nombre = account.getGivenName();
        } else if (SesionAuth() != null) {
            nombre = "Usuario";
        }

        // Crear el animador de texto y configurar la velocidad de escritura
        TextAnimator animator = new TextAnimator("Hola, " + nombre, saludo);
        animator.setDuration(3000); // Duración de la animación en milisegundos
        saludo.startAnimation(animator);

        obtenerYMostrarDatos();
    }

    private void obtenerYMostrarDatos() {
        FirebaseUser usuario = FirebaseAuth.getInstance().getCurrentUser();
        String emailUsuario = usuario.getEmail();

        // Llamar al método para obtener los datos de totaldias del usuario
        FirebaseManager firebaseManager = new FirebaseManager();
        firebaseManager.obtenerTotalDiasDeUsuario(emailUsuario, new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot != null && dataSnapshot.exists()) {
                    // Si hay datos, mostrarlos en la interfaz de usuario
                    ArrayList<ArrayList> totalDias = (ArrayList<ArrayList>) dataSnapshot.getValue();
                    mostrarDatos(totalDias);
                } else {
                    // Si no hay datos, mostrar un mensaje indicando que no se encontraron datos
                    mostrarMensaje("No se encontraron datos de totaldias para este usuario.");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Manejar el error en caso de que ocurra
            }
        });
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

    private void mostrarDatos(ArrayList<ArrayList> totalDias) {
        // Construir el texto a mostrar en el TextView
        StringBuilder texto = new StringBuilder();
        texto.append("Valores seleccionados:\n");
        texto.append("- ").append(totalDias.get(0)).append("\n\n");
        texto.append("Actividades realizadas:\n\n");
        LocalDate fechaActual = LocalDate.now();

        ScrollView scrollView = findViewById(R.id.Main);
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        if (totalDias != null) {
            for (int i = totalDias.size() - 1; i > 0; i--) {
                ArrayList<Object> diaActual = totalDias.get(i);
                if (diaActual != null) {
                    String dia = (String) diaActual.get(2);
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                    LocalDate fechaDia = LocalDate.parse(dia, formatter);
                    if (fechaDia.equals(fechaActual)) {
                        FrameLayout cardView = new FrameLayout(this);
                        LinearLayout.LayoutParams cardLayoutParams = new LinearLayout.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT
                        );
                        cardLayoutParams.setMargins(16, 16, 16, 16); // Margen de 16dp en todos los lados
                        cardView.setLayoutParams(cardLayoutParams);// Fondo blanco
                        cardView.setBackground(getResources().getDrawable(R.drawable.minitarjetas));

                        LinearLayout innerLinearLayout = new LinearLayout(this);
                        innerLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
                        innerLinearLayout.setLayoutParams(new ViewGroup.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT
                        ));

                        TextView textViewDia = new TextView(this);
                        LinearLayout.LayoutParams diaLayoutParams = new LinearLayout.LayoutParams(
                                0,
                                ViewGroup.LayoutParams.WRAP_CONTENT,
                                1
                        );
                        textViewDia.setLayoutParams(diaLayoutParams);
                        Typeface typeface = ResourcesCompat.getFont(this, R.font.medio);
                        textViewDia.setTypeface(typeface);
                        textViewDia.setText(dia);
                        textViewDia.setTypeface(null, Typeface.BOLD);
                        textViewDia.setTextSize(20); // Tamaño del texto 20sp
                        textViewDia.setPadding(50, 15, 15, 15);
                        innerLinearLayout.addView(textViewDia);

                        TextView textViewActividades = new TextView(this);
                        LinearLayout.LayoutParams actividadesLayoutParams = new LinearLayout.LayoutParams(
                                0,
                                ViewGroup.LayoutParams.WRAP_CONTENT,
                                1
                        );
                        textViewActividades.setLayoutParams(actividadesLayoutParams);
                        textViewActividades.setTextSize(15);

                        StringBuilder actividadesText = new StringBuilder();
                        ArrayList<String> estadosDeAnimo = (ArrayList<String>) diaActual.get(0);
                        if (estadosDeAnimo != null) {
                            for (String estado : estadosDeAnimo) {
                                actividadesText.append(estado).append("\n");
                            }
                        }

                        textViewActividades.setText(actividadesText.toString());
                        innerLinearLayout.addView(textViewActividades);

                        // Agrega el LinearLayout horizontal al CardView
                        cardView.addView(innerLinearLayout);

                        // Agrega el CardView al LinearLayout vertical
                        linearLayout.addView(cardView);
                    }
                }
            }
            scrollView.addView(linearLayout);
        } else {
            texto.append("No hay datos disponibles.");
        }
    }
    private void mostrarMensaje(String mensaje) {
        // Mostrar un Toast con el mensaje proporcionado
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show();
    }

    public void onClick(View view) {

        if (view.getId() == R.id.icono1) {
            Intent signup = new Intent(Main_Home.this, Main_Home.class);
            startActivity(signup);
        } else if (view.getId() == R.id.icono2) {
            Intent animo = new Intent(Main_Home.this, Main_Estadisticas.class);
            startActivity(animo);
        } else if (view.getId() == R.id.icono3) {
            Intent animo = new Intent(Main_Home.this, Main_Seleccion.class);
            startActivity(animo);
        }else if (view.getId() == R.id.icono5){
            Intent remember2 = new Intent(Main_Home.this, Main_Usuario.class);
            startActivity(remember2);
        }
    }
}
