package es.sanchez.jaime.statusme;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Main_Home extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_home);

        // Obtener la referencia del TextView
        TextView fechaTextView = findViewById(R.id.fechaTextView);

        // Obtener la fecha actual
        Date fechaActual = new Date();

        // Formatear la fecha en el formato deseado
        SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String fechaFormateada = formatoFecha.format(fechaActual);

        // Establecer la fecha formateada en el TextView
        fechaTextView.setText(fechaFormateada);
    }
}
