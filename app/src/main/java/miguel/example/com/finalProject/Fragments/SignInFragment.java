package miguel.example.com.finalProject.Fragments;
/**
 * Created by Migue on 04/07/2017.
 */

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Pattern;

import miguel.example.com.finalProject.Activities.MainActivity;
import miguel.example.com.finalProject.FirebaseServices;
import miguel.example.com.finalProject.Models.Score;
import miguel.example.com.finalProject.R;
import miguel.example.com.finalProject.Models.User;

public class SignInFragment extends Fragment implements View.OnClickListener, View.OnFocusChangeListener, OnCompleteListener<AuthResult> {

    private TextInputLayout labelEmail, labelPassword;
    private EditText email, password;
    private Button submit;
    private Pattern regexPassword;
    private AlertDialog.Builder message;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    private final String LOG_TAG = "Login fragment";
    private SharedPreferences ShPrUser;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        ShPrUser = getActivity().getSharedPreferences("User", Context.MODE_PRIVATE);
        View rootView = inflater.inflate(R.layout.fragment_sign_in, container, false);
        labelEmail = rootView.findViewById(R.id.label_email);
        labelPassword = rootView.findViewById(R.id.label_password);
        email = rootView.findViewById(R.id.email);
        password = rootView.findViewById(R.id.password);
        submit = rootView.findViewById(R.id.submit);
        submit.setOnClickListener(this);
        regexPassword = regexPassword.compile("^.{4,}$");
        email.setOnFocusChangeListener(this);
        password.setOnFocusChangeListener(this);
        password.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_DONE ||
                        event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {

                    if (!validatePassword()) {
                        labelPassword.setError(getString(R.string.activity_login_incorrect_password));
                    } else {
                        labelPassword.setError(null);
                        labelPassword.setErrorEnabled(false);
                    }

                }
                return false; // pass on to other listeners.
            }
        });
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);
        progressDialog.setTitle(R.string.dialog_default_title);
        message = new AlertDialog.Builder(getActivity()).setTitle(R.string.dialog_default_title).setCancelable(true);
        firebaseAuth = FirebaseAuth.getInstance();

        return rootView;
    }

    boolean validateEmail() {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email.getText()).matches();
    }

    boolean validatePassword() {
        return regexPassword.matcher(password.getText()).matches();
    }

    @Override
    public void onClick(View v) {

        if (validateEmail() && validatePassword()) {
            StartLogin();
        } else {
            Toast.makeText(getActivity(), R.string.activity_login_data_error, Toast.LENGTH_SHORT).show();
        }

    }

    void StartLogin() {
        progressDialog.setMessage(getString(R.string.fragment_login_progress_dialog_label));
        progressDialog.show();
        firebaseAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                .addOnCompleteListener(getActivity(), this);
    }


    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (!hasFocus) {
            switch (v.getId()) {
                case R.id.email:
                    if (!validateEmail()) {
                        labelEmail.setError(getString(R.string.activity_login_incorrect_email));
                    } else {
                        labelEmail.setError(null);
                        labelEmail.setErrorEnabled(false);
                    }
                    break;
                case R.id.password:
                    if (!validatePassword()) {
                        labelPassword.setError(getString(R.string.activity_login_incorrect_password));
                    } else {
                        labelPassword.setError(null);
                        labelPassword.setErrorEnabled(false);
                    }
                    break;
            }
        }
    }


    @Override
    public void onComplete(@NonNull final Task<AuthResult> task) {
        if (task.isSuccessful()) {
            // Sign in success, update UI with the signed-in user's information
            Log.d(LOG_TAG, "signInWithEmail:success");
            DatabaseReference userReference = FirebaseDatabase.getInstance().getReference("users").child(task.getResult().getUser().getUid());
            userReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    User user = dataSnapshot.getValue(User.class);
                    System.out.println(user);
                    progressDialog.dismiss();
                    ShPrUser.edit().putString("birthDate", user.getBirthDate()).putString("name", user.getName()).putString("email", user.getEmail()).putString("genre", user.getGenre()).putString("uid", task.getResult().getUser().getUid()).apply();

                    FirebaseServices.getInstance(getActivity()).getScore("GamesScore", new FirebaseServices.GamesScoreListener() {
                        @Override
                        public void onScoreReady(Score score) {
                            if (score != null){
                                ShPrUser.edit().putInt("wonGames", score.getWonGames()).apply();
                                ShPrUser.edit().putInt("lostGames", score.getLostGames()).apply();
                            }
                            Intent intent = new Intent(getActivity(), MainActivity.class);
                            getActivity().startActivity(intent);
                        }

                        @Override
                        public void onError(String error) {
                            System.out.println("The read failed: " + error);
                        }
                    });

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    progressDialog.dismiss();
                    System.out.println("The read failed: " + databaseError.getCode());
                }
            });
        } else {
            // If sign in fails, display a message to the user.
            progressDialog.dismiss();
            Log.w(LOG_TAG, "signInWithEmail:failure", task.getException());
            Toast.makeText(getActivity(), "Authentication failed.", Toast.LENGTH_SHORT).show();
        }
    }
}

