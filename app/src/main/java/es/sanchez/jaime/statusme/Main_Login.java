package es.sanchez.jaime.statusme;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CompoundButton;

import com.google.android.material.switchmaterial.SwitchMaterial;

public class Main_Login extends AppCompatActivity {
    Button myButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_login);

        myButton = findViewById(R.id.Login);
        SwitchMaterial switchDarkMode = findViewById(R.id.DarkMode);

        switchDarkMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // Si el interruptor está activado, cambia al modo oscuro
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                } else {
                    // Si el interruptor está desactivado, cambia al modo claro (predeterminado)
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                }
            }
        });
    }

    public void onClick(View view) {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.button_scale);
        view.startAnimation(animation);

        // Aquí puedes agregar cualquier otra acción que desees realizar cuando se presione el botón.
    }
}