package es.sanchez.jaime.statusme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class Main_Login extends AppCompatActivity implements View.OnClickListener {
    private EditText  mail, password;
    Button myButton;
    private FirebaseManager firebaseManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_login);

        TextView signup = findViewById(R.id.SignUp);
        signup.setOnClickListener(this);

        mail = findViewById(R.id.User);
        password = findViewById(R.id.Password);

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

    private void iniciarSesion(){
        String etmail = mail.getText().toString();
        String etpassword = password.getText().toString();

        boolean datos = comprobacion();

        if(datos) {
            firebaseManager.obtenerContactos(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Usuario usuario;
                    boolean existe = false;

                    if (dataSnapshot.exists()) {
                        String nombre = etmail;
                        String clave = etpassword;

                        for (DataSnapshot usaurioSnapshot : dataSnapshot.getChildren()) {
                            usuario = usaurioSnapshot.getValue(Usuario.class);

                            if (usuario.getNombre().equals(nombre) && usuario.getContraseña().equals(clave)) {
                                existe = true;
                            }
                        }
                        if(existe){
                            Intent intent = new Intent(LoginActivity.this, FeetActivity.class);
                            intent.putExtra("nombre",nombre);
                            startActivity(intent);
                        }
                        else{
                            Toast.makeText(LoginActivity.this, "Usuario incorrecto", Toast.LENGTH_SHORT).show();

                        }
                    } else {
                        Toast.makeText(LoginActivity.this, "No existe el usuario", Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(LoginActivity.this, "Error al obtener los usuarios", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private boolean comprobacion(){
        boolean datos = false;
        String nombre = String.valueOf(editTextUsername.getText());
        String contrasena = String.valueOf(editTextPassword.getText());

        if(!nombre.isEmpty() && !contrasena.isEmpty()){
            datos = true;
        } else{
            Toast.makeText(LoginActivity.this, "Rellena todos los campos", Toast.LENGTH_SHORT).show();
        }
        return datos;
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