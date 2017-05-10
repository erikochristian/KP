package com.example.offset.json.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.offset.json.R;
import com.example.offset.json.model.Absen;
import com.example.offset.json.model.User;

import java.util.List;

/**
 * Created by ERIKO on 26/04/2017.
 */

public class AdapterAkun extends ArrayAdapter<User> {
    public AdapterAkun(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public AdapterAkun(Context context, int resource, List<User> Users) {
        super(context, resource, Users);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.item_akun, null);
        }

        User p = getItem(position);

        if (p != null) {
            TextView t_nik = (TextView) v.findViewById(R.id.text_akun_nik);
            TextView t_nama = (TextView) v.findViewById(R.id.text_akun_nama);
            TextView t_username = (TextView) v.findViewById(R.id.text_akun_username);
            TextView t_status = (TextView) v.findViewById(R.id.text_akun_status);



            if (t_username != null) {
                t_username.setText(p.getId());
            }

            if (t_nik != null) {
                t_nik.setText(p.getNik());
            }

            if (t_nama != null) {
                t_nama.setText(p.getNama());
            }

            if (t_status != null) {
                if (p.getStatus().equals("n")){
                    t_status.setText("Terkunci");

                }
                else{
                    t_status.setText("Aktif");
                }
            }

        }
        return v;
    }
}
