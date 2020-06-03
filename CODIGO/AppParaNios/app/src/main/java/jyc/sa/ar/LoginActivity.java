package jyc.sa.ar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    private Button btnLogin ;
    private EditText txtEmail;
    private EditText txtPassword;
    public TextView txtResp;
    public String token="";


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
                    Intent i = new Intent(LoginActivity.this, ServicioHttp.class);
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


    }

    private void enviarIntent() {
        if(token!="") {
            Intent i = new Intent(LoginActivity.this, JuegoActivity.class);
            i.putExtra("token", token);

            startActivity(i);
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


    @Override
    protected void onPause() {
        super.onPause();
    }



}
