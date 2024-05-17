package es.sanchez.jaime.statusme;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class Main_Calendario extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_calendario);
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