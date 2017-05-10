package com.example.offset.json.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.offset.json.R;
import com.example.offset.json.model.Izin;

import java.util.List;

public class AdapterIjin extends ArrayAdapter<Izin> {

    public AdapterIjin(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public AdapterIjin(Context context, int resource, List<Izin> Izins) {
        super(context, resource, Izins);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.item_ijin, null);
        }

        Izin p = getItem(position);

        if (p != null) {
            TextView tt1 = (TextView) v.findViewById(R.id.text_tanggal);
            TextView tt2 = (TextView) v.findViewById(R.id.text_jenis);
            TextView tt3 = (TextView) v.findViewById(R.id.text_keterangan);

            if (tt1 != null) {
                tt1.setText(p.getTGL());
            }

            if (tt2 != null) {
                String jenis = p.getJENIS();
                switch (jenis){
                    case "1" : tt2.setText("Dinas Luar"); break;
                    case "2" : tt2.setText("Cuti Dokter");break;
                    case "3" : tt2.setText("Ijin KKB");break;
                    case "4" : tt2.setText("Cuti");break;
                    case "5" : tt2.setText("Cuti Hamil");break;
                    case "6" : tt2.setText("Diliburkan");break;
                    case "7" : tt2.setText("Sakit");break;
                    case "8" : tt2.setText("Pamit");break;
                    case "9" : tt2.setText("Mangkir");break;
                }
            }

            if (tt3 != null) {
                tt3.setText(p.getKET());
            }
            else{
                tt3.setText("Tidak Ada Keterangan");
            }
        }
        return v;
    }

}

