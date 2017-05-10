package com.example.offset.json.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.offset.json.R;
import com.example.offset.json.model.Absen;
import com.example.offset.json.model.Post;

import java.util.List;

/**
 * Created by OFFSET on 3/7/2017.
 */

public class AdapterKaryawan extends ArrayAdapter<Post> {
    public AdapterKaryawan(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public AdapterKaryawan(Context context, int resource, List<Post> Absens) {
        super(context, resource, Absens);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.item_pegawai, null);
        }

        Post p = getItem(position);

        if (p != null) {
            TextView t_nik = (TextView) v.findViewById(R.id.text_nik_list);
            TextView t_tanggal_dari = (TextView) v.findViewById(R.id.text_nama_list);
            TextView t_tanggal_sampai = (TextView) v.findViewById(R.id.text_bagian_list);

            if (t_nik != null) {
                t_nik.setText(p.getNIK());
            }

            if (t_tanggal_dari != null) {
                t_tanggal_dari.setText(p.getNAMA());
            }

            if (t_tanggal_sampai != null) {
                t_tanggal_sampai.setText(p.getBAGIAN());
            }


        }
        return v;
    }
}
