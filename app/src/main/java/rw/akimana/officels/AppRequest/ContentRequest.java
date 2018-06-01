package rw.akimana.officels.AppRequest;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import rw.akimana.officels.Adapters.CommentVAdapter;
import rw.akimana.officels.Adapters.ContentAdapter;
import rw.akimana.officels.ContentActivity;
import rw.akimana.officels.Controllers.AppSingleton;
import rw.akimana.officels.Controllers.DatabaseHelper;
import rw.akimana.officels.Controllers.SessionManager;
import rw.akimana.officels.Models.Comment;
import rw.akimana.officels.Models.Content;
import rw.akimana.officels.Models.IpAddress;

import static rw.akimana.officels.Controllers.SessionManager.KEY_USER_ID;

public class ContentRequest {

    private Context context;

    private static final String TAG = "Content Request";
    private String chapiter_id;
    private String protocol, ipAddress;

    private String dataUrl;

    private DatabaseHelper helper;

    private ArrayList<Content> arrayList = new ArrayList<>();
    private Content content;

    private RecyclerView recyclerView;
    private ContentAdapter contentAdapter;

    private SessionManager sessionManager;

    public ContentRequest(Context context, RecyclerView recyclerView, String chapiter_id){
        this.context = context;
        this.recyclerView = recyclerView;
        this.helper = new DatabaseHelper(context);
        this.chapiter_id = chapiter_id;

        sessionManager = new SessionManager(context);
    }

    public void displayContentss(){
        String cancel_req_tag = "Contents";

        String URL_PREFIX = "/officels/apis/contentctrl.php?contents&chap=";
        HashMap<String, String> hashMap = helper.findIpDetails();
        if(hashMap == null) dataUrl = "http://192.168.0.122" + URL_PREFIX + chapiter_id;
        else{
            String id = hashMap.get(IpAddress.IpAttributes.COL_ID);
            protocol = hashMap.get(IpAddress.IpAttributes.COL_PROTOCAL);
            ipAddress = hashMap.get(IpAddress.IpAttributes.COL_IPADDRESS);
            dataUrl = protocol + "://" + ipAddress + URL_PREFIX + chapiter_id;
        }

        StringRequest strReq = new StringRequest(Request.Method.GET,
                dataUrl, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Contents Response: " + response);
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    if (!error) {
                        JSONArray array = jObj.getJSONArray("contents");
                        for (int i = 0; i < array.length(); i++){
                            JSONObject contentObj = array.getJSONObject(i);
                            content = new Content();

                            content.setId(contentObj.getString("id"));
                            content.setTitle(contentObj.getString("title"));
                            content.setDetail(contentObj.getString("content"));
                            content.setImage(protocol + "://" + ipAddress+"/officels/images/cont_photos/"+contentObj.getString("cont_image"));
                            content.setChapiterId(contentObj.getString("chapiter_id"));
                            content.setCreated(contentObj.getString("created_at"));
                            content.setUpdated(contentObj.getString("updated_at"));

                            ArrayList<Comment> commentArrayList = new ArrayList<>();

                            JSONArray commArray = contentObj.getJSONArray("comments");
                            for(int j = 0; j < commArray.length(); j++){
                                JSONObject commentObj = commArray.getJSONObject(j);
                                Comment comment = new Comment();

                                comment.setUserName(commentObj.getString("user_names"));
                                comment.setComment(commentObj.getString("comment"));
                                comment.setCreated(commentObj.getString("created_at"));
                                comment.setId(commentObj.getString("id"));
                                comment.setUpdated(commentObj.getString("updated_at"));

                                commentArrayList.add(comment);
                            }
                            content.setCommentArrayList(commentArrayList);
                            arrayList.add(content);
                        }
                        contentAdapter = new ContentAdapter(context, arrayList);
                        recyclerView.setAdapter(contentAdapter);
                    } else {
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(context, errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Chapiter Error: " + error.getMessage());
                Toast.makeText(context, error.getMessage(), Toast.LENGTH_LONG).show();
//                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                // Params will be here
                return params;
            }

        };
        // Adding request to request queue
        AppSingleton.getInstance(context).addToRequestQueue(strReq,cancel_req_tag);
    }
    public void sendComment(final String contentId, final String comment){
        String cancel_req_tag = "Send comment";

        HashMap<String, String> user = sessionManager.getUserDetails();
        final String user_id = user.get(KEY_USER_ID);

        String URL_PREFIX = "/officels/apis/contentctrl.php?add_comment";
        HashMap<String, String> hashMap = helper.findIpDetails();
        if(hashMap == null) dataUrl = "http://192.168.0.122" + URL_PREFIX;
        else{
            String id = hashMap.get(IpAddress.IpAttributes.COL_ID);
            protocol = hashMap.get(IpAddress.IpAttributes.COL_PROTOCAL);
            ipAddress = hashMap.get(IpAddress.IpAttributes.COL_IPADDRESS);
            dataUrl = protocol + "://" + ipAddress + URL_PREFIX;
        }

        StringRequest strReq = new StringRequest(Request.Method.POST,
                dataUrl, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    if (!error){

                    }else {
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(context, errorMsg, Toast.LENGTH_LONG).show();
                    }
                }catch (Exception e){
                    Log.d(TAG + "h Tag", e.getMessage());
                    Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Send Error: " + error.getMessage());
                Toast.makeText(context, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("comment", comment);
                params.put("content_id", contentId);
                params.put("user_id", user_id);
                return params;
            }
        };
        AppSingleton.getInstance(context).addToRequestQueue(strReq,cancel_req_tag);
    }
}
