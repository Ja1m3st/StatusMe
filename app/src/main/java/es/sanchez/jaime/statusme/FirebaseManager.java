package es.sanchez.jaime.statusme;

import static android.content.ContentValues.TAG;

import android.util.Log;

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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;

public class FirebaseManager {

    // ----------------------------------- ZONA DECLARATIVA ----------------------------------- //
    private DatabaseReference databaseReference;

    // ----------------------------------- CONSTRUCTOR ----------------------------------- //
    public FirebaseManager() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Usuarios");
    }

    // ----------------------------------- MÉTODOS PARA AGREGAR CONTACTOS ----------------------------------- //
    public void agregarContactoJson(String name, String lastname, String mail, String password, ArrayList<ArrayList> totaldias) {
        String contactoId = databaseReference.push().getKey();
        Usuario nuevoContacto = new Usuario(name, lastname, mail, password, totaldias);
        databaseReference.child(contactoId).setValue(nuevoContacto);
    }

    public void agregarContactoGoogleJson(String name, String lastname, String mail, ArrayList<ArrayList> totaldias) {
        String contactoId = databaseReference.push().getKey();
        Usuario nuevoContacto = new Usuario.UsuarioGoogle(name, lastname, mail, totaldias);
        databaseReference.child(contactoId).setValue(nuevoContacto);
    }

    // ----------------------------------- MÉTODO PARA GUARDAR ARRAYLIST EN FIREBASE ----------------------------------- //
    public void guardarArrayListEnFirebase(ArrayList<Object> lista) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Usuarios");

        databaseReference.orderByChild("mail").equalTo(getMailUser()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String userId = snapshot.getKey();
                        DatabaseReference userTotalDiasReference = FirebaseDatabase.getInstance().getReference().child("Usuarios").child(userId).child("totaldias");

                        userTotalDiasReference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    ArrayList<ArrayList<Object>> totalDias = (ArrayList<ArrayList<Object>>) dataSnapshot.getValue();
                                    totalDias.add(lista);
                                    userTotalDiasReference.setValue(totalDias)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Log.d("Firebase", "ArrayList actualizado en Firebase correctamente");
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Log.e("Firebase", "Error al actualizar ArrayList en Firebase", e);
                                                }
                                            });
                                } else {
                                    userTotalDiasReference.setValue(lista)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Log.d("Firebase", "ArrayList guardado en Firebase correctamente");
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Log.e("Firebase", "Error al guardar ArrayList en Firebase", e);
                                                }
                                            });
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                Log.e("Firebase", "Error al leer datos de Firebase: " + databaseError.getMessage());
                            }
                        });
                    }
                } else {
                    Log.d("Firebase", "No se encontró ningún usuario con el correo electrónico proporcionado");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Firebase", "Error al buscar el usuario por correo electrónico: " + databaseError.getMessage());
            }
        });
    }
    public void obtenerInformacionUsuarioActual(FirebaseUser usuarioActual, ValueEventListener listener) {
        if (usuarioActual != null) {
            String emailUsuario = usuarioActual.getEmail();

            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Usuarios");
            databaseReference.orderByChild("mail").equalTo(emailUsuario).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        // Como solo debe haber un usuario con el mismo correo electrónico, podemos obtener su información directamente
                        DataSnapshot usuarioSnapshot = dataSnapshot.getChildren().iterator().next();
                        listener.onDataChange(usuarioSnapshot);
                    } else {
                        // Si no se encuentra ningún usuario con el correo electrónico proporcionado
                        listener.onDataChange(null);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Manejar el error de la base de datos si es necesario
                    Log.e("Firebase", "Error al buscar información del usuario: " + databaseError.getMessage());
                    listener.onCancelled(databaseError);
                }
            });
        } else {
            // Manejar el caso donde no hay un usuario actualmente autenticado
            Log.e("Firebase", "No hay usuario actualmente autenticado");
        }
    }

    // ----------------------------------- MÉTODO PARA OBTENER TOTAL DIAS DE USUARIO ----------------------------------- //
    public void obtenerTotalDiasDeUsuario(ValueEventListener listener) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Usuarios");
        databaseReference.orderByChild("mail").equalTo(getMailUser()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        DataSnapshot totalDiasSnapshot = snapshot.child("totaldias");
                        listener.onDataChange(totalDiasSnapshot);
                    }
                }
                listener.onDataChange(null);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Firebase", "Error al buscar totaldias del usuario: " + databaseError.getMessage());
                listener.onCancelled(databaseError);
            }
        });
    }

    // ----------------------------------- MÉTODO PARA ELIMINAR REGISTRO DE USUARIO ----------------------------------- //
    public static void eliminarRegistroUsuario(String firebaseKey) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Usuarios");
        databaseReference.orderByChild("mail").equalTo(getMailUser()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        DataSnapshot totalDiasSnapshot = snapshot.child("totaldias");
                        for (DataSnapshot diaSnapshot : totalDiasSnapshot.getChildren()) {
                            if (diaSnapshot.getKey().equals(firebaseKey)) {
                                diaSnapshot.getRef().removeValue()
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Log.d("Firebase", "Entrada eliminada con éxito");
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.e("Firebase", "Error al eliminar la entrada", e);
                                            }
                                        });
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Firebase", "Error al buscar totaldias del usuario: " + databaseError.getMessage());
            }
        });
    }

    // ----------------------------------- MÉTODO PARA OBTENER EMAIL DEL USUARIO ACTUAL ----------------------------------- //
    public static String getMailUser() {
        FirebaseUser usuario = FirebaseAuth.getInstance().getCurrentUser();
        return usuario.getEmail();
    }

    // ----------------------------------- MÉTODO PARA OBTENER USUARIO ACTUAL ----------------------------------- //
    public FirebaseUser obtenerUsuarioActual() {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        return firebaseAuth.getCurrentUser();
    }

    // ----------------------------------- MÉTODO PARA OBTENER CONTACTOS ----------------------------------- //
    public void obtenerContactos(ValueEventListener listener) {
        databaseReference.addValueEventListener(listener);
    }

    // ----------------------------------- MÉTODO PARA ACTUALIZAR CONTACTO ----------------------------------- //
    public void actualizarContacto(String id, String contraseña) {
        DatabaseReference contactoRef = databaseReference.child(id);
        contactoRef.child("password").setValue(contraseña);
    }

    // ----------------------------------- MÉTODO PARA BUSCAR EMAIL ----------------------------------- //
    public void buscarEmail(String email, EmailCallback callback) {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Usuarios");

        databaseReference.orderByChild("mail").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                callback.onEmailFound(dataSnapshot.exists());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "Error al buscar el email: " + databaseError.getMessage());
                callback.onEmailFound(false);
            }
        });
    }

    // ----------------------------------- MÉTODO PARA CREAR CARPETA EN STORAGE ----------------------------------- //
    public void crearCarpetaStorage(String email) {
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

    // ----------------------------------- INTERFACE CALLBACK PARA EMAIL ----------------------------------- //
    public interface EmailCallback {
        void onEmailFound(boolean found);
    }
}
