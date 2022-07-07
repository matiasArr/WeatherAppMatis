package com.example.weatherapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
//import com.google.type.DateTime;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class ActivityB extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_b);
        //se comento un import time
        Intent intent = getIntent();
        searchCity(intent);
    }
    public void searchCity(Intent intent){
        String text = intent.getStringExtra(Intent.EXTRA_TEXT);
        String nameCity = text;
        buscarCuidad(nameCity);
    }

    public void buscarCuidad(String nameCity){
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://api.openweathermap.org/data/2.5/weather?q="+nameCity+"&appid=0c6e0566dd523e8054a8bb5944ea320f";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject reader = null;
                System.out.println("");
                //  String url2 = "https://api.openweathermap.org/data/2.5/onecall?lat="+lat+"&lon="+lon+"&appid=c7d28225f2d5ea29d5c3ada03438af7d&units=metric";
                try {
                    //Preparar Strings para concatenar link de fotos

                    String urlParaPNGClimas1 = "https://openweathermap.org/img/wn/";
                    String urlParaPNGClimas2 = "";
                    String urlParaPNGClimas3 = "@2x.png";

                    TextView textView = (TextView) findViewById(R.id.ciudad);
                    TextView textView1 = (TextView) findViewById(R.id.grados);
                    TextView tempMinMaxCiudad1 = (TextView) findViewById(R.id.tempMinMax1);
                    TextView textView3 = (TextView) findViewById(R.id.clima);
                    TextView textView4 = (TextView) findViewById(R.id.nombreClima1);
                    ImageView imageViewFotoClima1 = findViewById(R.id.imagenClima11);

                    reader = new JSONObject(response);
                    JSONObject aux = reader.getJSONObject("main");
                    JSONObject aux2 = reader.getJSONObject("coord");
                    String lon = aux2.getString("lon");
                    String lat = aux2.getString("lat");
                    String temp = aux.getString("temp");
                    String tempMin = aux.getString("temp_min");
                    String tempMax = aux.getString("temp_max");
                    String nomCiudad = reader.getString("name");


                    JSONArray clima = reader.getJSONArray("weather");
                    JSONObject clima2 = clima.getJSONObject(0);
                    String textoClima1 = clima2.getString("main");
                    urlParaPNGClimas2 = clima2.getString("icon");


                    //Temperatura
                    temp=transformKToC(temp);
                    tempMin=transformKToC(tempMin);
                    tempMax=transformKToC(tempMax);

                    //obtener pais
                    JSONObject auxSys = reader.getJSONObject("sys");
                    String country = auxSys.getString("country");

                    Picasso.get()
                            .load(urlParaPNGClimas1.concat(urlParaPNGClimas2).concat(urlParaPNGClimas3))
                            .error(R.mipmap.ic_launcher)
                            .fit().centerInside()
                            .into(imageViewFotoClima1);


                    textView.setText(nomCiudad+", "+country);
                    textView1.setText(temp +"°C");
                    tempMinMaxCiudad1.setText(tempMin + "° / " + tempMax+"°");
                    textView3.setText(textoClima1);
                    textView4.setText("Today  • "+ textoClima1);


                    datosAProfundidadDeCiudad(lon,lat);

                }catch(JSONException e){
                    System.out.println("fallo");
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try {
                    TimeUnit.MICROSECONDS.sleep(200);
                    Intent intent2 = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        queue.add(stringRequest);
    }
    private String transformKToC(String temp){
        double temperatura = Double.parseDouble(temp)-273.15;
        temperatura = Math.round(temperatura*100.0)/100.0;
        temp = String.valueOf(temperatura);
        return temp;
    }

    private void anadirFoto(ImageView textClima, String icono){
        String urlParaPNGClimas1 = "https://openweathermap.org/img/wn/";
        String urlParaPNGClimas2 = icono;
        String urlParaPNGClimas3 = "@2x.png";

        Picasso.get()
                .load(urlParaPNGClimas1.concat(urlParaPNGClimas2).concat(urlParaPNGClimas3))
                .error(R.mipmap.ic_launcher)
                .fit().centerInside()
                .into(textClima);

    }

    private void datosAProfundidadDeCiudad(String lon, String lat) {
        RequestQueue queue = Volley.newRequestQueue(this);

        String url = "https://api.openweathermap.org/data/2.5/onecall?lat="+lat+"&lon="+lon+"&appid=0c6e0566dd523e8054a8bb5944ea320f&units=metric";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(String response) {
                JSONObject reader = null;

                try {

                    TextView clima2 = (TextView) findViewById(R.id.nombreClima2);
                    TextView clima3 = (TextView) findViewById(R.id.nombreClima3);
                    TextView clima4 = (TextView) findViewById(R.id.nombreClima4);
                    TextView clima5 = (TextView) findViewById(R.id.nombreClima5);

                    TextView tempMinMax2 = (TextView) findViewById(R.id.tempMinMax2);
                    TextView tempMinMax3 = (TextView) findViewById(R.id.tempMinMax3);
                    TextView tempMinMax4 = (TextView) findViewById(R.id.tempMinMax4);
                    TextView tempMinMax5 = (TextView) findViewById(R.id.tempMinMax5);

                    ImageView imageViewFotoClima2 = findViewById(R.id.imagenClima12);
                    ImageView imageViewFotoClima3 = findViewById(R.id.imagenClima13);
                    ImageView imageViewFotoClima4 = findViewById(R.id.imagenClima14);
                    ImageView imageViewFotoClima5 = findViewById(R.id.imagenClima15);

                    reader = new JSONObject(response);
                    JSONObject aux = reader.getJSONObject("current");

                    ////////////// Segunda Fila /////////////

                    //obtener la fecha, temperatura y clima de mañana
                    JSONArray daily = reader.getJSONArray("daily");
                    JSONObject auxDt = daily.getJSONObject(1);
                    String dt = auxDt.getString("dt");
                    JSONArray arrayweather = auxDt.getJSONArray("weather");
                    JSONObject weatherob = arrayweather.getJSONObject(0);
                    String main = weatherob.getString("main");
                    String icon = weatherob.getString("icon");

                    JSONObject auxtemp = auxDt.getJSONObject("temp");
                    String min1 = auxtemp.getString("min");
                    String max1 = auxtemp.getString("max");


                    //seteo de información
                    anadirFoto(imageViewFotoClima2,icon);
                    tempMinMax2.setText(min1 + "° / " + max1+"°");
                    clima2.setText("Tomorrow  • "+ main);


                    ////////////////// Tercera FIla /////////////////////7

                    //obtener la fecha de pasado mañana
                    JSONObject auxDt2 = daily.getJSONObject(2);
                    String dt2 = auxDt2.getString("dt");
                    JSONArray arrayweather2 = auxDt2.getJSONArray("weather");
                    JSONObject weatherob2 = arrayweather2.getJSONObject(0);
                    String main2 = weatherob2.getString("main");
                    String icon2 = weatherob2.getString("icon");

                    JSONObject auxtemp2 = auxDt2.getJSONObject("temp");
                    String min2 = auxtemp2.getString("min");
                    String max2 = auxtemp2.getString("max");

                    String word =conseguirDiaDeSemana(dt2);

                    //seteo de información
                    anadirFoto(imageViewFotoClima3,icon2);
                    tempMinMax3.setText(min2 + "° / " + max2+"°");
                    clima3.setText(word+"  • "+ main2);


                    /////////////// Cuarta Fila ///////////////

                    //obtener la fecha pasado pasado mañana
                    JSONObject auxDt3 = daily.getJSONObject(3);
                    String dt3 = auxDt3.getString("dt");
                    JSONArray arrayweather3 = auxDt3.getJSONArray("weather");
                    JSONObject weatherob3 = arrayweather3.getJSONObject(0);
                    String main3 = weatherob3.getString("main");
                    String icon3 = weatherob3.getString("icon");

                    JSONObject auxtemp3 = auxDt3.getJSONObject("temp");
                    String min3 = auxtemp3.getString("min");
                    String max3 = auxtemp3.getString("max");

                    String word3 =  conseguirDiaDeSemana(dt3);

                    //seteo de información
                    anadirFoto(imageViewFotoClima4,icon3);
                    tempMinMax4.setText(min3 + "° / " + max3+"°");
                    clima4.setText(word3+"  • "+ main3);

                    //////////////////// Quinta fila ///////////

                    //obtener la fecha pasado pasado mañana
                    JSONObject auxDt4 = daily.getJSONObject(4);
                    String dt4 = auxDt4.getString("dt");
                    JSONArray arrayweather4 = auxDt4.getJSONArray("weather");
                    JSONObject weatherob4 = arrayweather4.getJSONObject(0);
                    String main4 = weatherob4.getString("main");
                    String icon4 = weatherob4.getString("icon");

                    JSONObject auxtemp4 = auxDt4.getJSONObject("temp");
                    String min4 = auxtemp4.getString("min");
                    String max4 = auxtemp4.getString("max");

                    String word4 = conseguirDiaDeSemana(dt4);

                    //seteo de información
                    anadirFoto(imageViewFotoClima5,icon4);
                    tempMinMax5.setText(min4 + "° / " + max4+"°");
                    clima5.setText(word4+"  • "+ main4);

                }catch(JSONException e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        queue.add(stringRequest);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String conseguirDiaDeSemana(String dt){
        int fechaEpoch = Integer.parseInt(dt);
        Date date = new Date(fechaEpoch);
        Instant fecha = Instant.ofEpochSecond(fechaEpoch);
        LocalDate fecha2 = fecha.atZone(ZoneOffset.UTC).toLocalDate();
        DayOfWeek dayOfWeek = fecha2.getDayOfWeek();
        String word =dayOfWeek.toString();
        word = word.charAt(0) + word.substring(1).toLowerCase();

        return word;
    }
}
