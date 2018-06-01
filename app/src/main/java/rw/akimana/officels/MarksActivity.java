package rw.akimana.officels;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import java.util.HashMap;

import rw.akimana.officels.AppRequest.MarkRequest;
import rw.akimana.officels.Controllers.SessionManager;

import static rw.akimana.officels.Controllers.SessionManager.*;

public class MarksActivity extends AppCompatActivity {

    private TextView tvTitle;
    private RecyclerView rvMarkView;
    private SessionManager sessionManager;
    private MarkRequest markRequest;
    private HashMap<String, String> user;
    private String userId, userName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marks);

        tvTitle = findViewById(R.id.tv_text_title);
        rvMarkView = findViewById(R.id.rv_marks_view);

        sessionManager = new SessionManager(getApplicationContext());
        sessionManager.checkLogin();

        user = sessionManager.getUserDetails();
        userId = user.get(KEY_USER_ID);
        userName = user.get(KEY_USER_NAMES);
        tvTitle.setText(userName);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);

        rvMarkView.setLayoutManager(layoutManager);

        markRequest = new MarkRequest(getApplicationContext(), rvMarkView);
        markRequest.displayMarks(userId);
    }
}
