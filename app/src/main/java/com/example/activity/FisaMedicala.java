package com.example.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.R;
import com.example.model.DoctorModel;
import com.example.model.FisaMedicalaModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class FisaMedicala extends AppCompatActivity {

    private TextView inaltime, greutate, grupaSanguina, alergii, intolerante, boli,nume,prenume,gen;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private List<FisaMedicalaModel> fisaMedicalaModels;
    private ImageButton btnBack;
    private Button btnAddFisa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fisa_medicala);

        inaltime = findViewById(R.id.inaltimeFisa);
        greutate = findViewById(R.id.greutateFisa);
        grupaSanguina = findViewById(R.id.grupaSanguinaFisa);
        alergii = findViewById(R.id.alergiiFisa);
        intolerante = findViewById(R.id.intoleranteFisa);
        boli = findViewById(R.id.boliFisa);
        nume=findViewById(R.id.numeFisa);
        prenume=findViewById(R.id.prenumeFisa);
        gen=findViewById(R.id.genFisa);
        btnBack=findViewById(R.id.imageButton);
        btnAddFisa=findViewById(R.id.btnAdaugareFisa);

        Intent intent;
        intent=getIntent();


        fisaMedicalaModels=new ArrayList<>();


        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();


        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent=getIntent();
                String type=intent.getStringExtra("type");
                if(type.equals("patient"))
                startActivity(new Intent(getApplicationContext(), PatientInterface.class));
                else if(type.equals("doctor"))
                    startActivity(new Intent(getApplicationContext(), ListaPacientiFisa.class));
            }
        });

        btnAddFisa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseFirestore.collection("users").get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (DocumentSnapshot snapshot : task.getResult()) {
                                        //String type=snapshot.getString("type");
                                        if(snapshot.getId().equals(firebaseAuth.getCurrentUser().getUid()))
                                        {
                                            String type=snapshot.getString("type");
                                            if(type.equals("doctor"))
                                            {
                                                String idPatient=intent.getStringExtra("ID");
                                                Intent intent1 = new Intent(getApplicationContext(), AddFisaMedicala.class);
                                                intent1.putExtra("ceva1", idPatient);
                                                startActivity(intent1);

                                               // startActivity(new Intent(getApplicationContext(), AddFisaMedicala.class));
                                            }
                                            else
                                            {
                                                Toast.makeText(FisaMedicala.this,"Nu aveti acces",Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                    }
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(FisaMedicala.this,"Eroare la introducerea datelor",Toast.LENGTH_SHORT).show();
                    }

                });


            }
        });


        String idPatient=intent.getStringExtra("ID");
        showData(idPatient);


    }



    public void showData(String id) {
        firebaseFirestore.collection("fisa").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        HashMap<String,String> ceva=new HashMap<>();
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot snapshot : task.getResult()) {
                                FisaMedicalaModel model = new FisaMedicalaModel(snapshot.getString("PacientID").toString(), snapshot.getLong("Inaltime").intValue(),
                                        snapshot.getLong("Greutate").intValue(), snapshot.getString("GrupaSanguina"), snapshot.getString("Alergii"), snapshot.getString("Intoleranta"),
                                        snapshot.getString("Gen"),
                                        (HashMap) snapshot.get("Diagnostic"));
                                ceva=(HashMap)snapshot.get("Diagnostic");


                                fisaMedicalaModels.add(model);


                                if(model.getPatientID().equals(firebaseAuth.getCurrentUser().getUid()) || model.getPatientID().equals(id))
                                {
                                    HashMap<String, String> finalCeva = ceva;
                                    HashMap<String, String> finalCeva1 = ceva;
                                    firebaseFirestore.collection("patient").document(model.getPatientID()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                            if(documentSnapshot.exists())
                                            {
                                                nume.setText(documentSnapshot.getString("nume") + " " + documentSnapshot.getString("prenume"));
                                                prenume.setText(documentSnapshot.getString("dataNasterii"));

                                                inaltime.setText(model.getHeight() + "");
                                                greutate.setText(model.getWeight() + "");
                                                grupaSanguina.setText(model.getBlood_type());
                                                alergii.setText(model.getAllergies());
                                                intolerante.setText(model.getIntolerances());
                                                gen.setText(model.getGen());
                                                for(Map.Entry<String,String> entry: finalCeva1.entrySet())
                                                {
                                                    String key=entry.getKey();
                                                    String value=entry.getValue();
                                                    boli.setText(key + "-" + value);
                                                }

                                            }
                                        }
                                    });



                                }

                            }
                        }
                    }
                });

                }
    }