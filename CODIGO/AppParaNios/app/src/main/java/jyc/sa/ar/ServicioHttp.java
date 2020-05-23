package jyc.sa.ar;


import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.Nullable;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class ServicioHttp extends IntentService {
    private Exception msjExc = null;
    private HttpURLConnection conexionHttp;
    private URL mURL;

    public ServicioHttp() {
        super("ServicioHttpPOST");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("SERVICIO_REGISTRO", "Servicio onCreate()");
    }

    protected void onHandleIntent(Intent intent){
        try {
            String uri = intent.getExtras().getString("uri");
            JSONObject datosJson = new JSONObject(intent.getExtras().getString("datosJson"));
            servidorPost(uri,datosJson);
        } catch (JSONException e) {
            Log.e("SERVICIO_REGISTRO","ERROR"+ e.toString());
        }

    }

    private void servidorPost(String uri, JSONObject datosJson) {

        String result  = post (uri,datosJson);
        if (result == null){
            Log.e("SERVICIO_REGISTRO","Error en GET\n" + msjExc.toString());
            return;
        }
        if (result == "NO_OK"){
            Log.e("SERVICIO_REGISTRO","Se recibio una respuesta NO_OK\n" + msjExc.toString());
            return;
        }

        Intent i =new Intent("jyc.sa.ar.intent.action.MAIN");
        i.putExtra("datosJson", result);
        sendBroadcast(i);

    }

    private String post(String uri, JSONObject datosJson) {
        HttpURLConnection urlConnection=null;
        String result="";

        try {
            URL mUrl=new URL(uri);
            conexionHttp = (HttpURLConnection) mUrl.openConnection();
            conexionHttp.setRequestProperty("Content-Type","application/json; charset=UTF-8");
            conexionHttp.setDoOutput(true);
            conexionHttp.setDoInput(true);
            conexionHttp.setConnectTimeout(5000);
            conexionHttp.setRequestMethod("POST");
            DataOutputStream wr =new DataOutputStream(conexionHttp.getOutputStream());
            wr.write(datosJson.toString().getBytes("UTF-8"));
            Log.i("SERVICIO_REGISTRO", "Se envia al server"+datosJson.toString());
            wr.flush();
            wr.close();
            conexionHttp.connect();
            int responseCode= conexionHttp.getResponseCode();
            if((responseCode == conexionHttp.HTTP_OK) || (responseCode == conexionHttp.HTTP_CREATED))
                result=convertInputStreamToString(new InputStreamReader(conexionHttp.getInputStream()));
            else
                result="NO_OK";


            msjExc=null;
            conexionHttp.disconnect();
            return result;


        }catch (Exception e) {
            msjExc = e;
            return null;
        }

    }

    private String convertInputStreamToString(InputStreamReader inputStreamReader) {//POSIBLE PROBLEMA
        String convert = inputStreamReader.toString();
        return convert;

         /*   ByteArrayOutputStream result = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStreamReader.read(buffer)) != -1) {
                result.write(buffer, 0, length);
            }

            return result.toString(StandardCharsets.UTF_8.name());

        }*/
    }

}
