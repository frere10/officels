package rw.akimana.officels;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import rw.akimana.officels.AppRequest.ContentRequest;
import rw.akimana.officels.Controllers.SessionManager;

public class ContentActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private static final String TAG = "ChapiterActivity------";

    String chap_id, chap_name;
    TextView tvCourseDisplay;

    SessionManager sessionManager;
    ContentRequest contentRequest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);

        sessionManager = new SessionManager(getApplicationContext());
        sessionManager.checkLogin();

        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            chap_id = (String) bundle.get("chap_id");
            chap_name = (String) bundle.get("chap_name");
        }

        recyclerView = (RecyclerView)findViewById(R.id.rv_contents_view);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);

//        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);

        contentRequest = new ContentRequest(getApplicationContext(), recyclerView, chap_id);
        contentRequest.displayContentss();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.content_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.menu_exercise:
                Intent intent = new Intent(getApplicationContext(), ExerciseActivity.class);
                intent.putExtra("chap_id", chap_id);
                intent.putExtra("chap_name", chap_name);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
