package es.sanchez.jaime.statusme;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import java.util.ArrayList;

public class Main_Seleccion extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_seleccion);

        CheckBox checkBoxFeliz = findViewById(R.id.feliz);
        CheckBox checkBoxMedio = findViewById(R.id.medio);
        CheckBox checkBoxMal = findViewById(R.id.mal);

        checkBoxFeliz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBoxFeliz.isChecked()) {
                    checkBoxMedio.setChecked(false);
                    checkBoxMal.setChecked(false);
                }
            }
        });

        checkBoxMedio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBoxMedio.isChecked()) {
                    checkBoxFeliz.setChecked(false);
                    checkBoxMal.setChecked(false);
                }
            }
        });

        checkBoxMal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBoxMal.isChecked()) {
                    checkBoxFeliz.setChecked(false);
                    checkBoxMedio.setChecked(false);
                }
            }
        });

        CheckBox checkBox = findViewById(R.id.correr); // Obtén la referencia del CheckBox desde el layout

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Se llama cuando el estado del CheckBox cambia
                if (isChecked) {
                    // El CheckBox está marcado
                    // Realiza las acciones que desees cuando el CheckBox esté marcado
                    Log.d("CheckBox", "Está marcado");
                } else {
                    // El CheckBox no está marcado
                    // Realiza las acciones que desees cuando el CheckBox no esté marcado
                    Log.d("CheckBox", "No está marcado");
                }
            }
        });

    }

    @Override
    public void onClick(View v) {

    }
}