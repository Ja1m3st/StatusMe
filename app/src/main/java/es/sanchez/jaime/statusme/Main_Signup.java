package es.sanchez.jaime.statusme;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

public class Main_Signup extends AppCompatActivity implements View.OnClickListener{

    private EditText name, lastname , mail, password, repassword;
    private FirebaseManager firebaseManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_signup);

        name = findViewById(R.id.Name);
        lastname = findViewById(R.id.Lastname);
        mail = findViewById(R.id.Mail);
        password = findViewById(R.id.Password);
        repassword = findViewById(R.id.RePassword);

        firebaseManager = new FirebaseManager();

        TextView back = findViewById(R.id.Back);
        back.setOnClickListener(this);

        Button signup= findViewById(R.id.SignUp);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.SignUp) {
                    agregarContacto();
                    Intent login = new Intent(Main_Signup.this, Main_Login.class);
                    startActivity(login);
                }
            }
        });
    }

    private void agregarContacto() {
        String etname = name.getText().toString();
        String etlastname = lastname.getText().toString();
        String etmail = mail.getText().toString();
        String etpassord = password.getText().toString();

        firebaseManager.agregarContacto(etname, etlastname, etmail, etpassord);
    }

    @Override
    public void onClick(View view) {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.button_scale);
        view.startAnimation(animation);

        if (view.getId() == R.id.Back) {
            Intent back = new Intent(Main_Signup.this, Main_Login.class);
            startActivity(back);
        }
    }
}