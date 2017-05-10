package com.example.offset.json.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.offset.json.R;
import com.example.offset.json.model.LiburNasional;
import com.example.offset.json.model.ReportAbsen;
import com.example.offset.json.model.ReportLembur;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by OFFSET on 2/14/2017.
 */

public class AdapterReportAbsen extends ArrayAdapter<ReportLembur> {
    public AdapterReportAbsen(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public AdapterReportAbsen(Context context, int resource, List<ReportLembur> ReportAbsens) {
        super(context, resource, ReportAbsens);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.item_report_absen, null);
        }

        ReportLembur p = getItem(position);

        if (p != null) {
            TextView tt1 = (TextView) v.findViewById(R.id.text_tanggal_report);
            TextView tt2 = (TextView) v.findViewById(R.id.text_jmsk_report);
            TextView tt3 = (TextView) v.findViewById(R.id.text_jklr_report);
            TextView tt4 = (TextView) v.findViewById(R.id.text_keterangan_report);
            TextView tt5 = (TextView) v.findViewById(R.id.text_lambat_report);
            TextView tt6 = (TextView) v.findViewById(R.id.text_izin_report);

            if (tt1 != null) {
                tt1.setText(p.getTanggal());
            }
            if (tt2 != null) {
                tt2.setText(p.getJMSK());
            }
            if (tt3 != null) {
                tt3.setText(p.getJKLR());
            }
            if (tt5 != null) {
                tt5.setText(p.getPotongTerlambat());
            }

            if (tt6 != null) {
                tt6.setText(p.getPotongIzin());
            }
            if (tt4 != null) {
                tt4.setText(p.getKeterangan());
            }
        }
        return v;
    }

}
