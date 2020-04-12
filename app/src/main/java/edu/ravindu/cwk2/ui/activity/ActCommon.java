package edu.ravindu.cwk2.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

import edu.ravindu.cwk2.R;

public class ActCommon extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_common);
        setupActionbar("", false);
    }

    protected void setupActionbar(String title, boolean showBackIcon) {
        Objects.requireNonNull(getSupportActionBar()).setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.custom_action_bar);
        View actionbarView = getSupportActionBar().getCustomView();

        ImageView ivBack = actionbarView.findViewById(R.id.ivBack);
        TextView tvTitle = actionbarView.findViewById(R.id.tvTitle);

        if (showBackIcon) {
            ivBack.setVisibility(View.VISIBLE);
            tvTitle.setGravity(Gravity.CENTER);
            ivBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onBackPressed();
                }
            });
        } else { // home ui
            ivBack.setVisibility(View.GONE);
            tvTitle.setGravity(Gravity.CENTER);
        }
        tvTitle.setText(title);
    }

    // hide soft input keyboard when touched outside of a EditText
    public void hideKeyboard(View view, final Activity activity) {
        // Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {
            if (view == null) return;
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    hideSoftKeyboard(activity);
                    return false;
                }
            });
        }

        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                hideKeyboard(innerView, activity);
            }
        }
    }

    private static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(
                Activity.INPUT_METHOD_SERVICE);
        if (activity.getCurrentFocus() != null) {
            Objects.requireNonNull(inputMethodManager).hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        }
    }

    protected void showActivity(Class<? extends ActCommon> nextActivity, Bundle param) {
        Intent intent = new Intent(this, nextActivity);
        if (param != null) {
            intent.putExtras(param);
        }
        startActivity(intent);
    }

    protected boolean isEmptyText(EditText editText) {
        return editText.getText() == null || editText.getText().toString().trim().isEmpty();
    }

    protected String getTrimmedText(EditText editText) {
        return editText.getText().toString().trim();
    }

//    protected ArrayList<Phrase> getDataFromCursor(Cursor cursor) {
//        ArrayList<Phrase> list = new ArrayList<>();
//        if (cursor != null && cursor.getCount() > 0) {
//            if (cursor.moveToFirst()) {
//                do {
//                    String data = cursor.getString(cursor.getColumnIndex("data"));
//                    // do what ever you want here
//                } while (cursor.moveToNext());
//            }
//        }
//        cursor.close();
//    }

}


/*
 * References -
 * https://stackoverflow.com/a/45632962 - Why should one use Objects.requireNonNull()?
 * https://stackoverflow.com/a/11656129 - How to hide soft keyboard on android after clicking outside EditText?
 *  */
