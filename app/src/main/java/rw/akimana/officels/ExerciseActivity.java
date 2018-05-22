package rw.akimana.officels;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import rw.akimana.officels.Adapters.ExamVAdapter;
import rw.akimana.officels.AppRequest.ExamRequest;
import rw.akimana.officels.Controllers.ItemDivider;

public class ExerciseActivity extends AppCompatActivity {

    String chap_id, chap_name;
    RecyclerView recyclerView;
    ExamRequest examRequest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            chap_id = (String) bundle.get("chap_id");
            chap_name = (String) bundle.get("chap_name");
        }

        recyclerView = findViewById(R.id.rv_exam_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new ItemDivider(getApplicationContext(), LinearLayoutManager.HORIZONTAL, 16));

        examRequest = new ExamRequest(getApplicationContext(), recyclerView);
        examRequest.displayExams(chap_id);
    }
}
