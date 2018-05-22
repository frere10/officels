package rw.akimana.officels.AppRequest;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
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

import rw.akimana.officels.Adapters.ExamVAdapter;
import rw.akimana.officels.ContentActivity;
import rw.akimana.officels.Controllers.AppSingleton;
import rw.akimana.officels.Controllers.DatabaseHelper;
import rw.akimana.officels.Controllers.ItemDivider;
import rw.akimana.officels.Models.Exam;
import rw.akimana.officels.Models.IpAddress;

public class ExamRequest {
        private Context context;

        private static final String TAG = "Chapter Request";
        private static final String URL_PREFIX = "/officels/apis/assmtctrl.php?list&chap=";
        private String dataUrl, chapiter_id;

        private HashMap<String, String> hashMap;

        private DatabaseHelper helper;

        private ArrayList<Exam> arrayList = new ArrayList<>();
        private Exam exam;

        private RecyclerView recyclerView;
        private ExamVAdapter examVAdapter;

        public ExamRequest (Context context, RecyclerView recyclerView, String chapiter_id){
            this.context = context;
            this.recyclerView = recyclerView;
            this.helper = new DatabaseHelper(context);
            this.chapiter_id = chapiter_id;
        }

        public void displayExams(){
            String cancel_req_tag = "Exams ";

            //Create login url
            hashMap = helper.findIpDetails();
            if(hashMap == null) dataUrl = "http://192.168.0.122" + URL_PREFIX + chapiter_id;
            else{
                String id = hashMap.get(IpAddress.IpAttributes.COL_ID);
                String protocal = hashMap.get(IpAddress.IpAttributes.COL_PROTOCAL);
                String ipAddress = hashMap.get(IpAddress.IpAttributes.COL_IPADDRESS);
                dataUrl = protocal + "://" + ipAddress + URL_PREFIX + chapiter_id;
            }
            StringRequest strReq = new StringRequest(Request.Method.GET,
                    dataUrl, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    Log.d(TAG, "Exam Response: " + response.toString());
//                hideDialog();
                    try {
                        JSONObject jObj = new JSONObject(response);
                        boolean error = jObj.getBoolean("error");

                        if (!error) {
//                        JSONObject  = jObj.getJSONObject("user");
                            JSONArray array = jObj.getJSONArray("chapiters");
                            for (int i = 0; i < array.length(); i++){
                                JSONObject chapObj = array.getJSONObject(i);
                                exam = new Exam();

                                exam.setId(chapObj.getString("id"));
                                exam.setTitle(chapObj.getString("title"));
                                exam.setMarks(chapObj.getString("marks"));
                                exam.setFileContent(chapObj.getString("content"));
                                exam.setCreated(chapObj.getString("created_at"));
                                exam.setUpdated(chapObj.getString("updated_at"));

                                arrayList.add(exam);
                            }
                            examVAdapter = new ExamVAdapter(context, arrayList);
                            recyclerView.addItemDecoration(new ItemDivider(context, LinearLayoutManager.VERTICAL, 16));

                            examVAdapter.setOnItemClickListener(new ExamVAdapter.onItemClickListener() {
                                @Override
                                public void onItemClickListener(View view, int position, Exam exam) {
//                                    Intent intent = new Intent(context, ContentActivity.class);
//                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//
//                                    intent.putExtra("chap_id", chapiter.getId());
//                                    intent.putExtra("chap_name", chapiter.getName());
//                                    intent.putExtra("chap_content", chapiter.getContents());
//                                    intent.putExtra("chap_course_id", chapiter.getCourseId());
//                                    intent.putExtra("chap_c", chapiter.getCreated());
//                                    intent.putExtra("chap_u", chapiter.getUpdated());
//
//                                    context.startActivity(intent);
                                }
                            });
                            recyclerView.setAdapter(examVAdapter);
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
                    Log.e(TAG, "Exam Error: " + error.getMessage());
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
}

