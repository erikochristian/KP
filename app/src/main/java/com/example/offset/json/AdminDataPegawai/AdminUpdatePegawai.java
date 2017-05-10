package com.example.offset.json.AdminDataPegawai;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
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

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
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

public class AdminUpdatePegawai extends AppCompatActivity {
    private static  String ENDPOINT ;
    private static  String ENDPOINT2 ;
    private static  String ENDPOINT_bagian ;
    private static  String ENDPOINT_bagian_add;
    private static String ENDPOINT_goljam ;

    private RequestQueue requestQueue;
    private Gson gson;
    EditText t_nik, t_nama, t_tgl_lhr, t_goljamker, t_umr, t_spsi, t_jamsos, t_dsu, t_bpjs, t_beras,t_domisili;
    String nik, nama, tgl_lhr, agama, bagian, goljamker, umr, spsi, jamsos, dsu, bpjs, beras, domisili, bagian_baru, bagian_lama, goljam_lama;
    String nik_search="",searchby = "",cari="",user="";
    String kelamin="";
    RadioButton r_perempuan,r_laki;
    private Calendar calendar;
    private int year, month, day;
    private String array_spinner[];
    List<String> list_spin_goljam;
    Spinner spin_agama, spin_bagian, spin_goljam;
    boolean check_error = true;
    List<String> list_spin_bagian;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_update_pegawai);

        //link to web service
        ENDPOINT = "http://"+getResources().getString(R.string.ip)+"/KP/cekKaryawan";
        ENDPOINT2 = "http://"+getResources().getString(R.string.ip)+"/KP/updateKaryawan";
        ENDPOINT_bagian = "http://"+getResources().getString(R.string.ip)+"/KP/cekBagian";
        ENDPOINT_bagian_add = "http://"+getResources().getString(R.string.ip)+"/KP/insertBagian";
        ENDPOINT_goljam = "http://"+getResources().getString(R.string.ip)+"/KP/cekGoljam";

        //get nik to update from intent
        nik_search = getIntent().getStringExtra("nik");
        searchby = getIntent().getStringExtra("searchby");
        cari = getIntent().getStringExtra("cari");
        user = getIntent().getStringExtra("user");

        //gson,volley requirement
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        GsonBuilder gsonBuilder = new GsonBuilder();
        gson = gsonBuilder.create();

        //init other component
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
        t_nik.setEnabled(false);
        list_spin_goljam = new ArrayList<String>();



        //pilih tanggal clicked
        t_tgl_lhr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(999);
            }

        });

        //Spinner Agama
        array_spinner =new String[6];
        array_spinner[0]="ISLAM";
        array_spinner[1]="KRISTEN";
        array_spinner[2]="KATHOLIK";
        array_spinner[3]="HINDU";
        array_spinner[4]="BUDHA";
        array_spinner[5]="LAINYA";
        spin_agama = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, array_spinner);
        spin_agama.setAdapter(adapter);

        //calendar dialog requirement
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR)-40;
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        //spinner bagian,
        bagian_baru="";
        bagian_lama="";
        list_spin_bagian = new ArrayList<String>();


        //fetching old data to update
        fetchPosts();

        //fetching data bagian to spin_bagian
        spin_bagian = (Spinner) findViewById(R.id.spinner2);
        fetchPosts_bagian();
        spin_bagian.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            //when bagian clicked, get value
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                int pos = spin_bagian.getSelectedItemPosition();
                if(pos==list_spin_bagian.size()-1){
                    // get prompts.xml view
                    LayoutInflater li = LayoutInflater.from(AdminUpdatePegawai.this);
                    View promptsView = li.inflate(R.layout.prompt_bagian_dialog, null);

                    android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(
                            AdminUpdatePegawai.this);

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
                                            // get user input and set it to result
                                            // edit text
                                            bagian_baru = userInput.getText()+"";
                                            Log.i("Inputan",bagian_baru);
                                            fetchPosts_bagian_add();
                                            fetchPosts_bagian();
                                            Toast.makeText(AdminUpdatePegawai.this, "Bagian baru "+bagian_baru+" berhasil ditambahkan", Toast.LENGTH_SHORT).show();

                                        }
                                    })
                            .setNegativeButton("Cancel",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    });

                    // create alert dialog
                    android.support.v7.app.AlertDialog alertDialog = alertDialogBuilder.create();

                    // show it
                    alertDialog.show();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });


        //spinner goljam
        fetchPosts_goljam();
    }

    //DATE DIALOG
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

    //button update clicked
    public void update(View view){
        //get data from edittext
        nik = t_nik.getText()+"";
        nama = t_nama.getText()+"";
        tgl_lhr = t_tgl_lhr.getText()+"";
        agama = spin_agama.getSelectedItem().toString();
        bagian = spin_bagian.getSelectedItem()+"";
        goljamker = spin_goljam.getSelectedItem().toString();
        umr = t_umr.getText()+"";
        spsi = t_spsi.getText()+"";
        jamsos = t_jamsos.getText()+"";
        dsu = t_dsu.getText()+"";
        bpjs = t_bpjs.getText()+"";
        beras = t_beras.getText()+"";
        domisili = t_domisili.getText()+"";

        //Null Handler, if null, check_error=false;
        if (nama.equals("")){
            check_error=false;
            t_nama.requestFocus();
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
            new MaterialDialog.Builder(view.getContext())
                    .title("Konfirmasi")
                    .content("Update data karyawan dengan NIK = "+nik+" ?")
                    .positiveText("Ya")
                    .negativeText("Tidak")
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            fetchPosts2();
                            }

                    })
                    .onNegative(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            dialog.dismiss();
                        }
                    })
                    .show();
        }
        else{
            Toast.makeText(AdminUpdatePegawai.this, "Data Tidak Lengkap. Cek Kembali!", Toast.LENGTH_SHORT).show();
        }
    }

    //radio button changed, get value L/P
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

    //Fetching old data procedure
    private void fetchPosts() {
        StringRequest request = new StringRequest(Request.Method.POST, ENDPOINT, onPostsLoaded, onPostsError){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                //send nik to web service to get the data
                params.put("nik", nik_search);
                return params;
            }
        };
        requestQueue.add(request);
    }

    private final Response.Listener<String> onPostsLoaded = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            try{
                //set the old data to edit text
                Post post = gson.fromJson(response, Post.class);
                t_nik.setText(post.getNIK());
                t_nama.setText(post.getNAMA());
                bagian_lama = post.getBAGIAN();
                goljam_lama = post.getGOLJAMKER();
                t_tgl_lhr.setText(post.getTGL_LHR());
                Log.i("Goljam Lama",goljam_lama);
                agama = post.getAGAMA();
                t_beras.setText(post.getBERAS());
                t_bpjs.setText(post.getBPJS());
                t_domisili.setText(post.getDOMISILI());
                t_dsu.setText(post.getDSU());
                t_jamsos.setText(post.getJAMSOS());
                t_spsi.setText(post.getSPSI());
                t_umr.setText(post.getUMR());
                kelamin = post.getKELAMIN();

                if (kelamin.equals("P")){
                    r_perempuan.setChecked(true);
                }
                else{
                    r_laki.setChecked(true);
                }
                if(post.getAGAMA().toLowerCase().equals("islam   ")){
                    spin_agama.setSelection(0);
                }
                else if(post.getAGAMA().toLowerCase().equals("kristen ")){
                    spin_agama.setSelection(1);
                }
                else if(post.getAGAMA().toLowerCase().equals("katholik")){
                    spin_agama.setSelection(2);
                }
                else if(post.getAGAMA().toLowerCase().equals("hindu   ")){
                    spin_agama.setSelection(3);
                }
                else if(post.getAGAMA().toLowerCase().equals("budha   ")){
                    spin_agama.setSelection(4);
                }
                else{
                    spin_agama.setSelection(5);
                }

            }
            catch (Exception e){
                Toast.makeText(AdminUpdatePegawai.this, "NIK Tidak ditemukan", Toast.LENGTH_LONG).show();
                Intent back = new Intent(AdminUpdatePegawai.this, MainActivity.class);
                startActivity(back);
            }
        }
    };

    private final Response.ErrorListener onPostsError = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.e("MainActivity", error.toString());
            Toast.makeText(AdminUpdatePegawai.this,"Koneksi gagal. Cek koneksi ke server!",Toast.LENGTH_SHORT).show();
        }
    };

    //update data procedure
    private void fetchPosts2() {
        StringRequest request = new StringRequest(Request.Method.POST, ENDPOINT2, onPostsLoaded2, onPostsError2){
            @Override
            protected Map<String, String> getParams() {
                //send all new data to web service
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

    private final Response.Listener<String> onPostsLoaded2 = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            Toast.makeText(AdminUpdatePegawai.this, "Pegawai dengan nik = "+nik_search+" berhasil diupdate", Toast.LENGTH_SHORT).show();
            Intent back = new Intent(AdminUpdatePegawai.this, AdminPegawai.class);
            startActivity(back);
            AdminUpdatePegawai.this.finish();
        }
    };

    private final Response.ErrorListener onPostsError2 = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.e("MainActivity", error.toString());
            Toast.makeText(AdminUpdatePegawai.this,"Koneksi gagal. Cek koneksi ke server!",Toast.LENGTH_SHORT).show();
        }
    };

    //get data bagian from web service
    private void fetchPosts_bagian() {
        StringRequest request = new StringRequest(Request.Method.POST, ENDPOINT_bagian, onPostsLoaded_bagian, onPostsError_bagian){
        };
        requestQueue.add(request);
    }

    private final Response.Listener<String> onPostsLoaded_bagian = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            //Get JsonArray
            List<Bagian> bagian = Arrays.asList(gson.fromJson(response, Bagian[].class));
            int ctr = 0;
            int fix = 0;
            list_spin_bagian.clear();
            for (Bagian b : bagian) {

                if (b.getNama_bagian().equals(bagian_lama)){
                    list_spin_bagian.add(b.getNama_bagian());
                    fix = ctr;
                }
                else{
                    list_spin_bagian.add(b.getNama_bagian());
                }

                ctr++;
            }
            list_spin_bagian.add("Tambah Bagian Baru");
            ArrayAdapter adapter_bagian = new ArrayAdapter(AdminUpdatePegawai.this, android.R.layout.simple_spinner_item, list_spin_bagian);
            spin_bagian.setAdapter(adapter_bagian);
            spin_bagian.setSelection(fix);
        }
    };

    private final Response.ErrorListener onPostsError_bagian = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.e("MainActivity", error.toString());
        }
    };

    private void fetchPosts_bagian_add() {
        StringRequest request = new StringRequest(Request.Method.POST, ENDPOINT_bagian_add, onPostsLoaded_bagian_add, onPostsError_bagian_add){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
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
            fetchPosts();
            int ctr = 0;
            int fix = 0;
            list_spin_goljam.clear();

            for (KodeGoljam b : goljam) {
                Log.i("Cocok1 = ",b.getKode()+"aaa"+goljam_lama);
                if (b.getKode().equals(goljam_lama)){
                    list_spin_goljam.add(b.getKode());
                    Log.i("Cocok2 = ",b.getKode()+"aaa"+goljam_lama);
                    fix = ctr;
                }
                else{
                    list_spin_goljam.add(b.getKode());
                }
                Log.i("FIX = ",fix+"");
                Log.i("CTR = ",ctr+"");
                ctr++;
            }
            ArrayAdapter adapter_goljam = new ArrayAdapter(AdminUpdatePegawai.this, android.R.layout.simple_spinner_item, list_spin_goljam);
            spin_goljam.setAdapter(adapter_goljam);
            spin_goljam.setSelection(fix);
        }
    };

    private final Response.ErrorListener onPostsError_goljam = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.e("MainActivity", error.toString());
        }
    };

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(AdminUpdatePegawai.this, ListPegawai.class);
        i.putExtra("crud","u");
        i.putExtra("cari",cari);
        i.putExtra("searchby",searchby);
        i.putExtra("user",user);
        startActivity(i);
        finish();
    }

    public void logout(View view){
        Intent i = new Intent(AdminUpdatePegawai.this, Login_Activity.class);
        startActivity(i);
        AdminUpdatePegawai.this.finish();
    }

}
