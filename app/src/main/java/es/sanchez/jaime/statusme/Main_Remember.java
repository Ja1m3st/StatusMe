package es.sanchez.jaime.statusme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import java.text.BreakIterator;

public class Main_Remember extends AppCompatActivity implements View.OnClickListener  {

    private EditText mail;
    private Button send;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_remember);
        TextView back = findViewById(R.id.Back);
        back.setOnClickListener(this);

        mail = findViewById(R.id.Mail);
        send = findViewById(R.id.Send);

        String email = mail.getText().toString();

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.Back) {
            Intent back = new Intent(Main_Remember.this, Main_Login.class);
            startActivity(back);
        } else if (view.getId() == R.id.Send){
            String email = mail.getText().toString();

            FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(Main_Remember.this, "Se ha enviado un correo electrónico para restablecer la contraseña.", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(Main_Remember.this, "Error al enviar el correo electrónico para restablecer la contraseña.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }
}