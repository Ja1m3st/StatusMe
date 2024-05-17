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

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class Main_Usuario extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri imageUri;
    private StorageReference storageRef;
    private FirebaseStorage storage;
    private ImageView image;
    private TextView name, lastname;
    private String nombre = "";
    private String apellido = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_usuario);

        // Inicialización de Firebase Storage
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();

        // Inicialización de vistas
        image = findViewById(R.id.image);
        name = findViewById(R.id.name);
        lastname = findViewById(R.id.lastaname);

        // Obtener cuenta de Google
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);

        // Comprobar la sesión activa
        if (SesionGoogle() != null) {
            nombre = account.getGivenName();
            apellido = account.getFamilyName();
        } else if (SesionAuth() != null) {
            nombre = "Usuario";
            apellido = "Auth";
        }

        // Establecer nombre y apellido en las vistas
        name.setText(nombre);
        lastname.setText(apellido);

        // Cargar imagen del usuario
        CargarImagen();

        // Asignar listener para abrir la galería
        image.setOnClickListener(v -> openGallery());
    }

    // Método para abrir la galería
    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST);
    }

    // Método para manejar el resultado de la actividad de selección de imagen
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            uploadImageToCloudStorage();
        }
    }

    // Método para subir la imagen seleccionada a Firebase Storage
    private void uploadImageToCloudStorage() {
        if (imageUri != null) {
            String imageName = "";
            String ruta = "";

            if (SesionGoogle() != null) {
                imageName = "logo" + SesionGoogle();
                ruta = SesionGoogle();
            } else if (SesionAuth() != null) {
                imageName = "logo" + SesionAuth();
                ruta = SesionAuth();
            }

            StorageReference imageRef = storageRef.child(ruta).child(imageName);
            imageRef.putFile(imageUri)
                    .addOnSuccessListener(taskSnapshot -> imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        String downloadUrl = uri.toString();
                        Glide.with(Main_Usuario.this)
                                .load(downloadUrl)
                                .into(image);
                    }))
                    .addOnFailureListener(exception -> Toast.makeText(Main_Usuario.this, "Error al subir la imagen", Toast.LENGTH_SHORT).show());
        }
    }

    // Método para cargar la imagen del usuario desde Firebase Storage
    private void CargarImagen() {
        String imageName = "";
        String ruta = "";

        if (SesionGoogle() != null) {
            imageName = "logo" + SesionGoogle();
            ruta = SesionGoogle();
        } else if (SesionAuth() != null) {
            imageName = "logo" + SesionAuth();
            ruta = SesionAuth();
        }

        StorageReference imageRef = storageRef.child(ruta).child(imageName);
        imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
            String downloadUrl = uri.toString();
            Glide.with(image)
                    .load(downloadUrl)
                    .into(image);
        }).addOnFailureListener(exception -> {
            Log.e(TAG, "Error al obtener la URL de descarga de la imagen: " + exception.getMessage());
            // Cargar la imagen predeterminada "noregistrado.png"
            StorageReference defaultImageRef = storageRef.child("noregistrado.png");
            defaultImageRef.getDownloadUrl().addOnSuccessListener(uri1 -> {
                String defaultDownloadUrl = uri1.toString();
                Glide.with(image)
                        .load(defaultDownloadUrl)
                        .into(image);
            }).addOnFailureListener(exception1 -> Log.e(TAG, "Error al cargar la imagen predeterminada: " + exception1.getMessage()));
        });
    }

    // Método para verificar la sesión de Google
    private String SesionGoogle() {
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        return account != null ? account.getEmail() : null;
    }

    // Método para verificar la sesión de Firebase Auth
    private String SesionAuth() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        return currentUser != null ? currentUser.getEmail() : null;
    }

    // Método para manejar los clics en los íconos
    public void onClick(View view) {
        if (view.getId() == R.id.icono1) {
            Intent signup = new Intent(Main_Usuario.this, Main_Home.class);
            startActivity(signup);
        } else if (view.getId() == R.id.icono5) {
            Intent remember2 = new Intent(Main_Usuario.this, Main_Usuario.class);
            startActivity(remember2);
        } else if (view.getId() == R.id.back && SesionGoogle() != null) {
            GoogleSignIn.getClient(this, GoogleSignInOptions.DEFAULT_SIGN_IN).signOut();
            FirebaseAuth.getInstance().signOut();
            Toast.makeText(this, "Sesión cerrada Google", Toast.LENGTH_SHORT).show();
            Intent remember2 = new Intent(Main_Usuario.this, Main_Login.class);
            startActivity(remember2);
        } else if (view.getId() == R.id.back && SesionAuth() != null) {
            FirebaseAuth.getInstance().signOut();
            Toast.makeText(this, "Sesión cerrada Auth", Toast.LENGTH_SHORT).show();
            Intent remember2 = new Intent(Main_Usuario.this, Main_Login.class);
            startActivity(remember2);
        } else if (view.getId() == R.id.icono2) {
            Intent remember2 = new Intent(Main_Usuario.this, Main_Estadisticas.class);
            startActivity(remember2);
        } else if (view.getId() == R.id.icono3) {
            Intent remember2 = new Intent(Main_Usuario.this, Main_Seleccion.class);
            startActivity(remember2);
        }
    }
}
