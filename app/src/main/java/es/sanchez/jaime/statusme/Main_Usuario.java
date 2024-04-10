package es.sanchez.jaime.statusme;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main_Usuario extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri imageUri;
    private StorageReference storageRef;
    private FirebaseStorage storage;
    private ImageView image;
    private TextView name, lastname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_usuario);
        String nombre = "";
        String apellido = "";
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);

        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();

        image = findViewById(R.id.image);
        name = findViewById(R.id.name);
        lastname = findViewById(R.id.lastaname);

        if (SesionGoogle() != null) {
            nombre = account.getGivenName();
            apellido = account.getFamilyName();
        } else if (SesionAuth() != null) {
            nombre = "Usuario";
            apellido = "Auth";
        }

        name.setText(nombre);
        lastname.setText(apellido);

        CargarImagen();
        findViewById(R.id.image).setOnClickListener(v -> openGallery());
    }

    // --------------------------------- METODO ABRIR GALERIA ---------------------------------//
    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST);
    }

    // --------------------------------- METODO SELECCION IMAGEN ---------------------------------//
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            uploadImageToCloudStorage();
        }
    }

    // --------------------------------- METODOS IMAGEN USUARIO---------------------------------//

    private void uploadImageToCloudStorage() {
        if (imageUri != null) {
            String imageName = "";
            String ruta = "";

            if (SesionGoogle() != null){
                imageName = "logo"+ SesionGoogle();
                ruta = SesionGoogle();
            } else if (SesionAuth() != null){
                imageName = "logo"+ SesionAuth();
                ruta = SesionAuth();
            }

            StorageReference imageRef = storageRef.child(ruta).child(imageName);
            imageRef.putFile(imageUri)
                    .addOnSuccessListener(taskSnapshot -> {
                        imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                            String downloadUrl = uri.toString();
                            Glide.with(Main_Usuario.this)
                                    .load(downloadUrl)
                                    .into(image);
                        });
                    })
                    .addOnFailureListener(exception -> {
                        Toast.makeText(Main_Usuario.this, "Error al subir la imagen", Toast.LENGTH_SHORT).show();
                    });
        }
    }
    private void CargarImagen(){
        String imageName = "";
        String ruta = "";

        if (SesionGoogle() != null){
            imageName = "logo"+ SesionGoogle();
            ruta = SesionGoogle();
        } else if (SesionAuth() != null){
            imageName = "logo"+ SesionAuth();
            ruta = SesionAuth();
        }

        StorageReference imageRef = storageRef.child(ruta).child(imageName);
        imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                String downloadUrl = uri.toString();
                Glide.with(image)
                        .load(downloadUrl)
                        .into(image);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.e(TAG, "Error al obtener la URL de descarga de la imagen: " + exception.getMessage());
                // Cargar la imagen predeterminada "noregistrado.png"
                StorageReference defaultImageRef = storageRef.child("noregistrado.png");
                defaultImageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String defaultDownloadUrl = uri.toString();
                        Glide.with(image)
                                .load(defaultDownloadUrl)
                                .into(image);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Log.e(TAG, "Error al cargar la imagen predeterminada: " + exception.getMessage());
                    }
                });
            }
        });
    }

    // --------------------------------- METODO IDENTIFICACIÓN ---------------------------------//
    private String SesionGoogle(){
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if (account != null) {
            return account.getEmail();
        } else {
            return null;
        }
    }
    private String SesionAuth(){
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null) {
            String email = currentUser.getEmail();
            return email;
        } else {
            return null;
        }
    }

    // --------------------------------- METODO ONLICK ---------------------------------//
    public void onClick(View view) {
        if (view.getId() == R.id.icono1) {
            Intent signup = new Intent(Main_Usuario.this, Main_Home.class);
            startActivity(signup);
        } else if (view.getId() == R.id.icono5){
            Intent remember2 = new Intent(Main_Usuario.this, Main_Usuario.class);
            startActivity(remember2);
        } else if (view.getId() == R.id.back && SesionGoogle() != null){
            GoogleSignIn.getClient(this, GoogleSignInOptions.DEFAULT_SIGN_IN).signOut();
            FirebaseAuth.getInstance().signOut();
            Toast.makeText(this, "Sesión cerrada Google", Toast.LENGTH_SHORT).show();
            Intent remember2 = new Intent(Main_Usuario.this, Main_Login.class);
            startActivity(remember2);
        }
        else if (view.getId() == R.id.back && SesionAuth() != null){
            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            mAuth.signOut();
            Toast.makeText(this, "Sesión cerrada Auth", Toast.LENGTH_SHORT).show();
            Intent remember2 = new Intent(Main_Usuario.this, Main_Login.class);
            startActivity(remember2);
        }
    }
}
