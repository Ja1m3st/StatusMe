package es.sanchez.jaime.statusme;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

public class Main_Remember extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_remember);
    }

    public void onClick(View view) {

        if (view.getId() == R.id.Back) {
            Intent back = new Intent(Main_Remember.this, Main_Login.class);
            startActivity(back);
        }
    }
}