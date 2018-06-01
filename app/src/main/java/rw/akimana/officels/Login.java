package rw.akimana.officels;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import rw.akimana.officels.AppRequest.LoginRequest;
import rw.akimana.officels.Controllers.SessionManager;

public class Login extends AppCompatActivity {
    private TextView tvReg;
    private EditText etUname,etPass;
    private Button btnLogin;
    LoginRequest loginRequest;
    SessionManager sessionManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        tvReg = (TextView)findViewById(R.id.tv_register);
        etUname = (EditText)findViewById(R.id.et_username);
        etPass = (EditText)findViewById(R.id.et_password);
        btnLogin = (Button)findViewById(R.id.btn_login);

        sessionManager = new SessionManager(getApplicationContext());

//        loginBT= new LoginBT(getApplicationContext());

        tvReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Register.class));
            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uname = etUname.getText().toString();
                String upass = etPass.getText().toString();
                loginRequest = new LoginRequest(getApplicationContext(), uname, upass);
                loginRequest.Login();
//                new LoginBT(getApplicationContext(), uname, upass).execute();
//                if(TextUtils.isEmpty(uname)&&TextUtils.isEmpty(upass)){
//                    Toast.makeText(getApplicationContext(),"Type your credentials",Toast.LENGTH_LONG).show();
//                }
//                else loginBT.execute(uname, upass);
            }
        });
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
            case R.id.action_view_marks:
                startActivity(new Intent(getApplicationContext(), MarksActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
