package es.sanchez.jaime.statusme;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

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

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Main_Home extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private ArrayList<String> firebaseKeys = new ArrayList<>();
    private String nombre = "";
    private TextView saludo;
    private ScrollView scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_home);

        // Inicialización de vistas
        saludo = findViewById(R.id.saludo);
        scrollView = findViewById(R.id.Main);

        // Obtener cuenta de Google o Auth y establecer nombre de usuario
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        FirebaseManager firebaseManager = new FirebaseManager();
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


        // Obtener y mostrar datos de Firebase
        obtenerYMostrarDatos();
    }

    private void obtenerYMostrarDatos() {
        FirebaseManager firebaseManager = new FirebaseManager();

        firebaseManager.obtenerTotalDiasDeUsuario(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot != null && dataSnapshot.exists()) {
                    ArrayList<ArrayList> totalDias = (ArrayList<ArrayList>) dataSnapshot.getValue();
                    mostrarDatos(totalDias);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }

    // Método para obtener sesión de Google
    private String SesionGoogle() {
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        return account != null ? account.getEmail() : null;
    }

    // Método para obtener sesión de Firebase Auth
    private String SesionAuth() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        return currentUser != null ? currentUser.getEmail() : null;
    }

    // Método para mostrar los datos obtenidos en la UI
    private void mostrarDatos(ArrayList<ArrayList> totalDias) {
        LinearLayout linearLayout = new LinearLayout(this);
        LocalDate fechaActual = LocalDate.now();
        ScrollView scrollView = findViewById(R.id.Main);
        String dia = "";

        linearLayout.setOrientation(LinearLayout.VERTICAL);

        if (totalDias != null) {
            for (int i = totalDias.size() - 1; i > 0; i--) {
                ArrayList<Object> diaActual = totalDias.get(i);
                if (diaActual != null) {
                    dia = (String) diaActual.get(2);
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
                        innerLinearLayout.setPadding(10,10,10,10);

                        TextView textViewDia = new TextView(this);
                        LinearLayout.LayoutParams diaLayoutParams = new LinearLayout.LayoutParams(
                                0,
                                ViewGroup.LayoutParams.WRAP_CONTENT,
                                2
                        );
                        diaLayoutParams.setMargins(30,0,0,0);
                        textViewDia.setLayoutParams(diaLayoutParams);
                        Typeface typeface = ResourcesCompat.getFont(this, R.font.medio);
                        textViewDia.setTypeface(typeface);
                        textViewDia.setText(dia);
                        textViewDia.setTypeface(null, Typeface.BOLD);
                        textViewDia.setTextSize(18);
                        textViewDia.setPadding(0, 15, 0, 15);

                        TextView textViewActividades = new TextView(this);
                        LinearLayout.LayoutParams actividadesLayoutParams = new LinearLayout.LayoutParams(
                                0,
                                ViewGroup.LayoutParams.WRAP_CONTENT,
                                1
                        );
                        actividadesLayoutParams.setMargins(0,0,200,0);
                        textViewActividades.setTextSize(15);
                        textViewActividades.setPadding(0,0,0,0);
                        textViewActividades.setLayoutParams(actividadesLayoutParams);


                        StringBuilder actividadesText = new StringBuilder();
                        ArrayList<String> estadosDeAnimo = (ArrayList<String>) diaActual.get(0);
                        if (estadosDeAnimo != null) {
                            for (String estado : estadosDeAnimo) {
                                actividadesText.append(estado).append("\n");
                            }
                        }
                        textViewActividades.setText(actividadesText.toString());

                        ImageButton botonEliminar = new ImageButton(this);
                        LinearLayout.LayoutParams botonLayoutParams = new LinearLayout.LayoutParams(
                                0,
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                1
                        );
                        botonEliminar.setImageResource(R.drawable.basura);
                        botonEliminar.setScaleType(ImageView.ScaleType.CENTER);
                        botonEliminar.setPadding(20,15,10,20);
                        botonEliminar.setAdjustViewBounds(true);
                        botonEliminar.setBackground(getResources().getDrawable(R.drawable.eliminar));
                        botonLayoutParams.setMargins(50,10,10,10);
                        botonEliminar.setLayoutParams(botonLayoutParams);

                        botonEliminar.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                int index = linearLayout.indexOfChild((View) v.getParent().getParent());
                                int posicionEliminar = totalDias.size() - 1 - index;
                                String posi = String.valueOf(posicionEliminar);
                                if (posicionEliminar >= 0 && posicionEliminar < totalDias.size()) {

                                    FirebaseManager.eliminarRegistroUsuario(posi);

                                    // Eliminar el elemento del ArrayList local
                                    totalDias.remove(posicionEliminar);

                                    // Eliminar la vista correspondiente
                                    linearLayout.removeViewAt(index);
                                }
                            }
                        });

                        innerLinearLayout.addView(textViewDia);
                        innerLinearLayout.addView(textViewActividades);
                        innerLinearLayout.addView(botonEliminar);

                        cardView.addView(innerLinearLayout);

                        linearLayout.addView(cardView);
                    }
                }
            }
            scrollView.addView(linearLayout);
        }
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
        }else if (view.getId() == R.id.icono4){
            Intent remember2 = new Intent(Main_Home.this, Main_Calendario.class);
            startActivity(remember2);
        }else if (view.getId() == R.id.icono5){
            Intent remember2 = new Intent(Main_Home.this, Main_Usuario.class);
            startActivity(remember2);
        }
    }
}
