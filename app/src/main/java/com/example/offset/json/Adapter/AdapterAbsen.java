package com.example.offset.json.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.offset.json.R;
import com.example.offset.json.model.Absen;
import com.example.offset.json.model.Izin;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by OFFSET on 2/10/2017.
 */

public class AdapterAbsen extends ArrayAdapter<Absen> {
    public AdapterAbsen(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public AdapterAbsen(Context context, int resource, List<Absen> Absens) {
        super(context, resource, Absens);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.item_absen, null);
        }

        Absen p = getItem(position);

        if (p != null) {
            TextView t_nik = (TextView) v.findViewById(R.id.text_nik_absen);
            TextView t_tanggal_dari = (TextView) v.findViewById(R.id.text_tglmsk_absen);
            TextView t_tanggal_sampai = (TextView) v.findViewById(R.id.text_tglklr_absen);
            TextView t_goljam = (TextView) v.findViewById(R.id.text_goljam_absen);

            if (t_nik != null) {
                t_nik.setText(p.getNIK());
            }

            if (t_tanggal_dari != null) {
                t_tanggal_dari.setText(p.getTMSK());
            }

            if (t_tanggal_sampai != null) {
                t_tanggal_sampai.setText(p.getTKLR());
            }

            if (t_goljam != null) {
                t_goljam.setText(p.getGOLJAM());
            }

        }
        return v;
    }
}
