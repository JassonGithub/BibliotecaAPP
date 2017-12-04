package com.example.jasson.bibliotecasapp;

import android.Manifest;
import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.jasson.bibliotecasapp.datosAppi.SitiosService;
import com.example.jasson.bibliotecasapp.models.Biblioteca;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private ArrayList<Biblioteca> list_bibliotecas = new ArrayList<>();
    private ArrayList<String> georef = new ArrayList<>();

    private ArrayList<Double> list_latitud = new ArrayList<>();
    private ArrayList<Double> list_longitud = new ArrayList<>();

    private GoogleMap mMap;
    private Retrofit retrofit;
    public static final String TAG = "Biblioteca --- ";
    private Spinner spinner;
    private String item_spinner;

    List<String> lista_nombres = new ArrayList<String>();//lista con los nombres de departamentos
    ArrayList<Biblioteca> list_dpt = new ArrayList<>();//lista de marcadores por departamento








    ArrayList<Biblioteca> list_dpt2 = new ArrayList<>();//auxiliar

    //mi ubicacion
    double lati = 0.0;
    double longi = 0.0;
    double lat2 = 0.0;
    double log2 = 0.0;
    private Marker marcadoru;
    Polyline line;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        //retrofit
        retrofit = new Retrofit.Builder()
                .baseUrl("https://www.datos.gov.co/resource/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        spinner = (Spinner) findViewById(R.id.spinner);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        obtenerdatos();
    }


    public void obtenerdatos() {
        SitiosService servicio = retrofit.create(SitiosService.class);
        Call<List<Biblioteca>> sitiorespuestacall = servicio.obtenerlistabibliotecas();
        sitiorespuestacall.enqueue(new Callback<List<Biblioteca>>() {
            @Override
            public void onResponse(Call<List<Biblioteca>> call, Response<List<Biblioteca>> response) {
                if (response.isSuccessful()) {

                    List lista = response.body();
                    for (int i = 0; i < lista.size(); i++) {
                        Biblioteca b = (Biblioteca) lista.get(i);
                        list_bibliotecas.add(b);
                        Log.i(TAG, "NOMBRE: " + b.getNombre_de_la_biblioteca() + " Municipio: " + b.getMunicipio()
                                + " Departamento: " + b.getDepartamento() + " georeferencia: " + b.getGeoreferencia()
                                + " Telefonos: " + b.getTel_fonos_de_contacto());

                        //procesando lat y long

                        //georef.add(b.getGeoreferencia().substring(b.getGeoreferencia().indexOf("(") + 1,
                          //      b.getGeoreferencia().indexOf(")")));

                        list_latitud.add(Double.parseDouble(b.getLatitud()));
                        list_longitud.add(Double.parseDouble(b.getLongitud()));


                    }
                    Toast.makeText(MapsActivity.this, R.string.txtdcorrectos, Toast.LENGTH_LONG).show();
                    poblar_spinner();
                } else {
                    Log.e(TAG, "OnResponse: " + response.errorBody());
                    Toast.makeText(MapsActivity.this, "nada de datos!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<Biblioteca>> call, Throwable t) {
                Toast.makeText(MapsActivity.this, R.string.txtfalloinicio, Toast.LENGTH_LONG).show();
            }
        });


    }

    public void poblar_spinner() {
        // String text = mySpinner.getSelectedItem().toString();
        for (int i = 0; i < list_bibliotecas.size(); i++) {//creamos la lista string

            lista_nombres.add(list_bibliotecas.get(i).getDepartamento());
        }

        //creamos un HashSet para trabajar con los datos repetidos
        HashSet hs = new HashSet();
        //Lo cargamos con los valores del array, esto hace quite los repetidos
        hs.addAll(lista_nombres);
        //Limpiamos el array
        lista_nombres.clear();
        //Agregamos los valores sin repetir
        lista_nombres.addAll(hs);


        lista_nombres.remove(5);
        lista_nombres.remove(8);
        lista_nombres.remove(12);
        lista_nombres.remove(16);
        lista_nombres.remove(20);
        lista_nombres.remove(26);

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, lista_nombres);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                cargar_dpto();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng sydney = new LatLng(1.20128248,-77.28352909);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(8));

        mi_ubicacion();

    }



    public void agre_mi_ubica(double lat, double lon) {
        LatLng coordenadas = new LatLng(lat, lon);
        CameraUpdate miUbicacion = CameraUpdateFactory.newLatLngZoom(coordenadas, 10);
        if (marcadoru != null) marcadoru.remove();
        marcadoru = mMap.addMarker(new MarkerOptions()
                .position(coordenadas)
                .title("yo")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN)));
        //mMap.animateCamera(miUbicacion);
    }

    private void actualizar_mi_ubicacion(Location location) {
        if (location != null) {
            lati = location.getLatitude();
            longi = location.getLongitude();
            agre_mi_ubica(lati, longi);
        }

    }

    LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            actualizar_mi_ubicacion(location);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };

    private void mi_ubicacion() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Location location = locationManager.getLastKnownLocation(locationManager.GPS_PROVIDER);
        actualizar_mi_ubicacion(location);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,15000,0,locationListener);
    }


    public void cargar_dpto(){

        mMap.clear();
        mi_ubicacion();
        //int cont=list_dpt.size();
        item_spinner=spinner.getSelectedItem().toString();

        for(int i=0;i<list_bibliotecas.size();i++){
            if(list_bibliotecas.get(i).getDepartamento().equals(item_spinner)){
                list_dpt.add(list_bibliotecas.get(i));
            }
            else{list_dpt2.add(list_bibliotecas.get(i));}
        }



        //Toast.makeText(MapsActivity.this,item_spinner+" CUENTA CON "+list_dpt.size()+" BIBLIOTECAS",Toast.LENGTH_LONG).show();
        for(int k=0;k<list_dpt.size();k++){
            lat2=Double.parseDouble(list_dpt.get(k).getLatitud());
            log2=Double.parseDouble(list_dpt.get(k).getLongitud());
            LatLng mardpt = new LatLng(lat2,log2);

            mMap.addMarker(new MarkerOptions().position(mardpt)
            .title(list_dpt.get(k).toString())
            .icon(BitmapDescriptorFactory.fromResource(R.mipmap.marca_libro)));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(mardpt));

            mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener(){
                @Override
                public boolean onMarkerClick(Marker marker) {
                    mMap.clear();
                    cargar_dpto();
                    Toast.makeText(MapsActivity.this,marker.getTitle(),Toast.LENGTH_LONG ).show();
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(marker.getPosition()));
                    ruta(lati,longi,marker.getPosition().latitude,marker.getPosition().longitude);

                    return true;

                }
            });
        }

        list_dpt.clear();
        list_dpt2.clear();



    }

    public void mostrar_todo(View view){
        mMap.clear();

        for(int k=0;k<list_bibliotecas.size();k++){
            LatLng marcas = new LatLng(list_latitud.get(k),list_longitud.get(k));
            mMap.addMarker(new MarkerOptions().position(marcas)
                    .title(list_bibliotecas.get(k).toString())
                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.marca_libro)));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(marcas));
            mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener(){
                @Override
                public boolean onMarkerClick(Marker marker) {
                    Toast.makeText(MapsActivity.this,marker.getTitle(),Toast.LENGTH_LONG ).show();

                    return true;

                }
            });
        }
        mi_ubicacion();
        list_dpt.clear();
    }
//this.showPolylineAndShade(ny1, ny2);
    public void ruta(double la1, double lo1, double la2, double lo2) {
                line = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(la1, lo1),new LatLng(la2,lo2))
                .width(5)
                .color(Color.BLUE)
                .geodesic(true));

    }

    public void vista_satelite(View view){
        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        mMap.getUiSettings().setZoomControlsEnabled(true);
    }
    public void vista_hibrido(View view){
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.getUiSettings().setZoomControlsEnabled(true);
    }
    public void acerca_de(View view){
        Intent i = new Intent(this,acerca_de.class);
        startActivity(i);
    }

}
