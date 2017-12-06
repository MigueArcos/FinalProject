package miguel.example.com.finalProject.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import miguel.example.com.finalProject.FirebaseServices;
import miguel.example.com.finalProject.Fragments.PlacesByWeatherListFragment;
import miguel.example.com.finalProject.Fragments.PlacesListFragment;
import miguel.example.com.finalProject.Fragments.RoutineListFragment;
import miguel.example.com.finalProject.Fragments.TvShowsListFragment;
import miguel.example.com.finalProject.MyUtils;
import miguel.example.com.finalProject.R;
import miguel.example.com.finalProject.TicTacToe.TicTacToeFragment;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private NavigationView navigationView;
    private AlertDialog.Builder message;
    private DrawerLayout drawer;
    private ProgressDialog progressDialog;
    private SharedPreferences ShPrUser;
    public static final int LOCATION_PERMISSION = 1;
    private int CurrentFragmentID = 0;
    private RoutineListFragment routineListFragment;
    private PlacesListFragment placesListFragment;
    private PlacesByWeatherListFragment placesByWeatherListFragment;
    private TicTacToeFragment ticTacToeFragment;
    private TvShowsListFragment tvShowsListFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkPermissions();
        }
        ShPrUser = getSharedPreferences("User", Context.MODE_PRIVATE);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setTitle(R.string.dialog_default_title);
        message = new AlertDialog.Builder(this);
        message.setTitle(R.string.dialog_default_title);
        LoadUserData();
        initializeFragments();
        //FirebaseServices.getInstance(this).saveScore("GamesScore", false);
        getSupportFragmentManager().beginTransaction().add(R.id.content_frame, routineListFragment).commit();
    }

    void checkPermissions() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION);
        }
    }

    private void initializeFragments(){
        placesByWeatherListFragment = new PlacesByWeatherListFragment();
        placesListFragment = new PlacesListFragment();
        ticTacToeFragment = new TicTacToeFragment();
        routineListFragment = new RoutineListFragment();
        tvShowsListFragment = new TvShowsListFragment();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case LOCATION_PERMISSION:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // El permiso ha sido otorgado :D

                } else {

                    this.finish();

                }
        }

    }

    private void LoadUserData() {
        View drawerHeader = navigationView.getHeaderView(0);
        TextView name = drawerHeader.findViewById(R.id.name);
        TextView email = drawerHeader.findViewById(R.id.email);
        name.setText(ShPrUser.getString("name", ""));
        email.setText(ShPrUser.getString("email", ""));
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        Fragment fragment = null;
        MyUtils.hideKeyboard(this);
        if (item.getItemId() == CurrentFragmentID) {
            drawer.closeDrawer(GravityCompat.START);
            return false;
        }
        switch (item.getItemId()) {
            case R.id.it1:
                fragment = routineListFragment;
                CurrentFragmentID = item.getItemId();
                break;
            case R.id.it2:
                fragment = placesByWeatherListFragment;
                CurrentFragmentID = item.getItemId();
                break;
            case R.id.it3:
                fragment = placesListFragment;
                CurrentFragmentID = item.getItemId();
                break;
            case R.id.it4:
                fragment = ticTacToeFragment;
                CurrentFragmentID = item.getItemId();
                break;
            case R.id.it5:
                fragment = tvShowsListFragment;
                CurrentFragmentID = item.getItemId();
                break;
            case R.id.about:
                message.setMessage(getString(R.string.about_app));
                message.show();
                return false;
            case R.id.close_session:
                message = new AlertDialog.Builder(this);
                message.setMessage(getString(R.string.main_activity_close_session_confirmation))
                        .setPositiveButton(R.string.positive_button_label, new DialogInterface.OnClickListener() {
                            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                            public void onClick(DialogInterface dialog, int id) {
                                closeSession();
                                getSupportFragmentManager().findFragmentById(R.id.content_frame).onResume();
                                drawer.closeDrawer(GravityCompat.START);
                            }
                        })
                        .setNegativeButton(R.string.negative_button_label, null);
                message.show();
                return false;
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).commit();
        getSupportActionBar().setTitle(item.getTitle());
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void closeSession(){
        ShPrUser.edit().clear().apply();
        Intent i = new Intent(this, LoginActivity.class);
        finishAffinity();
        startActivity(i);
    }

}
