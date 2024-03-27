package es.sanchez.jaime.statusme;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FirebaseManager {
    private DatabaseReference databaseReference;

    public FirebaseManager() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Usuarios");
    }
    public void agregarContactoJson(String name, String lastname, String mail, String password) {
        String contactoId = databaseReference.push().getKey();
        Usuario nuevoContacto = new Usuario(name, lastname, mail, password);
        databaseReference.child(contactoId).setValue(nuevoContacto);
    }
    public void obtenerContactos(ValueEventListener listener) {
        databaseReference.addValueEventListener(listener);
    }
    public void actualizarContacto(String id,String contraseña) {
        DatabaseReference contactoRef = databaseReference.child(id);
        contactoRef.child("password").setValue(contraseña);
    }

}
