package com.example.admininterface;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;

public class AddDoctor extends AppCompatActivity {

    private Button btnAdauga;
    private EditText nume,prenume,telefon,mail,specializare,parola;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private String uNume,uPrenume,uTelefon,uMail,uSpecializare,uId;
    private String idBun;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_doctor);

        nume=findViewById(R.id.addDoctorNume);
        prenume=findViewById(R.id.addDoctorPrenume);
        telefon=findViewById(R.id.addDoctorTelefon);
        mail=findViewById(R.id.addDoctorEmail);
        specializare=findViewById(R.id.addDoctorSpecializare);
        parola=findViewById(R.id.addDoctorParola);
        btnAdauga=findViewById(R.id.buttonAddDoctor);

        firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseAuth=FirebaseAuth.getInstance();

        Bundle bundle=getIntent().getExtras();

        if(bundle!=null) {
            btnAdauga.setText("Update");
            uId = bundle.getString("id");
            uNume = bundle.getString("nume");
            uPrenume = bundle.getString("prenume");
            uMail = bundle.getString("email");
            uTelefon = bundle.getString("telefon");
            uSpecializare = bundle.getString("specializare");

            nume.setText(uNume);
            prenume.setText(uPrenume);
            mail.setText(uMail);
            telefon.setText(uTelefon);
            specializare.setText(uSpecializare);
        }
        else
        {
            btnAdauga.setText("Adauga");
        }



        btnAdauga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nume1=nume.getText().toString();
                String prenume1=prenume.getText().toString();
                String telefon1=telefon.getText().toString();
                String email1=mail.getText().toString();
                String specializare1=specializare.getText().toString();
                String parola1=parola.getText().toString();

                Bundle bundle1=getIntent().getExtras();

                if(bundle!=null)
                {
                    readId(email1,new FirestoreCallback(){
                        @Override
                        public void onCallback(String id) {
                            Log.d("id",email1);
                            UpdateToFireStore(id,nume1,prenume1,telefon1,email1,specializare1);
                        }
                    });
                }
                else {


                    firebaseAuth.createUserWithEmailAndPassword(email1, parola1).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser user = task.getResult().getUser();

                                String id = user.getUid();

                                AddToFirestore(id, nume1, prenume1, telefon1, email1, specializare1, parola1);


                            }

                        }
                    });
                }


            }
        });


    }
    private void AddToFirestore(String id,String nume1,String prenume1,String telefon1,String email1,String specializare1,String parola1)
    {
        if(!nume1.isEmpty() && !prenume1.isEmpty() &&  !email1.isEmpty() && !telefon1.isEmpty() && !specializare1.isEmpty() && !parola1.isEmpty())
        {
            HashMap<String,Object> map=new HashMap<>();
           // map.put("id",id);
            map.put("nume",nume1);
            map.put("prenume",prenume1);
            map.put("telefon",telefon1);
            map.put("email",email1);
            map.put("specializare",specializare1);

            HashMap<String,Object> mapUser=new HashMap<>();
            mapUser.put("email",email1);
            mapUser.put("password",parola1);
            mapUser.put("type","doctor");

            firebaseFirestore.collection("users").document(id).set(mapUser);

            firebaseFirestore.collection("doctor").document(id).set(map)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                Toast.makeText(AddDoctor.this,"Doctor introdus",Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(), Admin.class));
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(AddDoctor.this,"Eroare la introducerea datelor",Toast.LENGTH_SHORT).show();
                }
            });


        }
        else
        {
            Toast.makeText(this,"You must complete all fields",Toast.LENGTH_SHORT).show();
        }
    }

    private void UpdateToFireStore(String id,String nume1,String prenume1,String telefon1,String email1,String specializare1)
    {

        firebaseFirestore.collection("doctor").document(id).update("nume",nume1,"prenume",prenume1,"telefon",telefon1,"email",email1,"specializare",specializare1)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(AddDoctor.this,"Doctor actualizat", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), Admin.class));

                        }
                        else
                        {
                            Toast.makeText(AddDoctor.this,"Eroare la actualizarea datelor" + task.getException(), Toast.LENGTH_SHORT).show();
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AddDoctor.this,e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void readId(String email, AddDoctor.FirestoreCallback firestoreCallback)
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
                                snapshot.getString("telefon"),snapshot.getString("email"),
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