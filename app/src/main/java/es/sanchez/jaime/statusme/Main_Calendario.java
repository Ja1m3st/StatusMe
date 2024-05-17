package es.sanchez.jaime.statusme;


import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
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
import java.util.ArrayList;
import java.util.Calendar;

public class Main_Calendario extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private ArrayList<String> firebaseKeys = new ArrayList<>();
    private String nombre = "";
    private TextView saludo;
    private ScrollView scrollView;
    private String selectedDate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_calendario);

        CalendarView calendarView = findViewById(R.id.calendarView);
        saludo = findViewById(R.id.saludo);

        // Obtener la fecha actual
        Calendar calendar = Calendar.getInstance();
        long currentTime = calendar.getTimeInMillis();

        // Establecer la fecha actual en el CalendarView
        calendarView.setDate(currentTime, false, true);

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                // month is 0-indexed, so add 1 to it if you need 1-indexed month
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                Calendar selectedCalendar = Calendar.getInstance();
                selectedCalendar.set(year, month, dayOfMonth);
                selectedDate = sdf.format(selectedCalendar.getTime());
                obtenerYMostrarDatos();
            }
        });


        // Animación de saludo
        TextAnimator animator = new TextAnimator("Calendario", saludo);
        animator.setDuration(3000);
        saludo.startAnimation(animator);
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
                Toast.makeText(Main_Calendario.this, "Error al obtener datos", Toast.LENGTH_SHORT).show();
            }
        });

    }


    private void mostrarDatos(ArrayList<ArrayList> totalDias) {
        LinearLayout linearLayout = new LinearLayout(this);
        ScrollView scrollView = findViewById(R.id.Main2);
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        String day = selectedDate;
        scrollView.removeAllViews();
        if (totalDias != null) {
            for (int i = totalDias.size() - 1; i >= 0; i--) {
                ArrayList<Object> diaActual = totalDias.get(i);
                if (diaActual != null) {
                    String dia = (String) diaActual.get(2);
                    if (dia.equals(day)) {
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

                        innerLinearLayout.addView(textViewDia);
                        innerLinearLayout.addView(textViewActividades);

                        cardView.addView(innerLinearLayout);

                        linearLayout.addView(cardView);
                    }
                }
            }
            scrollView.addView(linearLayout);
        }
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

    public void onClick(View view) {
        if (view.getId() == R.id.icono1) {
            Intent signup = new Intent(Main_Calendario.this, Main_Home.class);
            startActivity(signup);
        } else if (view.getId() == R.id.icono5) {
            Intent remember2 = new Intent(Main_Calendario.this, Main_Usuario.class);
            startActivity(remember2);
        } else if (view.getId() == R.id.icono3) {
            Intent animo = new Intent(Main_Calendario.this, Main_Seleccion.class);
            startActivity(animo);
        } else if (view.getId() == R.id.icono2) {
            Intent animo = new Intent(Main_Calendario.this, Main_Estadisticas.class);
            startActivity(animo);
        } else if (view.getId() == R.id.icono4) {
            Intent calendario = new Intent(Main_Calendario.this, Main_Calendario.class);
            startActivity(calendario);
        }
    }
}