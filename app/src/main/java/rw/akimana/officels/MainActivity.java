package rw.akimana.officels;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import rw.akimana.officels.Adapters.CourseVAdapter;
import rw.akimana.officels.AppRequest.CourseRequest;
import rw.akimana.officels.Controllers.ItemDivider;
import rw.akimana.officels.Controllers.RVItemClickListener;
import rw.akimana.officels.Controllers.SessionManager;
import rw.akimana.officels.Models.Course;

public class MainActivity extends AppCompatActivity {
    SessionManager sessionManager;
    private RecyclerView recyclerView;
    private static final String TAG = "MAinActivity------";

    CourseRequest courseRequest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sessionManager = new SessionManager(getApplicationContext());
        sessionManager.checkLogin();

        recyclerView = (RecyclerView)findViewById(R.id.rv_courses);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 2);

        recyclerView.addItemDecoration(new ItemDivider(getApplicationContext(), LinearLayoutManager.VERTICAL, 32));
        recyclerView.addItemDecoration(new ItemDivider(getApplicationContext(), LinearLayoutManager.HORIZONTAL, 32));
        recyclerView.setLayoutManager(gridLayoutManager);

        courseRequest = new CourseRequest(getApplicationContext(), recyclerView);
        courseRequest.displayCourses();
    }

    public void clearUser(View view) {
        sessionManager.logoutUser();
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
