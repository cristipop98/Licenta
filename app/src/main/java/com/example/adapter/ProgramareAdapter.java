package com.example.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.R;
import com.example.model.DoctorModel;
import com.example.model.PatientModel;
import com.example.model.ProgramareModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class ProgramareAdapter extends RecyclerView.Adapter<ProgramareAdapter.MyViewHolder> {


    Context context;
    public List<ProgramareModel> programareModelList;
    public FirebaseFirestore firebaseFirestore= FirebaseFirestore.getInstance();
    public String idBun;

    public ProgramareAdapter(Context context, List<ProgramareModel> programareModelList)
    {
        this.context=context;
        this.programareModelList=programareModelList;
    }
    @NonNull
    @Override
    public ProgramareAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.programare_item,parent,false);
        return new ProgramareAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ProgramareAdapter.MyViewHolder holder, int position) {

        firebaseFirestore.collection("patient").document(programareModelList.get(position).getPacientId()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot snapshot) {
                PatientModel model = new PatientModel(snapshot.getString("nume"), snapshot.getString("prenume"),
                        snapshot.getString("dataNasterii"), snapshot.getString("adresa"), snapshot.getString("email"), snapshot.getString("telefon"));

                holder.nume.setText(model.getNume() + " " + model.getPrenume());
            }
        });
        holder.programare.setText(programareModelList.get(position).getDataProgramare());

    }

    @Override
    public int getItemCount() {
        return programareModelList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        public TextView nume,programare;

        public MyViewHolder(@NonNull View itemView)
        {
            super(itemView);

            nume=(TextView) itemView.findViewById(R.id.doctor_text);
            programare=(TextView) itemView.findViewById(R.id.programare_text);



        }



    }
}
