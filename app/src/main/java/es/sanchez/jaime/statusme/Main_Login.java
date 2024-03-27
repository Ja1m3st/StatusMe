package es.sanchez.jaime.statusme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class Main_Login extends AppCompatActivity implements View.OnClickListener {
    private EditText  mail, password;
    Button myButton;
    private FirebaseManager firebaseManager;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_login);

        TextView signup = findViewById(R.id.SignUp);
        mail = findViewById(R.id.User);
        password = findViewById(R.id.Password);
        signup.setOnClickListener(this);


        firebaseManager = new FirebaseManager();
        mAuth = FirebaseAuth.getInstance();

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

        myButton = findViewById(R.id.Login);
        myButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
    }

    public void login() {
        String etemail = mail.getText().toString();
        String etpassword = password.getText().toString();
        if (!TextUtils.isEmpty(etemail) && !TextUtils.isEmpty(etpassword)){
            mAuth.signInWithEmailAndPassword(etemail, etpassword)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Intent intent = new Intent(Main_Login.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(Main_Login.this, "Usuario no registrado", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }else{
            Toast.makeText(Main_Login.this, "Rellena todos los campos.", Toast.LENGTH_SHORT).show();
        }
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