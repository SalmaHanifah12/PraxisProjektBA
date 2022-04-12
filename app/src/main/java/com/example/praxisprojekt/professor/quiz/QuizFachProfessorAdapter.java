package com.example.praxisprojekt.professor.quiz;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

        import com.example.praxisprojekt.model.QuizFachModel;
        import com.example.praxisprojekt.R;
        import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;
import java.util.Map;

public class QuizFachProfessorAdapter extends RecyclerView.Adapter<QuizFachProfessorAdapter.ViewHolder> {
    private List<QuizFachModel> fach_list;

    public QuizFachProfessorAdapter(List<QuizFachModel> fach_list) {
        this.fach_list = fach_list;
    }

    @NonNull
    @Override
    public QuizFachProfessorAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cat_item_layoutprof,viewGroup,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuizFachProfessorAdapter.ViewHolder viewHolder, int pos) {

        String title = fach_list.get(pos).getName();

        viewHolder.setData(title, pos, this);

    }

    @Override
    public int getItemCount() {
        return fach_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView FachName;
        private ImageView deleteB;
        private Dialog loadingDialog;
        private Dialog editDialog;
        private EditText tv_editCatName;
        private Button updateCatB;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            FachName = itemView.findViewById(R.id.catName);
            deleteB = itemView.findViewById(R.id.catDelB);

            loadingDialog = new Dialog(itemView.getContext());
            loadingDialog.setContentView(R.layout.loading_progressbar);
            loadingDialog.setCancelable(false);
            loadingDialog.getWindow().setBackgroundDrawableResource(R.drawable.progress_background);
            loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);

            editDialog = new Dialog(itemView.getContext());
            editDialog.setContentView(R.layout.edit_category_dialog);
            editDialog.setCancelable(true);
            editDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);

            tv_editCatName = editDialog.findViewById(R.id.ec_cat_name);
            updateCatB = editDialog.findViewById(R.id.ec_add_btn);
        }

        private void setData(String title, final int pos, final QuizFachProfessorAdapter adapter)
        {
            FachName.setText(title);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    QuizFachProfessorActivity.selected_fach_index = pos;
                    QuizFachProfessorActivity.selected_fach_name = fach_list.get(pos).getName();
                    Intent intent = new Intent(itemView.getContext(), QuestionsProfActivity.class);
                    itemView.getContext().startActivity(intent);
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    tv_editCatName.setText(fach_list.get(pos).getName());
                    editDialog.show();

                    return false;
                }
            });

            updateCatB.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public void onClick(View v) {

                    if(tv_editCatName.getText().toString().isEmpty())
                    {
                        tv_editCatName.setError("Name des Fachs");
                        return;
                    }

                    updateCategory(tv_editCatName.getText().toString(), pos, itemView.getContext(), adapter);

                }
            });

            deleteB.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    AlertDialog dialog = new AlertDialog.Builder(itemView.getContext())
                            .setTitle("Fach löschen")
                            .setMessage("das Fach löschen ?")
                            .setPositiveButton("löschen", new DialogInterface.OnClickListener() {
                                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    deleteCategory(pos, itemView.getContext(), adapter);
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

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        private void deleteCategory(final int id, final Context context, final QuizFachProfessorAdapter adapter)
        {
            loadingDialog.show();

            FirebaseFirestore firestore = FirebaseFirestore.getInstance();

            firestore.collection("QUIZ3").document(fach_list.get(id).getId()).delete();

            Map<String,Object> fachDoc = new ArrayMap<>();
            int index=1;
            for(int i = 0; i < fach_list.size(); i++)
            {
                if( i != id)
                {
                    fachDoc.put("FACH" + String.valueOf(index) + "_ID", fach_list.get(i).getId());
                    fachDoc.put("FACH" + String.valueOf(index) + "_NAME", fach_list.get(i).getName());
                    index++;
                }

            }

            fachDoc.put("count", index - 1);

            firestore.collection("QUIZ3").document("FACH")
                    .set(fachDoc)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                            Toast.makeText(context,"Fach wurde gelöcht",Toast.LENGTH_SHORT).show();

                            QuizFachProfessorActivity.fachList.remove(id);

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

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        private void updateCategory(final String fachNewName, final int pos, final Context context, final QuizFachProfessorAdapter adapter)
        {
            editDialog.dismiss();

            loadingDialog.show();

            Map<String,Object> fachData = new ArrayMap<>();
            fachData.put("NAME",fachNewName);

            final FirebaseFirestore firestore = FirebaseFirestore.getInstance();

            firestore.collection("QUIZ3").document(fach_list.get(pos).getId())
                    .update(fachData)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                            Map<String,Object> catDoc = new ArrayMap<>();
                            catDoc.put("FACH" + String.valueOf(pos + 1) + "_NAME",fachNewName);

                            firestore.collection("QUIZ3").document("FACH")
                                    .update(catDoc)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {

                                            Toast.makeText(context,"Name des Fachs wurde geändert",Toast.LENGTH_SHORT).show();
                                            QuizFachProfessorActivity.fachList.get(pos).setName(fachNewName);
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
