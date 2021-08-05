package com.example.doctorinterface;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.R;
import com.example.admininterface.PatientModel;
import com.example.patientinterface.CalendarProgramare;
import com.example.patientinterface.FisaMedicalaModel;
import com.example.patientinterface.ListaDoctoriProgramare;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class AddFisaMedicala extends AppCompatActivity {

    private Button btnAdauga;
    private EditText inaltime,greutate,grupaSanguina,alergii,intolerante,boli;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private String idBun;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_fisa_medicala);

        inaltime=findViewById(R.id.inaltime);
        greutate=findViewById(R.id.greutate);
        grupaSanguina=findViewById(R.id.grupaSanguina);
        alergii=findViewById(R.id.alergii);
        intolerante=findViewById(R.id.intoleranta);
        boli=findViewById(R.id.boala);

        btnAdauga=findViewById(R.id.btnAddFisa);

        firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseAuth=FirebaseAuth.getInstance();

        Intent intent=getIntent();
        String idBun1=intent.getStringExtra("ID");



        btnAdauga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String s=inaltime.getText().toString();
                int inaltime1=Integer.parseInt(s);
                String s1=greutate.getText().toString();
                int greutate1=Integer.parseInt(s);
                String grupaSanguina1=grupaSanguina.getText().toString();
                String alergii1=alergii.getText().toString();
                String intoleranta1=intolerante.getText().toString();
                String boli1=boli.getText().toString();


                AddToFirestore(idBun1,inaltime1,greutate1,grupaSanguina1,alergii1,intoleranta1,boli1);



            }
        });
    }

    private void AddToFirestore(String id,int inaltime,int greutate,String grupaSanguina,String alergii,String intoleranta,String boli)
    {
        if(!id.isEmpty() && inaltime!=0 && greutate!=0 && !grupaSanguina.isEmpty() && !alergii.isEmpty() && !intoleranta.isEmpty() && !boli.isEmpty()) {

            DocumentReference collection = firebaseFirestore.collection("patient").document(id);

            HashMap<String, Object> map = new HashMap<>();
            map.put("PacientID", id);
            map.put("Inaltime", inaltime);
            map.put("Greutate", greutate);
            map.put("GrupaSanguina", grupaSanguina);
            map.put("Alergii", alergii);
            map.put("Intoleranta", intoleranta);
            map.put("Boli", boli);


                firebaseFirestore.collection("fisa").document(id).set(map)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {

                                    Toast.makeText(AddFisaMedicala.this, "Fisa introdusa", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(getApplicationContext(), ListaPacientiFisa.class));
                                }

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AddFisaMedicala.this, "Eroare la introducerea datelor", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
}

