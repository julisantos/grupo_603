package jyc.sa.ar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;

public class ScoreActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager adminSensores;
    private static final int UMBRAL_SACUDIDA = 250;
    private static final int UMBRAL_ACTUALIZACION = 500;
    private static final int LIMITE_PROXIMIDAD = 3;
    private static final int ACELERACION_SWIPE_DERECHA = -4;
    private static final int ACELERACION_SWIPE_IZQUIERDA = 4;
    private long tiempoUltimaActualizacion;
    private float ultimoX;
    private float ultimoY;
    private float ultimoZ;
    private TextView tvAciertos;
    private TextView tvDesaciertos;

    private Boolean isFirstTime = true, hasSwiped = false;

    AlertDialog dialog;

    private static final int TO_INT_CONSTANT = 100;
    private static final double MIN_VALUE_TO_INT = 0.1;
    private static final int COMPLETE_UNIT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

         setContentView(R.layout.activity_score);

         tvAciertos = (TextView)findViewById(R.id.textAciertos);
         tvDesaciertos = (TextView)findViewById(R.id.textDesaciertos);
         recibirYsetiarAciertosYDesaciertos();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Proxima imagen").setTitle("Saltear imagen");
        dialog = builder.create();


        adminSensores = (SensorManager) getSystemService(SENSOR_SERVICE);
        inicializarSensores();


    }

    @Override
    protected void onRestart() {
        inicializarSensores();
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        inicializarSensores();
    }

    @Override
    protected void onPause() {
        pararSensores();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        pararSensores();
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        pararSensores();
        super.onStop();
    }

    private void inicializarSensores() {
        adminSensores.registerListener(this, adminSensores.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
        adminSensores.registerListener(this, adminSensores.getDefaultSensor(Sensor.TYPE_PROXIMITY), SensorManager.SENSOR_DELAY_NORMAL);
    }

    private void pararSensores() {
        adminSensores.unregisterListener(this, adminSensores.getDefaultSensor(Sensor.TYPE_ACCELEROMETER));
        adminSensores.unregisterListener(this, adminSensores.getDefaultSensor(Sensor.TYPE_PROXIMITY));
    }

    private void getShake(SensorEvent event) {
        float x, y, z;
        double aceleracionAnterior, aceleracionActual, velocidad;
        long tiempoActual = System.currentTimeMillis();
        long diferenciaDeTiempo = tiempoActual - tiempoUltimaActualizacion;

        if (diferenciaDeTiempo > UMBRAL_ACTUALIZACION) {

            tiempoUltimaActualizacion = tiempoActual;

            x = event.values[0];
            y = event.values[1];
            z = event.values[2];

            aceleracionActual = x + y + z;
            aceleracionAnterior = this.ultimoX + this.ultimoY + this.ultimoZ;

            velocidad = Math.abs(aceleracionActual - aceleracionAnterior) / diferenciaDeTiempo * 10000;

            if (velocidad > UMBRAL_SACUDIDA) {
                //***************PASAR A PROXIMA IMAGEN******************
            }

            this.ultimoX = x;
            this.ultimoY = y;
            this.ultimoZ = z;
        }
    }

    private void getProximity(SensorEvent event) {
        if (!this.isFirstTime && event.values[0] < LIMITE_PROXIMIDAD) {
            // ***************MOSTRAR SCORE*************
        }
        this.isFirstTime = false;
    }




    @Override
    public void onSensorChanged(SensorEvent event) {

        synchronized (this) {
            int tipoEvento = event.sensor.getType();

            switch(tipoEvento) {

                case Sensor.TYPE_ACCELEROMETER:
                    getShake(event);
                    break;

                case Sensor.TYPE_PROXIMITY:
                    getProximity(event);
                    break;

            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private  void recibirYsetiarAciertosYDesaciertos()
    {
        Bundle extras = getIntent().getExtras();
        String desaciertos = extras.getString("cantDesaciertos");
        String aciertos = extras.getString("cantAciertos");
        tvAciertos.setText(aciertos);
        tvDesaciertos.setText(desaciertos);
        tvAciertos.setTextColor(Color.GREEN);
        tvDesaciertos.setTextColor(Color.RED);

    }

}
