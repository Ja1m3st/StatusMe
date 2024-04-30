package es.sanchez.jaime.statusme;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.util.ArrayList;
import java.util.List;

public class Main_Estadisticas extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_estadisticas);

        // Establecer la altura del LineChart

        List<Entry> entries = new ArrayList<>();
        entries.add(new Entry(0, 1));
        entries.add(new Entry(1, 5));
        entries.add(new Entry(2, 3));
        entries.add(new Entry(3, 2));
        entries.add(new Entry(4, 6));

        LineDataSet dataSet = new LineDataSet(entries, "Label para la línea");

        // Crear una instancia de LineData y agregar el conjunto de datos a ella
        LineData lineData = new LineData(dataSet);

        // Obtener referencia del LineChart desde el layout
        LineChart lineChart = findViewById(R.id.line_chart);

        // Configurar el LineChart con los datos
        lineChart.setData(lineData);
        lineChart.invalidate();

        // Personalizar ejes
        customizeAxes(lineChart);
    }
    private void customizeAxes(LineChart lineChart) {
        // Personalizar eje X
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setValueFormatter(new XAxisValueFormatter());

        // Personalizar eje Y
        lineChart.getAxisLeft().setLabelCount(7, true); // 7 etiquetas en el eje Y
        lineChart.getAxisLeft().setAxisMinimum(0f); // Mínimo valor del eje Y
        lineChart.getAxisLeft().setAxisMaximum(7f); // Máximo valor del eje Y
    }


    private class XAxisValueFormatter extends ValueFormatter {
        @Override
        public String getAxisLabel(float value, AxisBase axis) {
            // Cambiar los valores en el eje X
            if (value == 0f) {
                return "Muy mal";
            } else if (value == 1f) {
                return "Mal";
            } else if (value == 2f) {
                return "Normal";
            } else if (value == 3f) {
                return "Bien";
            } else if (value == 4f) {
                return "Muy bien";
            } else {
                return "";
            }
        }
    }

    public void onClick(View view) {

        if (view.getId() == R.id.icono1) {
            Intent signup = new Intent(Main_Estadisticas.this, Main_Home.class);
            startActivity(signup);
        } else if (view.getId() == R.id.icono5) {
            Intent remember2 = new Intent(Main_Estadisticas.this, Main_Usuario.class);
            startActivity(remember2);
            //overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        } else if (view.getId() == R.id.icono3) {
            Intent animo = new Intent(Main_Estadisticas.this, Main_Seleccion.class);
            startActivity(animo);
        } else if (view.getId() == R.id.icono2) {
            Intent animo = new Intent(Main_Estadisticas.this, Main_Estadisticas.class);
            startActivity(animo);
        }

    }
}
