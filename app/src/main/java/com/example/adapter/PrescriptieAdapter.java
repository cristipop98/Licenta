package com.example.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.R;
import com.example.model.PrescriptieModel;
import com.example.model.ProgramareModel;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class PrescriptieAdapter extends RecyclerView.Adapter<PrescriptieAdapter.MyViewHolder> {

    Context context;
    public List<PrescriptieModel> prescriptieModelList;
    public FirebaseFirestore firebaseFirestore= FirebaseFirestore.getInstance();
    public String idBun;

    public PrescriptieAdapter(Context context, List<PrescriptieModel> prescriptieModelList)
    {
        this.context=context;
        this.prescriptieModelList=prescriptieModelList;
    }

    @NonNull
    @Override
    public PrescriptieAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.prescriptie_item,parent,false);
        return new PrescriptieAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PrescriptieAdapter.MyViewHolder holder, int position) {

        holder.nume.setText(prescriptieModelList.get(position).getNume());
        holder.efecteSecundare.setText(prescriptieModelList.get(position).getEfecteSecundare());
        holder.administrare.setText(prescriptieModelList.get(position).getAdministrare());
        holder.pret.setText(prescriptieModelList.get(position).getPret()+"");

    }

    @Override
    public int getItemCount() {
        return prescriptieModelList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        public TextView nume,efecteSecundare,administrare,pret;

        public MyViewHolder(@NonNull View itemView)
        {
            super(itemView);

            nume=(TextView) itemView.findViewById(R.id.nume_prescriptie);
            efecteSecundare=(TextView) itemView.findViewById(R.id.efecteSecundare_precriptie);
            administrare=(TextView) itemView.findViewById(R.id.administrare_precriptie);
            pret=(TextView) itemView.findViewById(R.id.pret_prescriptie);

        }

    }
}
