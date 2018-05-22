package rw.akimana.officels;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import rw.akimana.officels.AppRequest.ChapiterRequest;
import rw.akimana.officels.Controllers.SessionManager;

public class ChapiterActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private static final String TAG = "ChapiterActivity------";

    String course_id, course_name, course_weight;
    TextView tvCourseDisplay;

    SessionManager sessionManager;
    ChapiterRequest chapiterRequest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapiter);

        sessionManager = new SessionManager(getApplicationContext());
        sessionManager.checkLogin();

        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
           course_id = (String) bundle.get("course_id");
           course_name = (String) bundle.get("course_name");
           course_weight = (String) bundle.get("course_weight");
        }
        tvCourseDisplay = (TextView)findViewById(R.id.tv_course_display);
        recyclerView = (RecyclerView)findViewById(R.id.rv_chapiters);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);

        chapiterRequest = new ChapiterRequest(getApplicationContext(), recyclerView, course_id);

        tvCourseDisplay.setText(course_name);

        chapiterRequest.displayChapiters();
    }
}
