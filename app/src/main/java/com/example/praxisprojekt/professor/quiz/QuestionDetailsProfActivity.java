package com.example.praxisprojekt.professor.quiz;

        import static com.example.praxisprojekt.professor.quiz.QuestionsProfActivity.quesList;
import static com.example.praxisprojekt.professor.quiz.QuizFachProfessorActivity.fachList;
import static com.example.praxisprojekt.professor.quiz.QuizFachProfessorActivity.selected_fach_index;

import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.util.ArrayMap;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.praxisprojekt.model.QuestionModelProf;
import com.example.praxisprojekt.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

public class QuestionDetailsProfActivity extends AppCompatActivity {

    private EditText ques, optionA, optionB, optionC, optionD, answer;
    private Button addQB;
    private String qStr, aStr, bStr, cStr, dStr, ansStr;
    private Dialog loadingDialog;
    private FirebaseFirestore firestore;
    private String action;
    private int qID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_detailsprof);

        Toolbar toolbar = findViewById(R.id.qdetails_toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ques = findViewById(R.id.question);
        optionA = findViewById(R.id.optionA);
        optionB = findViewById(R.id.optionB);
        optionC = findViewById(R.id.optionC);
        optionD = findViewById(R.id.optionD);
        answer = findViewById(R.id.answer);
        addQB = findViewById(R.id.addQB);

        loadingDialog = new Dialog(QuestionDetailsProfActivity.this);
        loadingDialog.setContentView(R.layout.loading_progressbar);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawableResource(R.drawable.progress_background);
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);

        firestore = FirebaseFirestore.getInstance();

        action = getIntent().getStringExtra("ACTION");

        if(action.compareTo("EDIT") == 0)
        {
            qID = getIntent().getIntExtra("Q_ID",0);
            loadData(qID);
            getSupportActionBar().setTitle("Frage " + String.valueOf(qID + 1));
            addQB.setText("Aktualisieren");
        }
        else
        {
            getSupportActionBar().setTitle("Frage " + String.valueOf(quesList.size() + 1));
            addQB.setText("Hinzufügen");
        }

        addQB.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {

                qStr = ques.getText().toString();
                aStr = optionA.getText().toString();
                bStr = optionB.getText().toString();
                cStr = optionC.getText().toString();
                dStr = optionD.getText().toString();
                ansStr = answer.getText().toString();

                if(qStr.isEmpty()) {
                    ques.setError("Geben Sie die Frage ab");
                    return;
                }

                if(aStr.isEmpty()) {
                    optionA.setError("Geben Sie die Option A ab");
                    return;
                }

                if(bStr.isEmpty()) {
                    optionB.setError("Geben Sie die Option B ab ");
                    return;
                }
                if(cStr.isEmpty()) {
                    optionC.setError("Geben Sie die Option C ab");
                    return;
                }
                if(dStr.isEmpty()) {
                    optionD.setError("Geben Sie die Option D ab");
                    return;
                }
                if(ansStr.isEmpty()) {
                    answer.setError("Geben Sie die Antwort ab");
                    return;
                }
                if(Integer.valueOf(ansStr)!=1){
                    if(Integer.valueOf(ansStr)!=2){
                        if(Integer.valueOf(ansStr)!=3){
                            if(Integer.valueOf(ansStr)!=4){
                                answer.setError("Geben Sie die Antwort zwichen 1 und 4");
                                return;
                            }
                        }
                    }
                }

                if(action.compareTo("EDIT") == 0)
                {
                    editQuestion();
                }
                else {
                    addNewQuestion();
                }

            }
        });
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void addNewQuestion()
    {
        loadingDialog.show();

        Map<String,Object> quesData = new ArrayMap<>();

        quesData.put("QUESTION",qStr);
        quesData.put("A",aStr);
        quesData.put("B",bStr);
        quesData.put("C",cStr);
        quesData.put("D",dStr);
        quesData.put("ANSWER",ansStr);


        final String doc_id = firestore.collection("QUIZ3").document(fachList.get(selected_fach_index).getId())
                .collection(fachList.get(selected_fach_index).getName()).document().getId();

        firestore.collection("QUIZ3").document(fachList.get(selected_fach_index).getId())
                .collection(fachList.get(selected_fach_index).getName()).document(doc_id)
                .set(quesData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        Map<String,Object> quesDoc = new ArrayMap<>();
                        quesDoc.put("Q" + String.valueOf(quesList.size() + 1) + "_ID", doc_id);
                        quesDoc.put("COUNT",String.valueOf(quesList.size() + 1));

                        firestore.collection("QUIZ3").document(fachList.get(selected_fach_index).getId())
                                .collection(fachList.get(selected_fach_index).getName()).document("QUESTIONS_LIST")
                                .update(quesDoc)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(QuestionDetailsProfActivity.this, "Fragen wurde hinzugefügt", Toast.LENGTH_SHORT).show();

                                        quesList.add(new QuestionModelProf(
                                                doc_id,
                                                qStr,aStr,bStr,cStr,dStr, Integer.valueOf(ansStr)
                                        ));

                                        loadingDialog.dismiss();
                                        QuestionDetailsProfActivity.this.finish();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(QuestionDetailsProfActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                                        loadingDialog.dismiss();
                                    }
                                });


                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(QuestionDetailsProfActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                        loadingDialog.dismiss();
                    }
                });


    }

    private void loadData(int id)
    {
        ques.setText(quesList.get(id).getQuestion());
        optionA.setText(quesList.get(id).getOptionA());
        optionB.setText(quesList.get(id).getOptionB());
        optionC.setText(quesList.get(id).getOptionC());
        optionD.setText(quesList.get(id).getOptionD());
        answer.setText(String.valueOf(quesList.get(id).getCorrectAns()));
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void editQuestion()
    {
        loadingDialog.show();

        Map<String,Object> quesData = new ArrayMap<>();
        quesData.put("QUESTION", qStr);
        quesData.put("A",aStr);
        quesData.put("B",bStr);
        quesData.put("C",cStr);
        quesData.put("D",dStr);
        quesData.put("ANSWER",ansStr);


        firestore.collection("QUIZ3").document(fachList.get(selected_fach_index).getId())
                .collection(fachList.get(selected_fach_index).getName()).document(quesList.get(qID).getQuesID())
                .set(quesData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        Toast.makeText(QuestionDetailsProfActivity.this,"Frage wurde Aktualisiert",Toast.LENGTH_SHORT).show();

                        quesList.get(qID).setQuestion(qStr);
                        quesList.get(qID).setOptionA(aStr);
                        quesList.get(qID).setOptionB(bStr);
                        quesList.get(qID).setOptionC(cStr);
                        quesList.get(qID).setOptionD(dStr);
                        quesList.get(qID).setCorrectAns(Integer.valueOf(ansStr));

                        loadingDialog.dismiss();
                        QuestionDetailsProfActivity.this.finish();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(QuestionDetailsProfActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                        loadingDialog.dismiss();
                    }
                });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == android.R.id.home)
        {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

}
