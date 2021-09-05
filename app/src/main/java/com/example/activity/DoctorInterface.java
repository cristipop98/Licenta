package com.example.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

public class DoctorInterface extends AppCompatActivity {

    Button btnPacienti,btnProgramari,btnFise,btnMedicamente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_interface);

        FloatingActionButton fab = findViewById(R.id.fab2);

        btnPacienti=findViewById(R.id.btnVizualizarePacienti);
        btnProgramari=findViewById(R.id.btnVizualizareProgramare);
        btnFise=findViewById(R.id.btnEditareFisaMedicala);
        btnMedicamente=findViewById(R.id.btnPrescriereMedicamente);

        FirebaseAuth fAuth=FirebaseAuth.getInstance();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(fAuth.getCurrentUser()!=null)
                {
                    fAuth.getInstance().signOut();
                }
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            }
        });

        btnPacienti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ListaPacienti.class));

            }
        });

        btnFise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ListaPacientiFisa.class));
            }
        });

        btnMedicamente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ListaPacientiPrescriptie.class));
            }
        });

        btnProgramari.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), VizualizareProgramare.class));
            }
        });



    }
}