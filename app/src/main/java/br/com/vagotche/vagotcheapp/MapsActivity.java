package br.com.vagotche.vagotcheapp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener{

    private GoogleMap mMap;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    LocationRequest mLocationRequest;
    Spinner mySpinner, spinner2;
    JSONArray ja;
    ArrayAdapter <String> adapter;

    private void alert(String s){
        Toast.makeText(this,s,Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        //Teste Spinner
        mySpinner = (Spinner) findViewById(R.id.spinner1);
        mySpinner.setAdapter(new MyAdapterFiltroVagas(this, R.layout.rowfiltrovagas, getAllList()));

        spinner2 = (Spinner) findViewById(R.id.spinner2);


        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        }
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


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
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        //Desligar Toolbar
        mMap.getUiSettings().setMapToolbarEnabled(false);
        //Desligar Compasso
        mMap.getUiSettings().setCompassEnabled(false);
//
//        //Parquimetros
//        addParquimetrosArray();

        mySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                switch (position) {
                    case 0: //Modo Localizar
                        //alert("Spinner item 1!" + parentView.getSelectedItem().toString());
                        mMap.clear();
                        onLocationChanged(mLastLocation);
                        break;
                    case 1: //Comuns
                        //alert("Spinner item 2!" + parentView.getSelectedItem().toString());
                        mMap.clear();
                        addParquimetrosArray();
                        break;
                    case 2: //Idosos
                        //alert("Spinner item 3!" + parentView.getSelectedItem().toString());
                        mMap.clear();
                        addParquimetrosIdososArray();
                        break;
                    case 3: //DF
                        //alert("Spinner item 4!" + parentView.getSelectedItem().toString());
                        mMap.clear();
                        addParquimetrosDFArray();
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
                //addParquimetrosArray();
            }

        });

