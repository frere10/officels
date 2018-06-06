package rw.akimana.officels;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import rw.akimana.officels.Controllers.CheckPermission;
import rw.akimana.officels.Controllers.DatabaseHelper;
import rw.akimana.officels.Controllers.FileDownloader;
import rw.akimana.officels.Controllers.Utils;
import rw.akimana.officels.Models.IpAddress;

public class ViewExamActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    String examId, examFile, examTitle, dataUrl, examMarks;
    String URL_PREFIX = "/officels/images/ass_files/";
    HashMap<String, String> hashMap;
    DatabaseHelper helper;

    private PDFView pdfView;
    private TextView tvTitle;
    private Button btnDoExam;

    private TextView tvOutOf;

    private String extStorageDirectory;
    private File folder, pdfFile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_exam);

        pdfView = findViewById(R.id.pdfView);
        tvTitle = findViewById(R.id.tv_header);
        btnDoExam = findViewById(R.id.btn_go_to_answer);
        tvOutOf = findViewById(R.id.tv_out_of);
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            examId = (String) bundle.get("exam_id");
            examTitle = (String) bundle.get("exam_title");
            examFile = (String) bundle.get("exam_file");
            examMarks = (String) bundle.get("exam_marks");
        }
        helper = new DatabaseHelper(getApplicationContext());
        hashMap = helper.findIpDetails();
        if(hashMap == null) dataUrl = "http://192.168.0.122" + URL_PREFIX + examFile;
        else{
            String id = hashMap.get(IpAddress.IpAttributes.COL_ID);
            String protocal = hashMap.get(IpAddress.IpAttributes.COL_PROTOCAL);
            String ipAddress = hashMap.get(IpAddress.IpAttributes.COL_IPADDRESS);
            dataUrl = protocal + "://" + ipAddress + URL_PREFIX + examFile;
        }
        tvOutOf.setText("Out of " + examMarks);

        try {
            CheckPermission.verifyStoragePermissions(this);
            new DownloadFile().execute(dataUrl);
//            new DownloadTask(getApplicationContext(), tvTitle, pdfView, examFile);
//            openPdfFile(dataUrl);
        }catch (Exception e){
            Log.d("PDF Error", e.toString());
            Toast.makeText(getApplicationContext(), "Sorry something is wrong", Toast.LENGTH_LONG).show();
        }
        btnDoExam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AnswerActivity.class);
                intent.putExtra("exam_id", examId);
                startActivity(intent);
            }
        });
    }
    public void openPdfFile(String fileName){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(Uri.parse(fileName), "application/pdf");
        startActivity(intent);
    }
    private class DownloadFile extends AsyncTask<String, Void, Void> implements OnLoadCompleteListener, OnPageChangeListener {
        @Override
        protected void onPreExecute() {
            tvTitle.setText(R.string.downloadStarted);
        }

        @Override
        protected Void doInBackground(String... strings) {
            String fileUrl = strings[0];   // -> http://maven.apache.org/maven-1.x/maven.pdf
            String fileName = "downloadFile";  // -> maven.pdf
            extStorageDirectory = Environment.getExternalStorageDirectory().toString();
            folder = new File(extStorageDirectory, Utils.downloadDirectory);

            if(!folder.exists()) {
                folder.mkdir();
            }
            pdfFile = new File(folder, examTitle + ".pdf");

            try{
                tvTitle.setText("Downloading...");
                pdfFile.createNewFile();
            }catch (IOException e){
                e.printStackTrace();
            }
            FileDownloader.downloadFile(fileUrl, pdfFile);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            tvTitle.setText(examTitle);
            pdfView.fromFile(pdfFile)
                    .defaultPage(0)
                    .enableSwipe(true)
                    .onLoad(this)
                    .onPageChange(this)
                    .load();
            super.onPostExecute(aVoid);
        }

        @Override
        public void loadComplete(int nbPages) {

        }

        @Override
        public void onPageChanged(int page, int pageCount) {

        }
    }
}
