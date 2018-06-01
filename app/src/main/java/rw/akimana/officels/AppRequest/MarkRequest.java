package rw.akimana.officels.AppRequest;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

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

import rw.akimana.officels.Adapters.MarksVAdapter;
import rw.akimana.officels.Controllers.AppSingleton;
import rw.akimana.officels.Controllers.DatabaseHelper;
import rw.akimana.officels.Controllers.ItemDivider;
import rw.akimana.officels.Models.IpAddress;
import rw.akimana.officels.Models.Mark;

public class MarkRequest {
    private Context context;

    private static final String TAG = "Marks Request";
    private static final String URL_PREFIX = "/officels/apis/userctrl.php?mark";
    private String dataUrl;

    private HashMap<String, String> hashMap;

    private DatabaseHelper helper;

    private ArrayList<Mark> arrayList = new ArrayList<>();
    private Mark mark;

    private RecyclerView recyclerView;
    private TextView tvMarksTotal;
    private MarksVAdapter marksVAdapter;

    public MarkRequest(Context context, RecyclerView recyclerView, TextView tvMarksTotal){
        this.context = context;
        this.recyclerView = recyclerView;
        this.helper = new DatabaseHelper(context);
        this.tvMarksTotal = tvMarksTotal;
    }

    public void displayMarks(final String userId){
        String cancel_req_tag = "Marks";

        //Create login url
        hashMap = helper.findIpDetails();
        if(hashMap == null) dataUrl = "http://192.168.0.122" + URL_PREFIX;
        else{
            String id = hashMap.get(IpAddress.IpAttributes.COL_ID);
            String protocal = hashMap.get(IpAddress.IpAttributes.COL_PROTOCAL);
            String ipAddress = hashMap.get(IpAddress.IpAttributes.COL_IPADDRESS);
            dataUrl = protocal + "://" + ipAddress + URL_PREFIX;
        }
        StringRequest strReq = new StringRequest(Request.Method.POST,
                dataUrl, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Marks Response: " + response.toString());
//                hideDialog();
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    if (!error) {
//                        JSONObject  = jObj.getJSONObject("user");
                        JSONArray array = jObj.getJSONArray("marks");
                        int totatMark = 0, totalWeight = 0;
                        for (int i = 0; i < array.length(); i++){
                            JSONObject marksObj = array.getJSONObject(i);
                            mark = new Mark();

                            mark.setCourse(marksObj.getString("course_name"));
                            mark.setChapiter(marksObj.getString("chap_name"));
                            mark.setExam(marksObj.getString("exam_title"));
                            mark.setUserMarks(marksObj.getString("user_marks"));
                            mark.setMarksWeight(marksObj.getString("out_of"));

                            totatMark += Integer.parseInt(marksObj.getString("user_marks"));
                            totalWeight += Integer.parseInt(marksObj.getString("out_of"));
                            arrayList.add(mark);
                        }
                        if (totatMark!=0||totalWeight!=0)
                            tvMarksTotal.setText(String.valueOf(totatMark)+" Out of "+String.valueOf(totalWeight));
                        marksVAdapter = new MarksVAdapter(context, arrayList);

                        recyclerView.setAdapter(marksVAdapter);
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
                Log.e(TAG, "Mark Error: " + error.getMessage());
                Toast.makeText(context, error.getMessage(), Toast.LENGTH_LONG).show();
//                hideDialog();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("userid", userId);
                return params;
            }
        };
        // Adding request to request queue
        AppSingleton.getInstance(context).addToRequestQueue(strReq,cancel_req_tag);
    }
}
