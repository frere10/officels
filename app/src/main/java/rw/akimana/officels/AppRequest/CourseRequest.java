package rw.akimana.officels.AppRequest;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import rw.akimana.officels.Adapters.CourseVAdapter;
import rw.akimana.officels.ChapiterActivity;
import rw.akimana.officels.Controllers.AppSingleton;
import rw.akimana.officels.Controllers.DatabaseHelper;
import rw.akimana.officels.Controllers.ItemDivider;
import rw.akimana.officels.Controllers.RVItemClickListener;
import rw.akimana.officels.Controllers.SessionManager;
import rw.akimana.officels.MainActivity;
import rw.akimana.officels.Models.Course;
import rw.akimana.officels.Models.IpAddress;

public class CourseRequest {
    private Context mCxt;

    private static final String TAG = "Course Request";
    private static final String URL_PREFIX = "/officels/apis/coursectrl.php?courses";
    private String dataUrl;

    private HashMap<String, String> hashMap;

    private DatabaseHelper helper;

    private ArrayList<Course> arrayList = new ArrayList<>();
    private Course course;

    private RecyclerView recyclerView;
    private CourseVAdapter courseVAdapter;

    public CourseRequest(Context context, RecyclerView recyclerView){
        this.mCxt = context;
        this.recyclerView = recyclerView;
        this.helper = new DatabaseHelper(context);
    }
    public void displayCourses(){
        String cancel_req_tag = "courses";

        //Create login url
        hashMap = helper.findIpDetails();
        if(hashMap == null) dataUrl = "http://192.168.0.122" + URL_PREFIX;
        else{
            String id = hashMap.get(IpAddress.IpAttributes.COL_ID);
            String protocal = hashMap.get(IpAddress.IpAttributes.COL_PROTOCAL);
            String ipAddress = hashMap.get(IpAddress.IpAttributes.COL_IPADDRESS);
            dataUrl = protocal + "://" + ipAddress + URL_PREFIX;
        }
        StringRequest strReq = new StringRequest(Request.Method.GET,
                dataUrl, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Courses Response: " + response.toString());
//                hideDialog();
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    if (!error) {
//                        JSONObject  = jObj.getJSONObject("user");
                        JSONArray array = jObj.getJSONArray("courses");
                        for (int i = 0; i < array.length(); i++){
                            JSONObject courObj = array.getJSONObject(i);
                            course = new Course();
                            course.setName(courObj.getString("name"));
                            course.setAssNum(courObj.getString("num_ass")+" Tests");
                            course.setId(courObj.getString("id"));
                            course.setChapNum(courObj.getString("num_chap")+" Chapiters");
                            course.setWeight(courObj.getString("weight"));
                            course.setCreated(courObj.getString("created_at"));
                            course.setUpdated(courObj.getString("updated_at"));

                            arrayList.add(course);
                        }
                        courseVAdapter = new CourseVAdapter(mCxt, arrayList);

                        courseVAdapter.setOnItemClickListener(new CourseVAdapter.onItemClickListener() {
                            @Override
                            public void onItemClickListener(View view, int position, Course myCourse) {
                                Intent intent = new Intent(mCxt, ChapiterActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                                intent.putExtra("course_id", myCourse.getId());
                                intent.putExtra("course_name", myCourse.getName());
                                intent.putExtra("course_weight", myCourse.getWeight());
                                intent.putExtra("course_c", myCourse.getCreated());
                                intent.putExtra("course_u", myCourse.getUpdated());

                                mCxt.startActivity(intent);
                                Log.e(TAG, "Item click: " + myCourse.getName());
                            }
                        });
                        recyclerView.setAdapter(courseVAdapter);
                    } else {
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(mCxt, errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(mCxt, e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Course Error: " + error.getMessage());
                Toast.makeText(mCxt, error.getMessage(), Toast.LENGTH_LONG).show();
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
        AppSingleton.getInstance(mCxt).addToRequestQueue(strReq,cancel_req_tag);
    }
}
