package miguel.example.com.finalProject.Fragments;

/**
 * Created by 79812 on 02/12/2017.
 */

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import miguel.example.com.finalProject.Activities.RoutesActivity;
import miguel.example.com.finalProject.Adapters.PlacesListAdapter;
import miguel.example.com.finalProject.Adapters.RoutineListAdapter;
import miguel.example.com.finalProject.FirebaseServices;
import miguel.example.com.finalProject.Models.Place;
import miguel.example.com.finalProject.R;
import miguel.example.com.finalProject.VolleySingleton;

import static miguel.example.com.finalProject.Activities.MainActivity.LOCATION_PERMISSION;


/**
 * Created by Miguel on 19/07/2017.
 */
public class PlacesListFragment extends Fragment implements PlacesListAdapter.AdapterActions, VolleySingleton.PlacesListListener {
    private RecyclerView list;
    private PlacesListAdapter adapter;
    private List<Place> data;
    private SwipeRefreshLayout refreshLayout;
    private LocationManager locationManager;
    private android.location.LocationListener myLocationListener;
    private Location location;
    private double latitude = 0, longitude = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_places_list, container, false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION);
            }
        }
        list = rootView.findViewById(R.id.list);
        data = new ArrayList<>();
        adapter = new PlacesListAdapter(data, this);
        LinearLayoutManager llm = new LinearLayoutManager(this.getActivity());
        list.setLayoutManager(llm);
        list.setAdapter(adapter);
        refreshLayout = rootView.findViewById(R.id.swipeRefresh);
        checkLocation();
        refreshLayout.setRefreshing(true);
        VolleySingleton.getInstance(getActivity()).getPlacesList(latitude, longitude, this);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                checkLocation();
                VolleySingleton.getInstance(getActivity()).getPlacesList(latitude, longitude, PlacesListFragment.this);
            }
        });
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //inflater.inflate(R.menu.menu_notes, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
    }


    @Override
    public void onClick(int position) {
        Intent i = new Intent(getActivity(), RoutesActivity.class);
        i.putExtra("placeLat", data.get(position).getGeometry().getLocation().getLat());
        i.putExtra("placeLng", data.get(position).getGeometry().getLocation().getLng());
        i.putExtra("deviceLat", latitude);
        i.putExtra("deviceLng", longitude);
        i.putExtra("name", data.get(position).getName());
        i.putExtra("address", data.get(position).getVicinity());
        startActivity(i);
    }

    @Override
    public void onSwipe(int position) {

    }

    @Override
    public void onPlacesListFetched(List<Place> data) {
        this.data = data;
        adapter.setData(data);
        refreshLayout.setRefreshing(false);
    }

    @Override
    public void onPlacesListError(String error) {
        Log.d("ERR", error);
    }


    /*
    public void checkLocation() {

        String serviceString = Context.LOCATION_SERVICE;
        locationManager = (LocationManager) getActivity().getSystemService(serviceString);


        if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }


        myLocationListener = new android.location.LocationListener() {
            public void onLocationChanged(Location locationListener) {

                if (isGPSEnabled(getActivity())) {
                    if (locationListener != null) {
                        if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            return;
                        }

                        if (locationManager != null) {
                            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (location != null) {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                                Log.d("placesfragment", "" + latitude);
                            }
                        }
                    }
                } else if (isInternetConnected(getActivity())) {
                    if (locationManager != null) {
                        location = locationManager
                                .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                            Log.d("placesfragment", "" + latitude);
                        }
                    }
                }


            }

            public void onProviderDisabled(String provider) {

            }

            public void onProviderEnabled(String provider) {

            }

            public void onStatusChanged(String provider, int status, Bundle extras) {

            }
        };

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, myLocationListener);
    }
    */
    public void checkLocation() {
        if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (locationManager != null) {
                location = locationManager
                        .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                if (location != null) {
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                }
            }
        }
    }

    public boolean isGPSEnabled(Context mContext) {
        LocationManager locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    public static boolean isInternetConnected(Context ctx) {
        ConnectivityManager connectivityMgr = (ConnectivityManager) ctx
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifi = connectivityMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobile = connectivityMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        // Check if wifi or mobile network is available or not. If any of them is
        // available or connected then it will return true, otherwise false;
        if (wifi != null) {
            if (wifi.isConnected()) {
                return true;
            }
        }
        if (mobile != null) {
            if (mobile.isConnected()) {
                return true;
            }
        }
        return false;
    }
}
