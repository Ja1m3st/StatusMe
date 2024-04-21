package es.sanchez.jaime.statusme;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.CheckBox;

import java.util.ArrayList;

public class Main_Seleccion extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_seleccion);

        CheckBox feliz = findViewById(R.id.feliz);
        String selecionado= "";
        String bien = "Bien";
        String mal = "Mal";

        if (feliz.isChecked()) {
            selecionado = "Bien";
        }
    }



}