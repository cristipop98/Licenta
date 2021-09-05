package com.example.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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
import com.example.activity.AddServiciuAmbulanta;
import com.example.model.DoctorModel;
import com.example.model.ServiciuAmbulantaModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;
import java.util.UUID;

public class ServiciuAmbulantaAdapter extends RecyclerView.Adapter<ServiciuAmbulantaAdapter.MyViewHolder>{

    public interface OnItemClickListener {
        void onItemClick(ServiciuAmbulantaModel item);
    }

    private Context context;
    private List<ServiciuAmbulantaModel> ambulantaModelList;
    private FirebaseFirestore firebaseFirestore= FirebaseFirestore.getInstance();
    private String idBun;
    private OnItemClickListener listener;

    public ServiciuAmbulantaAdapter(Context context, List<ServiciuAmbulantaModel> ambulantaModelList,OnItemClickListener listener){
        this.context=context;
        this.ambulantaModelList=ambulantaModelList;
        this.listener=listener;
    }

    public void update(int position)
    {
        ServiciuAmbulantaModel model=ambulantaModelList.get(position);
        Bundle bundle=new Bundle();
        UUID id=model.getId();
        bundle.putString("nume",model.getNume());
        bundle.putString("disponiblittate","True");
        bundle.putString("email",model.getMail());
        bundle.putString("telefon",model.getTelefon());
        Intent intent=new Intent(context, AddServiciuAmbulanta.class);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    public void deleteData(int position)
    {
        ServiciuAmbulantaModel model=ambulantaModelList.get(position);
        readId(model.getTelefon(), new ServiciuAmbulantaAdapter.FirestoreCallback() {
            @Override
            public void onCallback(String idBun) {
                Log.d("id",idBun);
                firebaseFirestore.collection("users").document(idBun).delete();

                firebaseFirestore.collection("ambulanta").document(idBun).delete()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful())
                                {
                                    notifyRemove(position);
                                    Toast.makeText(context,"Serviciu de ambulanta sters", Toast.LENGTH_SHORT).show();
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
        ambulantaModelList.remove(position);
        notifyItemRemoved(position);
    }

    @NonNull
    @Override
    public ServiciuAmbulantaAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.serviciuambulanta_item,parent,false);
        return new ServiciuAmbulantaAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ServiciuAmbulantaAdapter.MyViewHolder holder, int position) {

        int i=1;
        int j=0;

        holder.nume.setText(ambulantaModelList.get(position).getNume());

        if(ambulantaModelList.get(position).isDisponibilitate()) {
            holder.disponibilitate.setText("True");
            holder.disponibilitate.setTextColor(Color.GREEN);
        }
        else
        {
            holder.disponibilitate.setText("False");
            holder.disponibilitate.setTextColor(Color.RED);
        }
        holder.mail.setText(ambulantaModelList.get(position).getMail());
        holder.telefon.setText(ambulantaModelList.get(position).getTelefon());


    }

    @Override
    public int getItemCount() {
        return ambulantaModelList.size();
    }



    public static class MyViewHolder extends RecyclerView.ViewHolder{

        private TextView nume,mail,disponibilitate,telefon;

        public MyViewHolder(@NonNull View itemView)
        {
            super(itemView);

            nume=(TextView) itemView.findViewById(R.id.nume_text_ambulanta);
            mail=(TextView) itemView.findViewById(R.id.mail_text_ambulanta);
            disponibilitate=(TextView) itemView.findViewById(R.id.disponibilitate_text_ambulanta);
            telefon=(TextView) itemView.findViewById(R.id.telefon_text_ambulanta);



        }

    }

    public void readId(String email, ServiciuAmbulantaAdapter.FirestoreCallback firestoreCallback)
    {
        //  PatientIdList=new ArrayList();
        idBun=new String();

        firebaseFirestore.collection("ambulanta").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful())
                {
                    for(DocumentSnapshot snapshot: task.getResult())
                    {
                        ServiciuAmbulantaModel model = new ServiciuAmbulantaModel(snapshot.getString("nume"), snapshot.getBoolean("disponibilitate"),
                                snapshot.getString("email"),snapshot.getString("telefon"));

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
