package jyc.sa.ar;

import androidx.annotation.InspectableProperty;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.nio.file.Files;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    ImageView imgVocal;
    String vocalImg;
    int cantCorrectas=0;
    int cantIncorrectas=0;

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
            Toast.makeText(this, "Respuesta Correcta el animal de la imagen empieza con " + vocalImg, Toast.LENGTH_LONG).show();
            cantCorrectas++;
            //Toast.makeText(this, "Cantidad de respuestas correctas: "+cantCorrectas, Toast.LENGTH_LONG).show();
            generarImgRandom();

        } else {
            Toast.makeText(this,"Respuesta Incorrecta el animal de la imagen empieza con "+ vocalImg,Toast.LENGTH_LONG).show();
            cantIncorrectas++;
            //Toast.makeText(this, "Cantidad de respuestas incorrectas: "+cantIncorrectas, Toast.LENGTH_LONG).show();
        }
    }
}
