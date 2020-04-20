package edu.ravindu.cwk2.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import edu.ravindu.cwk2.R;
import edu.ravindu.cwk2.model.Phrase;
import edu.ravindu.cwk2.ui.event_listener.PhraseListListener;

/**
 * Created by Ravindu Fernando on 2020-04-20 at 07:46 AM
 */
public class TranslateListAdapter extends ArrayAdapter {
    private Context context;
    private int layoutResource;
    private List<Phrase> listAllPhrases;
    private PhraseListListener listener;
    private int selectedPosition = -1;

    public TranslateListAdapter(@NonNull Context context, int resource, @NonNull List<Phrase> objects, PhraseListListener listener) {
        super(context, resource, objects);
        this.context = context;
        this.layoutResource = resource;
        this.listAllPhrases = objects;
        this.listener = listener;
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
        viewHolder.tvPhrase.setText(p.getPhrase());
        if(selectedPosition == position){
           viewHolder.tvPhrase.setBackground(context.getDrawable(R.drawable.bg_list_item_selected));
        } else {
            viewHolder.tvPhrase.setBackground(context.getDrawable(R.drawable.bg_list_item_normal));
        }

        viewHolder.tvPhrase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedPosition = position;
                listener.onPhraseItemClick(position, p.getPhrase());
                notifyDataSetChanged();
            }
        });

        return convertView;
    }

    private class ViewHolder {
        TextView tvPhrase;
    }
}
