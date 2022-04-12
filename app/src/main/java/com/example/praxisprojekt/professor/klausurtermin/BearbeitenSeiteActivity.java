package com.example.praxisprojekt.professor.klausurtermin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.praxisprojekt.FCMSend;
import com.example.praxisprojekt.R;
import com.example.praxisprojekt.model.Einsicht;
import com.example.praxisprojekt.model.Klausur;
import com.example.praxisprojekt.professor.klausureinsicht.KlausurEinsichtActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class BearbeitenSeiteActivity extends AppCompatActivity {
    private EditText Pruefer, Datum, Beginnzeit, Raum,FachName;
    private Button SaveButton,DeleteButton;
    private ProgressDialog loadingBar;
    private Spinner spinnerTextSize;
    private String fName;
    ArrayList<Klausur> list;
    ArrayList<Klausur> list1;
    DatabaseReference RootRef1;
    private String parentDbName = "Klausur";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bearbeiten_seite);
        fName = getIntent().getStringExtra("fName");
        SaveButton = (Button) findViewById(R.id.save_btn);
        DeleteButton = (Button) findViewById(R.id.delete_btn);
        FachName = (EditText) findViewById(R.id.Fach);
        Pruefer = (EditText) findViewById(R.id.Pruefer);
        Datum = (EditText) findViewById(R.id.Datum);
        Beginnzeit = (EditText) findViewById(R.id.Beginnzeit);
        Raum = (EditText) findViewById(R.id.Raum);
        loadingBar = new ProgressDialog(this);
        RootRef1 = FirebaseDatabase.getInstance().getReference().child("Klausur");
        RootRef1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list1 = new ArrayList<>();

                for(DataSnapshot ds:dataSnapshot.getChildren()) {
                    Einsicht einsicht = ds.getValue(Einsicht.class);
                    list1.add(ds.getValue(Klausur.class));}
                FachName.setText(fName);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        SaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                final String pruefer = Pruefer.getText().toString();
                final String datum = Datum.getText().toString();
                final String beginnzeit = Beginnzeit.getText().toString();
                final String raum = Raum.getText().toString();
                final String Fächer = FachName.getText().toString();
                final DatabaseReference RootRef;
                RootRef = FirebaseDatabase.getInstance().getReference().child("Klausur");
                RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        list = new ArrayList<>();
                        for(DataSnapshot ds:dataSnapshot.getChildren()) {
                            Klausur klausur = ds.getValue(Klausur.class);
                            String fach1 = klausur.getFach();
                            if (fach1.equals(fName)  ) {//Toast.makeText(BearbeitenSeite.this,Fächer,Toast.LENGTH_SHORT).show();
                                final HashMap<String, Object> userdataMap = new HashMap<>();
                                if (TextUtils.isEmpty(pruefer)) {
                                    userdataMap.put("pruefer", klausur.getPruefer());
                                } else {
                                    userdataMap.put("pruefer", pruefer);
                                }
                                if (TextUtils.isEmpty(datum)) {
                                    userdataMap.put("datum", klausur.getDatum());
                                } else {
                                    userdataMap.put("datum", datum);
                                }
                                if (TextUtils.isEmpty(beginnzeit)) {
                                    userdataMap.put("startzeit", klausur.getStartzeit());
                                } else {
                                    userdataMap.put("startzeit", beginnzeit);
                                }
                                if (TextUtils.isEmpty(raum)) {
                                    userdataMap.put("raum", klausur.getRaum());
                                } else {
                                    userdataMap.put("raum", raum);
                                }

                                userdataMap.put("fach", fach1);
                                userdataMap.put("KlausurId", klausur.getKlausurId());
                                RootRef.child(klausur.getKlausurId()).setValue(userdataMap);
                                HashMap<String, String> data1 = new HashMap<>();
                                data1.put("key1", "data 1");
                                data1.put("key2", "data 2");
                                data1.put("key3", "data 3");
                                FCMSend.Builder build = new FCMSend.Builder(Fächer, true)
                                        .setTitle(Fächer+"-Klausurtermin")
                                        .setBody("Klausurtermin von "+Fächer+ " wurde aktualisiert.")
                                        .setClickAction("<Action>") // Optional
                                        .setData(data1); // Optional
                                build.send();
                                Intent intent = new Intent(v.getContext(), KlausurEinsichtActivity.class);
                                v.getContext().startActivity(intent);
                            }
                            Intent intent = new Intent(v.getContext(), KlausurtermineProf.class);
                            v.getContext().startActivity(intent);
                        }


                        //}
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

        });
        DeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                final DatabaseReference RootRef;
                RootRef = FirebaseDatabase.getInstance().getReference().child("Klausur");
                RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        list = new ArrayList<>();
                        for(DataSnapshot ds:dataSnapshot.getChildren()) {
                            Klausur klausur = ds.getValue(Klausur.class);
                            String fach1 = klausur.getFach();
                            if (fach1.equals(fName)  ) {//Toast.makeText(BearbeitenSeite.this,Fächer,Toast.LENGTH_SHORT).show();
                                final HashMap<String, Object> userdataMap = new HashMap<>();
                                    userdataMap.put("pruefer", " ");
                                    userdataMap.put("datum", " ");
                                    userdataMap.put("startzeit", " ");
                                    userdataMap.put("raum", " ");
                                userdataMap.put("fach", fach1);
                                userdataMap.put("KlausurId", klausur.getKlausurId());
                                RootRef.child(klausur.getKlausurId()).setValue(userdataMap);
                            }
                            Intent intent = new Intent(v.getContext(), KlausurtermineProf.class);
                            v.getContext().startActivity(intent);
                        }


                        //}
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

        });
    }



}
