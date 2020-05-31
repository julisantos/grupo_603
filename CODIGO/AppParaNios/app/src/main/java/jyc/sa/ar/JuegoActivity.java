package jyc.sa.ar;

import androidx.annotation.InspectableProperty;
import androidx.appcompat.app.AppCompatActivity;

import android.app.IntentService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorEventListener2;
import android.hardware.SensorManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.file.Files;
import java.util.Random;

public class JuegoActivity extends AppCompatActivity {

    ImageView imgVocal;
    String vocalImg;
    int cantCorrectas=0;
    int cantIncorrectas=0;
    String token="";
    public IntentFilter filtro;
    LinearLayout juego;
    SensorManager smprox,smacel;
    Sensor sensorprox,sensoracel;
    SensorEventListener sensorEventListeneracel,sensorEventListenerprox;

    private BroadcastReceiver networkStateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo ni = manager.getActiveNetworkInfo();
            onNetworkChange(ni);

        }

        private void onNetworkChange(NetworkInfo networkInfo) {
            if (networkInfo != null && networkInfo.isConnected() ) {
                Log.d("MenuActivity", "CONNECTED");
            }else{
                Log.d("MenuActivity", "DISCONNECTED");
            }

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        informarEvento("login","ACTIVO","El usuario se encuentra logueado al sistema");


        juego = (LinearLayout)findViewById(R.id.activitySeleccionarLetra);
        smprox = (SensorManager)getSystemService(SENSOR_SERVICE);
        sensorprox = smprox.getDefaultSensor(Sensor.TYPE_PROXIMITY);

        smacel = (SensorManager)getSystemService(SENSOR_SERVICE);
        sensoracel = smacel.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        imgVocal = (ImageView) findViewById(R.id.imageObj);
        generarImgRandom();

        sensorAcelerometroCambiarImg();
        sensorProximidadMostrarResultados();
        activarSensores();





    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        informarEvento("login","INACTIVO","El usuario cerro la aplicacion");

    }

    @Override
    protected void onPause() {
        super.onPause();
        desactivarSensores();




        informarEvento("sensorAcelerometro","INACTIVO","se activo el sensor acelerometro");
        informarEvento("sensorProximidad","INACTIVO","se activo el sensor de proximidad");


    }

    @Override
    protected void onResume() {
        super.onResume();
        activarSensores();



        informarEvento("sensorAcelerometro","ACTIVO","se activo el sensor acelerometro");
        informarEvento("sensorProximidad","ACTIVO","se activo el sensor de proximidad");


    }



    public void seleccionarVocal(View view) {
        switch (view.getId()){
            case R.id.btnA: compararConImg("a");
            break;
            case R.id.btnE: compararConImg("e");
            break;
            case R.id.btnI: compararConImg("i");
            break;
            case R.id.btnO: compararConImg("o");
            break;
            case R.id.btnU: compararConImg("u");
            break;
        }

    }

    private void generarImgRandom() {

        final Random letraRandom = new Random();
        switch (letraRandom.nextInt(5)){
            case 0: vocalImg = "a";
                break;
            case 1: vocalImg = "e";
                break;
            case 2: vocalImg = "i";
                break;
            case 3: vocalImg = "o";
                break;
            case 4: vocalImg = "u";
                break;
        }
        final Random numRandom = new Random();
        String numVocal = String.format("%d", numRandom.nextInt(3));
        imgVocal.setImageResource(getResources().getIdentifier(vocalImg+numVocal,"drawable", getPackageName()));
    }

    private void compararConImg(String vocal) {

        if(vocal.equals(vocalImg)) {
            //Toast.makeText(this, "Respuesta Correcta el animal de la imagen empieza con " + vocalImg, Toast.LENGTH_LONG).show();
            cantCorrectas++;
            //Toast.makeText(this, "Cantidad de respuestas correctas: "+cantCorrectas, Toast.LENGTH_LONG).show();
            generarImgRandom();

        } else {
            //Toast.makeText(this,"Respuesta Incorrecta el animal de la imagen empieza con "+ vocalImg,Toast.LENGTH_LONG).show();
            cantIncorrectas++;
            //Toast.makeText(this, "Cantidad de respuestas incorrectas: "+cantIncorrectas, Toast.LENGTH_LONG).show();
        }
    }

    private void sensorProximidadMostrarResultados() {
        sensorEventListenerprox = new SensorEventListener() {

            @Override
            public void onSensorChanged(SensorEvent event) {
                Float valorProximidad = Float.valueOf(event.values[0]);
                if(valorProximidad == 0){
                    Intent isensorProx = new Intent(JuegoActivity.this, ScoreActivity.class);
                    isensorProx.putExtra("cantAciertos",String.valueOf(cantCorrectas));
                    isensorProx.putExtra("cantDesaciertos",String.valueOf(cantIncorrectas));
                    startActivity(isensorProx);
                }

            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };
    }

    private void  sensorAcelerometroCambiarImg() {
        sensorEventListeneracel = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                float ejeX = event.values[0];
                float ejeY = event.values[1];
                float ejeZ = event.values[2];
                if(ejeX<-8 || ejeX>8 || ejeY<-8 || ejeY>8 || ejeY<-8 || ejeY>8){
                    generarImgRandom();

                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };
    }

    private void activarSensores()
    {



                smacel.registerListener(sensorEventListeneracel,sensoracel,SensorManager.SENSOR_DELAY_NORMAL);
                smprox.registerListener(sensorEventListenerprox, sensorprox, SensorManager.SENSOR_DELAY_NORMAL);





    }
    private void desactivarSensores()
    {
        smacel.unregisterListener(sensorEventListeneracel);
        smprox.unregisterListener(sensorEventListenerprox);

    }

    private void informarEvento(String tipoEvento,  String estado, String descripcion){

        //VALIDAR SI PUDO CONECTARSE AL SERVIDOR ANTES DE HACER ESTO
        Bundle extras = getIntent().getExtras();
        String token = extras.getString("token");

        JSONObject obj = new JSONObject();
        try {
            obj.put("env", "DEV");
            obj.put("type_events", tipoEvento);
            obj.put("state", estado);
            obj.put("description", descripcion);
            Intent i = new Intent(JuegoActivity.this, ServicioHttpEvento.class);
            i.putExtra("uri", "http://so-unlam.net.ar/api/api/event");
            i.putExtra("token", token);
            i.putExtra("datosJson", obj.toString());

            startService(i);

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

}

