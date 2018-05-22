package rw.akimana.officels;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.pdf.PdfRenderer;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


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

    ImageView ivPdf;
    TextView tvTitle;
    Button btnDoExam;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_exam);

        ivPdf = findViewById(R.id.pdfView);
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
            openPDF();
        }catch (Exception e){
            Toast.makeText(getApplicationContext(), "Your phone cannot open file in the App.", Toast.LENGTH_LONG).show();
            openPdfFile(dataUrl);
        }
        btnDoExam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), AnswerActivity.class));
            }
        });
    }
    public void openPdfFile(String fileName){
//        String pdfUrl = protocal+"://"+ipAddress+"/officels/images/ass_files/"+exam.getFileContent();
//        String googleDocsUrl = "http://docs.google.com/viewer?url="+pdfUrl;
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(Uri.parse(fileName), "application/pdf");
        startActivity(intent);
    }
    @SuppressLint("NewApi")
    private void openPDF() throws IOException {
        File file = new File(getCacheDir(),dataUrl);

        ParcelFileDescriptor fileDescriptor = null;
        fileDescriptor = ParcelFileDescriptor.open(
                file, ParcelFileDescriptor.MODE_READ_ONLY);

        //min. API Level 21
        PdfRenderer pdfRenderer = null;
        pdfRenderer = new PdfRenderer(fileDescriptor);

        final int pageCount = pdfRenderer.getPageCount();
        Toast.makeText(this,
                "pageCount = " + pageCount,
                Toast.LENGTH_LONG).show();

        //Display page 0
        PdfRenderer.Page rendererPage = pdfRenderer.openPage(0);
        int rendererPageWidth = rendererPage.getWidth();
        int rendererPageHeight = rendererPage.getHeight();
        Bitmap bitmap = Bitmap.createBitmap(
                rendererPageWidth,
                rendererPageHeight,
                Bitmap.Config.ARGB_8888);
        rendererPage.render(bitmap, null, null,
                PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);

        ivPdf.setImageBitmap(bitmap);
        rendererPage.close();

        pdfRenderer.close();
        fileDescriptor.close();
    }
}
