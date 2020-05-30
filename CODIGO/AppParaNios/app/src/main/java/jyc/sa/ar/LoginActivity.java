package jyc.sa.ar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
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

    //private Handler HandlerbtnRegistrar;///POSIBLE PROBLEMA
    public IntentFilter filtro;
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                String datosJsonString = intent.getStringExtra("datosJson");
                JSONObject datosJson = new JSONObject(datosJsonString);
                Log.i("SERVICIO_REGISTRO", "Se envia al server" + datosJsonString );
                txtResp = (TextView) findViewById(R.id.textrespuesta);

                txtResp.setText(datosJsonString);

                Toast.makeText(context.getApplicationContext(), "Se recibio respuesta del Server", Toast.LENGTH_LONG).show();
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
            onNetworkChange(ni,context);
        }

        private void onNetworkChange(NetworkInfo networkInfo, Context context) {
            if (networkInfo != null && networkInfo.isConnected() ) {
                Log.d("MenuActivity", "CONNECTED");
            }else{
                Toast.makeText(context.getApplicationContext(), "No hay acceso a internet", Toast.LENGTH_LONG).show();

                Log.d("MenuActivity", "DISCONNECTED");
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
                    obj.put("env", "TEST");
                    obj.put("email", txtEmail.getText().toString());
                    obj.put("password", txtPassword.getText().toString());
                    Intent i = new Intent(LoginActivity.this, ServicioHttpLoginPOST.class);
                    i.putExtra("uri", URI_LOGIN);
                    i.putExtra("datosJson", obj.toString());

                    startService(i);

                    startActivity(new Intent(LoginActivity.this,JuegoActivity.class));


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        });
        configurarBroadcastReceiver();


    }

    private void configurarBroadcastReceiver() {
        filtro=new IntentFilter("android.intent.action.MAIN");
        filtro.addCategory("android.intent.category.LAUNCHER");
        registerReceiver(receiver, filtro);
    }


}
