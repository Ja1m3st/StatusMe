package es.sanchez.jaime.statusme;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.material.switchmaterial.SwitchMaterial;

public class Main_Login extends AppCompatActivity implements View.OnClickListener {
    Button myButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_login);

        TextView signup = findViewById(R.id.SignUp);
        signup.setOnClickListener(this);

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

    @Override
    public void onClick(View view) {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.button_scale);
        view.startAnimation(animation);

        if (view.getId() == R.id.SignUp) {
            Intent signup = new Intent(Main_Login.this, Main_Signup.class);
            startActivity(signup);
        }
    }
}