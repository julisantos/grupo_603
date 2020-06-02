package jyc.sa.ar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    private Button btnLogin ;
    private EditText txtEmail;
    private EditText txtPassword;
    public TextView txtResp;
    public String token="";

    public IntentFilter filtro;
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {

                String datosJsonString = intent.getStringExtra("datosJson");
                JSONObject datosJson = new JSONObject(datosJsonString);
                Log.i("SERVICIO_LOGIN", "Se recibe del server" + datosJsonString );

                if(datosJson.toString()==null) return;

                txtResp = (TextView) findViewById(R.id.textrespuesta);
                txtResp.setText(datosJsonString);
                token= datosJson.getString("token");

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    };


    private BroadcastReceiver networkStateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo ni = manager.getActiveNetworkInfo();
            onNetworkChange(ni, context);
        }

        private void onNetworkChange(NetworkInfo networkInfo, Context context) {
            if (networkInfo != null && networkInfo.isConnected() ) {
                Log.d("MenuActivity", "CONNECTED");
            }else{
                Log.d("MenuActivity", "DISCONNECTED");
                Toast.makeText(context.getApplicationContext(), "ATENCION! No hay acceso a internet", Toast.LENGTH_LONG).show();
            }
        }
    };

    private static final String URI_LOGIN = "http://so-unlam.net.ar/api/api/login";

    @Override
    protected void onCreate(Bundle savedInstanceState1) {

        super.onCreate(savedInstanceState1);
        setContentView(R.layout.activity_login);
        btnLogin = (Button) findViewById(R.id.btnLogin);

        txtEmail = (EditText) findViewById(R.id.txtEmail);
        txtPassword = (EditText) findViewById(R.id.txtPass);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v1) {
                JSONObject obj = new JSONObject();
                try {
                    obj.put("env", "DEV");
                    obj.put("email", txtEmail.getText().toString());
                    obj.put("password", txtPassword.getText().toString());
                    Intent i = new Intent(LoginActivity.this, ServicioHttpLoginPOST.class);
                    i.putExtra("uri", URI_LOGIN);
                    i.putExtra("datosJson", obj.toString());

                    startService(i);

                        Handler handler = new Handler();

                        handler.postDelayed(new Runnable() {
                            public void run() {
                                enviarIntent();

                            }
                        }, 2000);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        });

        configurarBroadcastReceiver();
    }

    private void enviarIntent() {
        if(token!="") {
            Intent i = new Intent(LoginActivity.this, JuegoActivity.class);
            i.putExtra("token", token);

            startActivity(i);
            finish();
        }
        else {
            txtResp = (TextView) findViewById(R.id.textrespuesta);
            txtResp.setText("ATENCION! Fallo la conexion con el servidor. Puede que alguno de los campos ingresados no sea valido");
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    txtResp.setText("");

                }
            }, 3000);
        }
    }

    private void configurarBroadcastReceiver() {
        filtro=new IntentFilter("android.intent.action.MAIN");
        filtro.addCategory("android.intent.category.LAUNCHER");
        registerReceiver(receiver, filtro);
    }


    @Override
    protected void onPause() {
        super.onPause();

    }



}
