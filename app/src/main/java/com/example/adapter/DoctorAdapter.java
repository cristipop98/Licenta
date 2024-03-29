package com.example.adapter;

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
import com.example.activity.AddDoctor;
import com.example.model.DoctorModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;
import java.util.UUID;

public class DoctorAdapter extends RecyclerView.Adapter<DoctorAdapter.MyViewHolder>{

    public interface OnItemClickListener {
        void onItemClick(DoctorModel item);
    }


    Context context;
    public List<DoctorModel> doctorModelList;
    public FirebaseFirestore firebaseFirestore= FirebaseFirestore.getInstance();
    public String idBun;
    public OnItemClickListener listener;




    public DoctorAdapter(Context context, List<DoctorModel> doctorModelList,OnItemClickListener listener){
        this.context=context;
        this.doctorModelList=doctorModelList;
        this.listener=listener;
    }


    public void update(int position)
    {
        DoctorModel model=doctorModelList.get(position);
        Bundle bundle=new Bundle();
        UUID id=model.getId();
        bundle.putString("nume",model.getNume());
        bundle.putString("prenume",model.getPrenume());
        bundle.putString("email",model.getMail());
        bundle.putString("telefon",model.getTelefon());
        bundle.putString("specializare",model.getSpecializare());
        Intent intent=new Intent(context, AddDoctor.class);
        intent.putExtras(bundle);
        context.startActivity(intent);


    }

    public void deleteData(int position)
    {
        DoctorModel model=doctorModelList.get(position);
        readId(model.getTelefon(), new DoctorAdapter.FirestoreCallback() {
            @Override
            public void onCallback(String idBun) {
                Log.d("id",idBun);
                firebaseFirestore.collection("users").document(idBun).delete();

                firebaseFirestore.collection("doctor").document(idBun).delete()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful())
                                {
                                    notifyRemove(position);
                                    Toast.makeText(context,"Doctor sters", Toast.LENGTH_SHORT).show();
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

    public void notifyRemove(int position)
    {
        doctorModelList.remove(position);
        notifyItemRemoved(position);
    }


    @NonNull
    @Override
    public DoctorAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.doctor_item,parent,false);
        return new DoctorAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull DoctorAdapter.MyViewHolder holder, int position) {

        holder.nume.setText(doctorModelList.get(position).getNume());
        holder.prenume.setText(doctorModelList.get(position).getPrenume());
        holder.telefon.setText(doctorModelList.get(position).getTelefon());
        holder.mail.setText(doctorModelList.get(position).getMail());
        holder.specializare.setText(doctorModelList.get(position).getSpecializare());



    }

    @Override
    public int getItemCount() {
        return doctorModelList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        public TextView nume,prenume,telefon,mail,specializare;

        public MyViewHolder(@NonNull View itemView)
        {
            super(itemView);

            nume=(TextView) itemView.findViewById(R.id.nume_text_doctor);
            prenume=(TextView) itemView.findViewById(R.id.prenume_text_doctor);
            telefon=(TextView) itemView.findViewById(R.id.telefon_text_doctor);
            mail=(TextView) itemView.findViewById(R.id.mail_text_doctor);
            specializare=(TextView) itemView.findViewById(R.id.specializare_text_doctor);



        }

    }

    public void readId(String email, DoctorAdapter.FirestoreCallback firestoreCallback)
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



                        if(model.getTelefon().equals(email))
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

    public interface FirestoreCallback{
        void onCallback(String idBun);
    }

}
