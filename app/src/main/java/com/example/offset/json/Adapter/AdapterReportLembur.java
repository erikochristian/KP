package com.example.offset.json.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.offset.json.R;
import com.example.offset.json.model.ReportAbsen;
import com.example.offset.json.model.ReportLembur;

import java.util.List;

/**
 * Created by OFFSET on 2/21/2017.
 */

public class AdapterReportLembur  extends ArrayAdapter<ReportLembur> {
    public AdapterReportLembur(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public AdapterReportLembur(Context context, int resource, List<ReportLembur> ReportLemburs) {
        super(context, resource, ReportLemburs);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.item_report_lembur, null);
        }

        ReportLembur p = getItem(position);

        if (p != null) {
            TextView tt1 = (TextView) v.findViewById(R.id.text_tanggal_report);
            TextView tt2 = (TextView) v.findViewById(R.id.text_jmsk_report);
            TextView tt3 = (TextView) v.findViewById(R.id.text_jklr_report);
            TextView tt4 = (TextView) v.findViewById(R.id.text_lembur_report);
            TextView tt5 = (TextView) v.findViewById(R.id.text_upahlembur_report);

            if (tt1 != null) {
                tt1.setText(p.getTanggal());
            }
            if (tt2 != null) {
                tt2.setText(p.getJMSK());
            }
            if (tt3 != null) {
                tt3.setText(p.getJKLR());
            }
            if (tt4 != null) {
                tt4.setText(p.getLembur()+" jam");
            }
            if (tt5 != null) {
                tt5.setText(p.getUpahLembur());
            }
        }
        return v;
    }

}
