package com.example.offset.json.Other;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.offset.json.User.UserMain;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.offset.json.R;
import com.example.offset.json.model.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Login_Activity extends AppCompatActivity {

    EditText id, password;

    //web service address
    private static  String ENDPOINT;

    //for parsing json
    private RequestQueue requestQueue;
    private Gson gson;
    boolean idfound = false;

    //parsing result saved here
    List<User> users = new ArrayList<User>();

    String id1 ="",password1="",password2="",jenis="",status="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        idfound = false;
        setContentView(R.layout.activity_login_);
        ENDPOINT = "http://"+getResources().getString(R.string.ip)+"/KP/Login";
        id = (EditText) findViewById(R.id.text_id);
        password = (EditText) findViewById(R.id.text_password);
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        GsonBuilder gsonBuilder = new GsonBuilder();
        gson = gsonBuilder.create();
        //Get data from web service
        fetchPosts();
    }

    public void daftar(View view){
        Intent daftar = new Intent(Login_Activity.this, RegisterActivity.class);
        startActivity(daftar);
        Login_Activity.this.finish();
    }

    public void login(View view){
        //get data from login form
        idfound = false;
        id1 = id.getText()+"";
        password1 = password.getText()+"";

        //if id is empty
        if(id.getText().length()==0){
            Toast.makeText(Login_Activity.this, "ID tidak boleh kosong", Toast.LENGTH_SHORT).show();
            id.requestFocus();
            YoYo.with(Techniques.Shake)
                    .duration(500)
                    .playOn(findViewById(R.id.text_id));
        }
        //if password is empty
        else if(password.getText().length()==0){
            Toast.makeText(Login_Activity.this, "Password tidak boleh kosong", Toast.LENGTH_SHORT).show();
            password.requestFocus();
            YoYo.with(Techniques.Shake)
                    .duration(500)
                    .playOn(findViewById(R.id.text_password));
        }
        //get id from ws where id = id from edit text
        else{
            for (User user : users) {
                //find id
                if(user.getId().equals(id1)){
                    password2 = user.getPass();
                    jenis = user.getJenis();
                    status = user.getStatus();
                    idfound=true;
                    break;
                }
            }
            //login for admin
            if(jenis.equals("admin") && password1.equals(password2)) {
                Intent intent = new Intent(Login_Activity.this, AdminMain.class);
                intent.putExtra("user","Admin");
                startActivity(intent);
                id.setText("");
                password.setText("");
                Login_Activity.this.finish();
            }
            //login for another user
            else{
                if(!idfound) {
                    Toast.makeText(Login_Activity.this, "ID Salah", Toast.LENGTH_SHORT).show();
                    id.requestFocus();
                    YoYo.with(Techniques.Shake)
                            .duration(500)
                            .playOn(findViewById(R.id.text_id));
                }
                    // login logic
                else{
                    if (status.equals("n")){
                        Toast.makeText(Login_Activity.this, "ID belum terverifikasi admin!", Toast.LENGTH_SHORT).show();
                        YoYo.with(Techniques.Shake)
                                .duration(500)
                                .playOn(findViewById(R.id.text_id));
                        YoYo.with(Techniques.Shake)
                                .duration(500)
                                .playOn(findViewById(R.id.text_password));
                    }
                    else{
                        if(jenis.equals("non") && password1.equals(password2)){
                            Toast.makeText(Login_Activity.this, "Login Berhasil", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(Login_Activity.this, UserMain.class);
                            intent.putExtra("user",id1);
                            startActivity(intent);
                            id.setText("");
                            password.setText("");
                            Login_Activity.this.finish();
                        }
                        else{
                            Toast.makeText(Login_Activity.this, "Password Salah", Toast.LENGTH_SHORT).show();
                            password.requestFocus();
                            YoYo.with(Techniques.Shake)
                                    .duration(500)
                                    .playOn(findViewById(R.id.text_password));
                        }
                    }
                }
            }
        }
    }

    //parsing json using volley
    private void fetchPosts() {
        StringRequest request = new StringRequest(Request.Method.POST, ENDPOINT,
                onPostsLoaded, onPostsError){
        };
        requestQueue.add(request);
    }
    private final Response.Listener<String>
            onPostsLoaded = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            try{
                //Get JsonArray
                users = Arrays.asList(gson.fromJson(response, User[].class));
                for(User u: users){
                    Log.i("COBA", u.getId() + u.getPass());
                }
            }
            catch (Exception e){
                Toast.makeText(Login_Activity.this, "ID Salah",
                        Toast.LENGTH_SHORT).show();
            }
        }
    };
    private final Response.ErrorListener onPostsError = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.e("Login", error.toString());
            Toast.makeText(Login_Activity.this,"Koneksi gagal. Cek koneksi ke server!",
                    Toast.LENGTH_SHORT).show();
        }
    };


}
