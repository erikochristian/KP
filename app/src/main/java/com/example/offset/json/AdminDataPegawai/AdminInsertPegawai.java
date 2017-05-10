package com.example.offset.json.AdminDataPegawai;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.offset.json.AdminAbsen.InsertAbsen;
import com.example.offset.json.Other.Login_Activity;
import com.example.offset.json.R;
import com.example.offset.json.model.Bagian;
import com.example.offset.json.model.KodeGoljam;
import com.example.offset.json.model.Post;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdminInsertPegawai extends AppCompatActivity {
    private static String ENDPOINT ;
    private static String ENDPOINT_bagian ;
    private static String ENDPOINT_bagian_add ;
    private static String ENDPOINT_karyawan ;
    private static String ENDPOINT_goljam ;

    private RequestQueue requestQueue;
    private Gson gson;
    EditText t_nik, t_nama, t_tgl_lhr,t_tanggal, t_bagian, t_goljamker, t_umr, t_spsi, t_jamsos, t_dsu, t_bpjs, t_beras,t_domisili;
    String nik, nama, tgl_lhr, agama, bagian, goljamker, umr, spsi, jamsos, dsu, bpjs, beras, domisili,bagian_baru;
    String kelamin="L";
    RadioButton r_perempuan,r_laki;
    boolean check_error = true;
    private Calendar calendar;
    private int year, month, day;
    private String array_spinner_agama[];
    List<String> list_spin_goljam;
    List<String> list_nik;
    boolean check_nik = true;
    Spinner spin_agama, spin_bagian, spin_goljam;
    List<String> list_spin_bagian;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_insert_pegawai);

        //Link to web service
        ENDPOINT = "http://"+getResources().getString(R.string.ip)+"/KP/insertKaryawan";
        ENDPOINT_karyawan = "http://"+getResources().getString(R.string.ip)+"/KP/cekAllKaryawan";
        ENDPOINT_bagian = "http://"+getResources().getString(R.string.ip)+"/KP/cekBagian";
        ENDPOINT_bagian_add = "http://"+getResources().getString(R.string.ip)+"/KP/insertBagian";
        ENDPOINT_goljam = "http://"+getResources().getString(R.string.ip)+"/KP/cekGoljam";

        //requirement volley, gson
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        GsonBuilder gsonBuilder = new GsonBuilder();
        gson = gsonBuilder.create();

        //initialize other component
        t_nik = (EditText) findViewById(R.id.txt_nik);
        t_nama = (EditText) findViewById(R.id.txt_nama);
        t_tgl_lhr = (EditText) findViewById(R.id.txt_tgl_lhr);
        t_umr = (EditText) findViewById(R.id.txt_umr);
        t_spsi = (EditText) findViewById(R.id.txt_spsi);
        t_jamsos = (EditText) findViewById(R.id.txt_jamsos);
        t_dsu = (EditText) findViewById(R.id.txt_dsu);
        t_bpjs = (EditText) findViewById(R.id.txt_bpjs);
        t_beras = (EditText) findViewById(R.id.txt_beras);
        t_domisili = (EditText) findViewById(R.id.txt_domisili);
        r_laki = (RadioButton) findViewById(R.id.radio_laki);
        r_perempuan = (RadioButton) findViewById(R.id.radio_perempuan);
        spin_goljam = (Spinner) findViewById(R.id.spinner_goljam);
        r_laki.setChecked(true);

        //spinner goljam
        list_spin_goljam = new ArrayList<String>();
        fetchPosts_goljam();

        //get nik from karyawan
        list_nik = new ArrayList<String>();
        fetchPosts_karyawan();

        //fetch bagian, set spinner adapter
        bagian_baru="";
        list_spin_bagian = new ArrayList<String>();
        fetchPosts_bagian();
        spin_bagian = (Spinner) findViewById(R.id.spinner2);

        //when spinner clicked (used for tambah baru bagian)
        spin_bagian.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                int pos = spin_bagian.getSelectedItemPosition();
                Log.i("Posisi",pos+"");

                //if tambah baru bagian clicked
                if(pos==list_spin_bagian.size()-1){

                    // get prompts.xml view
                    LayoutInflater li = LayoutInflater.from(AdminInsertPegawai.this);
                    View promptsView = li.inflate(R.layout.prompt_bagian_dialog, null);

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                            AdminInsertPegawai.this);

                    // set prompts.xml to alertdialog builder
                    alertDialogBuilder.setView(promptsView);

                    final EditText userInput = (EditText) promptsView
                            .findViewById(R.id.editTextDialogUserInput);

                    // set dialog message
                    alertDialogBuilder
                            .setCancelable(false)
                            .setPositiveButton("OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog,int id) {
                                            // add new bagian
                                            bagian_baru = userInput.getText()+"";
                                            Log.i("Inputan",bagian_baru);
                                            //add bagian procedure called
                                            fetchPosts_bagian_add();
                                            //fetch new data after insertion
                                            fetchPosts_bagian();
                                            Toast.makeText(AdminInsertPegawai.this, "Bagian baru "+bagian_baru+" berhasil ditambahkan", Toast.LENGTH_SHORT).show();

                                        }
                                    })
                            .setNegativeButton("Cancel",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    });

                    // create alert dialog
                    AlertDialog alertDialog = alertDialogBuilder.create();

                    // show it
                    alertDialog.show();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });


        //pilih tanggal clicked
        t_tgl_lhr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(999);
            }

        });

        //Spinner Agama
        array_spinner_agama =new String[6];
        array_spinner_agama[0]="ISLAM   ";
        array_spinner_agama[1]="KRISTEN ";
        array_spinner_agama[2]="KATHOLIK";
        array_spinner_agama[3]="HINDU   ";
        array_spinner_agama[4]="BUDHA   ";
        array_spinner_agama[5]="LAINYA  ";
        spin_agama = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, array_spinner_agama);
        spin_agama.setAdapter(adapter);

        //calender
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR)-40;
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);


    }

    //button insert clicked
    public void insert(View view){
        //reset check nik
        check_nik = true;

        //get data from user
        nik = t_nik.getText()+"";
        nama = t_nama.getText()+"";
        tgl_lhr = t_tgl_lhr.getText()+"";
        agama = spin_agama.getSelectedItem().toString();
        bagian = spin_bagian.getSelectedItem().toString();
        goljamker = spin_goljam.getSelectedItem().toString();
        umr = t_umr.getText()+"";
        spsi = t_spsi.getText()+"";
        jamsos = t_jamsos.getText()+"";
        dsu = t_dsu.getText()+"";
        bpjs = t_bpjs.getText()+"";
        beras = t_beras.getText()+"";
        domisili = t_domisili.getText()+"";
        View focusView = null;

        //check duplicated nik
        for( int i = 0; i<list_nik.size(); i++){
            if (nik.equals(list_nik.get(i))){
                check_nik = false;
            }
        }

        //Null Handler, if null, check_error=false;
        if (nik.equals("")){
            check_error=false;
            t_nik.requestFocus();
            focusView = t_nik;
            YoYo.with(Techniques.Shake)
                    .duration(500)
                    .playOn(findViewById(R.id.txt_nik));
        }
        else if (nama.equals("")){
            check_error=false;
            t_nama.requestFocus();
            focusView = t_nama;
            YoYo.with(Techniques.Shake)
                    .duration(500)
                    .playOn(findViewById(R.id.txt_nama));
        }
        else if (tgl_lhr.equals("")){
            check_error=false;
            YoYo.with(Techniques.Shake)
                    .duration(500)
                    .playOn(findViewById(R.id.txt_tgl_lhr));
        }
        else if (domisili.equals("")){
            check_error=false;
            t_domisili.requestFocus();
            YoYo.with(Techniques.Shake)
                    .duration(500)
                    .playOn(findViewById(R.id.txt_domisili));

        }
        else if (goljamker.equals("")){
            check_error=false;
        }
        else if (umr.equals("")){
            check_error=false;
            t_umr.requestFocus();
            YoYo.with(Techniques.Shake)
                    .duration(500)
                    .playOn(findViewById(R.id.txt_umr));
        }
        else if (spsi.equals("")){
            check_error=false;
            t_spsi.requestFocus();
            YoYo.with(Techniques.Shake)
                    .duration(500)
                    .playOn(findViewById(R.id.txt_spsi));
        }
        else if (jamsos.equals("")){
            check_error=false;
            t_jamsos.requestFocus();
            YoYo.with(Techniques.Shake)
                    .duration(500)
                    .playOn(findViewById(R.id.txt_jamsos));
        }
        else if (dsu.equals("")){
            check_error=false;
            t_dsu.requestFocus();
            YoYo.with(Techniques.Shake)
                    .duration(500)
                    .playOn(findViewById(R.id.txt_dsu));
        }
        else if (bpjs.equals("")){
            check_error=false;
            t_bpjs.requestFocus();
            YoYo.with(Techniques.Shake)
                    .duration(500)
                    .playOn(findViewById(R.id.txt_bpjs));
        }
        else if (beras.equals("")){
            check_error=false;
            t_beras.requestFocus();
            YoYo.with(Techniques.Shake)
                    .duration(500)
                    .playOn(findViewById(R.id.txt_domisili));
        }
        else{
            check_error=true;
        }

        if (check_error){
            //check nik, if duplicated make toast
            if(!check_nik){
                Toast.makeText(AdminInsertPegawai.this, "NIK "+nik+" telah digunakan!", Toast.LENGTH_SHORT).show();
            }
            else{
                try {
                    //insert pegawai procedure called
                    fetchPosts();

                }
                catch (Exception e){
                    Toast.makeText(AdminInsertPegawai.this, "Gagal menambah karyawan, Cek koneksi ke server!", Toast.LENGTH_LONG).show();
                }
            }
        }
        else{
            Toast.makeText(AdminInsertPegawai.this, "Data Tidak Lengkap. Cek Kembali!", Toast.LENGTH_SHORT).show();
        }

    }

    //setdate
    @SuppressWarnings("deprecation")
    public void setDate(View view) {
        showDialog(999);
    }@Override
    protected Dialog onCreateDialog(int id) {
        if (id == 999) {
            return new DatePickerDialog(this, myDateListener, year, month, day);
        }
        return null;
    }
    private DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
            // arg1 = year
            // arg2 = month
            // arg3 = day
            showDate(arg1, arg2+1, arg3);
        }
    };

    private void showDate(int year, int month, int day) {
        t_tgl_lhr.setText(new StringBuilder().append(year).append("-")
                .append(month).append("-").append(day));
    }

    //radio button jenis kelamin
    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radio_laki:
                if (checked)
                    kelamin = "L";
                break;
            case R.id.radio_perempuan:
                if (checked)
                    kelamin = "P";
                break;
        }
    }

    //insertion procedure
    private void fetchPosts() {
        StringRequest request = new StringRequest(Request.Method.POST, ENDPOINT, onPostsLoaded, onPostsError){
            @Override
            protected Map<String, String> getParams() {
                //send all data to web service
                Map<String, String> params = new HashMap<>();
                params.put("nik", nik);
                params.put("nama", nama);
                params.put("tgl_lhr", tgl_lhr);
                params.put("kelamin", kelamin);
                params.put("agama", agama);
                params.put("bagian", bagian);
                params.put("goljamker", goljamker);
                params.put("umr", umr);
                params.put("spsi", spsi);
                params.put("jamsos", jamsos);
                params.put("dsu", dsu);
                params.put("bpjs", bpjs);
                params.put("beras", beras);
                params.put("domisili", domisili);
                return params;
            }
        };
        requestQueue.add(request);
    }

    private final Response.Listener<String> onPostsLoaded = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            Toast.makeText(AdminInsertPegawai.this, "Karyawan dengan nik = "+nik+" berhasil ditambahkan", Toast.LENGTH_LONG).show();
            Intent back = new Intent(AdminInsertPegawai.this, AdminPegawai.class);
            startActivity(back);
            AdminInsertPegawai.this.finish();
        }
    };

    private final Response.ErrorListener onPostsError = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.e("MainActivity", error.toString());
            Toast.makeText(AdminInsertPegawai.this,"Koneksi gagal. Cek koneksi ke server!",Toast.LENGTH_SHORT).show();
        }
    };

    //fetch data bagian
    private void fetchPosts_bagian() {
        StringRequest request = new StringRequest(Request.Method.POST, ENDPOINT_bagian, onPostsLoaded_bagian, onPostsError_bagian){
        };
        requestQueue.add(request);
    }

    private final Response.Listener<String> onPostsLoaded_bagian = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            //Get JsonArray
            //fetch data onto list bagian
            List<Bagian> bagian = Arrays.asList(gson.fromJson(response, Bagian[].class));
            Log.i("MainActivity", bagian.size() + " posts loaded.");

            //sett data to spinner
            list_spin_bagian.clear();
            for (Bagian b : bagian) {
                list_spin_bagian.add(b.getNama_bagian());
            }
            list_spin_bagian.add("Tambah Bagian Baru");
            ArrayAdapter adapter_bagian = new ArrayAdapter(AdminInsertPegawai.this, android.R.layout.simple_spinner_item, list_spin_bagian);
            spin_bagian.setAdapter(adapter_bagian);

            spin_bagian.setSelection(bagian.size()-1);

        }
    };

    private final Response.ErrorListener onPostsError_bagian = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.e("MainActivity", error.toString());
            Toast.makeText(AdminInsertPegawai.this,"Koneksi gagal. Cek koneksi ke server!",Toast.LENGTH_SHORT).show();

        }
    };

    //bagian insertion procedure
    private void fetchPosts_bagian_add() {
        StringRequest request = new StringRequest(Request.Method.POST, ENDPOINT_bagian_add, onPostsLoaded_bagian_add, onPostsError_bagian_add){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                //send data bagian baru to web service
                params.put("nama_bagian", bagian_baru);
                return params;
            }
        };
        requestQueue.add(request);
        fetchPosts_bagian();
    }

    private final Response.Listener<String> onPostsLoaded_bagian_add = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            //Get JsonArray
        }
    };

    private final Response.ErrorListener onPostsError_bagian_add = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.e("MainActivity", error.toString());
            Toast.makeText(AdminInsertPegawai.this,"Koneksi gagal. Cek koneksi ke server!",Toast.LENGTH_SHORT).show();

        }
    };

    //fetch data karyawan
    private void fetchPosts_karyawan() {
        StringRequest request = new StringRequest(Request.Method.POST, ENDPOINT_karyawan, onPostsLoaded_karyawan, onPostsError_karyawan){
        };
        requestQueue.add(request);
    }

    private final Response.Listener<String> onPostsLoaded_karyawan = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            //Get JsonArray
            //get all nik to list_nik
            List<Post> karyawan = Arrays.asList(gson.fromJson(response, Post[].class));
            list_nik.clear();
            for(Post p : karyawan){
                list_nik.add(p.getNIK());
            }
        }
    };

    private final Response.ErrorListener onPostsError_karyawan = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.e("MainActivity", error.toString());
            Toast.makeText(AdminInsertPegawai.this,"Koneksi gagal. Cek koneksi ke server!",Toast.LENGTH_SHORT).show();

        }
    };
    private void fetchPosts_goljam() {
        StringRequest request = new StringRequest(Request.Method.POST, ENDPOINT_goljam, onPostsLoaded_goljam, onPostsError_goljam){
        };
        requestQueue.add(request);
    }

    private final Response.Listener<String> onPostsLoaded_goljam = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            //Get JsonArray
            //fetch data onto list bagian
            List<KodeGoljam> goljam = Arrays.asList(gson.fromJson(response, KodeGoljam[].class));

            //set data to spinner
            list_spin_goljam.clear();
            for (KodeGoljam b : goljam) {
                list_spin_goljam.add(b.getKode());
            }
            ArrayAdapter adapter_goljam = new ArrayAdapter(AdminInsertPegawai.this, android.R.layout.simple_spinner_item, list_spin_goljam);
            spin_goljam.setAdapter(adapter_goljam);
        }
    };

    private final Response.ErrorListener onPostsError_goljam = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.e("MainActivity", error.toString());
            Toast.makeText(AdminInsertPegawai.this,"Koneksi gagal. Cek koneksi ke server!",Toast.LENGTH_SHORT).show();

        }
    };

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(AdminInsertPegawai.this, AdminPegawai.class);
        startActivity(i);
        finish();
    }

    public void logout(View view){
        Intent i = new Intent(AdminInsertPegawai.this, Login_Activity.class);
        startActivity(i);
        AdminInsertPegawai.this.finish();
    }
}
