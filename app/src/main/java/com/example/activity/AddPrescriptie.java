package com.example.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.R;
import com.example.adapter.PrescriptieAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;

public class AddPrescriptie extends AppCompatActivity {

    private Button btnAdauga;
    private EditText nume,efecteSecundare,administare,pret;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private String uNume,uEfecteSecundare,uAdministrare,uPret,uId;
    private String id;
    private ArrayList PatientIdList;
    private CollectionReference itemref;
    private String idBun;
    private Toolbar toolbar;
    private PrescriptieAdapter prescriptieAdapter;
    private ImageButton btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_prescriptie);

        nume=findViewById(R.id.addPrescriptieNume);
        efecteSecundare=findViewById(R.id.addPrescriptieEfecteSecundare);
        administare=findViewById(R.id.addPrescriptieAdministrare);
        pret=findViewById(R.id.addPrescriptiePret);
        btnAdauga=findViewById(R.id.addPrescriptie);
        btnBack=findViewById(R.id.imageButton);

        firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseAuth=FirebaseAuth.getInstance();


        Intent intent=getIntent();
        String idBun1=intent.getStringExtra("ID");

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ListaPacientiPrescriptie.class));
            }
        });



        btnAdauga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nume1=nume.getText().toString();
                String efecteSecundare1=efecteSecundare.getText().toString();
                String administrare1=administare.getText().toString();
                String prt=pret.getText().toString();
                int pret1=Integer.parseInt(prt);

              AddToFirestore(nume1,efecteSecundare1,administrare1,pret1,idBun1);

            }
        });
    }

    private void AddToFirestore(String nume1,String efecteSecundare1,String administrare1,int pret1,String id)
    {
        if(!nume1.isEmpty() && !efecteSecundare1.isEmpty() && !administrare1.isEmpty() && pret1!=0 && !id.isEmpty())
        {
            HashMap<String, Object> map = new HashMap<>();
            map.put("PacientID", id);
            map.put("Nume", nume1);
            map.put("EfecteSecundare", efecteSecundare1);
            map.put("Administrare", administrare1);
            map.put("Pret", pret1);

            firebaseFirestore.collection("prescriptie").document().set(map)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {

                                Toast.makeText(AddPrescriptie.this, "Prescriptie adaugata", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(), ListaPacientiPrescriptie.class));
                            }

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(AddPrescriptie.this, "Eroare la introducerea datelor", Toast.LENGTH_SHORT).show();
                }
            });

        }
    }
}