package es.sanchez.jaime.statusme;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class Main_Signup extends AppCompatActivity implements View.OnClickListener {

    private EditText name, lastname , mail, password, repassword;
    private FirebaseManager firebaseManager;
    private FirebaseAuth mAuth;

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
        mAuth = FirebaseAuth.getInstance();

        TextView back = findViewById(R.id.Back);
        back.setOnClickListener(this);

        Button signup = findViewById(R.id.SignUp);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registrarUsuario();
            }
        });
    }

    private void agregarContactoJson() {
        String etname = name.getText().toString();
        String etlastname = lastname.getText().toString();
        String etmail = mail.getText().toString();
        String etpassord = password.getText().toString();

        firebaseManager.agregarContactoJson(etname, etlastname, etmail, etpassord);
    }

    public void registrarUsuario() {
        String etname = name.getText().toString();
        String etlastname = lastname.getText().toString();
        String etmail = mail.getText().toString();
        String etpassword = password.getText().toString();
        String etrepassword = repassword.getText().toString();


        if (!TextUtils.isEmpty(etmail) && !TextUtils.isEmpty(etname) && !TextUtils.isEmpty(etlastname)) {
            if (etpassword.equals(etrepassword)) {
                mAuth.createUserWithEmailAndPassword(etmail, etpassword)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getApplicationContext(), "Usuario Registrado", Toast.LENGTH_SHORT).show();
                                    FirebaseAuth mAuth = FirebaseAuth.getInstance();
                                    FirebaseUser currentUser = mAuth.getCurrentUser();
                                    String email = currentUser.getEmail();
                                    crearCarpetaStorage(email);
                                    Intent login = new Intent(Main_Signup.this, Main_Login.class);
                                    agregarContactoJson();
                                    startActivity(login);
                                } else {
                                    Toast.makeText(getApplicationContext(), "Contraseña o Usuario incorrectos", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            } else {
                Toast.makeText(getApplicationContext(), "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
            }
        }else {
            Toast.makeText(Main_Signup.this, "Rellena todos los campos.", Toast.LENGTH_SHORT).show();
        }
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

    private void crearCarpetaStorage(String email){

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference folderRef = storage.getReference().child(email);
        folderRef.child(email).putBytes(new byte[0]).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // La carpeta se creó exitosamente
                Log.d(TAG, "Carpeta creada correctamente.");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Ocurrió un error al crear la carpeta
                Log.e(TAG, "Error al crear la carpeta: " + e.getMessage());
            }
        });
    }
}
