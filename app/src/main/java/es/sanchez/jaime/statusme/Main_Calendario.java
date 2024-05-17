package es.sanchez.jaime.statusme;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CalendarView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class Main_Calendario extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_calendario);

        CalendarView calendarView = findViewById(R.id.calendarView);

        // Obtener la fecha actual
        Calendar calendar = Calendar.getInstance();
        long currentTime = calendar.getTimeInMillis();

        // Establecer la fecha actual en el CalendarView
        calendarView.setDate(currentTime, false, true);

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                // month is 0-indexed, so add 1 to it if you need 1-indexed month
                String date = dayOfMonth + "/" + (month + 1) + "/" + year;
                Toast.makeText(Main_Calendario.this, "Selected date: " + date, Toast.LENGTH_SHORT).show();
            }
        });
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