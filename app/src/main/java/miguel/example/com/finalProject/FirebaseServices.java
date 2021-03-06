package miguel.example.com.finalProject;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import miguel.example.com.finalProject.Models.Routine;
import miguel.example.com.finalProject.Models.User;

/**
 * Created by 79812 on 04/12/2017.
 */

public class FirebaseServices {
    private FirebaseAuth firebaseAuth;
    private static FirebaseServices Instance;
    private DatabaseReference databaseReference;
    private FirebaseDatabase database;
    private final String LOG_TAG = "Firebase services";
    private static SharedPreferences authCache;

    public interface RoutineReadyListener {
        void onRoutineReady(List<Routine> routineList);

        void onRoutineError(String error);
    }
    public interface GamesScoreListener {
        void onScoreReady(User.Score score);
        void onScoreError(String error);
    }

    public FirebaseServices() {
        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();
    }


    public static FirebaseServices getInstance() {
        if (Instance == null) {
            Instance = new FirebaseServices();
        }
        return Instance;
    }

    public static FirebaseServices getInstance(Context context) {
        if (Instance == null) {
            Instance = new FirebaseServices();
        }
        authCache = context.getSharedPreferences("User", Context.MODE_PRIVATE);
        return Instance;
    }

    public void addToRoutine(String activity, String baseActivity) {
        //The .push() method is a way to insert a new element with an unique key like a database
        DatabaseReference userRoutine = databaseReference.child("routine" + authCache.getString("uid", "NoUser")).push();
        userRoutine.setValue(new Routine(activity, MyUtils.getDate(), MyUtils.getTime(), baseActivity));
        userRoutine.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d(LOG_TAG, "Success when adding to routine_icon");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(LOG_TAG, "Error trying to save data");
            }
        });
    }

    public void saveScore(boolean didUserWin) {
        final DatabaseReference userScore = databaseReference.child("users/" + authCache.getString("uid", "NoUser")+"/gamesScore");
        int wonGames, lostGames;
        if (didUserWin) {
            authCache.edit().putInt("wonGames", 1 + authCache.getInt("wonGames", 0)).apply();
        } else {
            authCache.edit().putInt("lostGames", 1 + authCache.getInt("lostGames", 0)).apply();
        }
        wonGames = authCache.getInt("wonGames", 0);
        lostGames = authCache.getInt("lostGames", 0);
        userScore.setValue(new User.Score(wonGames, lostGames));
        userScore.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d(LOG_TAG, "Success saving data");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(LOG_TAG, "Error trying to save data");
            }
        });
    }

    public void getScore(final GamesScoreListener listener) {
        DatabaseReference userScore = databaseReference.child("users/" + authCache.getString("uid", "NoUser")+"/gamesScore");
        userScore.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                listener.onScoreReady(dataSnapshot.getValue(User.Score.class));
                //listener.onRoutineReady(routineList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(LOG_TAG, "The read failed: " + databaseError.getCode());
                listener.onScoreError("The read failed: " + databaseError.getCode());
            }
        });
    }

    public void getRoutine(final RoutineReadyListener listener) {
        DatabaseReference userRoutine = databaseReference.child("routine" + authCache.getString("uid", "NoUser"));

        userRoutine.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Routine> routineList = new ArrayList<>();
                for (DataSnapshot routine : dataSnapshot.getChildren()) {
                    routineList.add(routine.getValue(Routine.class));
                }
                //Log.d(LOG_TAG, "Read success " + routineList.get(0).getActivity());

                listener.onRoutineReady(routineList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(LOG_TAG, "The read failed: " + databaseError.getCode());
                listener.onRoutineError("The read failed: " + databaseError.getCode());
            }
        });
    }
}
