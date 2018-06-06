package rw.akimana.officels;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import rw.akimana.officels.AppRequest.ContentRequest;
import rw.akimana.officels.Controllers.SessionManager;

public class ContentActivity extends AppCompatActivity {

    private RecyclerView recyclerView, rvComment;
    private static final String TAG = "ContentActivity------";

    String chap_id, chap_name;
    TextView tvCourseDisplay;

    SessionManager sessionManager;
    ContentRequest contentRequest;
    private EditText etComment;
    private ImageView imgSend;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);

        sessionManager = new SessionManager(getApplicationContext());
        sessionManager.checkLogin();

        etComment = findViewById(R.id.et_comment);

        imgSend = findViewById(R.id.img_send_comment);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            chap_id = (String) bundle.get("chap_id");
            chap_name = (String) bundle.get("chap_name");
        }

        recyclerView = (RecyclerView)findViewById(R.id.rv_contents_view);
        rvComment = findViewById(R.id.rv_comments_view);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());

        LinearLayoutManager commentLManager = new LinearLayoutManager(getApplicationContext());
//        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        rvComment.setLayoutManager(commentLManager);

        contentRequest = new ContentRequest(getApplicationContext(), recyclerView, rvComment);
        int numOfItems = contentRequest.displayContentss(chap_id);

        Log.d(TAG, "Number of Items:"+ numOfItems);
        imgSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String comment = etComment.getText().toString();
                if(comment.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Type ypur comment", Toast.LENGTH_LONG).show();
                }else{
                    contentRequest.sendComment(chap_id, comment);
                }
            }
        });
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
