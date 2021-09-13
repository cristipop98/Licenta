package com.example.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.R;
import com.example.adapter.PrescriptieAdapter;
import com.example.model.DoctorModel;
import com.example.model.PrescriptieModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class AddPrescriptie extends AppCompatActivity {

    private Button btnAdauga;
    private EditText nume,efecteSecundare,administare,pret;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private String uNume,uEfecteSecundare,uAdministrare,uId;
    private int uPret;
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
        toolbar=findViewById(R.id.addPrescriptieToolbar);

        firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseAuth=FirebaseAuth.getInstance();

        Bundle bundle=getIntent().getExtras();


        Intent intent=getIntent();
        String idBun1=intent.getStringExtra("ID");

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ListaPacientiPrescriptie.class));
            }
        });

        if(bundle!=null)
        {
            btnAdauga.setText("Update");
            toolbar.setTitle("Editeaza prescriptie");
            uNume=bundle.getString("Nume");
            uEfecteSecundare=bundle.getString("EfecteSecundare");
            uAdministrare=bundle.getString("Administrare");
            uPret=bundle.getInt("Pret");

            nume.setText(uNume);
            efecteSecundare.setText(uEfecteSecundare);
            administare.setText(uAdministrare);
            pret.setText(""+uPret);
        }
        else
        {
            btnAdauga.setText("Adauga");
        }



        btnAdauga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nume1=nume.getText().toString();
                String efecteSecundare1=efecteSecundare.getText().toString();
                String administrare1=administare.getText().toString();
                String prt=pret.getText().toString();
                int pret1=Integer.parseInt(prt);

                if(bundle!=null)
                {
                    firebaseFirestore.collection("prescriptie").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful())
                            {
                                for(DocumentSnapshot snapshot: task.getResult())
                                {
                                    PrescriptieModel model = new PrescriptieModel(snapshot.getString("Nume"), snapshot.getString("EfecteSecundare"),
                                            snapshot.getString("Administrare"),snapshot.getLong("Pret").intValue(),snapshot.getString("PacientID"));

                                    if(model.getNume().equals(nume1))
                                    {
                                        String id=snapshot.getId();
                                        //Log.d("id1",id);
                                        idBun=id;
                                        UpdateToFireStore(idBun,nume1,efecteSecundare1,administrare1,pret1);

                                    }
                                }
                            }
                        }
                    });
                }
                else {

                    AddToFirestore(nume1, efecteSecundare1, administrare1, pret1, idBun1);
                }

            }
        });
    }

    private void AddToFirestore(String nume1,String efecteSecundare1,String administrare1,int pret1,String id)
    {
        if(!nume1.isEmpty() && !efecteSecundare1.isEmpty() && !administrare1.isEmpty() && pret1!=0 && !id.isEmpty())
        {
            UUID id1=new UUID(8,8);
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

    public void UpdateToFireStore(String id,String nume1,String efecteSecundare1,String administrare1,int pret1)
    {

        firebaseFirestore.collection("prescriptie").document(id).update("Nume",nume1,"EfecteSecundare",efecteSecundare1,"Administrare",administrare1,"Pret",pret1)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(AddPrescriptie.this,"Prescriptie actualizata", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), ListaPacientiPrescriptie.class));

                        }
                        else
                        {
                            Toast.makeText(AddPrescriptie.this,"Eroare la actualizarea datelor" + task.getException(), Toast.LENGTH_SHORT).show();
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AddPrescriptie.this,e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }
}