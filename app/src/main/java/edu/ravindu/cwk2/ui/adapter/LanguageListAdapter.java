package edu.ravindu.cwk2.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import edu.ravindu.cwk2.R;
import edu.ravindu.cwk2.model.Language;
import edu.ravindu.cwk2.ui.event_listener.ClickListener;

/**
 * Created by Ravindu Fernando on 2020-04-13 at 10:25 PM
 */
public class LanguageListAdapter extends ArrayAdapter {

    private Context context;
    private int layoutResource;
    private List<Language> listAllLanguages;
    private ClickListener clickListener;
    private int selectedPosition = -1;

    public LanguageListAdapter(@NonNull Context context, int resource, @NonNull List<Language> objects, ClickListener listener) {
        super(context, resource, objects);
        this.context = context;
        this.layoutResource = resource;
        this.listAllLanguages = objects;
        this.clickListener = listener;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder = new ViewHolder();

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(layoutResource, parent, false);
        }

        final Language lang = listAllLanguages.get(position);

        viewHolder.tvLang = convertView.findViewById(R.id.tvLang);
        viewHolder.cbLang = convertView.findViewById(R.id.cbLang);

        viewHolder.tvLang.setText(lang.getLanguageName());
        viewHolder.cbLang.setChecked(selectedPosition == position);
        viewHolder.cbLang.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                selectedPosition = position;
                clickListener.onListItemClickListener(position, lang.getLanguageName());
                notifyDataSetChanged();
            }
        });

        return convertView;
    }

    private class ViewHolder {
        TextView tvLang;
        CheckBox cbLang;
    }
}
