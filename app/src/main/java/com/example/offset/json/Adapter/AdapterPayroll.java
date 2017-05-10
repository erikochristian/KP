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
 * Created by OFFSET on 2/24/2017.
 */

public class AdapterPayroll extends ArrayAdapter<ReportLembur> {
    public AdapterPayroll(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public AdapterPayroll(Context context, int resource, List<ReportLembur> ReportLemburs) {
        super(context, resource, ReportLemburs);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.item_report_payroll, null);
        }

        ReportLembur p = getItem(position);

        if (p != null) {
            TextView tt1 = (TextView) v.findViewById(R.id.text_tanggal_payroll);
            TextView tt2 = (TextView) v.findViewById(R.id.text_upahlembur_payroll);
            TextView tt3 = (TextView) v.findViewById(R.id.text_upahharian_payroll);
            TextView tt4 = (TextView) v.findViewById(R.id.text_potongizin_payroll);
            TextView tt5 = (TextView) v.findViewById(R.id.text_potongterlambat_payroll);

            if (tt1 != null) {
                tt1.setText(p.getTanggal());
            }
            if (tt2 != null) {
                tt2.setText(p.getUpahLembur());
            }
            if (tt3 != null) {
                tt3.setText(p.getUpahHarian());
            }
            if (tt4 != null) {
                tt4.setText(p.getPotongIzin());
            }
            if (tt5 != null) {
                tt5.setText(p.getPotongTerlambat());
            }
        }
        return v;
    }
}
