package rw.akimana.officels;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import rw.akimana.officels.AppRequest.RegisterRequest;
import rw.akimana.officels.Controllers.SessionManager;

public class Register extends AppCompatActivity {

    EditText etUnames, etUsername, etEmail, etPassword, etPassword2;
    Button btnRegister;
    TextView tvLogin;
    ProgressBar progressBar;
    RegisterRequest registerRequest;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etUnames = (EditText)findViewById(R.id.et_name);
        etUsername = (EditText)findViewById(R.id.et_username);
        etPassword = (EditText)findViewById(R.id.et_password);
        etEmail = (EditText)findViewById(R.id.et_email);
        etPassword2 = (EditText)findViewById(R.id.et_password2);

        btnRegister = (Button)findViewById(R.id.btn_register);
        progressBar = (ProgressBar)findViewById(R.id.progressbar);

        tvLogin = (TextView)findViewById(R.id.tv_register);
        sessionManager = new SessionManager(getApplicationContext());

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            String names = etUnames.getText().toString().trim();
            String username = etUsername.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            String password2 = etPassword2.getText().toString().trim();

            if(names.isEmpty())
                Toast.makeText(getApplicationContext(),"Type your names!", Toast.LENGTH_LONG).show();
            else if(username.isEmpty())
                Toast.makeText(getApplicationContext(),"Type your username!", Toast.LENGTH_LONG).show();
            else if(email.isEmpty())
                Toast.makeText(getApplicationContext(),"Type your email!", Toast.LENGTH_LONG).show();
            else if(password.isEmpty())
                Toast.makeText(getApplicationContext(),"Type password!", Toast.LENGTH_LONG).show();
            else if(password2.isEmpty())
                Toast.makeText(getApplicationContext(),"Retype password!", Toast.LENGTH_LONG).show();
            else if(password==password2)
                Toast.makeText(getApplicationContext(),"Password dont match!", Toast.LENGTH_LONG).show();
            else{
                registerRequest = new RegisterRequest(getApplicationContext(),names,username,email,password);
                registerRequest.register();
            }
            }
        });
    }

    public void onLogin(View view) {
        startActivity(new Intent(getApplicationContext(), Login.class));
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.action_logout:
                sessionManager.logoutUser();
                break;
            case R.id.action_setting:
                startActivity(new Intent(getApplicationContext(),SettingActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
