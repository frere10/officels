package rw.akimana.officels;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.pdf.PdfRenderer;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.github.barteksc.pdfviewer.PDFView;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import rw.akimana.officels.Controllers.DatabaseHelper;
import rw.akimana.officels.Models.IpAddress;

public class ViewExamActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    String examId, examFile, examTitle, dataUrl;
    String URL_PREFIX = "/officels/images/ass_files/";
    HashMap<String, String> hashMap;
    DatabaseHelper helper;

    ImageView pdfView;
    TextView tvTitle;
    Button btnDoExam;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_exam);

        pdfView = findViewById(R.id.pdfView);
        tvTitle = findViewById(R.id.tv_header);
        btnDoExam = findViewById(R.id.btn_go_to_answer);
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            examId = (String) bundle.get("exam_id");
            examTitle = (String) bundle.get("exam_title");
            examFile = (String) bundle.get("exam_file");
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
        tvTitle.setText(examTitle);

        try {
            openPdfFile(dataUrl);
        }catch (Exception e){
            Log.d("PDF Error", e.toString());
            Toast.makeText(getApplicationContext(), "Your phone cannot open file in the App.", Toast.LENGTH_LONG).show();
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
}
