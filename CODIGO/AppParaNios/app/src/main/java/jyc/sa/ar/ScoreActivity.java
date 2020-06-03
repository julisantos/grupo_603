package jyc.sa.ar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class ScoreActivity extends AppCompatActivity  {

    private TextView tvAciertos;
    private TextView tvDesaciertos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        tvAciertos = (TextView)findViewById(R.id.textAciertos);
        tvDesaciertos = (TextView)findViewById(R.id.textDesaciertos);
        recibirYsetiarAciertosYDesaciertos();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }

    @Override
    protected void onStop() {

        super.onStop();
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
