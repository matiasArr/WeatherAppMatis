package com.example.weatherapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationRequest;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsoluteLayout;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        llamadaACiudades();
        llamadaACiudades2();
        llamadaACiudades3();

        Button buttonGPS = (Button) findViewById(R.id.buttonGPS);
        Button search = (Button) findViewById(R.id.search);
        Button fondoBloque1 = (Button) findViewById(R.id.bloque1);
        Button fondoBloque2 = (Button) findViewById(R.id.bloque2);
        Button fondoBloque3 = (Button) findViewById(R.id.bloque3);

        String arica = "Arica";
        String santiago = "Santiago";
        String puntaArenas = "Punta Arenas";

        //Se llama a la función que permite clikear las ciudades
        clickearCiudad(fondoBloque1, arica);
        clickearCiudad(fondoBloque2, santiago);
        clickearCiudad(fondoBloque3, puntaArenas);


        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Se obtiene el texto ingresado para luego enviarse a la activity B
                EditText editText = (EditText) findViewById(R.id.city);
                String textToPass = editText.getText().toString();

                Intent intent = new Intent(getApplicationContext(), ActivityB.class);
                intent.putExtra(Intent.EXTRA_TEXT, textToPass);
                // se demora 2 segundo en responder
                try {
                    TimeUnit.MICROSECONDS.sleep(200);
                    startActivity(intent);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        buttonGPS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

                if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){

                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                }

                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        double lat= location.getLatitude();
                        double lon= location.getLongitude();

                        String coords = String.valueOf(lat) +"°"+ String.valueOf(lon);

                        Intent intent = new Intent(getApplicationContext(), ActivityC.class);
                        intent.putExtra("coords",coords);
                        //se demora 5 segundos en responder
                        try {
                            TimeUnit.MICROSECONDS.sleep(1);
                            startActivity(intent);
                            locationManager.removeUpdates(this);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    public void onStatusChanged(String provider, int status, Bundle extras) { }

                    public void onProviderEnabled(String provider) { }

                    public void onProviderDisabled(String provider) { }

                });
            }
        });
    }

    public void clickearCiudad(Button fondoBloque, String ciudad){
        fondoBloque.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String textToPass = ciudad;
                Intent intent = new Intent(getApplicationContext(), ActivityB.class);
                intent.putExtra(Intent.EXTRA_TEXT, textToPass);
                // se demora 2 segundo en responder
                startActivity(intent);
            }
        });
    }



    public void llamadaACiudades(){

        //prepare request
        RequestQueue queue = Volley.newRequestQueue(this);

        //String con el Endpoints para el nombre de la Ciudad
        String url = "https://api.openweathermap.org/data/2.5/weather?q=arica&appid=0c6e0566dd523e8054a8bb5944ea320f";

        StringRequest stringrequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject reader = null;
                try {
                    //Preparar Strings para concatenar link de fotos

                    String urlParaPNGClimas1 = "https://openweathermap.org/img/wn/";
                    String urlParaPNGClimas2 = "";
                    String urlParaPNGClimas3 = "@2x.png";

                    //Vinculo entre la instancia de los Elementos con sus id's respectivos

                    TextView textViewCiudad1 = findViewById(R.id.idCiudadNombre1);
                    TextView textViewTemperatura1 = findViewById(R.id.idCiudadTemperatura1);
                    TextView textViewClima1 = findViewById(R.id.idCiudadClima1);
                    ImageView imageViewFotoClima1 = findViewById(R.id.imagenClima1);

                    reader = new JSONObject(response);

                    String nombreCiudad = reader.getString("name");
                    JSONObject aux1 = reader.getJSONObject("main");
                    String temperatura1 = aux1.getString("temp");
                    JSONArray clima = reader.getJSONArray("weather");
                    JSONObject clima2 = clima.getJSONObject(0);
                    String textoClima1 = clima2.getString("description");
                    urlParaPNGClimas2 = clima2.getString("icon");

                    double temperatura = Double.parseDouble(temperatura1)-273.15;
                    temperatura = Math.round(temperatura*100.0)/100.0;
                    temperatura1 = String.valueOf(temperatura);

                    textViewCiudad1.setText(nombreCiudad);
                    textViewTemperatura1.setText(temperatura1 + " °C");
                    textViewClima1.setText(textoClima1);

                    Picasso.get()
                            .load(urlParaPNGClimas1.concat(urlParaPNGClimas2).concat(urlParaPNGClimas3))
                            .error(R.mipmap.ic_launcher)
                            .fit().centerInside()
                            .into(imageViewFotoClima1);


                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        //Add request to queue
        queue.add(stringrequest);
    }

    public void llamadaACiudades2(){
        //prepare request
        RequestQueue queue = Volley.newRequestQueue(this);

        //String con los Endpoints para conseguir el nombre de la Ciudad
        String url = "https://api.openweathermap.org/data/2.5/weather?q=santiago&appid=0c6e0566dd523e8054a8bb5944ea320f";
        StringRequest stringrequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject reader = null;

                try {
                    //Preparar Strings para concatenar link de fotos

                    String urlParaPNGClimas1 = "https://openweathermap.org/img/wn/";
                    String urlParaPNGClimas2 = "";
                    String urlParaPNGClimas3 = "@2x.png";

                    //Vinculo entre la instancia de los Elementos con sus id's respectivos

                    TextView textViewCiudad2 = findViewById(R.id.idCiudadNombre2);
                    TextView textViewTemperatura2 = findViewById(R.id.idCiudadTemperatura2);
                    TextView textViewClima2 = findViewById(R.id.idCiudadClima2);
                    ImageView imageViewFotoClima2 = findViewById(R.id.imageClima2);

                    reader = new JSONObject(response);

                    String nombreCiudad = reader.getString("name");
                    JSONObject aux1 = reader.getJSONObject("main");
                    String temperatura1 = aux1.getString("temp");
                    JSONArray clima = reader.getJSONArray("weather");
                    JSONObject clima2 = clima.getJSONObject(0);
                    String textoClima1 = clima2.getString("description");
                    urlParaPNGClimas2 = clima2.getString("icon");

                    double temperatura = Double.parseDouble(temperatura1)-273.15;
                    temperatura = Math.round(temperatura*100.0)/100.0;
                    temperatura1 = String.valueOf(temperatura);

                    textViewCiudad2.setText(nombreCiudad);
                    textViewTemperatura2.setText(temperatura1 + " °C");
                    textViewClima2.setText(textoClima1);

                    Picasso.get()
                            .load(urlParaPNGClimas1.concat(urlParaPNGClimas2).concat(urlParaPNGClimas3))
                            .error(R.mipmap.ic_launcher)
                            .fit().centerInside()
                            .into(imageViewFotoClima2);


                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        //Add request to queue
        queue.add(stringrequest);
    }
        public void llamadaACiudades3(){
            //prepare request
            RequestQueue queue = Volley.newRequestQueue(this);
            //String con los Endpoints para conseguir el nombre de la Ciudad
            String url = "https://api.openweathermap.org/data/2.5/weather?q=punta arenas&appid=0c6e0566dd523e8054a8bb5944ea320f";

            StringRequest stringrequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    JSONObject reader = null;
                    try {

                        //Preparar el String para concatenar las ciudades

                        String urlParaNombreCiudades1 = "https://api.openweathermap.org/data/2.5/weather?q=";
                        String urlParaNombreCiudades2 = "";
                        String urlParaNombreCiudades3 = "&appid=c7d28225f2d5ea29d5c3ada03438af7d";

                        //Preparar Strings para concatenar link de fotos

                        String urlParaPNGClimas1 = "https://openweathermap.org/img/wn/";
                        String urlParaPNGClimas2 = "";
                        String urlParaPNGClimas3 = "@2x.png";

                        //Vinculo entre la instancia de los Elementos con sus id's respectivos

                        TextView textViewCiudad3 = findViewById(R.id.idCiudadNombre3);
                        TextView textViewTemperatura3 = findViewById(R.id.idCiudadTemperatura3);
                        TextView textViewClima3 = findViewById(R.id.idCiudadClima3);
                        ImageView imageViewFotoClima3 = findViewById(R.id.imageClima3);

                        reader = new JSONObject(response);

                        String nombreCiudad = reader.getString("name");
                        JSONObject aux1 = reader.getJSONObject("main");
                        String temperatura1 = aux1.getString("temp");
                        JSONArray clima = reader.getJSONArray("weather");
                        JSONObject clima2 = clima.getJSONObject(0);
                        String textoClima1 = clima2.getString("description");
                        urlParaPNGClimas2 = clima2.getString("icon");

                        double temperatura = Double.parseDouble(temperatura1)-273.15;
                        temperatura = Math.round(temperatura*100.0)/100.0;
                        temperatura1 = String.valueOf(temperatura);

                        textViewCiudad3.setText(nombreCiudad);
                        textViewTemperatura3.setText(temperatura1 + " °C");
                        textViewClima3.setText(textoClima1);

                        Picasso.get()
                                .load(urlParaPNGClimas1.concat(urlParaPNGClimas2).concat(urlParaPNGClimas3))
                                .error(R.mipmap.ic_launcher)
                                .fit().centerInside()
                                .into(imageViewFotoClima3);

                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                }
            });
            //Add request to queue
            queue.add(stringrequest);
        }
}