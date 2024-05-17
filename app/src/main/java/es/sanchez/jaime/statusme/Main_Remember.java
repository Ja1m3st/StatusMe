package es.sanchez.jaime.statusme;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class Main_Remember extends AppCompatActivity implements View.OnClickListener {

    // Declaración de variables
    private EditText mail;
    private TextView back;
    private Button send;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_remember);

        // Inicialización de vistas
        mail = findViewById(R.id.Mail);
        back = findViewById(R.id.Back);
        send = findViewById(R.id.Send);

        // Asignación de listeners
        back.setOnClickListener(this);
        send.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.Back) {
            // Navegar a la pantalla de inicio de sesión
            Intent backIntent = new Intent(Main_Remember.this, Main_Login.class);
            startActivity(backIntent);
        } else if (view.getId() == R.id.Send) {
            // Obtener el correo electrónico ingresado
            String email = mail.getText().toString().trim();
            if (!TextUtils.isEmpty(email)) {
                // Enviar correo electrónico para restablecer la contraseña
                FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    // Mostrar mensaje de éxito
                                    Toast.makeText(Main_Remember.this, "Se ha enviado un correo electrónico para restablecer la contraseña.", Toast.LENGTH_SHORT).show();
                                } else {
                                    // Mostrar mensaje de error
                                    Toast.makeText(Main_Remember.this, "Error al enviar el correo electrónico para restablecer la contraseña.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            } else {
                // Mostrar mensaje de error si el campo de correo electrónico está vacío
                Toast.makeText(Main_Remember.this, "Rellena todos los campos.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
