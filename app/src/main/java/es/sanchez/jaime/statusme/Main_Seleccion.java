package es.sanchez.jaime.statusme;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;

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
    }

    @Override
    public void onClick(View v) {

    }
}