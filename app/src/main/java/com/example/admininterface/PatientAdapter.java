package com.example.admininterface;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;
import java.util.UUID;

public class PatientAdapter extends RecyclerView.Adapter<PatientAdapter.MyViewHolder> {

   // private PatientFragment patientFragment;
    Context context;
    private List<PatientModel> patientModelList;
    private FirebaseFirestore firebaseFirestore= FirebaseFirestore.getInstance();
    private String idBun;

    public PatientAdapter(Context context,List<PatientModel> patientModelList){
       // this.patientFragment=patientFragment;
        this.context=context;
        this.patientModelList=patientModelList;
    }

    public void update(int position)
    {
        PatientModel model=patientModelList.get(position);
        Bundle bundle=new Bundle();
        UUID id=model.getIdd();
       // bundle.putString("id", String.valueOf(id));
        bundle.putString("nume",model.getNume());
        bundle.putString("prenume",model.getPrenume());
        bundle.putString("dataNasterii",model.getDataNasterii());
        bundle.putString("adresa",model.getAdresa());
        bundle.putString("email",model.getEmail());
        bundle.putString("telefon",model.getTelefon());
        Intent intent=new Intent(context,AddPatient.class);
        intent.putExtras(bundle);
        context.startActivity(intent);


    }

    public void deleteData(int position)
    {
        PatientModel model=patientModelList.get(position);
        readId(model.getTelefon(), new FirestoreCallback() {
            @Override
            public void onCallback(String idBun) {
                Log.d("id",idBun);
                firebaseFirestore.collection("users").document(idBun).delete();

                firebaseFirestore.collection("patient").document(idBun).delete()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful())
                                {
                                    notifyRemove(position);
                                    Toast.makeText(context,"Pacient sters", Toast.LENGTH_SHORT).show();
                                }
                                else
                                {
                                    Toast.makeText(context,"Eroare" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }

                            }
                        });

            }
        });

    }

    private void notifyRemove(int position)
    {
        patientModelList.remove(position);
        notifyItemRemoved(position);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.patient_item,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
       // holder.id.setText(patientModelList.get(position).getId());
       // UUID id=patientModelList.get(position).getId();
        holder.nume.setText(patientModelList.get(position).getNume());
        holder.prenume.setText(patientModelList.get(position).getPrenume());
        holder.dataNasterii.setText(patientModelList.get(position).getDataNasterii().toString());
        holder.adresa.setText(patientModelList.get(position).getAdresa());
        holder.mail.setText(patientModelList.get(position).getEmail());
        holder.telefon.setText(patientModelList.get(position).getTelefon());
      //  holder.parola.setText(patientModelList.get(position).getPassword());

    }

    @Override
    public int getItemCount() {
        return patientModelList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView nume,prenume,dataNasterii,adresa,telefon,mail,parola;

        public MyViewHolder(@NonNull View itemView)
            {
                super(itemView);

                nume=(TextView) itemView.findViewById(R.id.nume_text);
                prenume=(TextView) itemView.findViewById(R.id.prenume_text);
                dataNasterii=(TextView) itemView.findViewById(R.id.dataNasterii_text);
                adresa=(TextView) itemView.findViewById(R.id.adresa_text);
                mail=(TextView) itemView.findViewById(R.id.mail_text);
                telefon=(TextView) itemView.findViewById(R.id.telefon_text);
              //  parola=(TextView) itemView.findViewById(R.id.parola_text);



        }


    }

    private void readId(String email, PatientAdapter.FirestoreCallback firestoreCallback)
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
                                snapshot.getString("dataNasterii"), snapshot.getString("adresa"), snapshot.getString("email"), snapshot.getString("telefon"),
                                snapshot.getString("password"));

                          //Log.d("idCeva",snapshot.getId());
                         // Log.d("email",model.getTelefon());
                          //Log.d("email1",email);

                        if(model.getTelefon().equals(email))
                        {
                            String id=snapshot.getId();
                            Log.d("id1",id);
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
