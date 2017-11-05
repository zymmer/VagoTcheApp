package br.com.vagotche.vagotcheapp;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener,
        View.OnClickListener{

    //Variaveis
    int cdUsuario;
    private GoogleMap mMap;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    LocationRequest mLocationRequest;
    Spinner mySpinner, spinner2;
    JSONArray ja;
    ArrayAdapter <String> adapter;
    String tipoArray, snippet, tipoVaga, parquimetro;
    TextView nomeParquimetro, quatidadeVagas, quatidadeVagasDisponiveis, porcentagemOcupacao;
    Button btnReservar;

    private void alert(String s){
        Toast.makeText(this,s,Toast.LENGTH_LONG).show();
    }

    static final String TAG = "VagoTchê";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        //Teste Spinner
        mySpinner = (Spinner) findViewById(R.id.spinner1);
        mySpinner.setAdapter(new MyAdapterFiltroVagas(this, R.layout.rowfiltrovagas, getAllList()));

        cdUsuario = getIntent().getIntExtra("id_usuario", 0);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        }
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        //Inicializa com parametros de vagas comuns
        tipoArray = "parquimetrosArray";
        snippet = "Vagas comuns ocupadas ";
        tipoVaga = "nmVagasNormais";

        nomeParquimetro = (TextView) findViewById(R.id.txtNomeParquimetro);
        quatidadeVagas = (TextView) findViewById(R.id.txtQuatidadeVagasTotais);
        quatidadeVagasDisponiveis = (TextView) findViewById(R.id.txtQuatidadeVagasDisponiveis);
        porcentagemOcupacao = (TextView) findViewById(R.id.txtPorcentagemOcupacao);
        btnReservar = (Button) findViewById(R.id.btnReservar);

        // Button listeners
        btnReservar.setOnClickListener(this);
    }

    /**GoogleMapOptions options = new GoogleMapOptions().liteMode(true);
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
        //GoogleMapOptions options = new GoogleMapOptions().liteMode(true);
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        //Desligar Toolbar
        mMap.getUiSettings().setMapToolbarEnabled(false);
        //Desligar Compasso
        mMap.getUiSettings().setCompassEnabled(false);


        mySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                switch (position) {
                    case 0: //Modo Localizar
                        //alert("Spinner item 1!" + parentView.getSelectedItem().toString());
                        mMap.clear();
                        tipoArray = "parquimetrosArray";
                        snippet = "Vagas comuns ocupadas ";
                        tipoVaga = "nmVagasNormais";
                        //onLocationChanged(mLastLocation);
                        calcParquimetrosMaisProximos();
                        break;
                    case 1: //Idosos
                        //alert("Spinner item 3!" + parentView.getSelectedItem().toString());
                        mMap.clear();
                        tipoArray = "parquimetrosIdososArray";
                        snippet = "Vagas idosos ocupadas ";
                        tipoVaga = "nmVagasIdosos";
                        //addParquimetrosIdososArray();
                        calcParquimetrosMaisProximos();
                        break;
                    case 2: //DF
                        //alert("Spinner item 4!" + parentView.getSelectedItem().toString());
                        mMap.clear();
                        tipoArray = "parquimetrosDFArray";
                        snippet = "Vagas DF ocupadas ";
                        tipoVaga = "nmVagasDF";
                        //addParquimetrosIdososArray();
                        calcParquimetrosMaisProximos();
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
                tipoArray = "parquimetrosArray";
                snippet = "Vagas comuns ocupadas ";
                tipoVaga = "nmVagasNormais";
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

        //Testes
//        location.setLatitude(-30.038590);
//        location.setLongitude(-51.229572);

        //Place current location marker
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Você");
        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.car32));
        mCurrLocationMarker = mMap.addMarker(markerOptions);

        //move map camera
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(17));

        //stop location updates
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }

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
            ArrayList<ListParquimetrosMaisProximos> parquimetrosMaisProximosArray = new ArrayList<ListParquimetrosMaisProximos>();

            ja = new JSONArray(getIntent().getStringExtra(tipoArray));
            float results[] = new float[10];
            float resultsMaisProximos[] = new float[10];
            int vagasOcupadas = 5;
            //Porcentagens de ocupacao
            double menos30Perc = 0.29;
            double memos50Perc = 0.49;
            double menos80Perc = 0.79;
            double CemPerc = 1.00;
            //end
            int indiceCount = 0; //Flag para pegar o parquimetro mais proximo
            String ocupacao = "";


            //if (ja != null || ja.length() != 0) {

                for (int i = 0; i < ja.length(); i++) {
                    JSONObject jo = ja.getJSONObject(i);

                    Location.distanceBetween(mLastLocation.getLatitude(), mLastLocation.getLongitude(),
                            Float.parseFloat(jo.getString("Latitude")), Float.parseFloat(jo.getString("Longitude")), results);

                    float radius = 2000;// Medida em metros

                    if (results[0] < radius) {

                        indiceCount++;

                        LatLng latLng = new LatLng(Double.parseDouble(jo.getString("Latitude")), Double.parseDouble(jo.getString("Longitude")));
                        Marker parquimetro = mMap.addMarker(new MarkerOptions()
                                .position(latLng)
                                .title(jo.getString("cdEndereco"))
                                .snippet(snippet + vagasOcupadas + "/" + jo.getString(tipoVaga)));


                        float tipoIcon = 0;
                        int colorText = 0;

                        if (vagasOcupadas > Integer.parseInt(jo.getString(tipoVaga))){
                            vagasOcupadas = 1;
                        }

                        if (Double.parseDouble(jo.getString(tipoVaga)) == 0) {
                            ocupacao = "Parquímetro em Manutenção";
                            parquimetro.setSnippet("Parquímetro em Manutenção");
                            parquimetro.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
                            tipoIcon = 210.0F;
                            colorText = 0xFF00FFFF;
                        } else if ((vagasOcupadas / Double.parseDouble(jo.getString(tipoVaga))) <= menos30Perc) {
                            porcentagemOcupacao.setTextColor(Color.GREEN);
                            quatidadeVagasDisponiveis.setTextColor(Color.GREEN);
                            ocupacao = "Menos de 30% ocupado";
                            parquimetro.setSnippet(snippet + vagasOcupadas + "/" + jo.getString(tipoVaga) + " - " + ocupacao);
                            parquimetro.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                            tipoIcon = 120.0F;
                            colorText = 0xFF00FF00;
                        } else if ((vagasOcupadas / Double.parseDouble(jo.getString(tipoVaga))) <= memos50Perc) {
                            porcentagemOcupacao.setTextColor(Color.YELLOW);
                            quatidadeVagasDisponiveis.setTextColor(Color.YELLOW);
                            ocupacao = "Menos de 50% ocupado";
                            parquimetro.setSnippet(snippet + vagasOcupadas + "/" + jo.getString(tipoVaga) + " - " + ocupacao);
                            parquimetro.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
                            tipoIcon = 60.0F;
                            colorText = 0xFFFFFF00;
                        } else if ((vagasOcupadas / Double.parseDouble(jo.getString(tipoVaga))) <= menos80Perc) {
                            porcentagemOcupacao.setTextColor(Color.parseColor("#F06D2F"));
                            quatidadeVagasDisponiveis.setTextColor(Color.parseColor("#F06D2F"));
                            ocupacao = "Menos de 80% ocupado";
                            parquimetro.setSnippet(snippet + vagasOcupadas + "/" + jo.getString(tipoVaga) + " - " + ocupacao);
                            parquimetro.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
                            tipoIcon = 30.0F;
                            //colorText = '#F06D2F';
                        } else if ((vagasOcupadas / Double.parseDouble(jo.getString(tipoVaga))) == CemPerc) {
                            porcentagemOcupacao.setTextColor(Color.RED);
                            quatidadeVagasDisponiveis.setTextColor(Color.RED);
                            ocupacao = "100% ocupado";
                            parquimetro.setSnippet(snippet + vagasOcupadas + "/" + jo.getString(tipoVaga) + " - " + ocupacao);
                            parquimetro.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                            tipoIcon = 0.0F;
                            colorText = 0xFFFF0000;
                        }

                        //Adiciona parquimetros mais proximos no Array
                        resultsMaisProximos[indiceCount] = results[0];
                        ListParquimetrosMaisProximos item;
                        item = new ListParquimetrosMaisProximos();
                        String parq = jo.getString("cdEndereco");
                        item.setData(parq, resultsMaisProximos[indiceCount], jo.getString(tipoVaga), ocupacao, jo.getString("Latitude"),
                                jo.getString("Longitude"), tipoIcon, colorText);
                        parquimetrosMaisProximosArray.add(item);


//                        if (indice == 0) {
//                            porcentagemOcupacao.getCurrentTextColor();
//                            quatidadeVagasDisponiveis.getCurrentTextColor();
//                            nomeParquimetro.setTextColor(Color.WHITE);
//                            nomeParquimetro.setText(parquimetro.getTitle());
//                            quatidadeVagas.setText(jo.getString(tipoVaga));
//                            quatidadeVagasDisponiveis.setText(Integer.toString(vagasOcupadas));
//                            porcentagemOcupacao.setText(ocupacao);
//                            parquimetro.showInfoWindow();
//                            indiceCount = indiceCount + 1;
//
//                            if (Integer.parseInt(quatidadeVagasDisponiveis.getText().toString()) >= 1){
//                                btnReservar.setBackgroundResource(R.drawable.txt_view_border_maps_green);
//                                //btnReservar.setVisibility(View.VISIBLE);
//                            } else {
//                                btnReservar.setBackgroundResource(R.drawable.txt_view_border_maps_red);
//                                //btnReservar.setVisibility(View.INVISIBLE);
//                            }
//
//                        }

                        ListItemFiltroVagas item2;
                        item2 = new ListItemFiltroVagas();
                        String parq2 = jo.getString("cdEndereco");
                        item2.setData(parq2, R.mipmap.vehicle);
                        parquimetrosArray.add(item2);


                    } else if (results[0] > radius && indiceCount == 0) {
                        nomeParquimetro.setTextColor(Color.RED);
                        nomeParquimetro.setText("Não há parquímetros próximo num raio de 2km");
                        quatidadeVagas.setText("");
                        quatidadeVagasDisponiveis.setText("");
                        porcentagemOcupacao.setText("");
                        btnReservar.setBackgroundResource(R.drawable.txt_view_border_maps_red);
                        //btnReservar.setVisibility(View.INVISIBLE);
                    }

                }

            //Arrays.sort(resultsMaisProximos);

            //Ordena os results do menor ao maior
            Collections.sort(parquimetrosMaisProximosArray, new Comparator<ListParquimetrosMaisProximos>() {

                public int compare(ListParquimetrosMaisProximos p1, ListParquimetrosMaisProximos p2) {
                    return (int) ((float) p1.getLatLong()-p2.getLatLong());
                }
            });

            if (indiceCount > 0) {

                LatLng latLng = new LatLng(Double.parseDouble(parquimetrosMaisProximosArray.get(0).getLatitude()),
                                            Double.parseDouble(parquimetrosMaisProximosArray.get(0).getLongitude()));
                Marker parquimetro = mMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title(parquimetrosMaisProximosArray.get(0).getNomeParquimetro())
                        .snippet(snippet + vagasOcupadas + "/" + parquimetrosMaisProximosArray.get(0).getTipoVaga()));

                if (parquimetrosMaisProximosArray.get(0).getColorText() == 0){
                    porcentagemOcupacao.setTextColor(Color.parseColor("#F06D2F"));
                    quatidadeVagasDisponiveis.setTextColor(Color.parseColor("#F06D2F"));
                } else
                            porcentagemOcupacao.setTextColor(parquimetrosMaisProximosArray.get(0).getColorText());
                            quatidadeVagasDisponiveis.setTextColor(parquimetrosMaisProximosArray.get(0).getColorText());
                            nomeParquimetro.setTextColor(Color.WHITE);
                            nomeParquimetro.setText(parquimetrosMaisProximosArray.get(0).getNomeParquimetro());
                            quatidadeVagas.setText(parquimetrosMaisProximosArray.get(0).getTipoVaga());

                            if (parquimetrosMaisProximosArray.get(0).getTipoVaga().equals("0")){
                                quatidadeVagasDisponiveis.setText(Integer.toString(0));
                            } else {
                                quatidadeVagasDisponiveis.setText(Integer.toString(vagasOcupadas));
                            }

                            porcentagemOcupacao.setText(parquimetrosMaisProximosArray.get(0).getOcupacao());
                            parquimetro.showInfoWindow();
                            parquimetro.setIcon(BitmapDescriptorFactory.defaultMarker(parquimetrosMaisProximosArray.get(0).getTipoIcon()));

                            if (Integer.parseInt(quatidadeVagasDisponiveis.getText().toString()) >= 1){
                                btnReservar.setBackgroundResource(R.drawable.txt_view_border_maps_green);
                                //btnReservar.setVisibility(View.VISIBLE);
                            } else {
                                btnReservar.setBackgroundResource(R.drawable.txt_view_border_maps_red);
                                //btnReservar.setVisibility(View.INVISIBLE);
                            }
                

            }

            } catch(JSONException e){
                e.printStackTrace();
            }
    }

    @Override
    public void onClick(View v) {
        // Is the view now checked?
        //boolean checked = ((CheckBox) v).isChecked();

        switch (v.getId()) {
            case R.id.btnReservar:
                Intent it = new Intent(MapsActivity.this, MenuActivity.class);
                it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                it.putExtra("id_usuario", cdUsuario);
                it.putExtra("parquimetro", nomeParquimetro.getText());
                startActivity(it);
                break;
        }
    }

    void complain(String message) {
        Log.e(TAG, "**** Vago Tchê Error: " + message);
        alertDialog(message);
        //alert("Error: " + message);
    }

    void alertDialog(String message) {
        AlertDialog.Builder bld = new AlertDialog.Builder(this);
        bld.setMessage(message);
        bld.setNeutralButton("OK", null);
        Log.d(TAG, "Showing alert dialog: " + message);
        bld.create().show();
    }

    public ArrayList<ListItemFiltroVagas> getAllList() {

        ArrayList<ListItemFiltroVagas> allList = new ArrayList<ListItemFiltroVagas>();

        ListItemFiltroVagas item = new ListItemFiltroVagas();

        item = new ListItemFiltroVagas();
        item.setData("Modo Localizar", R.mipmap.parquimetro_40x40);
        allList.add(item);

//        item = new ListItemFiltroVagas();
//        item.setData("Vagas Comuns", R.mipmap.parquimetro_40x40);
//        allList.add(item);

        item = new ListItemFiltroVagas();
        item.setData("Vagas Idosos", R.mipmap.idoso_40x40);
        allList.add(item);

        item = new ListItemFiltroVagas();
        item.setData("Vagas D.Físicos", R.mipmap.cadeirante_40x40);
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
