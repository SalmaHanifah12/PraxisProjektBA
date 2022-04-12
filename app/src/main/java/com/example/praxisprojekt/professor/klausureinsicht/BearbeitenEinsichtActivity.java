package com.example.praxisprojekt.professor.klausureinsicht;

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
import com.example.praxisprojekt.SendNotificationPack.APIService;
import com.example.praxisprojekt.model.Einsicht;
import com.example.praxisprojekt.model.Klausur;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class BearbeitenEinsichtActivity extends AppCompatActivity {
    private EditText fach, zeit, Startzeit, Raum;
    private Button SaveButton,DeleteButton;
    private ProgressDialog loadingBar;
    private Spinner spinnerTextSize;
    ArrayList<Einsicht> list;
    ArrayList<Klausur> list1;
    ArrayList<String> fNames;
    private APIService apiService;
    private String parentDbName = "Einsicht";
    private String fName;
    DatabaseReference RootRef1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bearbeiten_einsicht);
        fName = getIntent().getStringExtra("fName");

        SaveButton = (Button) findViewById(R.id.save_btn);
        DeleteButton = (Button) findViewById(R.id.delete_btn);
        fach = (EditText) findViewById(R.id.fach);
        zeit = (EditText) findViewById(R.id.Zeit);
        Startzeit = (EditText) findViewById(R.id.startzeit);
        Raum = (EditText) findViewById(R.id.raum);
        loadingBar = new ProgressDialog(this);
        RootRef1 = FirebaseDatabase.getInstance().getReference().child("Klausur");
        RootRef1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list1 = new ArrayList<>();

                for(DataSnapshot ds:dataSnapshot.getChildren()) {
                    Einsicht einsicht = ds.getValue(Einsicht.class);
                    list1.add(ds.getValue(Klausur.class));}
                fach.setText(fName);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        SaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                final String Zeit = zeit.getText().toString();
                final String startzeit = Startzeit.getText().toString();
                final String raum = Raum.getText().toString();
                final String Fächer = fach.getText().toString();
                final DatabaseReference RootRef;
                RootRef = FirebaseDatabase.getInstance().getReference().child("Einsicht");
                RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        list = new ArrayList<>();
                        for(DataSnapshot ds:dataSnapshot.getChildren()) {
                            Einsicht einsicht = ds.getValue(Einsicht.class);
                            String fach1 = einsicht.getFach();
                            if (fach1.equals(Fächer)  ) {

                                final HashMap<String, Object> userdataMap = new HashMap<>();

                                if (TextUtils.isEmpty(Zeit)) {
                                    userdataMap.put("Zeit", einsicht.getFach());
                                } else {
                                    userdataMap.put("Zeit", Zeit);
                                }
                                if (TextUtils.isEmpty(startzeit)) {
                                    userdataMap.put("startzeit", einsicht.getStartzeit());
                                } else {
                                    userdataMap.put("startzeit", startzeit);
                                }
                                if (TextUtils.isEmpty(raum)) {
                                    userdataMap.put("raum", einsicht.getRaum());
                                } else {
                                    userdataMap.put("raum", raum);
                                }

                                userdataMap.put("fach", fach1);
                                userdataMap.put("KlausurId", einsicht.getKlausurId());
                                RootRef.child(einsicht.getKlausurId()).setValue(userdataMap);
                            }else{
                            }
                        } HashMap<String, String> data1 = new HashMap<>();
                        data1.put("key1", "data 1");
                        data1.put("key2", "data 2");
                        data1.put("key3", "data 3");
                        FCMSend.Builder build = new FCMSend.Builder(Fächer, true)
                                .setTitle(Fächer+"-Klausureinsicht")
                                .setBody("Klausureinsichttermin von "+Fächer+ " wurde aktualisiert.")
                                .setClickAction("<Action>") // Optional
                                .setData(data1); // Optional
                        build.send();
                        Intent intent = new Intent(v.getContext(), KlausurEinsichtActivity.class);
                        v.getContext().startActivity(intent);
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
                final String Fächer = fach.getText().toString();
                final DatabaseReference RootRef;
                RootRef = FirebaseDatabase.getInstance().getReference().child("Einsicht");
                RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        list = new ArrayList<>();
                        for(DataSnapshot ds:dataSnapshot.getChildren()) {
                            Einsicht einsicht = ds.getValue(Einsicht.class);
                            String fach1 = einsicht.getFach();
                            if (fach1.equals(Fächer)  ) {

                                final HashMap<String, Object> userdataMap = new HashMap<>();
                                    userdataMap.put("Zeit", " ");
                                    userdataMap.put("startzeit", " ");
                                    userdataMap.put("raum", " ");
                                userdataMap.put("fach", fach1);
                                userdataMap.put("KlausurId", einsicht.getKlausurId());
                                RootRef.child(einsicht.getKlausurId()).setValue(userdataMap);
                            }else{
                            }
                        }
                        Intent intent = new Intent(v.getContext(), KlausurEinsichtActivity.class);
                        v.getContext().startActivity(intent);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

        });
    }


}
