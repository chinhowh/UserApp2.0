package com.example.chinhowh.userapp20;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import java.io.IOException;

public class LoginActivity extends AppCompatActivity {

    private LocationManager lc;
    private LocationListener ll;
    private Button btLogin;
    private Button btSignUp;
    private EditText etEmail;
    private EditText etPasswd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etEmail = (EditText)findViewById(R.id.etEmail);
        etPasswd = (EditText)findViewById(R.id.etPasswd);

        if (!isConnected())
        {
        new AlertDialog.Builder(LoginActivity.this)
                .setTitle("錯誤")
                .setMessage("網路未連線")
                .setPositiveButton("立即設定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = getPackageManager().getLaunchIntentForPackage("com.android.settings");
                        startActivity(intent);
                    }
                })
                .setNegativeButton("取消", null).show();
        }


        btLogin = (Button)findViewById(R.id.btLogin);
        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etEmail.getText().toString();
                String passwd = etPasswd.getText().toString();

                try {
                    ClientSocket CS = new ClientSocket();
                    if (CS.socket.isConnected())
                    {
                        CS.output.write(email + "\n");
                        CS.output.write(passwd + "\n");
                    }
                    else {
                        new AlertDialog.Builder(LoginActivity.this)
                                .setTitle("錯誤")
                                .setMessage("未連線到伺服器")
                                .show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("text","onDestroy()="+e.toString());
                }

                /*
                Intent intent = new Intent();
                intent.setClass(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                */


            }
        });


        btSignUp = (Button)findViewById(R.id.btSignUp);
        btSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });


    }
    private boolean isConnected() {
        ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        }
        else {return false;}
    }
}