package com.example.patientinterface;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.R;
import com.example.admininterface.AddPatient;
import com.example.admininterface.DoctorAdapter;
import com.example.admininterface.DoctorModel;
import com.example.admininterface.PatientAdapter;
import com.example.admininterface.PatientModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;
import java.util.UUID;

public class DoctorAdapterMesaj extends RecyclerView.Adapter<DoctorAdapterMesaj.MyViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(DoctorModel item);
    }

    // private PatientFragment patientFragment;
    Context context;
    private List<DoctorModel> doctorModelList;
    private FirebaseFirestore firebaseFirestore= FirebaseFirestore.getInstance();
    private OnItemClickListener listener;

    public DoctorAdapterMesaj(Context context, List<DoctorModel> doctorModelList,OnItemClickListener listener){
        // this.patientFragment=patientFragment;
        this.context=context;
        this.doctorModelList=doctorModelList;
        this.listener=listener;

    }



    @NonNull
    @Override
    public DoctorAdapterMesaj.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.doctor_item_mesaj,parent,false);
        return new DoctorAdapterMesaj.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        DoctorModel model=doctorModelList.get(position);
        holder.nume.setText(model.getNume());
        //holder.nume.setText(" ");
        //holder.nume.setText(doctorModelList.get(position).getPrenume());
        holder.bind(doctorModelList.get(position),listener);

    }

    @Override
    public int getItemCount() {
        return doctorModelList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        public TextView nume;
        public ImageView imageView;

        public MyViewHolder(@NonNull View itemView)
        {
            super(itemView);

            nume=(TextView) itemView.findViewById(R.id.numeDoctor);
            imageView=itemView.findViewById(R.id.profile_image);




        }
        public void bind(final DoctorModel item, final DoctorAdapterMesaj.OnItemClickListener listener) {
            nume.setText("Dr."+item.getNume() + " " + item.getPrenume() + "-" + item.getSpecializare());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });
        }


    }


}
