package com.example.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.R;
import com.example.model.PatientModel;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class PatientAdapterMesaj extends RecyclerView.Adapter<PatientAdapterMesaj.MyViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(PatientModel item);
    }

    Context context;
    private List<PatientModel> patientModelList;
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private OnItemClickListener listener;

    public PatientAdapterMesaj(Context context, List<PatientModel> patientModelList, OnItemClickListener listener) {
        this.context = context;
        this.patientModelList = patientModelList;
        this.listener = listener;

    }

    @NonNull
    @Override
    public PatientAdapterMesaj.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.doctor_item_mesaj, parent, false);
        return new PatientAdapterMesaj.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PatientAdapterMesaj.MyViewHolder holder, int position) {
        holder.nume.setText(patientModelList.get(position).getNume());
        holder.bind(patientModelList.get(position), listener);
    }


    @Override
    public int getItemCount() {
        return patientModelList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView nume;
        public ImageView imageView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            nume = (TextView) itemView.findViewById(R.id.numeDoctor);
            imageView = itemView.findViewById(R.id.profile_image);


        }

        public void bind(final PatientModel item, final PatientAdapterMesaj.OnItemClickListener listener) {
            nume.setText(item.getNume() + " " + item.getPrenume());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });
        }
    }
}

