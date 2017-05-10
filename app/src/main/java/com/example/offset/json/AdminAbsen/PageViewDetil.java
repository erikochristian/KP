package com.example.offset.json.AdminAbsen;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.offset.json.Other.Login_Activity;
import com.example.offset.json.R;

public class PageViewDetil extends AppCompatActivity {

    String NIK_ABSEN = "-", SFT = "-", TMSK = "-", TKLR = "-", JMSK = "-", JKLR = "-", TR = "-", TR2 = "-";
    String MESIN = "-", UPDT = "-", GOLJAM = "-", SSL = "-", JKLR2 = "-",nik="",tgl_dari="",tgl_sampai="";
    TextView t_NIK, t_SFT, t_TMSK, t_TKLR, t_JMSK, t_JKLR, t_TR, t_TR2, t_MESIN, t_UPDT, t_GOLJAM, t_SSL, t_JKLR2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page_view_detil);

        nik = getIntent().getStringExtra("nik");
        tgl_dari = getIntent().getStringExtra("tgl_dari");
        tgl_sampai = getIntent().getStringExtra("tgl_sampai");

        NIK_ABSEN = getIntent().getStringExtra("NIK");
        SFT = getIntent().getStringExtra("SFT");
        TMSK = getIntent().getStringExtra("TMSK");
        TKLR = getIntent().getStringExtra("TKLR");
        JMSK = getIntent().getStringExtra("JMSK");
        JKLR = getIntent().getStringExtra("JKLR");
        TR = getIntent().getStringExtra("TR");
        TR2 = getIntent().getStringExtra("TR2");
        MESIN = getIntent().getStringExtra("MESIN");
        UPDT = getIntent().getStringExtra("UPDT");
        GOLJAM = getIntent().getStringExtra("GOLJAM");
        SSL = getIntent().getStringExtra("SSL");
        JKLR2 = getIntent().getStringExtra("JKLR2");

        t_NIK = (TextView) findViewById(R.id.text_nik_absen);
        t_SFT = (TextView) findViewById(R.id.text_sft_absen);
        t_TMSK = (TextView) findViewById(R.id.text_tglmsk_absen);
        t_TKLR = (TextView) findViewById(R.id.text_tglklr_absen);
        t_JMSK = (TextView) findViewById(R.id.text_jmsk_absen);
        t_JKLR = (TextView) findViewById(R.id.text_jklr_absen);
        t_TR = (TextView) findViewById(R.id.text_tr_absen);
        t_TR2 = (TextView) findViewById(R.id.text_tr2_absen);
        t_MESIN = (TextView) findViewById(R.id.text_mesin_absen);
        t_UPDT = (TextView) findViewById(R.id.text_updt_absen);
        t_GOLJAM = (TextView) findViewById(R.id.text_goljam_absen);
        t_SSL = (TextView) findViewById(R.id.text_ssl_absen);
        t_JKLR2 = (TextView) findViewById(R.id.text_jklr2_absen);


        t_NIK.setText("NIK : "+NIK_ABSEN);
        t_SFT.setText(SFT);
        t_TMSK.setText(TMSK);
        t_TKLR.setText(TKLR);
        t_JMSK.setText(JMSK);
        t_JKLR.setText(JKLR);
        t_TR.setText(TR);
        t_TR2.setText(TR2);
        t_MESIN.setText(MESIN);
        t_UPDT.setText(UPDT);
        t_GOLJAM.setText(GOLJAM);
        t_SSL.setText(SSL);
        t_JKLR2.setText(JKLR2);


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(PageViewDetil.this, PageViewAbsen.class);
        i.putExtra("nik",nik);
        i.putExtra("tgl_dari",tgl_dari);
        i.putExtra("tgl_sampai",tgl_sampai);
        startActivity(i);
        finish();
    }

    public void logout(View view){
        Intent i = new Intent(PageViewDetil.this, Login_Activity.class);
        startActivity(i);
        PageViewDetil.this.finish();
    }
}
