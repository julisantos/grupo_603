package jyc.sa.ar;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ServicioHttpLoginPOST extends IntentService {

    public ServicioHttpLoginPOST() {
        super("ServicioHttpLoginPOST");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("SERVICIO_LOGIN", "Servicio onCreate()");
    }

    protected void onHandleIntent(Intent intent){
        try {
            String uri = intent.getExtras().getString("uri");
            JSONObject datosJson = new JSONObject(intent.getExtras().getString("datosJson"));
            servidorPost(uri,datosJson);
        } catch (JSONException e) {
            Log.e("SERVICIO_LOGIN","ERROR"+ e.toString());
        }

    }

    private void servidorPost(String uri, JSONObject datosJson) {

        String result  = post (uri,datosJson);

        if (result == null){
            Log.e("SERVICIO_LOGIN","Error en POST");
            return;
        }

         if (result.equals("NO_OK")){
            Log.e("aca","Se recibio una respuesta NO_OK");
            Toast.makeText(this, "ATENCIÓN! Falló la conexión al servidor", Toast.LENGTH_LONG).show();
             Intent i =new Intent("android.intent.action.MAIN");
             i.putExtra("datosJson", result);
             sendBroadcast(i);
        }

        Intent i =new Intent("android.intent.action.MAIN");
        i.putExtra("datosJson", result);
        sendBroadcast(i);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private String post(String uri, JSONObject datosJson) {
        HttpURLConnection conexionHttp=null;
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
            Log.i("SERVICIO_LOGIN", "Se envia al server"+datosJson.toString());
            wr.flush();
            wr.close();

            conexionHttp.connect();
            int responseCode= conexionHttp.getResponseCode();


            if((responseCode == conexionHttp.HTTP_OK) || (responseCode == conexionHttp.HTTP_CREATED)) {
                result = convertInputStreamToString(new InputStreamReader(conexionHttp.getInputStream()));
            }else {
                result = "NO_OK";

                Toast.makeText(this, "El/los campo/s ingresados es incorrecto", Toast.LENGTH_LONG).show();

            }

            conexionHttp.disconnect();

        }catch (Exception e) {
            return null;
        }
        return result;
    }

    private String convertInputStreamToString(InputStreamReader input) throws IOException {
        BufferedReader streamReader = new BufferedReader(input);
        StringBuilder respondStreamBuild = new StringBuilder();
        String inputStr;
        while ((inputStr = streamReader.readLine()) != null)
            respondStreamBuild.append(inputStr);


        return respondStreamBuild.toString();

    }
}
