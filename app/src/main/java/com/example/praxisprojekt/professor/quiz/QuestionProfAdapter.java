package com.example.praxisprojekt.professor.quiz;


        import static com.example.praxisprojekt.professor.quiz.QuestionsProfActivity.quesList;
import static com.example.praxisprojekt.professor.quiz.QuizFachProfessorActivity.fachList;
import static com.example.praxisprojekt.professor.quiz.QuizFachProfessorActivity.selected_fach_index;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.util.ArrayMap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.praxisprojekt.R;
import com.example.praxisprojekt.model.QuestionModelProf;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;
import java.util.Map;

public class QuestionProfAdapter extends RecyclerView.Adapter<QuestionProfAdapter.ViewHolder> {

    private List<QuestionModelProf> ques_list;

    public QuestionProfAdapter(List<QuestionModelProf> ques_list) {
        this.ques_list = ques_list;
    }

    @NonNull
    @Override
    public QuestionProfAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cat_item_layoutprof, viewGroup,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionProfAdapter.ViewHolder viewHolder, int pos) {
        viewHolder.setData(pos, this);
    }

    @Override
    public int getItemCount() {
        return ques_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView title;
        private ImageView deleteB;
        private Dialog loadingDialog;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.catName);
            deleteB  = itemView.findViewById(R.id.catDelB);

            loadingDialog = new Dialog(itemView.getContext());
            loadingDialog.setContentView(R.layout.loading_progressbar);
            loadingDialog.setCancelable(false);
            loadingDialog.getWindow().setBackgroundDrawableResource(R.drawable.progress_background);
            loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        private void setData(final int pos, final QuestionProfAdapter adapter)
        {
            title.setText("Fragen " +  String.valueOf(pos+1));

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(itemView.getContext(), QuestionDetailsProfActivity.class);
                    intent.putExtra("ACTION","EDIT");
                    intent.putExtra("Q_ID", pos);
                    itemView.getContext().startActivity(intent);
                }
            });

            deleteB.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    AlertDialog dialog = new AlertDialog.Builder(itemView.getContext())
                            .setTitle("Fragen löschen")
                            .setMessage("Wollen Sie die Frage löschen ?")
                            .setPositiveButton("löschen", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    deleteQuestion(pos, itemView.getContext(), adapter);
                                }
                            })
                            .setNegativeButton("Abbrechen",null)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();

                    dialog.getButton(dialog.BUTTON_POSITIVE).setBackgroundColor(Color.RED);
                    dialog.getButton(dialog.BUTTON_NEGATIVE).setBackgroundColor(Color.RED);

                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams( LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    params.setMargins(0,0,50,0);
                    dialog.getButton(dialog.BUTTON_NEGATIVE).setLayoutParams(params);


                }
            });
        }

        private void deleteQuestion(final int pos, final Context context, final QuestionProfAdapter adapter)
        {
            loadingDialog.show();

            final FirebaseFirestore firestore = FirebaseFirestore.getInstance();

            firestore.collection("QUIZ3").document(fachList.get(selected_fach_index).getId())
                    .collection(fachList.get(selected_fach_index).getName()).document(quesList.get(pos).getQuesID())
                    .delete()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                        @Override
                        public void onSuccess(Void aVoid) {

                            Map<String,Object> quesDoc = new ArrayMap<>();
                            int index=1;
                            for(int i=0; i< quesList.size(); i++)
                            {
                                if(i != pos)
                                {
                                    quesDoc.put("Q" + String.valueOf(index) + "_ID", quesList.get(i).getQuesID());
                                    index++;
                                }
                            }

                            quesDoc.put("COUNT", String.valueOf(index - 1));

                            firestore.collection("QUIZ3").document(fachList.get(selected_fach_index).getId())
                                    .collection(fachList.get(selected_fach_index).getName()).document("QUESTIONS_LIST")
                                    .set(quesDoc)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(context,"Die Frage wurde gelöscht",Toast.LENGTH_SHORT).show();

                                            quesList.remove(pos);
                                            adapter.notifyDataSetChanged();

                                            loadingDialog.dismiss();

                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(context,e.getMessage(),Toast.LENGTH_SHORT).show();
                                            loadingDialog.dismiss();
                                        }
                                    });


                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(context,e.getMessage(),Toast.LENGTH_SHORT).show();
                            loadingDialog.dismiss();
                        }
                    });


        }



    }
}
