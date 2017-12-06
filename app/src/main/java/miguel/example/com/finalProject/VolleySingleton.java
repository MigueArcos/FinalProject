package miguel.example.com.finalProject;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.List;

import miguel.example.com.finalProject.Models.GoogleDirectionsStep;
import miguel.example.com.finalProject.Models.OpenWeather;
import miguel.example.com.finalProject.Models.Place;
import miguel.example.com.finalProject.Models.TvShow;


public class VolleySingleton {
    /*
    Example of Google Maps Places API:
    https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=-33.8670,151.1957&radius=500&types=food&name=cruise&key=AIzaSyDzQGWnaKRf5oEKUVWCyZKCgq_X2NaU7ko
     */

    /*
    Example of Google Maps Directions API:
    https://maps.googleapis.com/maps/api/directions/json?origin=75+9th+Ave+New+York,+NY&destination=MetLife+Stadium+1+MetLife+Stadium+Dr+East+Rutherford,+NJ+07073&key=YOUR_API_KEY
     */

    /*
    Example of OpenWeather API result
    http://api.openweathermap.org/data/2.5/weather?APPID=b992973a7cb4d9dc181ab38373cc5e1c&units=metric&lang=es&lat=35&lon=139
     */
    private static VolleySingleton volleyInstance;
    private RequestQueue requestQueue;
    private static Context AppContext;
    private static final String GOOGLE_PLACES_URL = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?radius=1500&key=";
    private static final String GOOGLE_PLACES_KEY = "AIzaSyDzQGWnaKRf5oEKUVWCyZKCgq_X2NaU7ko";
    private static final String GOOGLE_DIRECTIONS_URL = "https://maps.googleapis.com/maps/api/directions/json?key=";
    private static final String GOOGLE_DIRECTIONS_KEY = "AIzaSyA0AY22NCjty0gkFi9xSHz-LE_iHtoAyrg";
    private static final String OPEN_WEATHER_API_KEY = "b992973a7cb4d9dc181ab38373cc5e1c";
    private static final String OPEN_WEATHER_API_URL = "http://api.openweathermap.org/data/2.5/weather?units=metric&lang=es&";
    private static final String TVMAZE_API_URL = "http://api.tvmaze.com/shows";

    private final String LOG_TAG = "Volley Singleton";
    private ImageLoader mImageLoader;

    public interface PlacesListListener{
        void onPlacesListFetched(List<Place> data);
        void onPlacesListError(String error);
    }

    public interface RouteListener{
        void onRouteFetched(List<GoogleDirectionsStep> route);
        void onRouteError(String error);
    }

    public interface WeatherListener{
        void onWeatherReady(OpenWeather weather);
        void onWeatherError(String error);
    }

    public interface TvShowsListener{
        void onTvShowsReady(List<TvShow> tvShows);
        void onTvShowsError(String error);
    }

    private VolleySingleton(Context AppContext) {
        VolleySingleton.AppContext = AppContext;
        requestQueue = getRequestQueue();


        mImageLoader = new ImageLoader(requestQueue,
                new ImageLoader.ImageCache() {
                    private final LruCache<String, Bitmap>
                            cache = new LruCache<String, Bitmap>(20);

                    @Override
                    public Bitmap getBitmap(String url) {
                        return cache.get(url);
                    }

                    @Override
                    public void putBitmap(String url, Bitmap bitmap) {
                        cache.put(url, bitmap);
                    }
                });
    }

    public static synchronized VolleySingleton getInstance(Context context) {
        if (volleyInstance == null) {
            volleyInstance = new VolleySingleton(context.getApplicationContext());
        }
        return volleyInstance;
    }

    public static synchronized VolleySingleton getInstance(){
        return volleyInstance;
    }
    private RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            requestQueue = Volley.newRequestQueue(AppContext);
        }
        return requestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

    public void getPlacesList(final double myLat, final double myLng,  final PlacesListListener listener){
        String URL = GOOGLE_PLACES_URL + GOOGLE_PLACES_KEY +"&location="+myLat+","+myLng;
        Log.d(LOG_TAG, URL);
        Request MyRequest = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {
                        Gson gson = new Gson();
                        JSONArray results = null;
                        try {
                            results = new JSONObject(response).getJSONArray("results");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        List<Place> places = gson.fromJson(results.toString(), new TypeToken<List<Place>>() {
                        }.getType());
                        listener.onPlacesListFetched(places);
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        listener.onPlacesListError(getVolleyError(error));
                    }
                }
        );

        addToRequestQueue(MyRequest);
    }

    public void getTvShowsList(final TvShowsListener listener){
        String URL = TVMAZE_API_URL;
        Log.d(LOG_TAG, URL);
        Request MyRequest = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {
                        Gson gson = new Gson();
                        List<TvShow> tvShows = gson.fromJson(response, new TypeToken<List<TvShow>>() {
                        }.getType());
                        listener.onTvShowsReady(tvShows);
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        listener.onTvShowsError(getVolleyError(error));
                    }
                }
        );

        addToRequestQueue(MyRequest);
    }


    public void getWeather(final double myLat, final double myLng,  final WeatherListener listener){
        String URL = OPEN_WEATHER_API_URL +"APPID=" +OPEN_WEATHER_API_KEY+"&lat="+myLat+"&lon="+myLng;
        Log.d(LOG_TAG, URL);
        Request MyRequest = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {
                        Gson gson = new Gson();
                        OpenWeather weather = gson.fromJson(response,OpenWeather.class);
                        listener.onWeatherReady(weather);
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        listener.onWeatherError(getVolleyError(error));
                    }
                }
        );
        addToRequestQueue(MyRequest);
    }

    public void getRoute(final double originLat, final double originLng, final double destinationLat, final double destinationLng,  final RouteListener listener){
        String URL = GOOGLE_DIRECTIONS_URL + GOOGLE_DIRECTIONS_KEY +"&origin="+originLat+","+originLng+"&destination="+destinationLat+","+destinationLng;
        Log.d(LOG_TAG, URL);
        Request MyRequest = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {
                        Gson gson = new Gson();
                        JSONArray steps = null;
                        try {
                            steps = new JSONObject(response).getJSONArray("routes").getJSONObject(0).getJSONArray("legs").getJSONObject(0).getJSONArray("steps");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        List<GoogleDirectionsStep> route = gson.fromJson(steps.toString(), new TypeToken<List<GoogleDirectionsStep>>() {
                        }.getType());
                        Log.d(LOG_TAG, steps.toString() + "\n"+ route.size());
                        listener.onRouteFetched(route);
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        listener.onRouteError(getVolleyError(error));
                    }
                }
        );

        addToRequestQueue(MyRequest);
    }

    public ImageLoader getImageLoader() {
        return mImageLoader;
    }

    private String getVolleyError(VolleyError error){
        String message = "Unknown error";
        if (error instanceof NetworkError) {
            message = "Cannot connect to Internet...Please check your connection!";
        } else if (error instanceof ServerError) {
            message = "The server could not be found. Please try again after some time!!";
        } else if (error instanceof AuthFailureError) {
            message = "Cannot connect to Internet...Please check your connection!";
        } else if (error instanceof ParseError) {
            message = "Parsing error! Please try again after some time!!";
        } else if (error instanceof NoConnectionError) {
            message = "Cannot connect to Internet...Please check your connection!";
        } else if (error instanceof TimeoutError) {
            message = "Connection TimeOut! Please check your internet connection.";
        } else{
            message="Error desconocido";
        }
        if (error.networkResponse!=null){
            try {
                message+="\n"+new String(error.networkResponse.data,"utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return message;
    }
}