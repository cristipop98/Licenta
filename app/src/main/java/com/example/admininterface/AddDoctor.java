package com.example.admininterface;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class AddDoctor extends AppCompatActivity {

    private Button btnAdauga;
    private EditText nume,prenume,telefon,mail,specializare,username,parola;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private String uNume,uPrenume,uTelefon,uMail,uSpecializare,uUsername,uId;
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
        username=findViewById(R.id.addDoctorParola);
        parola=findViewById(R.id.addDoctorParola);
        btnAdauga=findViewById(R.id.buttonAddDoctor);

        firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseAuth=FirebaseAuth.getInstance();



        btnAdauga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nume1=nume.getText().toString();
                String prenume1=prenume.getText().toString();
                String telefon1=telefon.getText().toString();
                String email1=mail.getText().toString();
                String specializare1=specializare.getText().toString();
                String parola1=parola.getText().toString();



                firebaseAuth.createUserWithEmailAndPassword(email1,parola1);

                String id=firebaseAuth.getCurrentUser().getUid();

                AddToFirestore(id,nume1,prenume1,telefon1,email1,specializare1);
            }
        });


    }
    private void AddToFirestore(String id,String nume1,String prenume1,String telefon1,String email1,String specializare1)
    {
        if(!nume1.isEmpty() && !prenume1.isEmpty() &&  !email1.isEmpty() && !telefon1.isEmpty() && !specializare1.isEmpty())
        {
            HashMap<String,Object> map=new HashMap<>();
            map.put("id",id);
            map.put("nume",nume1);
            map.put("prenume",prenume1);
            map.put("telefon",telefon1);
            map.put("email",email1);
            map.put("specializare",specializare1);



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
}