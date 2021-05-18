package com.example.patientinterface;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.R;
import com.example.admininterface.AddPatient;
import com.example.admininterface.DoctorAdapter;
import com.example.admininterface.DoctorModel;
import com.example.admininterface.PatientModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ListaDoctoriProgramare extends AppCompatActivity{

    private RecyclerView recyclerView;
    List<DoctorModel> doctorModels;
    private FirebaseFirestore firebaseFirestore;
    private DoctorAdapter doctorAdapter;
    private String idBun;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_doctori_programare);

        firebaseFirestore=FirebaseFirestore.getInstance();

        doctorModels=new ArrayList<>();

        showData();

        recyclerView=findViewById(R.id.RecycleViewListaDoctoriProgramare);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        doctorAdapter=new DoctorAdapter(ListaDoctoriProgramare.this, doctorModels, new DoctorAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DoctorModel item) {
                readId(item.getMail(), new FirestoreCallback() {
                    @Override
                    public void onCallback(String idBun) {
                        Toast.makeText(ListaDoctoriProgramare.this,idBun,Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(getApplicationContext(), CalendarProgramare.class);
                        intent.putExtra("ID",idBun);
                        //setResult(CalendarProgramare.RESULT_OK,intent);
                        startActivity(intent);

                    }
                });

            }
        });
        recyclerView.setAdapter(doctorAdapter);


    }


    public void showData()
    {
        firebaseFirestore.collection("doctor").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            doctorModels.clear();
                            for (DocumentSnapshot snapshot : task.getResult()) {
                                DoctorModel model = new DoctorModel(snapshot.getString("nume"), snapshot.getString("prenume"),
                                        snapshot.getString("telefon"),snapshot.getString("email"),
                                        snapshot.getString("specializare"));




                                doctorModels.add(model);
                            }
                            doctorAdapter.notifyDataSetChanged();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ListaDoctoriProgramare.this,"Eroare la introducerea datelor",Toast.LENGTH_SHORT).show();
            }

        });

    }

    private void readId(String email, ListaDoctoriProgramare.FirestoreCallback firestoreCallback)
    {
        //  PatientIdList=new ArrayList();
        idBun=new String();

        firebaseFirestore.collection("doctor").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful())
                {
                    for(DocumentSnapshot snapshot: task.getResult())
                    {
                        DoctorModel model = new DoctorModel(snapshot.getString("nume"), snapshot.getString("prenume"),
                                snapshot.getString("telefon"), snapshot.getString("email"),
                                snapshot.getString("specializare"));



                        if(model.getMail().equals(email))
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