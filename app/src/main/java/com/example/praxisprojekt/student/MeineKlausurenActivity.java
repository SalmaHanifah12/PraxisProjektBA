package com.example.praxisprojekt.student;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.praxisprojekt.Prevalent;
import com.example.praxisprojekt.R;
import com.example.praxisprojekt.model.Student;
import com.example.praxisprojekt.model.Einsicht;
import com.example.praxisprojekt.model.Klausur;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MeineKlausurenActivity extends AppCompatActivity {
    DatabaseReference ref;
    ArrayList<Klausur> list1;
    ArrayList<Einsicht> list2;
    RecyclerView recyclerView1;
    RecyclerView recyclerView2;
    private TextView datum,  fach, pruefer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meine_klausuren);
        ref = FirebaseDatabase.getInstance().getReference();
        recyclerView1 = findViewById(R.id.rv);
        recyclerView2 = findViewById(R.id.rv2);
        fach = (TextView) findViewById(R.id.fach);
        datum = (TextView) findViewById(R.id.datum);
        pruefer = (TextView) findViewById(R.id.pruefer);
    }
    protected void onStart() {
        super.onStart();
        if(ref != null){
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Student Data = dataSnapshot.child("Student").child(Prevalent.StudentMatrikelnummer).getValue(Student.class);
                    String mKlausuren = Data.getMeineKlausuren();
                    String mKlausuren2 = Data.getMeineKlausuren();
                    List<String> Mkl = Arrays.asList((mKlausuren.split(",")));
                    List<String> Mkl2 = Arrays.asList((mKlausuren2.split(",")));
                        list1 = new ArrayList<>();
                        list2 = new ArrayList<>();
                        for (DataSnapshot ds : dataSnapshot.child("Klausur").getChildren()) {
                            Klausur data = ds.getValue(Klausur.class);
                            if(data.getPruefer().equals(" ")||data.getDatum().equals(" ")||data.getStartzeit().equals(" ")||data.getRaum().equals(" ")) {
                            }
                            else {
                                if (Mkl.contains(data.getKlausurId())) {
                                    list1.add(ds.getValue(Klausur.class));
                                }
                            }
                        }
                        MeineKlausurenAdapter adapterClass = new MeineKlausurenAdapter(list1);
                    for (DataSnapshot ds2 : dataSnapshot.child("Einsicht").getChildren()) {
                        Einsicht data2 = ds2.getValue(Einsicht.class);
                        if(data2.getZeit().equals(" ")||data2.getStartzeit().equals(" ")||data2.getRaum().equals(" ")) {
                        }
                        else {
                            if (Mkl2.contains(data2.getKlausurId())) {
                                list2.add(ds2.getValue(Einsicht.class));
                            }
                        }
                    }
                        MeineEinsicht adapterClass2 = new MeineEinsicht(list2);
                        recyclerView1.setAdapter(adapterClass);
                        recyclerView2.setAdapter(adapterClass2);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(MeineKlausurenActivity.this, databaseError.getMessage(),Toast.LENGTH_SHORT).show();
                }
            });
        }


    }


}