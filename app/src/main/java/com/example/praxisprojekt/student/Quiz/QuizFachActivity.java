package com.example.praxisprojekt.student.Quiz;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.GridView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.praxisprojekt.R;
import com.example.praxisprojekt.model.QuizFachStudent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class QuizFachActivity extends AppCompatActivity {
    public static List<QuizFachStudent> fachList = new ArrayList<>();
    public static int selected_fach_index = 0;
    private FirebaseFirestore firestore;
    private GridView catGrid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Fächer");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        catGrid = findViewById(R.id.catGridview);
        firestore = FirebaseFirestore.getInstance();
        fachList.clear();

        firestore.collection("QUIZ3").document("FACH")
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if(task.isSuccessful())
                {
                    DocumentSnapshot doc = task.getResult();

                    if(doc.exists())
                    {
                        long counts = (long)doc.get("count");

                        for(int i=1; i <= counts; i++)
                        {
                            String FachName = doc.getString("FACH" + String.valueOf(i) + "_NAME");
                            String FachId = doc.getString("FACH" + String.valueOf(i) + "_ID");

                            fachList.add(new QuizFachStudent(FachId,FachName));
                        }
                        QuizFachAdapter adapter = new QuizFachAdapter(fachList);
                        catGrid.setAdapter(adapter);


                    }
                    else
                    {
                        Toast.makeText(QuizFachActivity.this,"No Category Document Exists!",Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
                else
                {

                    Toast.makeText(QuizFachActivity.this,task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });





    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == android.R.id.home)
        {
            QuizFachActivity.this.finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
