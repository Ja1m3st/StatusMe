package es.sanchez.jaime.statusme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.res.ResourcesCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

public class Main_Home extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_home);
        //overridePendingTransition(R.anim.fade_out, R.anim.fade_in);
        // Obtener la fecha actual
        Date fechaActual = new Date();

        // Formatear la fecha en el formato deseado
        SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String fechaFormateada = formatoFecha.format(fechaActual);

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
            }
        });
    }

    private void mostrarDatos(ArrayList<ArrayList> totalDias) {
        // Construir el texto a mostrar en el TextView
        StringBuilder texto = new StringBuilder();
        texto.append("Valores seleccionados:\n");
        texto.append("- ").append(totalDias.get(0)).append("\n\n");
        texto.append("Actividades realizadas:\n\n");
        int dias = 0;

        if (totalDias != null) {
            for (ArrayList<ArrayList<String>> dia : totalDias) {
                if (dia != null) {
                    dias++;
                }
            }
        }
        dias--;

        ScrollView scrollView = findViewById(R.id.Main);
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        if (totalDias != null) {
            for (int i = totalDias.size() - 1; i > 0; i--) {
                ArrayList<ArrayList<String>> dia = totalDias.get(i);
                if (dia != null) {
                    CardView cardView = new CardView(this);
                    LinearLayout.LayoutParams cardLayoutParams = new LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT
                    );
                    cardLayoutParams.setMargins(16, 16, 16, 16); // Margen de 16dp en todos los lados
                    cardView.setLayoutParams(cardLayoutParams);// Fondo blanco
                    cardView.setRadius(8);
                    cardView.setCardElevation(4);

                    // Crea un nuevo LinearLayout horizontal dentro del CardView
                    LinearLayout innerLinearLayout = new LinearLayout(this);
                    innerLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
                    innerLinearLayout.setLayoutParams(new ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT
                    ));
                    // Crea un nuevo TextView dentro del LinearLayout para el día
                    TextView textViewDia = new TextView(this);
                    LinearLayout.LayoutParams diaLayoutParams = new LinearLayout.LayoutParams(
                            0,
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            1
                    );
                    textViewDia.setLayoutParams(diaLayoutParams);
                    textViewDia.setTextColor(getResources().getColor(R.color.green_main));
                    Typeface typeface = ResourcesCompat.getFont(this, R.font.medio);

                    textViewDia.setTypeface(typeface);
                    textViewDia.setText("Dia:" + (dias));
                    textViewDia.setTextSize(20); // Tamaño del texto 20sp
                    textViewDia.setPadding(16, 16, 16, 16); // Relleno de 16dp dentro de la tarjeta
                    innerLinearLayout.addView(textViewDia);

                    // Agrega un TextView para el contenido de las actividades
                    TextView textViewActividades = new TextView(this);
                    LinearLayout.LayoutParams actividadesLayoutParams = new LinearLayout.LayoutParams(
                            0,
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            3
                    );
                    textViewActividades.setLayoutParams(actividadesLayoutParams);
                    textViewActividades.setTextSize(15);

                    StringBuilder actividadesText = new StringBuilder();


                    ArrayList<String> actividades = dia.get(0);
                    if (actividades != null) {
                        for (String actividad : actividades) {
                            actividadesText.append(actividad).append("\n");
                        }
                    }
                   // for (ArrayList<String> actividades : dia) {
                     //   if (actividades != null) {
                       //     for (String actividad : actividades) {
                         //       actividadesText.append(actividades.get(0)).append("\n");
                           // }
                        //}
                    //}
                    textViewActividades.setText(actividadesText.toString());
                    innerLinearLayout.addView(textViewActividades);

                    // Agrega el LinearLayout horizontal al CardView
                    cardView.addView(innerLinearLayout);

                    // Agrega el CardView al LinearLayout vertical
                    linearLayout.addView(cardView);
                }
                dias--;
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
        } else if (view.getId() == R.id.icono5){
            Intent remember2 = new Intent(Main_Home.this, Main_Usuario.class);
            startActivity(remember2);
            //overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        } else if (view.getId() == R.id.icono3) {
            Intent animo = new Intent(Main_Home.this, Main_Seleccion.class);
            startActivity(animo);
        }
    }
}
