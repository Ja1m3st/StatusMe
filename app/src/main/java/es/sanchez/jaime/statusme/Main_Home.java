package es.sanchez.jaime.statusme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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
                // Manejar el error en la lectura de datos
                Log.e("Firebase", "Error al leer datos de Firebase: " + databaseError.getMessage());
                // Aquí puedes implementar la lógica para manejar el error
            }
        });
    }

    private void mostrarDatos(ArrayList<ArrayList> totalDias) {
        // Construir el texto a mostrar en el TextView
        StringBuilder texto = new StringBuilder();
        texto.append("Valores seleccionados:\n");
        texto.append("- ").append(totalDias.get(0)).append("\n\n");
        texto.append("Actividades realizadas:\n");
        if (totalDias != null) {
            for (ArrayList<ArrayList<String>> dia : totalDias) {
                Log.d("Firebase", "Dia: " + dia);
                if (dia != null) {
                    for (ArrayList<String> actividades : dia) {
                        if (actividades != null) {
                            for (String actividad : actividades) {
                                texto.append(actividad);
                            }
                        }
                    }
                }
            }
        } else {
            // Manejar el caso en el que totalDias es null
            texto.append("No hay datos disponibles.");
        }
        TextView textView = findViewById(R.id.registro);

        // Establecer el texto generado en el TextView
        textView.setText(texto.toString());
        //textView.setText(totalDias.toString());
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
