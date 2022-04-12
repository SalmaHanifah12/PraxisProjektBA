package com.example.praxisprojekt.student.Quiz;

        import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.praxisprojekt.R;
        import com.example.praxisprojekt.model.QuizFachStudent;

        import java.util.List;
import java.util.Random;

public class QuizFachAdapter extends BaseAdapter{

    private List<QuizFachStudent> fachList;

    public QuizFachAdapter(List<QuizFachStudent> fachList) {
        this.fachList = fachList;
    }

    @Override
    public int getCount() {
        return fachList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {


        View view;

        if(convertView == null)
        {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cat_item_layout,parent,false);
        }
        else
        {
            view = convertView;
        }


        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                QuizFachActivity.selected_fach_index = position;
                Intent intent = new Intent(parent.getContext(), QuizActivity.class);
                parent.getContext().startActivity(intent);
            }
        });


        ((TextView) view.findViewById(R.id.catName)).setText(fachList.get(position).getName());

       Random rnd = new Random();
        int color = Color.argb(255, rnd.nextInt(50),rnd.nextInt(255),rnd.nextInt(255));
        view.setBackgroundColor(color);


        return view;
    }
}
