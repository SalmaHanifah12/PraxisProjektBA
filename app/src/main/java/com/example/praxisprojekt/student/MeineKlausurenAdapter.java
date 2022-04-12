package com.example.praxisprojekt.student;
import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.praxisprojekt.Prevalent;
import com.example.praxisprojekt.R;
import com.example.praxisprojekt.model.Student;
import com.example.praxisprojekt.model.Klausur;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


public class MeineKlausurenAdapter extends RecyclerView.Adapter<MeineKlausurenAdapter.MyViewHolder>{
    ArrayList<Klausur> list;
    public MeineKlausurenAdapter(ArrayList<Klausur> list){
        this.list = list;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_meineklausur,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        holder.fach.setText(list.get(position).getFach());
        holder.datum.setText(list.get(position).getDatum());
        holder.pruefer.setText(list.get(position).getPruefer());
        holder.startzeit.setText(list.get(position).getStartzeit());
        holder.raum.setText(list.get(position).getRaum());
        holder.remove_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                final String fach = list.get(position).getKlausurId();
                final String Namefach = list.get(position).getFach();
                final DatabaseReference RootRef;

                RootRef = FirebaseDatabase.getInstance().getReference().child("Student").child(Prevalent.StudentMatrikelnummer);
                RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Student Data = dataSnapshot.getValue(Student.class);
                        final HashMap<String, Object> userdataMap = new HashMap<>();
                        userdataMap.put("matrikelnr", Data.getMatrikelnr());
                        userdataMap.put("password",Data.getPassword());
                        userdataMap.put("token",Data.getToken());
                        String mKlausuren = Data.getMeineKlausuren().replaceAll(fach, "!");
                        mKlausuren =mKlausuren.trim();
                        if(mKlausuren.length()<2){
                            mKlausuren = "";
                        }
                        List<String> Mkl = Arrays.asList((mKlausuren.split(",")));

                        String a = "true";

                        String neuKl = "";

                        for(int i = 0;i <Mkl.size();i++) {
                                neuKl += Mkl.get(i) + ",";
                        }
                        if(neuKl.charAt(0)== '!'){
                            neuKl = neuKl.substring(1);
                        }
                        else{
                          neuKl= neuKl.replaceAll(",!", "");
                            neuKl = neuKl.substring(0,neuKl.length()-1);
                        }
                        if(neuKl.length()<2){
                            neuKl = " ";
                        }
                        userdataMap.put("meineKlausuren", neuKl);
                        RootRef.setValue(userdataMap);

                        FirebaseMessaging.getInstance().unsubscribeFromTopic(Namefach)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (!task.isSuccessful()) {
                                            Toast.makeText(v.getContext(), "Nicht erfolgreich", Toast.LENGTH_SHORT).show();
                                        }
                                        else{
                                            Toast.makeText(v.getContext(), "Erfolgreich", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });

                        Toast.makeText(v.getContext(), "Die Klausur wird von `Meine Klausuren` gelöscht.",Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView tag, datum,  fach, pruefer,  raum, startzeit,  endezeit, aufsicht;
        ImageView remove_btn;
        public MyViewHolder(@NonNull View itemView){
            super(itemView);
            fach = itemView.findViewById(R.id.fach);
            remove_btn = itemView.findViewById(R.id.remove_btn);
            datum = itemView.findViewById(R.id.datum);
            startzeit = itemView.findViewById(R.id.startzeit);
            raum = itemView.findViewById(R.id.raum);
            pruefer = itemView.findViewById(R.id.pruefer);
        }
    }

}