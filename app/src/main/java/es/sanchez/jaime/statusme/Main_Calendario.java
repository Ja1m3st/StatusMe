package es.sanchez.jaime.statusme;


import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
                        Typeface typeface = ResourcesCompat.getFont(this, R.font.nueva);
                        RelativeLayout cardView = new RelativeLayout(this); // Cambia a RelativeLayout

                        LinearLayout.LayoutParams cardLayoutParams = new LinearLayout.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT
                        );
                        cardLayoutParams.setMargins(16, 16, 16, 16); // Margen de 16dp en todos los lados
                        cardView.setLayoutParams(cardLayoutParams);
                        cardView.setBackground(getResources().getDrawable(R.drawable.minitarjetas));

                        // Linear 1
                        LinearLayout innerLinearLayout = new LinearLayout(this);
                        innerLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
                        innerLinearLayout.setId(View.generateViewId()); // Generar un ID único
                        RelativeLayout.LayoutParams layoutParams1 = new RelativeLayout.LayoutParams(
                                RelativeLayout.LayoutParams.MATCH_PARENT,
                                RelativeLayout.LayoutParams.WRAP_CONTENT
                        );
                        innerLinearLayout.setLayoutParams(layoutParams1);
                        innerLinearLayout.setPadding(10, 20, 10, 10);

                        // Linear 2
                        LinearLayout innerLinearLayout2 = new LinearLayout(this);
                        innerLinearLayout2.setOrientation(LinearLayout.HORIZONTAL);
                        RelativeLayout.LayoutParams layoutParams2 = new RelativeLayout.LayoutParams(
                                RelativeLayout.LayoutParams.MATCH_PARENT,
                                RelativeLayout.LayoutParams.WRAP_CONTENT
                        );
                        layoutParams2.addRule(RelativeLayout.BELOW, innerLinearLayout.getId()); // Colocar debajo del primer LinearLayout
                        innerLinearLayout2.setLayoutParams(layoutParams2);
                        innerLinearLayout2.setPadding(50, 0, 10, 60);


                        TextView textViewActividades3 = new TextView(this);
                        LinearLayout.LayoutParams actividadesLayoutParams2 = new LinearLayout.LayoutParams(
                                0,
                                ViewGroup.LayoutParams.WRAP_CONTENT,
                                1
                        );
                        actividadesLayoutParams2.setMargins(0,0,200,0);
                        textViewActividades3.setTextSize(15);
                        textViewActividades3.setPadding(0,0,0,0);
                        textViewActividades3.setLayoutParams(actividadesLayoutParams2);
                        textViewActividades3.setTypeface(typeface);

                        ArrayList<String> actividades = (ArrayList<String>) diaActual.get(1);
                        if (actividades != null) {
                            StringBuilder actividadesText3 = new StringBuilder();
                            boolean isFirst = true;

                            for (String estado : actividades) {
                                if (estado != null && !estado.trim().isEmpty()) {
                                    if (isFirst) {
                                        isFirst = false;
                                    } else {
                                        actividadesText3.append(", ");
                                    }
                                    actividadesText3.append(estado);
                                }
                            }

                            // Asigna el texto final al TextView
                            textViewActividades3.setText(actividadesText3.toString());
                        }


                        TextView textViewDia = new TextView(this);
                        LinearLayout.LayoutParams diaLayoutParams = new LinearLayout.LayoutParams(
                                0,
                                ViewGroup.LayoutParams.WRAP_CONTENT,
                                2
                        );
                        diaLayoutParams.setMargins(30,0,0,0);
                        textViewDia.setLayoutParams(diaLayoutParams);
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
                        textViewActividades.setTypeface(typeface);


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

                        innerLinearLayout2.addView(textViewActividades3);
                        innerLinearLayout.addView(textViewDia);
                        innerLinearLayout.addView(textViewActividades);
                        innerLinearLayout.addView(botonEliminar);

                        cardView.addView(innerLinearLayout2);
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