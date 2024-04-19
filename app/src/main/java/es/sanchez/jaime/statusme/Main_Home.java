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
        //overridePendingTransition(R.anim.fade_out, R.anim.fade_in);
        // Obtener la fecha actual
        Date fechaActual = new Date();

        // Formatear la fecha en el formato deseado
        SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String fechaFormateada = formatoFecha.format(fechaActual);
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
