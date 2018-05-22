package rw.akimana.officels.AppRequest;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import rw.akimana.officels.Controllers.AppSingleton;
import rw.akimana.officels.Controllers.DatabaseHelper;
import rw.akimana.officels.Controllers.SessionManager;
import rw.akimana.officels.Login;
import rw.akimana.officels.MainActivity;
import rw.akimana.officels.Models.IpAddress;

public class RegisterRequest {
    private Context mCxt;
    private String names, username, email, password;

    private static final String TAG = "RegisterActivity";
//    private static final String URL_FOR_REGISTER = "http://192.168.0.122/officels/apis/userctrl.php?register";
    ProgressBar progressBar;
    SessionManager sessionManager;

    private String dataUrl;

    private HashMap<String, String> hashMap;

    private DatabaseHelper helper;
    public RegisterRequest(Context context, final String names, final String username, final String email, final String password){
        this.mCxt = context;
        this.names = names;
        this.username = username;
        this.email = email;
        this.password = password;

        progressBar = new ProgressBar(context);

        this.helper = new DatabaseHelper(context);

        sessionManager = new SessionManager(context);
    }
    public void register(){
        String cancel_req_tag = "login";
        //Create register url
        hashMap = helper.findIpDetails();
        if(hashMap == null) dataUrl = "http://192.168.0.122/officels/apis/userctrl.php?register";
        else{
            String id = hashMap.get(IpAddress.IpAttributes.COL_ID);
            String protocal = hashMap.get(IpAddress.IpAttributes.COL_PROTOCAL);
            String ipAddress = hashMap.get(IpAddress.IpAttributes.COL_IPADDRESS);
            dataUrl = protocal + "://" + ipAddress + "/officels/apis/userctrl.php?register";
        }
        showProgress();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                dataUrl, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Register Response: " + response.toString());
                hideProgress();
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    if (!error) {
                        JSONObject user = jObj.getJSONObject("user");
                        String userNames = user.getString("name");

//                         Launch User activity
                        Intent intent = new Intent(mCxt,Login.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        mCxt.startActivity(intent);
//                        finish();
                        Toast.makeText(mCxt, "Thanks "+userNames+"!", Toast.LENGTH_LONG).show();
                    } else {
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(mCxt,
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Register Error: " + error.getMessage());
                Toast.makeText(mCxt, error.getMessage(), Toast.LENGTH_LONG).show();
                hideProgress();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("name", names);
                params.put("email", email);
                params.put("password", password);
                params.put("username", username);
                return params;
            }

        };
        // Adding request to request queue
        AppSingleton.getInstance(mCxt).addToRequestQueue(strReq,cancel_req_tag);
    }
    public void showProgress(){
        progressBar.setVisibility(View.VISIBLE);
    }
    public void hideProgress(){
        progressBar.setVisibility(View.INVISIBLE);
    }
}
