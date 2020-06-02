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

public class RegistroActivity extends AppCompatActivity {

    private EditText txtNombre;
    private EditText txtApellido;
    private EditText txtDni;
    private EditText txtEmail;
    private EditText txtPassword;
    private EditText txtGrupo;
    private EditText txtComision;
    private Button btnRegistrar;
    public TextView txtResp;
    public IntentFilter filtro;
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                String datosJsonString = intent.getStringExtra("datosJson");
                JSONObject datosJson = new JSONObject(datosJsonString);
                Log.i("SERVICIO", "Se recibe del server" + datosJsonString );
                txtResp = (TextView) findViewById(R.id.textrespuesta);
                txtResp.setText(datosJsonString);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

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


    private static final String URI_REGISTRO = "http://so-unlam.net.ar/api/api/register";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        registerReceiver(networkStateReceiver, new IntentFilter(android.net.ConnectivityManager.CONNECTIVITY_ACTION));

        txtNombre = (EditText) findViewById(R.id.txtNombre);
        txtApellido = (EditText) findViewById(R.id.txtApellido);
        txtDni = (EditText) findViewById(R.id.numDni);
        txtEmail = (EditText) findViewById(R.id.txtEmail);
        txtPassword = (EditText) findViewById(R.id.txtPass);
        txtGrupo = (EditText) findViewById(R.id.numGrupo);
        txtComision = (EditText) findViewById(R.id.numComision);
        btnRegistrar = (Button) findViewById(R.id.btnRegistrar);



        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override


            public void onClick(View v) {

                if (esValido()) {

                    JSONObject obj = new JSONObject();

                    try {
                        obj.put("env", "DEV");
                        obj.put("name", txtNombre.getText().toString());
                        obj.put("lastname", txtApellido.getText().toString());
                        obj.put("dni", txtDni.getText().toString());
                        obj.put("email", txtEmail.getText().toString());
                        obj.put("password", txtPassword.getText().toString());
                        obj.put("commission", txtComision.getText().toString());
                        obj.put("group", txtGrupo.getText().toString());
                        Intent i = new Intent(RegistroActivity.this, ServicioHttpRegistroPOST.class);
                        i.putExtra("uri", URI_REGISTRO);
                        i.putExtra("datosJson", obj.toString());

                        startService(i);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }
        });

        configurarBroadcastReceiver();
    }

    private void configurarBroadcastReceiver() {
        filtro=new IntentFilter("jyc.sa.intent.action.MAIN");
        filtro.addCategory("jyc.sa.intent.category.LAUNCHER");
        registerReceiver(receiver, filtro);
    }


    public void accederALogin(View view) {

            startActivity(new Intent(RegistroActivity.this,LoginActivity.class));
            finish();
        }


    public boolean esValido() {

        String campNombre = txtNombre.getText().toString();
        String campApellido = txtApellido.getText().toString();
        String campDni = txtDni.getText().toString();
        String campEmail = txtEmail.getText().toString();
        String campPass = txtPassword.getText().toString();
        String campComi = txtComision.getText().toString();
        String campGrupo = txtGrupo.getText().toString();
        boolean valido = true;
        if(campNombre.isEmpty()){

            txtNombre.setError("Debe ingresar su nombre para registrarse");
            valido = false;
        }
        if(campApellido.isEmpty()){

            txtApellido.setError("Debe ingresar su apellido para registrarse");
            valido = false;
        }

        if(campDni.isEmpty() || campDni.length()>8){

            txtDni.setError("Numero de dni incorrecto,recuerde que no debe superar los 8 caracteres");
            valido = false;
        }

        if(campEmail.isEmpty()){
            txtEmail.setError("Debe ingresar su E-mal para registrarse");
            valido = false;
        }
        if(campPass.isEmpty() || campPass.length()<8){
            txtPassword.setError("Campo password incorrecto, recuerde ingresar 8 caracteres o mas");
            valido = false;
        }

        if(campComi.isEmpty()){
            txtComision.setError("Debe ingresar la comision para registrarse");
            valido = false;
        }

        if(campGrupo.isEmpty()){
            txtGrupo.setError("Debe ingresar su numero de grupo para registrarse");
            valido = false;
        }
        return valido;
    }
}
