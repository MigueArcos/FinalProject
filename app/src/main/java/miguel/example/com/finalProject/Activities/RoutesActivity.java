package miguel.example.com.finalProject.Activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.List;

import miguel.example.com.finalProject.FirebaseServices;
import miguel.example.com.finalProject.Models.GoogleDirectionsStep;
import miguel.example.com.finalProject.R;
import miguel.example.com.finalProject.VolleySingleton;

public class RoutesActivity extends AppCompatActivity implements OnMapReadyCallback, VolleySingleton.RouteListener {

    private double placeLat, placeLng, deviceLat, deviceLng;
    private GoogleMap googleMap;
    private String placeName, placeAddress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routes);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        deviceLat = getIntent().getDoubleExtra("deviceLat", 0);
        deviceLng = getIntent().getDoubleExtra("deviceLng", 0);
        placeLat = getIntent().getDoubleExtra("placeLat", 0);
        placeLng = getIntent().getDoubleExtra("placeLng", 0);
        placeName = getIntent().getStringExtra("name");
        placeAddress = getIntent().getStringExtra("address");
        FirebaseServices.getInstance(this).addToRoutine("routine", "Visitaste: "+placeName, "Visitar");
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng sydney = new LatLng(placeLat, placeLng);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        googleMap.setMyLocationEnabled(true);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 18));

        googleMap.addMarker(new MarkerOptions()
                .title(placeName)
                .snippet(placeAddress)
                .position(sydney));
        this.googleMap = googleMap;
        VolleySingleton.getInstance(this).getRoute(deviceLat, deviceLng, placeLat, placeLng, this);
    }

    @Override
    public void onRouteFetched(List<GoogleDirectionsStep> route) {
        Log.d("SIZE",""+ route.size());
        for (int i = 0; i<route.size(); i++){
            PolylineOptions polylineOptions = new PolylineOptions().width(8);
            LatLng origin = new LatLng(route.get(i).getStart_location().getLat(), route.get(i).getStart_location().getLng());
            LatLng destination = new LatLng(route.get(i).getEnd_location().getLat(), route.get(i).getEnd_location().getLng());
            polylineOptions.add(origin, destination);
            polylineOptions.color(Color.GREEN);
            googleMap.addPolyline(polylineOptions);
        }
    }

    @Override
    public void onRouteError(String error) {
        Log.d("SIZE",""+ error);
    }
}
