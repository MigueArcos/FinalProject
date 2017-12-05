package miguel.example.com.finalProject.Activities;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import miguel.example.com.finalProject.Activities.MainActivity;
import miguel.example.com.finalProject.Fragments.SignInFragment;
import miguel.example.com.finalProject.Fragments.SignUpFragment;
import miguel.example.com.finalProject.R;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private Button switchFragments;
    private Fragment signInFragment;
    private Fragment signUpFragment;
    private SharedPreferences ShPrUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ShPrUser = getSharedPreferences("User", Context.MODE_PRIVATE);
        if (ShPrUser.getString("uid", null) != null){
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
        }
        setContentView(R.layout.activity_login);
        signInFragment = new SignInFragment();
        signUpFragment = new SignUpFragment();
        switchFragments = findViewById(R.id.switch_fragments);
        switchFragments.setOnClickListener(this);
        getSupportActionBar().setTitle(R.string.activity_login_sign_in_label);
        getSupportFragmentManager().beginTransaction().replace(R.id.contenido, signInFragment).commit();
    }


    @Override
    public void onClick(View v) {
        if (getSupportActionBar().getTitle().equals(getString(R.string.activity_login_sign_in_label))) {
            getSupportFragmentManager().beginTransaction().replace(R.id.contenido, signUpFragment).commit();
            getSupportActionBar().setTitle(R.string.activity_login_sign_up_label);
            switchFragments.setText(R.string.activity_login_sign_in_label);
        } else {
            getSupportFragmentManager().beginTransaction().replace(R.id.contenido, signInFragment).commit();
            getSupportActionBar().setTitle(R.string.activity_login_sign_in_label);
            switchFragments.setText(R.string.activity_login_sign_up_label);
        }
    }
}