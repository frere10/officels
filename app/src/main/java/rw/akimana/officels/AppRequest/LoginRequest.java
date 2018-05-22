package rw.akimana.officels.AppRequest;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
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
import rw.akimana.officels.MainActivity;
import rw.akimana.officels.Models.IpAddress;

public class LoginRequest {
    private Context mCxt;
    private String uname, upass;

    private static final String TAG = "LoginActivity";
//    private static final String URL_FOR_LOGIN = "http://192.168.0.122/officels/apis/userctrl.php?login";
    private String dataUrl;

    SessionManager sessionManager;

    private HashMap<String, String> hashMap;

    private DatabaseHelper helper;

    public LoginRequest(Context context, final String uname, final String upass){
        this.mCxt = context;
        this.uname = uname;
        this.upass = upass;

        this.helper = new DatabaseHelper(context);

        sessionManager = new SessionManager(context);
    }
    public void Login(){
        String cancel_req_tag = "login";

        //Create login url
        hashMap = helper.findIpDetails();
        if(hashMap == null) dataUrl = "http://192.168.0.122/officels/apis/userctrl.php?login";
        else{
            String id = hashMap.get(IpAddress.IpAttributes.COL_ID);
            String protocal = hashMap.get(IpAddress.IpAttributes.COL_PROTOCAL);
            String ipAddress = hashMap.get(IpAddress.IpAttributes.COL_IPADDRESS);
            dataUrl = protocal + "://" + ipAddress + "/officels/apis/userctrl.php?login";
        }
        StringRequest strReq = new StringRequest(Request.Method.POST,
                dataUrl, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Register Response: " + response.toString());
//                hideDialog();
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    if (!error) {
                        JSONObject user = jObj.getJSONObject("user");
                        String userId = user.getString("id");
                        String userNames = user.getString("names");
                        String userName = user.getString("username");
                        String userEmail = user.getString("email");
                        String userAccess = user.getString("access");
//                        String userCreated_at = user.getString("created_at");
//                        String userUpdated_at = user.getString("updated_at");

//                         Launch User activity
                        Intent intent = new Intent(mCxt,MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        sessionManager.createLoginSession(userId,userNames,userName,userEmail,userAccess,"","");
                        mCxt.startActivity(intent);
//                        finish();
                        Toast.makeText(mCxt, "Welcome "+userNames+"!", Toast.LENGTH_LONG).show();
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
                Log.e(TAG, "Login Error: " + error.getMessage());
                Toast.makeText(mCxt,
                        error.toString(), Toast.LENGTH_LONG).show();
//                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("uname", uname);
                params.put("password", upass);
                return params;
            }

        };
        // Adding request to request queue
        AppSingleton.getInstance(mCxt).addToRequestQueue(strReq,cancel_req_tag);
    }
}
