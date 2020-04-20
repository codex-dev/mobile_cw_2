package edu.ravindu.cwk2.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import edu.ravindu.cwk2.R;
import edu.ravindu.cwk2.model.Phrase;
import edu.ravindu.cwk2.ui.event_listener.PhraseListListener;

/**
 * Created by Ravindu Fernando on 2020-04-13 at 08:37 AM
 */
public class EditListAdapter extends ArrayAdapter {

    private Context context;
    private int layoutResource;
    private List<Phrase> listAllPhrases;
    private PhraseListListener phraseListListener;
    private int selectedPosition = -1;

    public EditListAdapter(@NonNull Context context, int resource, @NonNull List<Phrase> objects, PhraseListListener listener) {
        super(context, resource, objects);
        this.context = context;
        this.layoutResource = resource;
        this.listAllPhrases = objects;
        this.phraseListListener = listener;

        Toast.makeText(context, "List loaded", Toast.LENGTH_SHORT).show();
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder = new ViewHolder();

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(layoutResource, parent, false);
        }

        final Phrase p = listAllPhrases.get(position);

        viewHolder.tvPhrase = convertView.findViewById(R.id.tvPhrase);
        viewHolder.rbPhrase = convertView.findViewById(R.id.rbPhrase);

        viewHolder.tvPhrase.setText(p.getPhrase());
        viewHolder.rbPhrase.setChecked(selectedPosition == position);
        viewHolder.rbPhrase.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                selectedPosition = position;
                phraseListListener.onPhraseItemClick(position, p.getPhrase());
                notifyDataSetChanged();
            }
        });

        return convertView;
    }

    private class ViewHolder {
        TextView tvPhrase;
        RadioButton rbPhrase;
    }
}
