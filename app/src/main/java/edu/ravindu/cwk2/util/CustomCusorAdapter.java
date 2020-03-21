package edu.ravindu.cwk2.util;


import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.cursoradapter.widget.SimpleCursorAdapter;

import edu.ravindu.cwk2.R;

/**
 * Created by Ravindu Fernando on 2020-03-20 at 01:11 AM
 */
public class CustomCusorAdapter extends SimpleCursorAdapter {

    private int selectedPosition = -1; // to avoid selecting item by default
    private String selectedPhrase;
    private Context context;
    private int layout;

    public CustomCusorAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
        super(context, layout, c, from, to, flags);
        this.context = context;
        this.layout = layout;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        int colName = cursor.getColumnIndex("phrase");
        final String phrase = cursor.getString(colName);
        int position = cursor.getPosition();

        TextView tvPhrase = view.findViewById(R.id.tvPhrase);
        RadioButton rbPhrase = view.findViewById(R.id.rbPhrase);
        if (tvPhrase != null) {
            tvPhrase.setText(phrase);
        }

        rbPhrase.setChecked(position == selectedPosition);
        view.setTag(position);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedPosition = (Integer) view.getTag();
                selectedPhrase = phrase;
                notifyDataSetChanged();
            }
        });
    }

    public int getSelectedPosition() {
        return selectedPosition;
    }

    public String getSelectedPhrase() {
        return selectedPhrase;
    }
}

/*
 * References -
 * https://stackoverflow.com/a/13028347 - Listview and CustomAdapter extending SimpleCursorAdapter
 * https://stackoverflow.com/a/21348318 - Android: Radio button in custom list view
 */
