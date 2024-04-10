package es.sanchez.jaime.statusme;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.cardview.widget.CardView;

import android.content.Context;
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
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class Main_Login extends AppCompatActivity implements View.OnClickListener {

    private EditText mail, password;
    private Button myButton;
    private FirebaseAuth mAuth;
    private CardView google;
    private GoogleSignInClient mGoogleSignInClient;
    private final int RC_SIGN_IN = 1;
    private FirebaseManager firebaseManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_login);

        TextView signup = findViewById(R.id.SignUp);
        TextView remember = findViewById(R.id.Remember);

        mail = findViewById(R.id.User);
        password = findViewById(R.id.Password);
        google = findViewById(R.id.card);

        signup.setOnClickListener(this);
        remember.setOnClickListener(this);

        firebaseManager = new FirebaseManager();
        mAuth = FirebaseAuth.getInstance();

        // ----------------------------------- LOGIN GOOGLE ---------------------------------//

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInWithGoogle();
            }
        });

        // ----------------------------------- MODO OSCURO ---------------------------------//

        SwitchMaterial switchDarkMode = findViewById(R.id.DarkMode);
        switchDarkMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                }
            }
        });

        // ----------------------------------- LOGIN AUTH ---------------------------------//
        myButton = findViewById(R.id.Login);
        myButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
    }

    // ----------------------------------- METODO LOGIN AUTH ---------------------------------//
    public void login() {
        String etemail = mail.getText().toString();
        String etpassword = password.getText().toString();
        if (!TextUtils.isEmpty(etemail) && !TextUtils.isEmpty(etpassword)) {
            mAuth.signInWithEmailAndPassword(etemail, etpassword)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(Main_Login.this, "Sesión iniciada con Auth", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(Main_Login.this, Main_Home.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(Main_Login.this, "Usuario no registrado", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } else {
            Toast.makeText(Main_Login.this, "Rellena todos los campos.", Toast.LENGTH_SHORT).show();
        }
    }


    // ----------------------------------- METODOS GOOGLE ---------------------------------//
    public void signInWithGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken());
                handleSignInResult(task);
            } catch (ApiException e) {
                Log.e(TAG, "Google sign in failed", e);
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Intent intent = new Intent(Main_Login.this, Main_Home.class);
                            startActivity(intent);
                            Toast.makeText(Main_Login.this, "Sesión iniciada con Google", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(Main_Login.this, "Error al iniciar sesión", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    // --------------------------------- METODOS STORAGE GOOGLE ---------------------------------//
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            String email = account.getEmail();
            firebaseManager.buscarEmail(email, new FirebaseManager.EmailCallback() {
                @Override
                public void onEmailFound(boolean found) {
                    String firstName = "";
                    String lastName = "";
                    if (found == false) { // Si el email no existe en la base de datos, agrégalo
                        firstName = account.getGivenName();
                        lastName = account.getFamilyName();
                        firebaseManager.agregarContactoGoogleJson(firstName, lastName, email);
                        crearCarpetaStorage(email);
                    }

                }
            });
        } catch (ApiException e) {
            Log.e(TAG, "Google sign in failed", e);
        }
    }

    private void crearCarpetaStorage(String email){

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference folderRef = storage.getReference().child(email);
        folderRef.child(email).putBytes(new byte[0]).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Log.d(TAG, "Carpeta creada correctamente.");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, "Error al crear la carpeta: " + e.getMessage());
            }
        });
    }

    // ----------------------------------- METODO ONCLICK ---------------------------------//
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.SignUp) {
            Intent signup = new Intent(Main_Login.this, Main_Signup.class);
            startActivity(signup);
        } else if (view.getId() == R.id.Remember){
            Intent remember2 = new Intent(Main_Login.this, Main_Remember.class);
            startActivity(remember2);
        }
    }
}