//        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
//
//                switch (position) {
//                    case 0: //Modo Localizar
//                        //alert("Spinner item 1!" + parentView.getSelectedItem().toString());
//                        mMap.clear();
//                        onLocationChanged(mLastLocation);
//                        break;
//                    case 1: //Comuns
//                        //alert("Spinner item 2!" + parentView.getSelectedItem().toString());
//                        mMap.clear();
//                        addParquimetrosArray();
//                        break;
//                    case 2: //Idosos
//                        //alert("Spinner item 3!" + parentView.getSelectedItem().toString());
//                        mMap.clear();
//                        addParquimetrosIdososArray();
//                        break;
//                    case 3: //DF
//                        //alert("Spinner item 4!" + parentView.getSelectedItem().toString());
//                        mMap.clear();
//                        addParquimetrosDFArray();
//                        break;
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parentView) {
//                // your code here
//                //addParquimetrosArray();
//            }
//
//        });

        //Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            }
        }
        else {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(Bundle bundle) {

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {

        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }

        //Place current location marker
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Posição Atual");
        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.transportnew));
        mCurrLocationMarker = mMap.addMarker(markerOptions);

        //move map camera
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(17));

        //stop location updates
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }

        //calcParquimetrosMaisProximos();
        calcParquimetrosMaisProximos();


    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    public boolean checkLocationPermission(){
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Asking user if explanation is needed
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted. Do the
                    // contacts-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }

                } else {

                    // Permission denied, Disable the functionality that depends on this permission.
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other permissions this app might request./
            // You can add here other case statements according to your requirement.
        }
    }

    //Parquímetros
    public void addParquimetrosArray() {

        try{
            ja = new JSONArray(getIntent().getStringExtra("parquimetrosArray"));

            //for(int i=0; i < ja.length(); i++) {
            for(int i = 0; i < ja.length(); i++) {
                JSONObject jo = ja.getJSONObject(i);

                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.title(jo.getString("cdEndereco"));
                LatLng latLng = new LatLng(Double.parseDouble(jo.getString("Latitude")), Double.parseDouble(jo.getString("Longitude")));
                markerOptions.position(latLng);
                markerOptions.snippet(  "Vagas Comuns ocupadas 0/" + jo.getString("nmVagasNormais") + "\n" +
                        "Vagas DF ocupadas 0/" + jo.getString("nmVagasDeficiente") +"\n" +
                        "Vagas Idosos ocupadas 0/" + jo.getString("nmVagasIdosos"));
                //markerOptions.icon(BitmapDescriptorFactory.defaultMarker(R.mipmap.parquimetro_40x40));
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                mMap.addMarker(markerOptions);

            }

        }

        catch(JSONException e){ e.printStackTrace(); }
    }

    //Parquímetros Idosos
    public void addParquimetrosIdososArray() {

        try{
            ja = new JSONArray(getIntent().getStringExtra("parquimetrosIdososArray"));

            //for(int i=0; i < ja.length(); i++) {
            for(int i = 0; i < ja.length(); i++) {
                JSONObject jo = ja.getJSONObject(i);

                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.title(jo.getString("cdEndereco"));
                LatLng latLng = new LatLng(Double.parseDouble(jo.getString("Latitude")), Double.parseDouble(jo.getString("Longitude")));
                markerOptions.position(latLng);
                markerOptions.snippet(  "Vagas comuns ocupadas 0/" + jo.getString("nmVagasNormais") + "\n" +
                        "Vagas deficientes ocupadas 0/" + jo.getString("nmVagasDeficiente") +"\n" +
                        "Vagas idosos ocupadas 0/" + jo.getString("nmVagasIdosos"));
                //markerOptions.icon(BitmapDescriptorFactory.defaultMarker(R.mipmap.parquimetro_40x40));
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                mMap.addMarker(markerOptions);

            }

        }

        catch(JSONException e){ e.printStackTrace(); }
    }

    //Parquímetros Idosos
    public void addParquimetrosDFArray() {

        try{
            ja = new JSONArray(getIntent().getStringExtra("parquimetrosDFArray"));

            //for(int i=0; i < ja.length(); i++) {
            for(int i = 0; i < ja.length(); i++) {
                JSONObject jo = ja.getJSONObject(i);

                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.title(jo.getString("cdEndereco"));
                LatLng latLng = new LatLng(Double.parseDouble(jo.getString("Latitude")), Double.parseDouble(jo.getString("Longitude")));
                markerOptions.position(latLng);
                markerOptions.snippet(  "Vagas comuns ocupadas 0/" + jo.getString("nmVagasNormais") + "\n" +
                        "Vagas deficientes ocupadas 0/" + jo.getString("nmVagasDeficiente") +"\n" +
                        "Vagas idosos ocupadas 0/" + jo.getString("nmVagasIdosos"));
                //markerOptions.icon(BitmapDescriptorFactory.defaultMarker(R.mipmap.parquimetro_40x40));
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                mMap.addMarker(markerOptions);

            }

        }

        catch(JSONException e){ e.printStackTrace(); }
    }


    //Parquímetros mais proximos

    public void calcParquimetrosMaisProximos() {

        try {

            ArrayList<ListItemFiltroVagas> parquimetrosArray = new ArrayList<ListItemFiltroVagas>();
            ja = new JSONArray(getIntent().getStringExtra("parquimetrosArray"));
            float results[] = new float[10];
            int vagasOcupadas = 5;
            double menos30Perc = 0.29;
            double memos50Perc = 0.49;
            double menos80Perc = 0.79;
            double CemPerc = 1.00;
            int indiceCount = 0;


            for (int i = 0; i < ja.length(); i++) {
                JSONObject jo = ja.getJSONObject(i);

                Location.distanceBetween(mLastLocation.getLatitude(), mLastLocation.getLongitude(),
                        Double.parseDouble(jo.getString("Latitude")), Double.parseDouble(jo.getString("Longitude")), results);

                float radius = 2000;// Medida em metros

                if (results[0] < radius) {

                    Arrays.sort(results);
                    int indice = indiceCount;

//                    alert("Contador: " + i);
//                    alert("Indice: " + indice);
//
//                    //Parquimetro mais proximo
//                    float maisProximo=results[0];
//                    alert("Mais proximo: "+ results[0] + " Parquimetro: " +jo.getString("cdEndereco"));

                    LatLng latLng = new LatLng(Double.parseDouble(jo.getString("Latitude")), Double.parseDouble(jo.getString("Longitude")));
                    Marker parquimetro = mMap.addMarker(new MarkerOptions()
                            .position(latLng)
                            .title(jo.getString("cdEndereco"))
                            .snippet("Vagas comuns ocupadas " + vagasOcupadas + "/" + jo.getString("nmVagasNormais")));

                    if(Integer.valueOf(jo.getString("nmVagasNormais")) != 0) {
                        alert("Calc: " + vagasOcupadas / Double.parseDouble(jo.getString("nmVagasNormais")));
                    }

                    if(Double.parseDouble(jo.getString("nmVagasNormais")) == 0) {
                        parquimetro.setSnippet("Parquímetro em Manutenção");
                        parquimetro.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
                    } else if((vagasOcupadas / Double.parseDouble(jo.getString("nmVagasNormais"))) <= menos30Perc){
                        parquimetro.setSnippet("Vagas comuns ocupadas " + vagasOcupadas + "/" + jo.getString("nmVagasNormais") + " - Menos de 30% ocupado");
                        parquimetro.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                    } else if((vagasOcupadas / Double.parseDouble(jo.getString("nmVagasNormais"))) <= memos50Perc ){
                        parquimetro.setSnippet("Vagas comuns ocupadas " + vagasOcupadas + "/" + jo.getString("nmVagasNormais") + " - Menos de 50% ocupado");
                        parquimetro.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
                    } else if((vagasOcupadas / Double.parseDouble(jo.getString("nmVagasNormais"))) <= menos80Perc){
                        parquimetro.setSnippet("Vagas comuns ocupadas " + vagasOcupadas + "/" + jo.getString("nmVagasNormais") + " - Menos de 80% ocupado");
                        parquimetro.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
                    } else if((vagasOcupadas / Double.parseDouble(jo.getString("nmVagasNormais"))) == CemPerc){
                        parquimetro.setSnippet("Vagas comuns ocupadas " + vagasOcupadas + "/" + jo.getString("nmVagasNormais") + " - 100% ocupado");
                        parquimetro.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                    }

                    if(indice == 0) {
                        parquimetro.showInfoWindow();
                        indiceCount = indiceCount + 1;
                    }

                    ListItemFiltroVagas item;
                    item = new ListItemFiltroVagas();
                    String parq  = jo.getString("cdEndereco");
                    item.setData(parq, R.mipmap.vehicle);
                    parquimetrosArray.add(item);


                }

                spinner2.setAdapter(new MyAdapterFiltroVagas(this, R.layout.rowfiltrovagas, parquimetrosArray));

            }


        } catch (JSONException e) {
            e.printStackTrace();
        }


    }


    public ArrayList<ListItemFiltroVagas> getAllList() {

        ArrayList<ListItemFiltroVagas> allList = new ArrayList<ListItemFiltroVagas>();

        ListItemFiltroVagas item = new ListItemFiltroVagas();

        item = new ListItemFiltroVagas();
        item.setData("Modo Localizar", R.mipmap.vehicle);
        allList.add(item);

        item = new ListItemFiltroVagas();
        item.setData("Vagas Comuns", R.mipmap.parquimetro_40x40);
        allList.add(item);

        item = new ListItemFiltroVagas();
        item.setData("Vagas Idosos", R.mipmap.idoso16);
        allList.add(item);

        item = new ListItemFiltroVagas();
        item.setData("Vagas D.Físicos", R.mipmap.transport);
        allList.add(item);
//
//        item = new ListItemFiltroVagas();
//        item.setData("HTC", "Taiwan", R.drawable.logo_vagotche);
//        allList.add(item);
//
//        for (int i = 0; i < 10000; i++) {
//            item = new ListItemFiltroVagas();
//            item.setData("Google " + i, "USA " + i, R.drawable.logo_vagotche);
//            allList.add(item);
//        }

        return allList;
    }

}
