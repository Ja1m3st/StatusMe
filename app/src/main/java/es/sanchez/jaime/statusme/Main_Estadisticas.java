package es.sanchez.jaime.statusme;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Main_Estadisticas extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_estadisticas);

        FirebaseUser usuario = FirebaseAuth.getInstance().getCurrentUser();
        String emailUsuario = usuario.getEmail();

        // Llamar al método para obtener los datos de totaldias del usuario
        FirebaseManager firebaseManager = new FirebaseManager();
        firebaseManager.obtenerTotalDiasDeUsuario(emailUsuario, new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot != null && dataSnapshot.exists()) {
                    // Si hay datos, mostrarlos en la interfaz de usuario
                    ArrayList<ArrayList> totalDias = (ArrayList<ArrayList>) dataSnapshot.getValue();
                    datos(totalDias);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        TextView saludo = findViewById(R.id.saludo);
        String nombre = "";

        if (SesionGoogle() != null) {
            nombre = account.getGivenName();
        } else if (SesionAuth() != null) {
            nombre = "Usuario";
        }

        // Crear el animador de texto y configurar la velocidad de escritura
        TextAnimator animator = new TextAnimator("Veamos que tal vas, " + nombre, saludo);
        animator.setDuration(3000); // Duración de la animación en milisegundos
        saludo.startAnimation(animator);

    }

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

    public ArrayList<String> datos(ArrayList<ArrayList> totalDias){
        int dias = 0;
        ArrayList<String> info = new ArrayList<String>();

        if (totalDias != null) {
            for (ArrayList<ArrayList<String>> dia : totalDias) {
                if (dia != null) {
                    dias++;
                }
            }
        }
        dias--;
        if (totalDias != null) {
            for (int i = totalDias.size() - 1; i > 0; i--) {
                ArrayList<ArrayList<String>> dia = totalDias.get(i);
                if (dia != null) {
                    ArrayList<String> actividades = dia.get(0);
                    if (actividades != null) {
                        for (String actividad : actividades) {
                            info.add(actividad);
                        }
                    }
                }
                dias--;
            }
        }
        return info;
    }

    public void onClick(View view) {

        if (view.getId() == R.id.icono1) {
            Intent signup = new Intent(Main_Estadisticas.this, Main_Home.class);
            startActivity(signup);
        } else if (view.getId() == R.id.icono5) {
            Intent remember2 = new Intent(Main_Estadisticas.this, Main_Usuario.class);
            startActivity(remember2);
            //overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        } else if (view.getId() == R.id.icono3) {
            Intent animo = new Intent(Main_Estadisticas.this, Main_Seleccion.class);
            startActivity(animo);
        } else if (view.getId() == R.id.icono2) {
            Intent animo = new Intent(Main_Estadisticas.this, Main_Estadisticas.class);
            startActivity(animo);
        }

    }
}
