package miguel.example.com.finalProject.Fragments;

/**
 * Created by 79812 on 02/12/2017.
 */

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
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

import miguel.example.com.finalProject.Adapters.PlacesByWeatherListAdapter;
import miguel.example.com.finalProject.Models.OpenWeather;
import miguel.example.com.finalProject.Models.Place;
import miguel.example.com.finalProject.R;
import miguel.example.com.finalProject.Activities.RoutesActivity;
import miguel.example.com.finalProject.VolleySingleton;

import static miguel.example.com.finalProject.Activities.MainActivity.LOCATION_PERMISSION;


/**
 * Created by Miguel on 19/07/2017.
 */
public class PlacesByWeatherListFragment extends Fragment implements PlacesByWeatherListAdapter.AdapterActions, VolleySingleton.WeatherListener, VolleySingleton.PlacesListListener {
    private RecyclerView list;
    private PlacesByWeatherListAdapter adapter;
    private List<Place> data;
    private SwipeRefreshLayout refreshLayout;
    private LocationManager locationManager;
    private Location location;
    private double latitude = 0, longitude = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_places_by_weather, container, false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION);
            }
        }
        list = rootView.findViewById(R.id.list);
        data = new ArrayList<>();
        adapter = new PlacesByWeatherListAdapter(data, this);
        LinearLayoutManager llm = new LinearLayoutManager(this.getActivity());
        list.setLayoutManager(llm);
        list.setAdapter(adapter);
        refreshLayout = rootView.findViewById(R.id.swipeRefresh);
        checkLocation();
        refreshLayout.setRefreshing(true);
        VolleySingleton.getInstance(getActivity()).getWeather(latitude, longitude, this);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                checkLocation();
                VolleySingleton.getInstance(getActivity()).getWeather(latitude, longitude, PlacesByWeatherListFragment.this);
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
    public void onPlacesListFetched(List<Place> data) {
        this.data = data;
        adapter.setData(data);
        refreshLayout.setRefreshing(false);
    }

    @Override
    public void onPlacesListError(String error) {
        Log.d("ERR", error);
    }


    public void checkLocation(){
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

    @Override
    public void onWeatherReady(OpenWeather weather) {
        adapter.setWeather(weather);
        VolleySingleton.getInstance(getActivity()).getPlacesList(latitude, longitude, PlacesByWeatherListFragment.this);
    }

    @Override
    public void onWeatherError(String error) {
        Log.d("WeatherError", error);
    }
}

