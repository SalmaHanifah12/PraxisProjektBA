package com.example.praxisprojekt.professor.klausureinsicht;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.praxisprojekt.model.Einsicht;
import com.example.praxisprojekt.Prevalent;
import com.example.praxisprojekt.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;



public class EinsichtAddProfAdapter extends RecyclerView.Adapter<EinsichtAddProfAdapter.MyViewHolder>{
    ArrayList<Einsicht> list;  private ImageView edit_kl;
    public EinsichtAddProfAdapter(ArrayList<Einsicht> list){
        this.list = list;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_add2,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.fach.setText(list.get(position).getFach());
        holder.Zeit.setText(list.get(position).getZeit());
        holder.raum.setText(list.get(position).getRaum());
        holder.startzeit.setText(list.get(position).getStartzeit());
        //final List<String> MKlausuren = new ArrayList<String>();
        //MKlausuren.add(list.get(position).getFach());
        final String fach = list.get(position).getKlausurId();
        final DatabaseReference RootRef;
        final String em= Prevalent.Profmail.replace('.','d');
        RootRef = FirebaseDatabase.getInstance().getReference().child("Professor").child(em);
        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        holder.add_kl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), BearbeitenEinsichtActivity.class);
                intent.putExtra("fName", list.get(position).getFach());
                v.getContext().startActivity(intent);
            }

        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView KlausurId, Zeit, raum, startzeit, fach;
        ImageView add_kl;
        public MyViewHolder(@NonNull View itemView){
            super(itemView);
            Zeit = itemView.findViewById(R.id.Zeit);
            raum = itemView.findViewById(R.id.raum);
            fach = itemView.findViewById(R.id.fach);
            startzeit =  itemView.findViewById(R.id.startzeit);
            add_kl = itemView.findViewById(R.id.add_kl);
        }
    }
}