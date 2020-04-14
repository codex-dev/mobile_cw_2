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
import edu.ravindu.cwk2.ui.event_listener.ClickListener;

/**
 * Created by Ravindu Fernando on 2020-04-13 at 12:17 AM
 */
public class DisplayListAdapter extends ArrayAdapter {

    private Context context;
    private int layoutResource;
    private List<Phrase> listAllPhrases;
    private ClickListener clickListener;

    public DisplayListAdapter(@NonNull Context context, int resource, @NonNull List<Phrase> objects, ClickListener listener) {
        super(context, resource, objects);
        this.context = context;
        this.layoutResource = resource;
        this.listAllPhrases = objects;
        this.clickListener = listener;
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
        viewHolder.tvPhrase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickListener.onListItemClickListener(position, p.getPhrase());
            }
        });

        return convertView;
    }

    private class ViewHolder {
        TextView tvPhrase;
    }
}
