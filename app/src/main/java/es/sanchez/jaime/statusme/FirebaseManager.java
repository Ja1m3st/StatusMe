package es.sanchez.jaime.statusme;

import static android.content.ContentValues.TAG;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

    public void guardarArrayListEnFirebase(String emailUsuario, ArrayList<ArrayList> lista) {
        // Obtener una referencia a la raíz de la base de datos
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Usuarios");

        // Realizar la consulta para buscar al usuario por su correo electrónico
        databaseReference.orderByChild("mail").equalTo(emailUsuario).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Iterar sobre los resultados (debería haber solo uno)
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String userId = snapshot.getKey(); // Obtener el UID del usuario

                        // Obtener una referencia al nodo totaldias del usuario
                        DatabaseReference userTotalDiasReference = FirebaseDatabase.getInstance().getReference().child("Usuarios").child(userId).child("totaldias");

                        // Obtener el ArrayList actual del usuario
                        userTotalDiasReference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    ArrayList<ArrayList<ArrayList>> totalDias = (ArrayList<ArrayList<ArrayList>>) dataSnapshot.getValue();
                                    totalDias.add(lista);
                                    // Actualizar el ArrayList en Firebase
                                    userTotalDiasReference.setValue(totalDias)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    // El ArrayList se ha actualizado correctamente en Firebase
                                                    Log.d("Firebase", "ArrayList actualizado en Firebase correctamente");
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    // Ocurrió un error al actualizar el ArrayList en Firebase
                                                    Log.e("Firebase", "Error al actualizar ArrayList en Firebase", e);
                                                }
                                            });
                                } else {
                                    // Si no hay un ArrayList para este usuario, creamos uno nuevo con los datos proporcionados
                                    userTotalDiasReference.setValue(lista)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    // El ArrayList se ha guardado correctamente en Firebase
                                                    Log.d("Firebase", "ArrayList guardado en Firebase correctamente");
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    // Ocurrió un error al guardar el ArrayList en Firebase
                                                    Log.e("Firebase", "Error al guardar ArrayList en Firebase", e);
                                                }
                                            });
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                // Manejar el error en la lectura de datos
                                Log.e("Firebase", "Error al leer datos de Firebase: " + databaseError.getMessage());
                            }
                        });
                    }
                } else {
                    // No se encontró ningún usuario con el correo electrónico proporcionado
                    Log.d("Firebase", "No se encontró ningún usuario con el correo electrónico proporcionado");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Manejar el error en la consulta
                Log.e("Firebase", "Error al buscar el usuario por correo electrónico: " + databaseError.getMessage());
            }
        });
    }


    public FirebaseUser obtenerUsuarioActual() {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        return firebaseAuth.getCurrentUser();
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
