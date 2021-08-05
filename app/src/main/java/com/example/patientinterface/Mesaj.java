package com.example.patientinterface;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.R;
import com.example.admininterface.AddDoctor;
import com.example.admininterface.Admin;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class Mesaj extends AppCompatActivity {

    TextView username;
    CircleImageView profile_image;
    FirebaseUser firebaseAuth;
    ImageButton btnSend;
    EditText mesaj;
    FirebaseFirestore firebaseFirestore;
    CollectionReference collectionReference;

    MessageAdapter messageAdapter;
    List<MesajModel> mesajModels;

    RecyclerView recyclerView;
    Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mesaj);

        intent=getIntent();
        String idDoctor=intent.getStringExtra("ID");

        username=findViewById(R.id.textViewNumeDoctor);
        btnSend=findViewById(R.id.btnSendMessage);
        mesaj=findViewById(R.id.textMesaj);

        recyclerView=findViewById(R.id.recycleViewMesaje);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);


        firebaseAuth=FirebaseAuth.getInstance().getCurrentUser();
        firebaseFirestore=FirebaseFirestore.getInstance();

        readMessage(firebaseAuth.getUid(),idDoctor);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg=mesaj.getText().toString();
                if(!msg.equals(""))
                {

                    sendMessage(firebaseAuth.getUid(),idDoctor,msg);
                }
                else
                {
                    Toast.makeText(Mesaj.this,"You can not send empty message",Toast.LENGTH_SHORT).show();
                }
                mesaj.setText("");
            }
        });



    }
    @ServerTimestamp
    private void sendMessage(String sender, String reciever, String message)
    {

        //DocumentReference collection1=firebaseFirestore.collection("patient").document(sender);
       // DocumentReference collection2=firebaseFirestore.collection("doctor").document(reciever);

        HashMap<String,Object> hashMap=new HashMap<>();;

        /*
        if(collection1.getId().equals(sender)) {
            hashMap.put("sender",collection2);
            hashMap.put("reciever",collection1);
        }
        else if(collection1.getId().equals(reciever))
        {
            hashMap.put("sender",collection1);
            hashMap.put("reciever",collection2);
        }

         */
        hashMap.put("sender",sender);
        hashMap.put("reciever",reciever);
        hashMap.put("message",message);
        hashMap.put("timestamp", FieldValue.serverTimestamp());


        firebaseFirestore.collection("messages").document().set(hashMap);

    }

    private void readMessage(String myid,String userId)
    {
        mesajModels=new ArrayList<>();

        firebaseFirestore.collection("messages").orderBy("timestamp")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        mesajModels.clear();
                        if (error != null) {
                            Log.d("Error:",error.getMessage());
                        } else {
                            for (QueryDocumentSnapshot querySnapshot : value) {
                                String sender = querySnapshot.getString("sender");
                                String reciever = querySnapshot.getString("reciever");
                                String message = querySnapshot.getString("message");


                                if (sender.equals(myid) && reciever.equals(userId) || sender.equals(userId) && reciever.equals(myid)) {
                                    MesajModel mesajModel = new MesajModel();
                                    mesajModel.setSender(sender);
                                    mesajModel.setReciever(reciever);
                                    mesajModel.setMessage(message);
                                    mesajModels.add(mesajModel);
                                    
                                }
                                messageAdapter = new MessageAdapter(Mesaj.this, mesajModels);
                                recyclerView.setAdapter(messageAdapter);
                            }

                        }
                    }
                });

    }

}