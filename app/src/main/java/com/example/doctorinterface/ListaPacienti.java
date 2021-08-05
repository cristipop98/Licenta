package com.example.doctorinterface;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.R;
import com.example.admininterface.DoctorModel;
import com.example.admininterface.PatientModel;
import com.example.patientinterface.DoctorAdapterMesaj;
import com.example.patientinterface.ListaDoctoriMesaj;
import com.example.patientinterface.Mesaj;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ListaPacienti extends AppCompatActivity {
    private RecyclerView recyclerView;
    List<PatientModel> patientModels;
    private FirebaseFirestore firebaseFirestore;
    private PatientAdapterMesaj patientAdapter;
    private String idBun;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_pacienti);

        firebaseFirestore = FirebaseFirestore.getInstance();

        patientModels = new ArrayList<>();

        showData();

        recyclerView = findViewById(R.id.RecycleViewListaPacientiMesaj);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        patientAdapter = new PatientAdapterMesaj(ListaPacienti.this, patientModels, new PatientAdapterMesaj.OnItemClickListener() {
            @Override
            public void onItemClick(PatientModel item) {
                readId(item.getEmail(), new ListaPacienti.FirestoreCallback() {
                    @Override
                    public void onCallback(String idBun) {
                        // Toast.makeText(ListaDoctoriMesaj.this, idBun, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), Mesaj.class);
                        intent.putExtra("ID", idBun);
                        //setResult(CalendarProgramare.RESULT_OK,intent);
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
                                        snapshot.getString("telefon"),snapshot.getString("email"),snapshot.getString("password"));




                                patientModels.add(model);
                            }
                            patientAdapter.notifyDataSetChanged();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ListaPacienti.this,"Eroare la introducerea datelor",Toast.LENGTH_SHORT).show();
            }

        });

    }

    private void readId(String email, ListaPacienti.FirestoreCallback firestoreCallback)
    {
        //  PatientIdList=new ArrayList();
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
                                snapshot.getString("telefon"),snapshot.getString("email"),snapshot.getString("password"));




                        if(model.getEmail().equals(email))
                        {
                            String id=snapshot.getId();
                            //Log.d("id1",id);
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