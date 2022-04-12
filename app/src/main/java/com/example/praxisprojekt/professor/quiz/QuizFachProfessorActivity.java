package com.example.praxisprojekt.professor.quiz;

        import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.util.ArrayMap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

        import com.example.praxisprojekt.model.QuizFachModel;
        import com.example.praxisprojekt.R;
        import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class QuizFachProfessorActivity extends AppCompatActivity {
    private RecyclerView cat_recycler_view;
    private Button addCatB;
    public static List<QuizFachModel> fachList = new ArrayList<>();
    public static int selected_fach_index =0;
    public static String selected_fach_name = "";

    private FirebaseFirestore firestore;
    private Dialog loadingDialog, addCatDialog;
    private EditText dialogCatName;
    private Button dialogAddB;
    private QuizFachProfessorAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categoryprof);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Fächer");


        cat_recycler_view = findViewById(R.id.cat_recycler);
        addCatB = findViewById(R.id.addCatB);

        loadingDialog = new Dialog(QuizFachProfessorActivity.this);
        loadingDialog.setContentView(R.layout.loading_progressbar);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawableResource(R.drawable.progress_background);
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);

        addCatDialog = new Dialog(QuizFachProfessorActivity.this);
        addCatDialog.setContentView(R.layout.add_category_dialog);
        addCatDialog.setCancelable(true);
        addCatDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);

        dialogCatName = addCatDialog.findViewById(R.id.ac_cat_name);
        dialogAddB = addCatDialog.findViewById(R.id.ac_add_btn);

        firestore = FirebaseFirestore.getInstance();

        addCatB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogCatName.getText().clear();
                addCatDialog.show();
            }
        });

        dialogAddB.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                if(dialogCatName.getText().toString().isEmpty())
                {
                    dialogCatName.setError("Name des Fachs");
                    return;
                }

                addNewCategory(dialogCatName.getText().toString());
            }
        });


        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        cat_recycler_view.setLayoutManager(layoutManager);

        loadData();

    }


    private void loadData()
    {
        loadingDialog.show();

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

                            fachList.add(new QuizFachModel(FachId,FachName));
                            Toast.makeText(QuizFachProfessorActivity.this,String.valueOf(fachList.size()),Toast.LENGTH_SHORT).show();
                        }

                        adapter = new QuizFachProfessorAdapter(fachList);
                        cat_recycler_view.setAdapter(adapter);

                    }
                    else
                    {
                        Toast.makeText(QuizFachProfessorActivity.this,"No Category Document Exists!",Toast.LENGTH_SHORT).show();
                        finish();
                    }

                }
                else
                {

                    Toast.makeText(QuizFachProfessorActivity.this,task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                }

                loadingDialog.dismiss();
            }
        });

    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void addNewCategory(final String title)
    {
        addCatDialog.dismiss();
        loadingDialog.show();

        final Map<String,Object> fachData = new ArrayMap<>();
        fachData.put("NAME",title);

        final String doc_id = firestore.collection("QUIZ3").document().getId();

        firestore.collection("QUIZ3").document(doc_id)
                .set(fachData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        Map<String,Object> catDoc = new ArrayMap<>();
                        catDoc.put("FACH" + String.valueOf(fachList.size() + 1) + "_NAME",title);
                        catDoc.put("FACH" + String.valueOf(fachList.size() + 1) + "_ID",doc_id);
                        catDoc.put("count", fachList.size() + 1);

                        firestore.collection("QUIZ3").document("FACH")
                                .update(catDoc)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {

                                        Toast.makeText(QuizFachProfessorActivity.this,"Erfolgreich",Toast.LENGTH_SHORT).show();

                                        fachList.add(new QuizFachModel(doc_id,title));

                                        adapter.notifyItemInserted(fachList.size());
                                        addNewSet(doc_id,title);

                                        loadingDialog.dismiss();

                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(QuizFachProfessorActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                                        loadingDialog.dismiss();
                                    }
                                });


                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(QuizFachProfessorActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                        loadingDialog.dismiss();
                    }
                });


    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void addNewSet(String doc_id,String title)
    {//Toast.makeText(QuizFachProfessorActivity.this,"Should aaaadd",Toast.LENGTH_SHORT).show();
        loadingDialog.show();

        final String curr_fach_id = fachList.get(selected_fach_index).getId();
        final String curr_fach_name = fachList.get(selected_fach_index).getName();

        Map<String,Object> qData = new ArrayMap<>();
        qData.put("COUNT","0");

        firestore.collection("QUIZ3").document(doc_id)
                .collection(title).document("QUESTIONS_LIST")
                .set(qData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onSuccess(Void aVoid) {

                        Map<String,Object> fachDoc = new ArrayMap<>();
                        firestore.collection("QUIZ3").document(curr_fach_id)
                                .update(fachDoc)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {

                                        loadingDialog.dismiss();

                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        loadingDialog.dismiss();
                                    }
                                });

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        loadingDialog.dismiss();
                    }
                });

    }

}
