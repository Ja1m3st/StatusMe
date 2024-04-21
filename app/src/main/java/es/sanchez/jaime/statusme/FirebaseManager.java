package es.sanchez.jaime.statusme;

import static android.content.ContentValues.TAG;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FirebaseManager {
    private DatabaseReference databaseReference;

    public FirebaseManager() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Usuarios");
    }
    public void agregarContactoJson(String name, String lastname, String mail, String password, ArrayList<ArrayList> totaldias) {
        String contactoId = databaseReference.push().getKey();
        Usuario nuevoContacto = new Usuario(name, lastname, mail, password, totaldias);
        databaseReference.child(contactoId).setValue(nuevoContacto);
    }
    public void agregarContactoGoogleJson(String name, String lastname, String mail,  ArrayList<ArrayList> totaldias) {
        String contactoId = databaseReference.push().getKey();
        Usuario nuevoContacto = new Usuario.UsuarioGoogle(name, lastname, mail, totaldias); // Corregir el nombre de la variable
        databaseReference.child(contactoId).setValue(nuevoContacto);
    }
    public void obtenerContactos(ValueEventListener listener) {
        databaseReference.addValueEventListener(listener);
    }
    public void actualizarContacto(String id,String contraseña) {
        DatabaseReference contactoRef = databaseReference.child(id);
        contactoRef.child("password").setValue(contraseña);
    }
    public void buscarEmail(String email, EmailCallback callback) {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Usuarios");

        databaseReference.orderByChild("mail").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    callback.onEmailFound(true); // Llama al método del callback si el email se encuentra
                } else {
                    callback.onEmailFound(false); // Llama al método del callback si el email no se encuentra
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "Error al buscar el email: " + databaseError.getMessage());
                callback.onEmailFound(false);
            }
        });
    }
    public interface EmailCallback {
        void onEmailFound(boolean found);
    }

}
