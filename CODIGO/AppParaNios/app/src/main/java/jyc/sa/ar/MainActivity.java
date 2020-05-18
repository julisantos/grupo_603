package jyc.sa.ar;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    ImageView imgVocal;
    String vocalImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imgVocal = (ImageView) findViewById(R.id.imageObj);
        generarImgRandom();

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
        switch (letraRandom.nextInt(4)){
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
        Drawable d = Drawable.createFromPath("@drawable/e1.jpg");
        imgVocal.setImageDrawable(d);
    }

    private void compararConImg(String vocal) {

        if(vocal.equals(vocalImg)) {
            Toast.makeText(this, "Respuesta Correcta el animal de la imagen empieza con " + vocalImg, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this,"Respuesta Incorrecta el animal de la imagen empieza con "+ vocalImg,Toast.LENGTH_LONG).show();
        }
    }
}
