package com.example.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.R;
import com.example.adapter.PatientAdapterMesaj;
import com.example.model.PatientModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ListaPacientiPrescriptie extends AppCompatActivity {

    private RecyclerView recyclerView;
    List<PatientModel> patientModels;
    private FirebaseFirestore firebaseFirestore;
    private PatientAdapterMesaj patientAdapter;
    private String idBun;
    private ImageButton btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_pacienti);

        btnBack=findViewById(R.id.imageButton);

        firebaseFirestore = FirebaseFirestore.getInstance();

        patientModels = new ArrayList<>();

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), DoctorInterface.class));
            }
        });


        showData();

        recyclerView = findViewById(R.id.RecycleViewListaPacientiMesaj);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        patientAdapter = new PatientAdapterMesaj(ListaPacientiPrescriptie.this, patientModels, new PatientAdapterMesaj.OnItemClickListener() {
            @Override
            public void onItemClick(PatientModel item) {
                readId(item.getEmail(), new ListaPacientiPrescriptie.FirestoreCallback() {
                    @Override
                    public void onCallback(String idBun) {
                        Intent intent = new Intent(getApplicationContext(), VizualizarePrescriptieDoctor.class);
                        intent.putExtra("ID", idBun);
                        startActivity(intent);

                    }
                });

            }
        });
        recyclerView.setAdapter(patientAdapter);

    }
    public void showData()
    {
        firebaseFirestore.collection("patient").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            patientModels.clear();
                            for (DocumentSnapshot snapshot : task.getResult()) {
                                PatientModel model = new PatientModel(snapshot.getString("nume"), snapshot.getString("prenume"),
                                        snapshot.getString("dataNasterii"),snapshot.getString("adresa"),
                                        snapshot.getString("telefon"),snapshot.getString("email"));


                                patientModels.add(model);
                            }
                            patientAdapter.notifyDataSetChanged();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ListaPacientiPrescriptie.this,"Eroare la introducerea datelor",Toast.LENGTH_SHORT).show();
            }

        });

    }

    private void readId(String email, ListaPacientiPrescriptie.FirestoreCallback firestoreCallback)
    {
        idBun=new String();

        firebaseFirestore.collection("patient").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful())
                {
                    for(DocumentSnapshot snapshot: task.getResult())
                    {
                        PatientModel model = new PatientModel(snapshot.getString("nume"), snapshot.getString("prenume"),
                                snapshot.getString("dataNasterii"),snapshot.getString("adresa"),
                                snapshot.getString("telefon"),snapshot.getString("email"));




                        if(model.getEmail().equals(email))
                        {
                            String id=snapshot.getId();
                            idBun=id;

                        }
                    }
                    firestoreCallback.onCallback(idBun);
                }
            }
        });

    }

    private interface FirestoreCallback{
        void onCallback(String idBun);
    }

    }