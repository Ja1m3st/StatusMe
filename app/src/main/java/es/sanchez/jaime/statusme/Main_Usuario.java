package es.sanchez.jaime.statusme;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class Main_Usuario extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri imageUri;
    private StorageReference storageRef;
    private  ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_usuario);

        image = findViewById(R.id.image);

        // Obtener una instancia de FirebaseStorage
        FirebaseStorage storage = FirebaseStorage.getInstance();

        // Crear una referencia a la ubicación en la nube donde se almacenará el archivo
        storageRef = storage.getReference().child("nombre_del_archivo.jpg");

        // Manejar el clic del botón o icono
        findViewById(R.id.image).setOnClickListener(v -> openGallery());
    }

    // Método para abrir la galería de imágenes
    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST);
    }

    // Método para manejar el resultado de la selección de la imagen desde la galería
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            // Subir la imagen seleccionada al almacenamiento en la nube
            uploadImageToCloudStorage();
        }
    }

    private void uploadImageToCloudStorage() {
        if (imageUri != null) {
            // Subir el archivo a Firebase Storage
            storageRef.putFile(imageUri)
                    .addOnSuccessListener(taskSnapshot -> {
                        // La carga se completó exitosamente
                        // Aquí puedes manejar la URL de descarga del archivo
                        storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                            String downloadUrl = uri.toString();
                            // Cargar la imagen en el ImageView usando Glide
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

    public void onClick(View view) {
        if (view.getId() == R.id.icono1) {
            Intent signup = new Intent(Main_Usuario.this, Main_Home.class);
            startActivity(signup);
        } else if (view.getId() == R.id.icono5){
            Intent remember2 = new Intent(Main_Usuario.this, Main_Usuario.class);
            startActivity(remember2);
        }
    }
}
