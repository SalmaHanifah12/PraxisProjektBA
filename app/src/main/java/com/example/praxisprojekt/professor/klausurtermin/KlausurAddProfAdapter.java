package com.example.praxisprojekt.professor.klausurtermin;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.praxisprojekt.Prevalent;
import com.example.praxisprojekt.R;
import com.example.praxisprojekt.model.Klausur;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class KlausurAddProfAdapter extends RecyclerView.Adapter<KlausurAddProfAdapter.MyViewHolder>{
    ArrayList<Klausur> list;
    public KlausurAddProfAdapter(ArrayList<Klausur> list){
        this.list = list;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_add,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        holder.fach.setText(list.get(position).getFach());
        holder.datum.setText(list.get(position).getDatum());
        holder.raum.setText(list.get(position).getRaum());
        holder.pruefer.setText(list.get(position).getPruefer());
        holder.startzeit.setText(list.get(position).getStartzeit());
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
                KlausurAddProfActivity.namefach = list.get(position).getFach();
                Intent intent = new Intent(v.getContext(), BearbeitenSeiteActivity.class);
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
        TextView tag, datum,  fach, pruefer,  raum, startzeit,  endezeit, aufsicht;
        ImageView add_kl;
        public MyViewHolder(@NonNull View itemView){
            super(itemView);
            fach = itemView.findViewById(R.id.fach);
            add_kl = itemView.findViewById(R.id.add_kl);
            raum = itemView.findViewById(R.id.raum);
            datum = itemView.findViewById(R.id.datum);
            startzeit = itemView.findViewById(R.id.startzeit);
            pruefer = itemView.findViewById(R.id.pruefer);
        }
    }
}