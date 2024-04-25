package es.sanchez.jaime.statusme;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class Main_Seleccion extends AppCompatActivity implements View.OnClickListener{

    FirebaseManager firebaseManager;
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_seleccion);

        firebaseManager = new FirebaseManager();


        CheckBox checkBoxFeliz = findViewById(R.id.feliz);
        CheckBox checkBoxMedio = findViewById(R.id.medio);
        CheckBox checkBoxMal = findViewById(R.id.mal);
        Button guardar = findViewById(R.id.botonguardar);

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


        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser usuario = FirebaseAuth.getInstance().getCurrentUser();
                String nombreUsuario = usuario.getEmail();
                firebaseManager.guardarArrayListEnFirebase(nombreUsuario, guardarRegistro());
                Intent back = new Intent(Main_Seleccion.this, Main_Home.class);
                startActivity(back);
            }
        });
    }

    public ArrayList<ArrayList<String>> guardarRegistro() {
        ArrayList<String> valoresSeleccionados = new ArrayList<>();

        CheckBox checkBox_bien = findViewById(R.id.feliz);
        CheckBox checkBox_normal = findViewById(R.id.medio);
        CheckBox checkBox_mal = findViewById(R.id.mal);

        // CheckBox "Bien"
        if (checkBox_bien.isChecked()) {
            valoresSeleccionados.add("Bien");
        }

        // CheckBox "Normal"
        if (checkBox_normal.isChecked()) {
            valoresSeleccionados.add("Normal");
        }

        // CheckBox "Mal"
        if (checkBox_mal.isChecked()) {
            valoresSeleccionados.add("Mal");
        }

        ArrayList<ArrayList<String>> dia = new ArrayList<>();
        dia.add(valoresSeleccionados);
        return dia;
    }

    @Override
    public void onClick(View v) {

    }

}