package com.example.admininterface;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.R;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class ServiciuAmbulantaAdapter extends RecyclerView.Adapter<ServiciuAmbulantaAdapter.MyViewHolder>{

    public interface OnItemClickListener {
        void onItemClick(ServiciuAmbulantaModel item);
    }

    Context context;
    private List<ServiciuAmbulantaModel> ambulantaModelList;
    private FirebaseFirestore firebaseFirestore= FirebaseFirestore.getInstance();
    private String idBun;
    private OnItemClickListener listener;

    public ServiciuAmbulantaAdapter(Context context, List<ServiciuAmbulantaModel> ambulantaModelList,OnItemClickListener listener){
        // this.patientFragment=patientFragment;
        this.context=context;
        this.ambulantaModelList=ambulantaModelList;
        this.listener=listener;
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
            //  holder.disponibilitate.setTextColor(Color.GREEN);
        }
        else
        {
            holder.disponibilitate.setText("False");
            holder.disponibilitate.setTextColor(Color.RED);
        }
        holder.mail.setText(ambulantaModelList.get(position).getMail());
        holder.telefon.setText(ambulantaModelList.get(position).getTelefon());

        holder.bind(ambulantaModelList.get(position),listener);

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
        public void bind(final ServiciuAmbulantaModel item, final ServiciuAmbulantaAdapter.OnItemClickListener listener) {
            nume.setText(item.nume);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });
        }


    }
}
