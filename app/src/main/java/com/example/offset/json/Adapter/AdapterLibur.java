package com.example.offset.json.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.offset.json.R;
import com.example.offset.json.model.LiburNasional;

import java.util.List;

/**
 * Created by OFFSET on 2/6/2017.
 */

public class AdapterLibur extends ArrayAdapter<LiburNasional> {

    public AdapterLibur(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public AdapterLibur(Context context, int resource, List<LiburNasional> LiburNasionals) {
        super(context, resource, LiburNasionals);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.item_libur, null);
        }

        LiburNasional p = getItem(position);

        if (p != null) {
            TextView tt1 = (TextView) v.findViewById(R.id.text_tanggal);
            TextView tt3 = (TextView) v.findViewById(R.id.text_keterangan);

            if (tt1 != null) {
                tt1.setText(p.getTanggal());
            }

            if (tt3 != null) {
                tt3.setText(p.getKeterangan());
            }
            else{
                tt3.setText("Tidak Ada Keterangan");
            }
        }
        return v;
    }

}
