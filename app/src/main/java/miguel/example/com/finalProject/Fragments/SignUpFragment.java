package miguel.example.com.finalProject.Fragments;

/**
 * Created by Migue on 04/07/2017.
 */

import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.regex.Pattern;

import miguel.example.com.finalProject.Activities.MainActivity;
import miguel.example.com.finalProject.R;
import miguel.example.com.finalProject.Models.User;

public class SignUpFragment extends Fragment implements View.OnClickListener, View.OnFocusChangeListener, OnCompleteListener<AuthResult> {

    private final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private TextInputLayout labelEmail, labelPassword, labelConfirmPassword, labelName;
    private EditText email, password, passwordConfirm, name, birthDate;
    private Spinner genre;
    private Button submit;
    private Pattern regexPassword;
    private AlertDialog.Builder message;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private String LOG_TAG = "SignUp fragment";
    private Calendar myCalendar = Calendar.getInstance();
    private SharedPreferences ShPrUser;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ShPrUser = getActivity().getSharedPreferences("User", Context.MODE_PRIVATE);
        View rootView = inflater.inflate(R.layout.fragment_sign_up, container, false);
        labelName = rootView.findViewById(R.id.label_name);
        labelEmail = rootView.findViewById(R.id.label_email);
        labelPassword = rootView.findViewById(R.id.label_password);
        labelConfirmPassword = rootView.findViewById(R.id.label_confirm_password);
        name = rootView.findViewById(R.id.username);
        email = rootView.findViewById(R.id.email);
        password = rootView.findViewById(R.id.password);
        passwordConfirm = rootView.findViewById(R.id.password_confirm);
        genre = rootView.findViewById(R.id.genre);
        birthDate = rootView.findViewById(R.id.birth_date);
        submit = rootView.findViewById(R.id.submit);
        submit.setOnClickListener(this);
        regexPassword = regexPassword.compile("^.{6,}$");
        name.setOnFocusChangeListener(this);
        email.setOnFocusChangeListener(this);
        password.setOnFocusChangeListener(this);
        passwordConfirm.setOnFocusChangeListener(this);
        passwordConfirm.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_DONE ||
                        event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    if (!validatePassword(passwordConfirm.getText())) {
                        labelConfirmPassword.setError(getString(R.string.activity_login_incorrect_password));
                    } else {
                        labelConfirmPassword.setError(null);
                        labelConfirmPassword.setErrorEnabled(false);
                    }

                }
                return false; // pass on to other listeners.
            }
        });
        message = new AlertDialog.Builder(getActivity());
        message.setTitle(R.string.dialog_default_title);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);
        progressDialog.setTitle(R.string.dialog_default_title);
        message = new AlertDialog.Builder(getActivity()).setTitle(R.string.dialog_default_title).setCancelable(true);
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = database.getReference().child("users");
        birthDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });
        updateLabel();
        return rootView;
    }

    boolean validateName() {
        return name.getText().length() != 0;
    }

    boolean validateEmail() {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email.getText()).matches();
    }

    boolean validatePassword(CharSequence text) {
        return regexPassword.matcher(text).matches();
    }

    @Override
    public void onClick(View v) {
        if (validateEmail() && validatePassword(password.getText()) && validateName() && validatePassword(passwordConfirm.getText())) {
            if (!(passwordConfirm.getText().toString().equals(password.getText().toString()))) {
                message.setMessage(getString(R.string.fragment_sign_up_mismatch_passwords));
                message.show();
                return;
            }
            startSignUp();
        } else {
            Toast.makeText(getActivity(), R.string.activity_login_data_error, Toast.LENGTH_SHORT).show();
        }

    }

    void startSignUp() {
        progressDialog.setMessage(getString(R.string.fragment_sign_up_progress_dialog_label));
        progressDialog.show();
        firebaseAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                .addOnCompleteListener(getActivity(), this);
    }


    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (!hasFocus) {
            switch (v.getId()) {
                case R.id.username:
                    if (!validateName()) {
                        labelName.setError(getString(R.string.fragment_sign_up_incorrect_username));
                    } else {
                        labelName.setError(null);
                        labelName.setErrorEnabled(false);
                    }
                    break;
                case R.id.email:
                    if (!validateEmail()) {
                        labelEmail.setError(getString(R.string.activity_login_incorrect_email));
                    } else {
                        labelEmail.setError(null);
                        labelEmail.setErrorEnabled(false);
                    }
                    break;
                case R.id.password:
                    if (!validatePassword(password.getText())) {
                        labelPassword.setError(getString(R.string.activity_login_incorrect_password));
                    } else {
                        labelPassword.setError(null);
                        labelPassword.setErrorEnabled(false);
                    }
                    break;
                case R.id.password_confirm:
                    if (!validatePassword(passwordConfirm.getText())) {
                        labelConfirmPassword.setError(getString(R.string.activity_login_incorrect_password));
                    } else {
                        labelConfirmPassword.setError(null);
                        labelConfirmPassword.setErrorEnabled(false);
                    }
                    break;
            }
        }
    }

    @Override
    public void onComplete(@NonNull final Task<AuthResult> task) {
        if (task.isSuccessful()) {
            // Sign in success, update UI with the signed-in user's information
            Log.d(LOG_TAG, "createUserWithEmail:success");
            DatabaseReference userReference = databaseReference.child(task.getResult().getUser().getUid());
            final String _birthDate = birthDate.getText().toString(), _name = name.getText().toString(), _email = email.getText().toString(), _genre = genre.getSelectedItem().toString();
            userReference.setValue(new User(_birthDate, _name, _email, _genre));
            userReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Log.d(LOG_TAG, "Data correctly saved");
                    progressDialog.dismiss();
                    ShPrUser.edit().putString("birthDate", _birthDate).putString("name", _name).putString("email", _email).putString("genre",_genre).putString("uid", task.getResult().getUser().getUid()).apply();
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    getActivity().startActivity(intent);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    progressDialog.dismiss();
                    Log.d(LOG_TAG, "Error trying to save data");
                }
            });
        } else {
            // If sign in fails, display a message to the user.
            progressDialog.dismiss();
            Log.w(LOG_TAG, "createUserWithEmail:failure", task.getException());
            Toast.makeText(getActivity(), "Authentication failed.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void showDatePicker() {
        new DatePickerDialog(getActivity(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        myCalendar.set(Calendar.YEAR, year);
                        myCalendar.set(Calendar.MONTH, month);
                        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        updateLabel();
                    }
                },
                myCalendar.get(Calendar.YEAR),
                myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void updateLabel() {
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        birthDate.setText(sdf.format(myCalendar.getTime()));
    }

}

