package edu.ravindu.cwk2.util;


import android.content.Context;
import android.database.Cursor;

import androidx.cursoradapter.widget.SimpleCursorAdapter;

/**
 * Created by Ravindu Fernando on 2020-03-20 at 01:11 AM
 */
public class CustomCusorAdapter extends SimpleCursorAdapter {

    private int mSelectedPosition;
    Cursor items;
    private Context context;
    private int layout;

    public CustomCusorAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
        super(context, layout, c, from, to, flags);
        this.context = context;
        this.layout = layout;
    }


}

/*
 * References -
 * https://stackoverflow.com/a/13028347 - Listview and CustomAdapter extending SimpleCursorAdapter
 * https://stackoverflow.com/a/21348318 - Android: Radio button in custom list view
 */
