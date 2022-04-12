package com.example.praxisprojekt.student;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.praxisprojekt.Prevalent;
import com.example.praxisprojekt.R;
import com.example.praxisprojekt.model.Student;
import com.example.praxisprojekt.model.Ergebnis;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class KlausurErgebnis extends AppCompatActivity {

    ListView myPDfListView;
    DatabaseReference databaseReference;
    List<Ergebnis> uploadPDFS;
    private Button allFile;
    private Button meineFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_klausur_ergebnis);

        myPDfListView = (ListView)findViewById(R.id.myListView);
        allFile = (Button)findViewById(R.id.allButton);
        meineFile=(Button)findViewById(R.id.meineButton);
        uploadPDFS = new ArrayList<>();
        allFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewAllFiles();
            }
        });
        meineFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewMeineFiles();
            }
        });

        myPDfListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Ergebnis Ergebnis = uploadPDFS.get(position);
                Intent intent = new Intent();
                intent.setData(Uri.parse(Ergebnis.getUrl()));
                startActivity(intent);

            }
        });
    }


    private void viewAllFiles() {
        databaseReference = FirebaseDatabase.getInstance().getReference("Ergebnis");
        databaseReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {    uploadPDFS.clear();
                for(DataSnapshot postSnapshot: dataSnapshot.getChildren()){
                    Ergebnis ergebnis = postSnapshot.getValue(com.example.praxisprojekt.model.Ergebnis.class);
                    uploadPDFS.add(ergebnis);
                }

                String[] uploads = new String[uploadPDFS.size()];

                for(int i = 0;i<uploads.length;i++){
                    uploads[i] = uploadPDFS.get(i).getName();
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,uploads){
                    @NonNull
                    @Override
                    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                        View view = super.getView(position, convertView,parent);

                        TextView myText = (TextView) view.findViewById(android.R.id.text1);
                        myText.setTextColor(Color.BLACK);
                        return view;
                    }
                };
                myPDfListView.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void viewMeineFiles() {
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                uploadPDFS.clear();
                Student Data = dataSnapshot.child("Student").child(Prevalent.StudentMatrikelnummer).getValue(Student.class);
                String mKlausuren = Data.getMeineKlausuren();
                List<String> Mkl = Arrays.asList((mKlausuren.split(",")));
                for (DataSnapshot ds : dataSnapshot.child("Ergebnis").getChildren()) {
                    Ergebnis data = ds.getValue(Ergebnis.class);
                    if(Mkl.contains(data.getKid())) {
                        Ergebnis ergebnis = ds.getValue(com.example.praxisprojekt.model.Ergebnis.class);
                        uploadPDFS.add(ergebnis);
                    }
                }

                String[] uploads = new String[uploadPDFS.size()];

                for(int i = 0;i<uploads.length;i++){
                    uploads[i] = uploadPDFS.get(i).getName();
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,uploads){
                    @NonNull
                    @Override
                    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                        View view = super.getView(position, convertView,parent);

                        TextView myText = (TextView) view.findViewById(android.R.id.text1);
                        myText.setTextColor(Color.BLACK);
                        return view;
                    }
                };

                myPDfListView.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
